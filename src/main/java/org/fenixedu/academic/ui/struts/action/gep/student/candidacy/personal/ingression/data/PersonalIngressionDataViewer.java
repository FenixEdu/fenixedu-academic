/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.gep.student.candidacy.personal.ingression.data;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacy;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.gep.GepApplication.GepRAIDESApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = GepRAIDESApp.class, path = "personal-ingression-data",
        titleKey = "link.personal.ingression.data.viewer")
@Mapping(path = "/personalIngressionDataViewer", module = "gep")
@Forwards({
        @Forward(name = "chooseStudent", path = "/gep/student/candidacy/personal/ingression/data/chooseStudent.jsp"),
        @Forward(name = "viewStudent", path = "/gep/student/candidacy/personal/ingression/data/viewStudent.jsp"),
        @Forward(name = "viewStudentCandidacy", path = "/gep/student/candidacy/personal/ingression/data/viewStudentCandidacy.jsp"),
        @Forward(name = "viewPersonalIngressionData",
                path = "/gep/student/candidacy/personal/ingression/data/viewPersonalIngressionData.jsp"),
        @Forward(name = "viewIndividualCandidacy",
                path = "/gep/student/candidacy/personal/ingression/data/viewIndividualCandidacy.jsp"),
        @Forward(name = "viewRegistration", path = "/gep/student/candidacy/personal/ingression/data/viewRegistration.jsp") })
public class PersonalIngressionDataViewer extends FenixDispatchAction {

    @EntryPoint
    public ActionForward chooseStudent(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute("chooseStudentBean", new ChooseStudentBean());

        return mapping.findForward("chooseStudent");
    }

    public ActionForward findStudents(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        ChooseStudentBean chooseStudentBean = getRenderedObject("chooseStudentBean");
        Set<Student> students = chooseStudentBean.findStudents();

        if (students.size() == 1) {
            request.setAttribute("student", students.iterator().next());
            return mapping.findForward("viewStudent");
        }

        request.setAttribute("students", students);
        request.setAttribute("chooseStudentBean", chooseStudentBean);

        return mapping.findForward("chooseStudent");
    }

    public ActionForward findStudentsInvalid(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        ChooseStudentBean chooseStudentBean = getRenderedObject("chooseStudentBean");

        request.setAttribute("chooseStudentBean", chooseStudentBean);

        return mapping.findForward("chooseStudent");
    }

    public ActionForward viewStudent(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        Student student = getDomainObject(request, "studentId");

        request.setAttribute("student", student);

        return mapping.findForward("viewStudent");
    }

    public ActionForward viewStudentCandidacy(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        StudentCandidacy studentCandidacy = getDomainObject(request, "studentCandidacyId");

        request.setAttribute("studentCandidacy", studentCandidacy);

        return mapping.findForward("viewStudentCandidacy");
    }

    public ActionForward viewPersonalIngressionData(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        PersonalIngressionData personalIngressionData = getDomainObject(request, "personalIngressionDataId");

        request.setAttribute("personalIngressionData", personalIngressionData);

        return mapping.findForward("viewPersonalIngressionData");
    }

    public ActionForward viewIndividualCandidacy(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        IndividualCandidacy individualCandidacy = getDomainObject(request, "individualCandidacyId");

        request.setAttribute("individualCandidacy", individualCandidacy);

        return mapping.findForward("viewIndividualCandidacy");
    }

    public ActionForward viewRegistration(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        Registration registration = getDomainObject(request, "registrationId");

        request.setAttribute("registration", registration);

        return mapping.findForward("viewRegistration");
    }
}
