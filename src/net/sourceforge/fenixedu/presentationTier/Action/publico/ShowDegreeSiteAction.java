package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão Create on 11/Nov/2003
 */
public class ShowDegreeSiteAction extends FenixContextDispatchAction {

    public ActionForward showDescription(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
        // request.setAttribute("executionPeriodOID", executionPeriodOId);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        Integer index = getFromRequest("index", request);
        request.setAttribute("index", index);
        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        // If degreeId is null then this was call by coordinator
        // Don't have a degreeId but a executionDegreeId
        // It's necessary read the executionDegree and obtain the correspond
        // degree
        if (degreeId == null) {

            // degree information
            Object[] args = { executionDegreeId };

            InfoExecutionDegree infoExecutionDegree = null;
            try {
                infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        null, "ReadExecutionDegreeByOID", args);
            } catch (FenixServiceException e) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (infoExecutionDegree == null || infoExecutionDegree.getInfoDegreeCurricularPlan() == null
                    || infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree() == null) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
            degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();
            request.setAttribute("degreeID", degreeId);
            request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal());

            // Read execution period
            InfoExecutionYear infoExecutionYear = infoExecutionDegree.getInfoExecutionYear();
            if (infoExecutionYear == null) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }

            Object[] args2 = { infoExecutionYear };

            List executionPeriods = null;
            try {
                executionPeriods = (List) ServiceManagerServiceFactory.executeService(null,
                        "ReadExecutionPeriodsByExecutionYear", args2);
            } catch (FenixServiceException e) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (executionPeriods == null || executionPeriods.size() <= 0) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }

            Collections.sort(executionPeriods, new BeanComparator("endDate"));

            InfoExecutionPeriod infoExecutionPeriod = ((InfoExecutionPeriod) executionPeriods
                    .get(executionPeriods.size() - 1));
            executionPeriodOId = infoExecutionPeriod.getIdInternal();

            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
                    .getIdInternal().toString());
            request.setAttribute("schoolYear", infoExecutionYear.getYear());

        }

        // degree information
        Object[] args = { executionPeriodOId, degreeId };

        InfoDegreeInfo infoDegreeInfo = null;
        try {
            infoDegreeInfo = (InfoDegreeInfo) ServiceManagerServiceFactory.executeService(null,
                    "ReadDegreeInfoByDegreeAndExecutionPeriod", args);
        } catch (FenixServiceException e) {
            errors.add("impossibleDegreeSite", new ActionError("error.public.DegreeInfoNotPresent"));
            saveErrors(request, errors);
            // return (new ActionForward(mapping.getInput()));
        }

        // execution degrees of this degree
        List executionDegreeList = null;
        try {
            executionDegreeList = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadExecutionDegreesByDegreeAndExecutionPeriod", args);
        } catch (FenixServiceException e) {
            errors.add("impossibleDegreeSite", new ActionError("error.impossibleExecutionDegreeList"));
            saveErrors(request, errors);
        }

        request.setAttribute("infoDegreeInfo", infoDegreeInfo);
        request.setAttribute("infoExecutionDegrees", executionDegreeList);

        if (inEnglish == null || inEnglish.booleanValue() == false) {
            return mapping.findForward("showDescription");
        }

        return mapping.findForward("showDescriptionEnglish");

    }

    public ActionForward showAccessRequirements(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
        // request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID,
        // executionPeriodOId);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        // degree information
        Object[] args = { executionPeriodOId, degreeId };

        InfoDegreeInfo infoDegreeInfo = null;
        try {
            infoDegreeInfo = (InfoDegreeInfo) ServiceManagerServiceFactory.executeService(null,
                    "ReadDegreeInfoByDegreeAndExecutionPeriod", args);
        } catch (FenixServiceException e) {
            errors.add("impossibleDegreeSite", new ActionError("error.public.DegreeInfoNotPresent"));
            saveErrors(request, errors);
            // return (new ActionForward(mapping.getInput()));

        }

        request.setAttribute("infoDegreeInfo", infoDegreeInfo);
        if (inEnglish == null || inEnglish.booleanValue() == false) {
            return mapping.findForward("showAccessRequirements");
        }

        return mapping.findForward("showAccessRequirementsEnglish");

    }

    public ActionForward showCurricularPlan(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);
        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);
        Integer index = getFromRequest("index", request);
        request.setAttribute("index", index);

        // if came in the request a executionDegreeId that it is necessary
        // find the correpond degree curricular plan

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = getInfoDegreeCurricularPlan(
                executionDegreeId, degreeId, degreeCurricularPlanId, mapping, request, errors);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        if (inEnglish == null || inEnglish.booleanValue() == false) {
            return mapping.findForward("showCurricularPlans");
        }

        return mapping.findForward("showCurricularPlansEnglish");

    }

    public ActionForward viewDegreeEvaluation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        ActionErrors errors = new ActionErrors();

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeId", executionDegreeId);

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        List executionPeriodList = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods",
                null);

        Object argsDegree[] = { degreeId };
        List allSummariesDegree = (List) ServiceUtils.executeService(userView,
                "ReadOldIquiriesSummaryByDegreeID", argsDegree);

        Iterator periodIter = executionPeriodList.iterator();
        while (periodIter.hasNext()) {
            InfoExecutionPeriod iep = (InfoExecutionPeriod) periodIter.next();
            boolean found = false;
            Iterator summaryIter = allSummariesDegree.listIterator();

            while (summaryIter.hasNext()) {
                InfoOldInquiriesSummary iois = (InfoOldInquiriesSummary) summaryIter.next();
                if (iep.getIdInternal() == iois.getKeyExecutionPeriod()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                periodIter.remove();
            }
        }

        Collections.sort(executionPeriodList, new Comparator() {
            public int compare(Object o1, Object o2) {
                InfoExecutionPeriod ep1 = (InfoExecutionPeriod) o1;
                InfoExecutionPeriod ep2 = (InfoExecutionPeriod) o2;
                return ep2.getBeginDate().compareTo(ep1.getBeginDate());
            }
        });

        request.setAttribute("executionPeriodList", executionPeriodList);

        // Getting information on the Degree
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = getInfoDegreeCurricularPlan(
                executionDegreeId, degreeId, degreeCurricularPlanId, mapping, request, errors);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);

        // Getting the summary information
        DynaActionForm degreeEvaluationForm = (DynaActionForm) actionForm;
        Integer executionPeriodId = (Integer) degreeEvaluationForm.get("executionPeriodId");

        if ((executionPeriodId != null) && (executionPeriodId.intValue() > 0)) {
            request.setAttribute("searchExecutionPeriodId", executionPeriodId);

            Iterator summaryIter = allSummariesDegree.listIterator();
            List oldInquiriesSummaries = new ArrayList();
            while (summaryIter.hasNext()) {
                InfoOldInquiriesSummary iois = (InfoOldInquiriesSummary) summaryIter.next();
                if (iois.getExecutionPeriod().getIdInternal().equals(executionPeriodId)) {
                    if ((iois.getNumberEnrollments().intValue() > 0)
                            && (iois.getNumberApproved().intValue() >= 0)
                            && (iois.getNumberEvaluated().intValue() > 0))
                        oldInquiriesSummaries.add(iois);
                }
            }

            if (oldInquiriesSummaries.size() > 0) {
                Collections.sort(oldInquiriesSummaries);
                request.setAttribute("oldInquiriesSummaries", oldInquiriesSummaries);

                int denominatorCourses = 0, denominatorTeachers = 0;
                double numeratorCourses = 0, numeratorTeachers = 0;
                Iterator iter = oldInquiriesSummaries.iterator();

                while (iter.hasNext()) {
                    InfoOldInquiriesSummary iois = (InfoOldInquiriesSummary) iter.next();
                    if ((iois.getNumberAnswers() != null) && (iois.getNumberAnswers().intValue() > 0)
                            && (iois.getNumberEnrollments() != null)
                            && (iois.getNumberEnrollments().intValue() > 0)) {
                        iois.setRepresentationQuota(new Double(
                                (iois.getNumberAnswers().doubleValue() / iois.getNumberEnrollments()
                                        .doubleValue()) * 100));
                    }

                    if ((iois.getAverage2_8() != null) && (iois.getAverage2_8().doubleValue() >= 0)
                            && (iois.getRepresentationQuota().doubleValue() > 10)) {
                        numeratorCourses += iois.getAverage2_8().doubleValue();
                        denominatorCourses++;
                    }
                    if ((iois.getAverage3_11() != null) && (iois.getAverage3_11().doubleValue() >= 0)
                            && (iois.getRepresentationQuota().doubleValue() > 10)) {
                        numeratorTeachers += iois.getAverage3_11().doubleValue();
                        denominatorTeachers++;
                    }
                }

                if (denominatorCourses > 0) {
                    request.setAttribute("averageAppreciationCourses", new Double(numeratorCourses
                            / denominatorCourses));
                }
                if (denominatorTeachers > 0) {
                    request.setAttribute("averageAppreciationTeachers", new Double(numeratorTeachers
                            / denominatorTeachers));
                }

            } else {

                request.setAttribute("emptyOldInquiriesSummaries", Boolean.TRUE);
            }

        }

        return mapping.findForward("viewDegreeEvaluation");
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

    private InfoDegreeCurricularPlan getInfoDegreeCurricularPlan(Integer executionDegreeId,
            Integer degreeId, Integer degreeCurricularPlanId, ActionMapping mapping,
            HttpServletRequest request, ActionErrors errors) {
        // if came in the request a executionDegreeId that it is necessary
        // find the correpond degree curricular plan
        if (executionDegreeId != null) {
            Object[] args = { executionDegreeId };

            InfoExecutionDegree infoExecutionDegree = null;
            try {
                infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        null, "ReadExecutionDegreeByOID", args);
            } catch (FenixServiceException e) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            } catch (FenixFilterException e) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
            }
            if (infoExecutionDegree == null || infoExecutionDegree.getInfoDegreeCurricularPlan() == null) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                return null;
            }
            request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
            request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal());
            return infoExecutionDegree.getInfoDegreeCurricularPlan();
        }
        Object[] args = { degreeId };

        List infoDegreeCurricularPlanList = null;
        try {
            infoDegreeCurricularPlanList = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadPublicDegreeCurricularPlansByDegree", args);
        } catch (FenixServiceException e) {
            errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));

            return null;
        } catch (FenixFilterException e) {
            errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));

            return null;
        }
        // order the list by state and next by begin date
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("state.degreeState"));
        comparatorChain.addComparator(new BeanComparator("initialDate"), true);

        Collections.sort(infoDegreeCurricularPlanList, comparatorChain);

        request.setAttribute("infoDegreeCurricularPlanList", infoDegreeCurricularPlanList);

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) infoDegreeCurricularPlanList
                .get(0);
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);

        // if came in the request a degreeCurricularPlanId that it is
        // necessary
        // find information about this degree curricular plan
        if (degreeCurricularPlanId != null) {
            Iterator iterator = infoDegreeCurricularPlanList.iterator();
            while (iterator.hasNext()) {
                InfoDegreeCurricularPlan infoDegreeCurricularPlanElem = (InfoDegreeCurricularPlan) iterator
                        .next();
                if (infoDegreeCurricularPlanElem.getIdInternal().equals(degreeCurricularPlanId)) {

                    return infoDegreeCurricularPlanElem;
                }
            }
        }
        return infoDegreeCurricularPlan;

    }
}