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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.credits;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.teacher.credits.NonRegularTeacherBean;
import org.fenixedu.academic.dto.teacher.credits.NonRegularTeachingServiceBean;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.NonRegularTeachingService;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.credits.scientificCouncil.ScientificCouncilViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/manageNonRegularTeachingService", module = "scientificCouncil",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards({ @Forward(name = "chooseNonRegularTeacher", path = "/scientificCouncil/credits/chooseNonRegularTeacher.jsp"),
        @Forward(name = "showNonRegularTeachingService", path = "/teacher/credits/showNonRegularTeachingService.jsp"),
        @Forward(name = "editNonRegularTeachingService", path = "/teacher/credits/editNonRegularTeachingService.jsp") })
public class NonRegularTeacherCreditsDispatchAction extends FenixDispatchAction {

    public ActionForward chooseNonRegularTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        NonRegularTeacherBean nonRegularTeacherBean = new NonRegularTeacherBean();
        request.setAttribute("nonRegularTeacherBean", nonRegularTeacherBean);
        return mapping.findForward("chooseNonRegularTeacher");
    }

    public ActionForward showNonRegularTeachingService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        NonRegularTeacherBean nonRegularTeacherBean = getRenderedObject();
        if (nonRegularTeacherBean == null) {
            nonRegularTeacherBean = (NonRegularTeacherBean) request.getAttribute("nonRegularTeacherBean");
        }
        if (nonRegularTeacherBean != null) {
            Person person = nonRegularTeacherBean.getPerson();
            if (person != null) {
                if (isNonRegularTeacher(person)) {
                    request.setAttribute("person", person);
                    ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
                    request.setAttribute("professorships", person.getProfessorshipsByExecutionSemester(executionSemester));
                    request.setAttribute("canEdit", true);
                    return mapping.findForward("showNonRegularTeachingService");
                } else {
                    addActionMessage("message", request, "error.invalid.nonRegularTeacher");
                }
            }
        }
        return chooseNonRegularTeacher(mapping, form, request, response);
    }

    private boolean isNonRegularTeacher(Person person) {
        return person.getTeacher() == null || person.getEmployee() == null
                || person.getTeacher().getDepartment() == null;
    }

    public ActionForward prepareEditNonRegularTeachingService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Professorship professorship = getDomainObject(request, "professorshipOID");

        List<NonRegularTeachingServiceBean> nonRegularTeachingServiceBean = new ArrayList<NonRegularTeachingServiceBean>();
        for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {
            nonRegularTeachingServiceBean.add(new NonRegularTeachingServiceBean(shift, professorship));
        }
        request.setAttribute("person", professorship.getPerson());
        request.setAttribute("professorship", professorship);
        request.setAttribute("nonRegularTeachingServiceBean", nonRegularTeachingServiceBean);
        return mapping.findForward("editNonRegularTeachingService");
    }

    public ActionForward editNonRegularTeachingService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        List<NonRegularTeachingServiceBean> nonRegularTeachingServiceBeans = getRenderedObject();
        for (NonRegularTeachingServiceBean nonRegularTeachingServiceBean : nonRegularTeachingServiceBeans) {
            try {
                NonRegularTeachingService.createOrEdit(nonRegularTeachingServiceBean.getProfessorship(),
                        nonRegularTeachingServiceBean.getShift(), nonRegularTeachingServiceBean.getPercentage());
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
                request.setAttribute("professorship", nonRegularTeachingServiceBean.getProfessorship());
                request.setAttribute("nonRegularTeachingServiceBean", nonRegularTeachingServiceBeans);
                return mapping.findForward("editNonRegularTeachingService");
            }
        }
        RenderUtils.invalidateViewState();

        request.setAttribute("nonRegularTeacherBean", new NonRegularTeacherBean(nonRegularTeachingServiceBeans.iterator().next()
                .getProfessorship().getPerson()));

        return showNonRegularTeachingService(mapping, form, request, response);
    }

}
