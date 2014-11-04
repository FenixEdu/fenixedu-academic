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
/*
 * Created on Oct 7, 2004
 */
package org.fenixedu.academic.ui.struts.action.commons.curriculumHistoric;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.dto.administrativeOffice.lists.ExecutionDegreeListBean;
import org.fenixedu.academic.service.services.commons.curriculumHistoric.ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminMarksheetApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author nmgo
 * @author lmre
 */
@StrutsFunctionality(app = AcademicAdminMarksheetApp.class, path = "consult", titleKey = "link.consult")
@Mapping(path = "/chooseExecutionYearAndDegreeCurricularPlan", module = "academicAdministration",
        formBean = "executionYearDegreeCurricularPlanForm")
@Forwards({ @Forward(name = "chooseExecutionYear", path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp"),
        @Forward(name = "showActiveCurricularCourses", path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp") })
public class DegreeCurricularPlanExecutionYearDispacthAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        request.setAttribute("executionDegreeBean", new ExecutionDegreeListBean());

        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward chooseDegree(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final ExecutionDegreeListBean executionDegreeBean = getRenderedObject("academicInterval");
        executionDegreeBean.setDegreeCurricularPlan(null);
        executionDegreeBean.setAcademicInterval(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("executionDegreeBean", executionDegreeBean);
        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward chooseDegreeCurricularPlan(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final ExecutionDegreeListBean executionDegreeBean = getRenderedObject("academicInterval");
        executionDegreeBean.setAcademicInterval(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("executionDegreeBean", executionDegreeBean);
        return mapping.findForward("chooseExecutionYear");
    }

    @SuppressWarnings("unchecked")
    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        final ExecutionDegreeListBean executionDegreeBean = getRenderedObject("academicInterval");
        try {
            final SortedSet<DegreeModuleScope> degreeModuleScopes =
                    ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear.run(executionDegreeBean
                            .getDegreeCurricularPlan().getExternalId(), executionDegreeBean.getAcademicInterval());

            final ActionErrors errors = new ActionErrors();
            if (degreeModuleScopes.isEmpty()) {
                errors.add("noDegreeCurricularPlan", new ActionError("error.nonExisting.AssociatedCurricularCourses"));
                saveErrors(request, errors);
            } else {
                request.setAttribute("degreeModuleScopes", degreeModuleScopes);
            }
        } catch (NotAuthorizedException ex1) {
            addActionMessage(request, "message.not-authorized");
            return mapping.findForward("chooseExecutionYear");
        }

        request.setAttribute(PresentationConstants.ACADEMIC_INTERVAL, executionDegreeBean.getAcademicInterval());
        request.setAttribute("degreeCurricularPlan", executionDegreeBean.getDegreeCurricularPlan());

        return mapping.findForward("showActiveCurricularCourses");
    }
}
