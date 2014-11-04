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
package org.fenixedu.academic.ui.struts.action.teacher.tutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.teacher.tutor.StudentsPerformanceInfoBean.StudentsPerformanceInfoNullEntryYearBean;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.TutorshipLog;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.commons.tutorship.ViewStudentsByTutorDispatchAction;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherTutorApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TeacherTutorApp.class, path = "students-by-tutor", titleKey = "link.teacher.tutorship.history")
@Mapping(path = "/viewStudentsByTutor", module = "teacher")
@Forwards({ @Forward(name = "viewStudentsByTutor", path = "/teacher/tutor/viewStudentsByTutor.jsp"),
        @Forward(name = "editStudent", path = "/teacher/tutor/editStudent.jsp") })
public class ViewStudentsDispatchAction extends ViewStudentsByTutorDispatchAction {

    @EntryPoint
    public ActionForward viewStudentsByTutor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Person person = getLoggedPerson(request);
        final Teacher teacher = person.getTeacher();

        getTutorships(request, teacher);

        request.setAttribute("performanceBean", getOrCreateBean(teacher));
        request.setAttribute("tutor", person);
        return mapping.findForward("viewStudentsByTutor");
    }

    public StudentsPerformanceInfoNullEntryYearBean getOrCreateBean(Teacher teacher) {
        StudentsPerformanceInfoNullEntryYearBean performanceBean = getRenderedObject("performanceBean");
        if (performanceBean == null) {
            performanceBean = StudentsPerformanceInfoNullEntryYearBean.create(teacher);
        }

        return performanceBean;
    }

    public ActionForward editStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Student student = FenixFramework.getDomainObject(request.getParameter("studentID"));

        Registration registration = FenixFramework.getDomainObject(request.getParameter("registrationID"));
        TutorshipLog tutorshipLog = Tutorship.getActiveTutorship(registration.getLastStudentCurricularPlan()).getTutorshipLog();

        request.setAttribute("tutor", getLoggedPerson(request));
        request.setAttribute("student", student);
        request.setAttribute("tutorshipLog", tutorshipLog);
        return mapping.findForward("editStudent");
    }
}
