/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor.InsertTutorship;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * Class CreateTutorshipsDA.java
 *
 * @author jaime created on Aug 3, 2010
 */

@StrutsFunctionality(app = TutorshipApp.class, path = "create-tutorships", titleKey = "link.tutorship.create",
        bundle = "ApplicationResources")
@Mapping(path = "/createTutorships", module = "pedagogicalCouncil")
@Forwards(@Forward(name = "prepareCreate", path = "/pedagogicalCouncil/tutorship/createTutorships.jsp"))
public class CreateTutorshipsDA extends TutorManagementDispatchAction {

    private static int TUTORSHIP_DURATION = 2;

    @EntryPoint
    public ActionForward prepareCreation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("tutorateBean", new ContextTutorshipCreationBean());

        return mapping.findForward("prepareCreate");
    }

    public ActionForward prepareViewCreateTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ContextTutorshipCreationBean bean = getRenderedObject("tutorateBean");
        request.setAttribute("tutorateBean", bean);
        if (bean.getShift() != null && bean.getExecutionCourse() != null && bean.getExecutionDegree() != null
                && bean.getExecutionSemester() != null) {
            // get all students from ExecCourse
            List<Person> students = new ArrayList<Person>();

            Shift shift = bean.getShift();
            for (Registration registration : shift.getStudentsSet()) {
                if (validForListing(registration, bean.getExecutionDegree())) {
                    students.add(registration.getPerson());
                }
            }
            RenderUtils.invalidateViewState();
            request.setAttribute("students", students);
            request.setAttribute("tutorBean", new TeacherTutorshipCreationBean(bean.getExecutionDegree()));
            return mapping.findForward("prepareCreate");
        } else {
            RenderUtils.invalidateViewState();
            return mapping.findForward("prepareCreate");
        }
    }

    /**
     * Select people which have registrations in the choosen Degree and have
     * never had a Tutor assigned
     *
     * @param registration
     * @param executionDegree
     * @return
     */
    public boolean validForListing(Registration registration, ExecutionDegree executionDegree) {
        Student student = registration.getStudent();

        if (student.hasActiveRegistrationFor(executionDegree.getDegree())) {
            if (registration.getActiveTutorship() == null) {
                for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    if (studentCurricularPlan.getTutorshipsSet().size() == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ActionForward prepareStudentsAndTeachers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ContextTutorshipCreationBean bean = getRenderedObject("tutorateBean");
        request.setAttribute("tutorateBean", bean);
        return mapping.findForward("prepareCreate");
    }

    public ActionForward createTutorship(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Boolean errorEncountered = false;
        String[] selectedPersons = request.getParameterValues("selectedPersons");
        TeacherTutorshipCreationBean tutorBean = getRenderedObject("tutorBean");
        ContextTutorshipCreationBean contextBean = getRenderedObject("tutorateBean");

        StudentsByEntryYearBean selectedStudentsAndTutorBean =
                new StudentsByEntryYearBean(contextBean.getExecutionSemester().getExecutionYear());
        // Initialize Tutorship creation bean to use in InsertTutorship Service
        BeanInitializer.initializeBean(selectedStudentsAndTutorBean, tutorBean, contextBean, selectedPersons, TUTORSHIP_DURATION);

        List<TutorshipErrorBean> tutorshipsNotInserted = new ArrayList<TutorshipErrorBean>();
        try {
            tutorshipsNotInserted =
                    InsertTutorship.runInsertTutorship(contextBean.getExecutionDegree().getExternalId(),
                            selectedStudentsAndTutorBean);
        } catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            errorEncountered = true;
        }
        if (!tutorshipsNotInserted.isEmpty()) {
            errorEncountered = true;
            for (TutorshipErrorBean tutorship : tutorshipsNotInserted) {
                addActionMessage(request, tutorship.getMessage(), tutorship.getArgs());
            }
            if (tutorshipsNotInserted.size() < selectedPersons.length) {
                Integer argument = selectedPersons.length - tutorshipsNotInserted.size();
                String[] messageArgs = { argument.toString() };
                addActionMessage(request, "label.create.tutorship.remaining.correct", messageArgs);
            }
            return mapping.findForward("prepareCreate");
        } else if (!errorEncountered) {
            request.setAttribute("success", "Sucess");
            return prepareCreation(mapping, actionForm, request, response);
        }
        return prepareCreation(mapping, actionForm, request, response);
    }
}
