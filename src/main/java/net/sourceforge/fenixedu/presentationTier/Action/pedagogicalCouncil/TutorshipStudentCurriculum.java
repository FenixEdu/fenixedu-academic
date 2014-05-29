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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.NumberBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TutorshipApp.class, path = "student-curriculum",
        titleKey = "link.teacher.tutorship.students.viewCurriculum", bundle = "ApplicationResources")
@Mapping(path = "/studentTutorshipCurriculum", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "showStudentCurriculum", path = "/pedagogicalCouncil/tutorship/showStudentCurriculum.jsp"),
        @Forward(name = "chooseRegistration", path = "/pedagogicalCouncil/tutorship/chooseRegistration.jsp") })
public class TutorshipStudentCurriculum extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("tutorateBean", new NumberBean());
        return mapping.findForward("showStudentCurriculum");
    }

    public ActionForward showStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return showOrChoose(mapping, request);
    }

    public ActionForward showStudentRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final NumberBean numberBean = new NumberBean();
        numberBean.setNumber(getIntegerFromRequest(request, "studentNumber"));
        request.setAttribute("tutorateBean", numberBean);
        return showOrChoose(mapping, request);
    }

    private ActionForward showOrChoose(final ActionMapping mapping, final HttpServletRequest request) {
        Registration registration = null;

        final String registrationOID = (String) getFromRequest(request, "registrationOID");
        NumberBean bean = (NumberBean) getObjectFromViewState("tutorateBean");
        if (bean == null) {
            bean = (NumberBean) request.getAttribute("tutorateBean");
        }

        if (registrationOID != null) {
            registration = FenixFramework.getDomainObject(registrationOID);
        } else {
            final Student student = Student.readStudentByNumber(bean.getNumber());
            if (student != null) {
                if (student.getRegistrationsSet().size() == 1) {
                    registration = student.getRegistrationsSet().iterator().next();
                } else {
                    request.setAttribute("student", student);
                    return mapping.findForward("chooseRegistration");
                }
            }
        }

        if (registration == null) {
            studentErrorMessage(request, bean.getNumber());
        } else {
            request.setAttribute("registration", registration);
            CurriculumDispatchAction.computeCurricularInfo(request, registration);
        }

        return mapping.findForward("showStudentCurriculum");
    }

    private void studentErrorMessage(HttpServletRequest request, Integer studentNumber) {
        addActionMessage("error", request, "student.does.not.exist", String.valueOf(studentNumber));
    }
}
