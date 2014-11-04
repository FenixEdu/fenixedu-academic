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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.FactoryExecutor;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonInformationLog;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.EditCandidacyInformationDA.ChooseRegistrationOrPhd;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/student", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "viewStudentDetails", path = "/academicAdminOffice/student/viewStudentDetails.jsp"),
        @Forward(name = "editPersonalData", path = "/academicAdminOffice/editPersonalData.jsp"),
        @Forward(name = "viewPersonalData", path = "/academicAdminOffice/viewPersonalData.jsp"),
        @Forward(name = "viewStudentLogChanges", path = "/academicAdminOffice/viewStudentLogChanges.jsp") })
public class StudentDA extends StudentRegistrationDA {

    public static class PersonBeanFactoryEditor extends PersonBean implements FactoryExecutor {
        public PersonBeanFactoryEditor(final Person person) {
            super(person);
        }

        @Override
        public Object execute() {
            getPerson().editPersonalInformation(this);
            return null;
        }
    }

    private Student getAndSetStudent(final HttpServletRequest request) {
        final String studentID = getFromRequest(request, "studentID").toString();
        final Student student = FenixFramework.getDomainObject(studentID);
        request.setAttribute("student", student);
        request.setAttribute("choosePhdOrRegistration", new ChooseRegistrationOrPhd(student));
        return student;
    }

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getAndSetStudent(request);

        request.setAttribute("personBean", new PersonBeanFactoryEditor(student.getPerson()));
        return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        getAndSetStudent(request);
        try {
            executeFactoryMethod();
            RenderUtils.invalidateViewState();
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());

            request.setAttribute("personBean", getRenderedObject());
            return mapping.findForward("editPersonalData");
        }

        addActionMessage(request, "message.student.personDataEditedWithSuccess");

        return mapping.findForward("viewStudentDetails");
    }

    public ActionForward viewPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("personBean", new PersonBeanFactoryEditor(getAndSetStudent(request).getPerson()));
        return mapping.findForward("viewPersonalData");
    }

    public ActionForward visualizeStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        getAndSetStudent(request);
        return mapping.findForward("viewStudentDetails");
    }

    public ActionForward viewStudentLog(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getAndSetStudent(request);

        Person person = student.getPerson();
        Collection<PersonInformationLog> logsList = person.getPersonInformationLogsSet();
        request.setAttribute("person", person);
        request.setAttribute("logsList", logsList);
        return mapping.findForward("viewStudentLogChanges");
    }
}