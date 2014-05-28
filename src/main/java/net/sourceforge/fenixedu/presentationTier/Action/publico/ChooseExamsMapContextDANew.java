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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDANew extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);
        Degree degree = FenixFramework.getDomainObject(degreeId);
        if (degree != null) {
            OldCmsSemanticURLHandler.selectSite(request, degree.getSite());
        }
        request.setAttribute("degree", degree);

        String executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        String degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
        if (executionPeriodsLabelValueList.size() > 1) {
            request.setAttribute("lista", executionPeriodsLabelValueList);
        } else {
            request.removeAttribute("lista");
        }

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);

        request.removeAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD);

        return mapping.findForward("prepare");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final ActionErrors errors = new ActionErrors();
        final DynaActionForm chooseExamContextoForm = (DynaActionForm) form;

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);

        // index
        String indexValue = getFromRequest("index", request);
        request.setAttribute("index", indexValue);

        // degreeID
        String degreeId = (String) chooseExamContextoForm.get("degreeID");
        request.setAttribute("degreeID", degreeId);
        final Degree degree = FenixFramework.getDomainObject(degreeId);
        if (degree != null) {
            OldCmsSemanticURLHandler.selectSite(request, degree.getSite());
        }
        request.setAttribute("degree", degree);

        // curricularYearList
        final Boolean selectAllCurricularYears = (Boolean) chooseExamContextoForm.get("selectAllCurricularYears");
        final List<Integer> curricularYears = buildCurricularYearList(selectAllCurricularYears, degree, chooseExamContextoForm);
        request.setAttribute("curricularYearList", curricularYears);

        // degreeCurricularPlanID
        String degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        final DegreeCurricularPlan degreeCurricularPlan;
        if (StringUtils.isEmpty(degreeCurricularPlanId)) {
            degreeCurricularPlan = degree.getMostRecentDegreeCurricularPlan();
        } else {
            degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        }

        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlan.getExternalId());

            if (!degreeCurricularPlan.getDegree().getExternalId().equals(degreeId)) {
                throw new FenixActionException();
            }

            // lista
            List<LabelValueBean> executionPeriodsLabelValueList =
                    buildExecutionPeriodsLabelValueList(degreeCurricularPlan.getExternalId());
            if (executionPeriodsLabelValueList.size() > 1) {
                request.setAttribute("lista", executionPeriodsLabelValueList);
            } else {
                request.removeAttribute("lista");
            }

            // infoDegreeCurricularPlan
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        }

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        String executionPeriodID = (String) chooseExamContextoForm.get("indice");
        if (executionPeriodID != null && !executionPeriodID.equals("")) {
            infoExecutionPeriod = ReadExecutionPeriodByOID.run(executionPeriodID);
        }
        request.setAttribute("indice", infoExecutionPeriod.getExternalId());
        chooseExamContextoForm.set("indice", infoExecutionPeriod.getExternalId());
        RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId().toString());

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        ExecutionDegree executionDegree = null;

        if (degreeCurricularPlan != null) {
            executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionSemester.getExecutionYear());
            if (executionDegree == null) {
                executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

                if (executionDegree != null) {
                    infoExecutionPeriod =
                            InfoExecutionPeriod.newInfoFromDomain(executionDegree.getExecutionYear().getExecutionSemesterFor(1));
                    request.setAttribute("indice", infoExecutionPeriod.getExternalId());
                    chooseExamContextoForm.set("indice", infoExecutionPeriod.getExternalId());
                    RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
                    request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
                    request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId()
                            .toString());
                }
            }
        }

        if (executionDegree != null) {
            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
            request.setAttribute("executionDegreeID", infoExecutionDegree.getExternalId().toString());
            RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
        } else {
            return mapping.findForward("viewExamsMap");
        }

        return mapping.findForward("showExamsMap");
    }

    private List<Integer> buildCurricularYearList(Boolean allCurricularYears, Degree degree, DynaActionForm chooseExamContextoForm) {
        if (allCurricularYears == null || allCurricularYears) {
            return degree.buildFullCurricularYearList();
        } else {
            return buildSelectedList(chooseExamContextoForm);
        }
    }

    private List<Integer> buildSelectedList(DynaActionForm chooseExamContextoForm) {
        String[] selectedCurricularYears = (String[]) chooseExamContextoForm.get("selectedCurricularYears");

        List<Integer> result = new ArrayList<Integer>(selectedCurricularYears.length);
        for (String selectedCurricularYear : selectedCurricularYears) {
            result.add(Integer.valueOf(selectedCurricularYear));
        }

        return result;
    }

}