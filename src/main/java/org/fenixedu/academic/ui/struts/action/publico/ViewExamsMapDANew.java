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
/**
 * Project sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package org.fenixedu.academic.ui.struts.action.publico;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.dto.InfoExamsMap;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.ReadFilteredExamsMap;
import org.fenixedu.academic.ui.struts.action.base.FenixContextDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
@Mapping(module = "publico", path = "/viewExamsMapNew", input = "/notFound.do", formBean = "examForm", scope = "request",
        validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "viewExecutionCourseByCode", path = "/siteViewer.do?method=executionCourseViewer"),
        @Forward(name = "viewExamsMap", path = "viewExamsMap") })
@Exceptions(
        value = { @ExceptionHandling(
                type = org.fenixedu.academic.service.services.resourceAllocationManager.exams.ReadFilteredExamsMap.ExamsPeriodUndefined.class,
                key = "error.exams.period.undefined",
                handler = org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ViewExamsMapDANew extends FenixContextDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {
        // inEnglish
        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        // index
        String index = getFromRequest("index", request);
        request.setAttribute("index", index);

        // degreeID
        String degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        // degreeCurricularPlanID
        String degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        // curricularYearList
        List<Integer> curricularYears = (List<Integer>) request.getAttribute("curricularYearList");
        if (curricularYears == null) {
            curricularYears = FenixFramework.<Degree> getDomainObject(degreeId).buildFullCurricularYearList();
        }
        request.setAttribute("curricularYearList", curricularYears);

        // lista
        List lista = (List) request.getAttribute("lista");
        request.setAttribute("lista", lista);

        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        infoExecutionDegree =
                infoExecutionDegree == null || infoExecutionDegree.getExecutionDegree() == null ? findLatestInfoExecutionDegree(degreeCurricularPlan) : infoExecutionDegree;
        if (infoExecutionDegree == null || infoExecutionDegree.getExecutionDegree() == null) {
            request.setAttribute("infoDegreeCurricularPlan", "");
        } else {
            request.setAttribute("infoDegreeCurricularPlan", new InfoDegreeCurricularPlan(degreeCurricularPlan));
        }
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);

        // indice
        String indice = getFromRequest("indice", request);
        request.setAttribute("indice", indice);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId().toString());

        // executionDegreeID
        String executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        request.removeAttribute(PresentationConstants.INFO_EXAMS_MAP);
        try {
            final User userView = getUserView(request);
            final InfoExamsMap infoExamsMap =
                    ReadFilteredExamsMap.runReadFilteredExamsMap(infoExecutionDegree, curricularYears, infoExecutionPeriod);
            request.setAttribute(PresentationConstants.INFO_EXAMS_MAP, infoExamsMap);
        } catch (NonExistingServiceException e) {
            return mapping.findForward("viewExamsMap");
        }

        return mapping.findForward("viewExamsMap");
    }

    private InfoExecutionDegree findLatestInfoExecutionDegree(DegreeCurricularPlan degreeCurricularPlan) {
        final Set<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegreesSet();
        final ExecutionDegree executionDegree =
                Collections.max(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        return new InfoExecutionDegree(executionDegree);
    }

}
