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
/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric.ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminMarksheetApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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
                    ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear
                            .runReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear(executionDegreeBean
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
