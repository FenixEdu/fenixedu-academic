package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão Created on 9/Out/2003
 */
public class ShowDegreeCurricularPlanAction extends FenixContextDispatchAction {

    public ActionForward showCurricularPlan(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        /** ************************************************* */

        Object argsLerLicenciaturas[] = { degreeCurricularPlanId };

        InfoExecutionDegree infoExecutionDegreeForPeriod = new InfoExecutionDegree();

        List infoExecutionDegreeList = new ArrayList();
        try {
            infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
                    "ReadPublicExecutionDegreeByDCPID", argsLerLicenciaturas);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List executionPeriodsLabelValueList = new ArrayList();
        InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) infoExecutionDegreeList.get(0);

        executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree1
                .getInfoExecutionYear().getYear(), ""
                + infoExecutionDegree1.getInfoExecutionYear().getIdInternal()));

        for (int i = 1; i < infoExecutionDegreeList.size(); i++) {
            infoExecutionDegreeForPeriod = (InfoExecutionDegree) infoExecutionDegreeList.get(i);

            if (infoExecutionDegreeForPeriod.getInfoExecutionYear().getYear() != infoExecutionDegree1
                    .getInfoExecutionYear().getYear()) {
                executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegreeForPeriod
                        .getInfoExecutionYear().getYear(), ""
                        + infoExecutionDegreeForPeriod.getInfoExecutionYear().getIdInternal()));
                infoExecutionDegree1 = (InfoExecutionDegree) infoExecutionDegreeList.get(i);
            }
        }

        if (executionPeriodsLabelValueList.size() > 1) {
            request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD,
                    executionPeriodsLabelValueList);

        } else {
            request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);
        }
        /*------------------------------------*/

        /* Criar o bean de anos curricutares */
        List anosCurriculares = new ArrayList();
        anosCurriculares.add(new LabelValueBean("Todos os anos", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));
        request.setAttribute("curricularYearList", anosCurriculares);

        /* ------------------------------------ */

        // If executionPeriod was previously selected,form has that value as
        // default
        DynaActionForm indexForm = (DynaActionForm) actionForm;

        InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        Integer curricularYear = (Integer) indexForm.get("curYear");

        if (selectedExecutionPeriod != null) {
           
            indexForm.set("indice", (Integer) indexForm.get("indice"));
            indexForm.set("curYear", new Integer(anosCurriculares.indexOf(anosCurriculares
                    .get(curricularYear.intValue()))));
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod
                    .getIdInternal().toString());

        } else {
            System.out.println("ERROR - InitateSessionDA: No executionPeriod in request");
        }

        /** *************************************************** */

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);

        Integer index = getFromRequest("index", request);
        request.setAttribute("index", index);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);
        List activeCurricularCourseScopes = null;

        InfoExecutionDegree infoExecutionDegree = RequestUtils.getExecutionDegreeFromRequest(request,
                selectedExecutionPeriod.getInfoExecutionYear());
        if (infoExecutionDegree == null) {
            infoExecutionDegree = new InfoExecutionDegree();
            Object arg[] = { degreeCurricularPlanId,
                    selectedExecutionPeriod.getInfoExecutionYear().getIdInternal() };

            try {
                infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
                        "ReadPublicExecutionDegreeByDCPID", arg);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

        }

        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
        InfoExecutionCourse executionCourse = new InfoExecutionCourse();
        executionCourse.setInfoExecutionPeriod(selectedExecutionPeriod);

        if (curricularYear.intValue() != 0) {

            Object[] args = { infoExecutionDegree, selectedExecutionPeriod, curricularYear };
            try {
                activeCurricularCourseScopes = (List) ServiceManagerServiceFactory.executeService(null,
                        "ReadActiveDegreeCurricularPlanByID", args);
            } catch (FenixServiceException e) {
                errors
                        .add("impossibleCurricularPlan", new ActionError(
                                "error.impossibleCurricularPlan"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        } else {

            Object[] args = { degreeCurricularPlanId, executionPeriodOId };
            try {
                activeCurricularCourseScopes = (List) ServiceManagerServiceFactory.executeService(null,
                        "ReadActiveDegreeCurricularPlanByID", args);
            } catch (FenixServiceException e) {
                errors
                        .add("impossibleCurricularPlan", new ActionError(
                                "error.impossibleCurricularPlan"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        }

        if (activeCurricularCourseScopes == null || activeCurricularCourseScopes.size() <= 0) {
            errors.add("noDegreeCurricularPlan", new ActionError("error.impossibleCurricularPlan"));
            saveErrors(request, errors);
        }
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = ((InfoCurricularCourseScope) ((List) activeCurricularCourseScopes
                .get(0)).get(0)).getInfoCurricularCourse().getInfoDegreeCurricularPlan();

        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        request.setAttribute("allActiveCurricularCourseScopes", activeCurricularCourseScopes);

        if (inEnglish == null || inEnglish.booleanValue() == false) {
            return mapping.findForward("showDegreeCurricularPlan");
        }

        return mapping.findForward("showDegreeCurricularPlanEnglish");

    }

    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterCode = new Integer(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterCode;
    }

    private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        Boolean parameterBoolean = null;

        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterBoolean = new Boolean(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterBoolean;
    }
}