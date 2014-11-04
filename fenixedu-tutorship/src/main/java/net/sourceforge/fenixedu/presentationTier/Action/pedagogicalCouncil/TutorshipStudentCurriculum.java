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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.pedagogicalCouncil.NumberBean;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.CurriculumDispatchAction;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

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
