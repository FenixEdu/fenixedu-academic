package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
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

public class ShowDegreeSiteAction extends FenixContextDispatchAction {

    public static int ANNOUNCEMENTS_NUMBER = 3;
    
    public ActionForward showDescription(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareInfo(request);

        return mapping.findForward("showDescription");
    }

    private void prepareInfo(HttpServletRequest request) throws FenixActionException {
        // inEnglish
        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);
        
        // degree
        Integer degreeId = getFromRequest("degreeID", request);
        Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        if (degree == null) {
            throw new FenixActionException();
        }
        request.setAttribute("degreeID", degreeId);
        request.setAttribute("degree", degree);

        Unit unit = degree.getUnit();
        if (unit != null) {
            AnnouncementBoard board = null;
            for (AnnouncementBoard unitBoard : unit.getBoards()) {
                if (unitBoard.isPublicToRead()) {
                    board = unitBoard;
                    break;
                }
            }
            
            if (board != null) {
                List<Announcement> announcements = board.getActiveAnnouncements();
                announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
                request.setAttribute("announcements", announcements);
            }
        }
        
        // execution year
        ExecutionYear executionYearToShow = getExecutionYearToShow(request, degree);
        request.setAttribute("executionYear", executionYearToShow);

        // info
        getInfoToShow(request, degree, executionYearToShow);
    }
    
    private ExecutionYear getExecutionYearToShow(HttpServletRequest request, Degree degree) throws FenixActionException {
        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        if (executionDegreeId != null) {
            // coordinator call
            request.setAttribute("executionDegreeID", executionDegreeId);
            
            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
            if (executionDegree == null || !executionDegree.getDegreeCurricularPlan().getDegree().equals(degree)) {
                throw new FenixActionException();
            }
            
            return executionDegree.getExecutionYear();
        } else {
            final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            
            List<ExecutionYear> whenDegreeIsExecuted = degree.getDegreeCurricularPlansExecutionYears();
            if (whenDegreeIsExecuted.isEmpty()) {
        	return currentExecutionYear;
            } else {
                final ExecutionYear firstExecutionYear = whenDegreeIsExecuted.get(0);
                final ExecutionYear lastExecutionYear = whenDegreeIsExecuted.get(whenDegreeIsExecuted.size() - 1);
                
                if (whenDegreeIsExecuted.contains(currentExecutionYear)) {
                    return currentExecutionYear;
                } else {
                    if (currentExecutionYear.isBefore(firstExecutionYear)) {
                        return firstExecutionYear;
                    } else {
                        return lastExecutionYear;
                    }
                }
            }
        }
    }

    private void getInfoToShow(HttpServletRequest request, Degree degree, ExecutionYear executionYearToShow) throws FenixActionException {
        // degree info
        DegreeInfo degreeInfoToShow = executionYearToShow.getDegreeInfo(degree);
        if (degreeInfoToShow == null) {
            degreeInfoToShow = degree.getMostRecentDegreeInfo(executionYearToShow);
        }
        if (degreeInfoToShow != null) {
            request.setAttribute("degreeInfo", degreeInfoToShow);
        }
        
        // campus
        Collection<Campus> campus = degree.getCampus(executionYearToShow);
        if (campus.isEmpty()) {
            campus = degree.getCurrentCampus();
        }
        request.setAttribute("campus", campus);
        
        // responsible coordinators
        Collection<Teacher> responsibleCoordinatorsTeachers = degree.getResponsibleCoordinatorsTeachers(executionYearToShow);
        if (responsibleCoordinatorsTeachers.isEmpty()) {
            responsibleCoordinatorsTeachers = degree.getCurrentResponsibleCoordinatorsTeachers();
        }
        request.setAttribute("responsibleCoordinatorsTeachers", responsibleCoordinatorsTeachers);

    }

    public ActionForward showAccessRequirements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareInfo(request);
        
        return mapping.findForward("showAccessRequirements");
    }

    public ActionForward showProfessionalStatus(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareInfo(request);
        
        return mapping.findForward("showProfessionalStatus");
    }
    
    public ActionForward showCurricularPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);
        request.setAttribute("degree", rootDomainObject.readDegreeByOID(degreeId));
        
        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);
        
        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);
        
        Integer index = getFromRequest("index", request);
        request.setAttribute("index", index);

        final ActionErrors errors = new ActionErrors();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = getInfoDegreeCurricularPlan(executionDegreeId, degreeId, degreeCurricularPlanId, mapping, request, errors);
        if (infoDegreeCurricularPlan == null) {
            errors.add("impossibleDegreeSite", new ActionError("error.impossibleCurricularPlan"));
            saveErrors(request, errors);
        } else {
            request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);            
        }

        return mapping.findForward("showCurricularPlans");
    }
    
    public ActionForward viewDegreeEvaluation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);

        Integer executionDegreeId = getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeId", executionDegreeId);

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        Object argsDegree[] = { degreeId };
        List<InfoOldInquiriesSummary> allSummariesDegree = (List<InfoOldInquiriesSummary>) ServiceUtils.executeService(userView, "ReadOldIquiriesSummaryByDegreeID", argsDegree);

        List<InfoExecutionPeriod> infoExecutionPeriods = (List<InfoExecutionPeriod>) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);
        List<InfoExecutionPeriod> executionPeriodList = new ArrayList<InfoExecutionPeriod>(infoExecutionPeriods);
        for (InfoExecutionPeriod iep : infoExecutionPeriods) {
            boolean found = false;

            for (InfoOldInquiriesSummary iois : allSummariesDegree) {
                if (iep.equals(iois.getExecutionPeriod())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                executionPeriodList.remove(iep);
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

        final ActionErrors errors = new ActionErrors();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = getInfoDegreeCurricularPlan(executionDegreeId, degreeId, degreeCurricularPlanId, mapping, request, errors);
        if (infoDegreeCurricularPlan == null) {
            errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);

        // Getting the summary information
        DynaActionForm degreeEvaluationForm = (DynaActionForm) actionForm;
        Integer executionPeriodId = (Integer) degreeEvaluationForm.get("executionPeriodId");

        if ((executionPeriodId != null) && (executionPeriodId.intValue() > 0)) {
            request.setAttribute("searchExecutionPeriodId", executionPeriodId);

            List<InfoOldInquiriesSummary> oldInquiriesSummaries = new ArrayList<InfoOldInquiriesSummary>();
            for (InfoOldInquiriesSummary iois : allSummariesDegree) {
                if (iois.getExecutionPeriod().getIdInternal().equals(executionPeriodId)) {
                    if ((iois.getNumberEnrollments() > 0)
                            && (iois.getNumberApproved() >= 0)
                            && (iois.getNumberEvaluated() > 0))
                        oldInquiriesSummaries.add(iois);
                }
            }

            if (!oldInquiriesSummaries.isEmpty()) {
                Collections.sort(oldInquiriesSummaries);
                request.setAttribute("oldInquiriesSummaries", oldInquiriesSummaries);

                int denominatorCourses = 0, denominatorTeachers = 0;
                double numeratorCourses = 0, numeratorTeachers = 0;

                for (InfoOldInquiriesSummary iois : oldInquiriesSummaries) {
                    if ((iois.getNumberAnswers() != null) 
                            && (iois.getNumberAnswers() > 0)
                            && (iois.getNumberEnrollments() != null)
                            && (iois.getNumberEnrollments() > 0)) {
                        iois.setRepresentationQuota(Double.valueOf((iois.getNumberAnswers() / iois.getNumberEnrollments()) * 100));
                    }

                    if ((iois.getAverage2_8() != null) 
                            && (iois.getAverage2_8() >= 0)
                            && (iois.getRepresentationQuota() > 10)) {
                        numeratorCourses += iois.getAverage2_8();
                        denominatorCourses++;
                    }
                    if ((iois.getAverage3_11() != null) 
                            && (iois.getAverage3_11() >= 0)
                            && (iois.getRepresentationQuota() > 10)) {
                        numeratorTeachers += iois.getAverage3_11();
                        denominatorTeachers++;
                    }
                }

                if (denominatorCourses > 0) {
                    request.setAttribute("averageAppreciationCourses", Double.valueOf(numeratorCourses / denominatorCourses));
                }
                if (denominatorTeachers > 0) {
                    request.setAttribute("averageAppreciationTeachers", Double.valueOf(numeratorTeachers / denominatorTeachers));
                }
            } else {
                request.setAttribute("emptyOldInquiriesSummaries", Boolean.TRUE);
            }
        }

        return mapping.findForward("viewDegreeEvaluation");
    }

    private InfoDegreeCurricularPlan getInfoDegreeCurricularPlan(Integer executionDegreeId,
            Integer degreeId, Integer degreeCurricularPlanId, ActionMapping mapping,
            HttpServletRequest request, ActionErrors errors) {

        if (executionDegreeId != null) {
            // if came in the request a executionDegreeId that it is necessary find the corresponding degree curricular plan
            return getByExecutionDegreeId(executionDegreeId, request);
        } 
        
        return getByDegreeIdOrDegreeCurricularPlanId(degreeId, degreeCurricularPlanId, request);    
    }

    private InfoDegreeCurricularPlan getByExecutionDegreeId(Integer executionDegreeId, HttpServletRequest request) {
        try {
            final Object[] args = { executionDegreeId };
            final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(null, "ReadExecutionDegreeByOID", args);
            if (infoExecutionDegree == null || infoExecutionDegree.getInfoDegreeCurricularPlan() == null) {
                return null;
            }
            
            request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
            request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal());
            return infoExecutionDegree.getInfoDegreeCurricularPlan();
        } catch (Exception e) {
            return null;
        }
    }

    private InfoDegreeCurricularPlan getByDegreeIdOrDegreeCurricularPlanId(Integer degreeId, Integer degreeCurricularPlanId, HttpServletRequest request) {
        final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlanList;
        try {
            final Object[] args = { degreeId };
            infoDegreeCurricularPlanList = (List<InfoDegreeCurricularPlan>) ServiceManagerServiceFactory.executeService(null, "ReadPublicDegreeCurricularPlansByDegree", args);
            if (infoDegreeCurricularPlanList.isEmpty()) {
                return null;   
            }
            
            // order the list by state and next by begin date
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("state"));
            comparatorChain.addComparator(new BeanComparator("initialDate"), true);
            Collections.sort(infoDegreeCurricularPlanList, comparatorChain);
            
            request.setAttribute("infoDegreeCurricularPlanList", infoDegreeCurricularPlanList);
        } catch (Exception e) {
            return null;
        }

        // if came in the request a degreeCurricularPlanId that it is necessary find information about this degree curricular plan
        if (degreeCurricularPlanId != null) {
            for (InfoDegreeCurricularPlan infoDegreeCurricularPlanElem : infoDegreeCurricularPlanList) {
                if (infoDegreeCurricularPlanElem.getIdInternal().equals(degreeCurricularPlanId)) {
                    request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlanElem);
                    return infoDegreeCurricularPlanElem;
                }
            }
        } else {
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoDegreeCurricularPlanList.get(0);
            request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
            return infoDegreeCurricularPlan;
        }
        
        return null;
    }

}
