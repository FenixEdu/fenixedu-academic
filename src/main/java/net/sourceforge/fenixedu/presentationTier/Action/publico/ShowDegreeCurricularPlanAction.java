package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadActiveDegreeCurricularPlanByID;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadPublicExecutionDegreeByDCPID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class ShowDegreeCurricularPlanAction extends FenixContextDispatchAction {

    public ActionForward showCurricularPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        return showOldDegreeCurricularPlan(mapping, actionForm, request, degreeCurricularPlan.getIdInternal());
    }

    private ActionForward showOldDegreeCurricularPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            Integer degreeCurricularPlanId) throws  FenixActionException {
        final ActionErrors errors = new ActionErrors();

        InfoExecutionDegree infoExecutionDegreeForPeriod = null;
        InfoExecutionDegree infoExecutionDegree1 = null;
        try {

            final List<InfoExecutionDegree> infoExecutionDegreeList =
                    ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId);

            if (!infoExecutionDegreeList.isEmpty()) {
                List<LabelValueBean> executionPeriodsLabelValueList = new ArrayList<LabelValueBean>();
                infoExecutionDegree1 = infoExecutionDegreeList.get(0);
                executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree1.getInfoExecutionYear().getYear(), ""
                        + infoExecutionDegree1.getInfoExecutionYear().getIdInternal()));

                for (int i = 1; i < infoExecutionDegreeList.size(); i++) {
                    infoExecutionDegreeForPeriod = infoExecutionDegreeList.get(i);

                    if (infoExecutionDegreeForPeriod.getInfoExecutionYear().getYear() != infoExecutionDegree1
                            .getInfoExecutionYear().getYear()) {
                        executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegreeForPeriod.getInfoExecutionYear()
                                .getYear(), "" + infoExecutionDegreeForPeriod.getInfoExecutionYear().getIdInternal()));
                        infoExecutionDegree1 = infoExecutionDegreeList.get(i);
                    }
                }

                request.setAttribute(PresentationConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List<LabelValueBean> anosCurriculares =
                constructCurricularYearLabelValueBeans(infoExecutionDegree1.getExecutionDegree().getDegree());
        request.setAttribute("curricularYearList", anosCurriculares);

        DynaActionForm indexForm = (DynaActionForm) actionForm;

        Integer curricularYear = (Integer) indexForm.get("curYear");
        if (curricularYear == null) {
            curricularYear = Integer.valueOf(0);
        }

        if (indexForm.get("indice") == null) {
            indexForm.set("indice", infoExecutionDegreeForPeriod.getInfoExecutionYear().getIdInternal());
            curricularYear = Integer.valueOf(0);
        }

        // If executionPeriod was previously selected,form has that value as
        // default
        RequestUtils.getExecutionPeriodFromRequest(request);
        InfoExecutionPeriod selectedExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        if (selectedExecutionPeriod != null) {
            indexForm.set("indice", indexForm.get("indice"));
            indexForm.set("curYear", Integer.valueOf(anosCurriculares.indexOf(anosCurriculares.get(curricularYear))));
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod.getIdInternal().toString());
        }

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        InfoExecutionDegree infoExecutionDegree =
                RequestUtils.getExecutionDegreeFromRequest(request, selectedExecutionPeriod.getInfoExecutionYear());
        if (infoExecutionDegree == null) {

            infoExecutionDegree =
                    ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId, selectedExecutionPeriod.getInfoExecutionYear()
                            .getIdInternal());
        }
        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);

        List<InfoCurricularCourseScope> activeCurricularCourseScopes = null;
        if (curricularYear != 0) {
            final Object[] args = { infoExecutionDegree, selectedExecutionPeriod, curricularYear, getLocale(request) };
            try {
                activeCurricularCourseScopes =
                        ReadActiveDegreeCurricularPlanByID.runReadActiveDegreeCurricularPlanByID(infoExecutionDegree,
                                selectedExecutionPeriod, curricularYear, getLocale(request));
            } catch (FenixServiceException e) {
                return new ActionForward(mapping.getInput());
            }
        } else {
            try {
                activeCurricularCourseScopes =
                        ReadActiveDegreeCurricularPlanByID.runReadActiveDegreeCurricularPlanByID(degreeCurricularPlanId,
                                selectedExecutionPeriod.getIdInternal(), getLocale(request), "");
            } catch (FenixServiceException e) {
                return new ActionForward(mapping.getInput());
            }
        }

        if (activeCurricularCourseScopes == null || activeCurricularCourseScopes.isEmpty()) {
            errors.add("noDegreeCurricularPlan", new ActionError("error.impossibleCurricularPlan"));
            saveErrors(request, errors);
        }
        request.setAttribute("allActiveCurricularCourseScopes", activeCurricularCourseScopes);

        return mapping.findForward("showDegreeCurricularPlan");
    }

    private List<LabelValueBean> constructCurricularYearLabelValueBeans(final Degree degree) {
        List<LabelValueBean> anosCurriculares = new ArrayList<LabelValueBean>();
        anosCurriculares.add(new LabelValueBean("--", ""));
        for (final Integer year : degree.buildFullCurricularYearList()) {
            anosCurriculares.add(new LabelValueBean(String.valueOf(year), String.valueOf(year)));
        }

        return anosCurriculares;
    }

}