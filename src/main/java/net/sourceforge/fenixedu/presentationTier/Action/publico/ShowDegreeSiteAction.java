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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegreeCurricularPlansByDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.inquiries.ReadOldIquiriesSummaryByDegreeID;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/showDegreeSite", input = "/showDegrees.do?method=nonMaster",
        attribute = "viewDegreeEvaluationForm", formBean = "viewDegreeEvaluationForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showDescriptionEnglish", path = "showDescriptionEnglish"),
        @Forward(name = "showProfessionalStatus", path = "showProfessionalStatus"),
        @Forward(name = "viewDegreeEvaluation", path = "viewDegreeEvaluation"),
        @Forward(name = "showDescription", path = "showDescription"),
        @Forward(name = "showCurricularPlans", path = "showCurricularPlans"),
        @Forward(name = "showAccessRequirementsEnglish", path = "showAccessRequirementsEnglish"),
        @Forward(name = "showAccessRequirements", path = "showAccessRequirements"),
        @Forward(name = "showCurricularPlansEnglish", path = "showCurricularPlansEnglish") })
public class ShowDegreeSiteAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ContextUtils.setExecutionPeriodContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public static int ANNOUNCEMENTS_NUMBER = 3;

    public ActionForward showDescription(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareInfo(request);

        return mapping.findForward("showDescription");
    }

    private void prepareInfo(HttpServletRequest request) throws FenixActionException {
        // inEnglish
        Boolean inEnglish = FenixContextDispatchAction.getFromRequestBoolean("inEnglish", request);
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);

        // degree
        Degree degree = getDegree(request);
        if (degree == null) {
            throw new FenixActionException();
        }
        request.setAttribute("degreeID", degree.getExternalId());
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
        String executionDegreeId = FenixContextDispatchAction.getFromRequest("executionDegreeID", request);
        if (executionDegreeId != null) {
            // coordinator call
            request.setAttribute("executionDegreeID", executionDegreeId);

            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
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
                final ExecutionYear firstExecutionYear = whenDegreeIsExecuted.iterator().next();
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

    private void getInfoToShow(HttpServletRequest request, Degree degree, ExecutionYear executionYearToShow)
            throws FenixActionException {
        // degree info
        DegreeInfo degreeInfoToShow = executionYearToShow.getDegreeInfo(degree);
        if (degreeInfoToShow == null) {
            degreeInfoToShow = degree.getMostRecentDegreeInfo(executionYearToShow);
        }
        if (degreeInfoToShow != null) {
            request.setAttribute("degreeInfo", degreeInfoToShow);
        }

        // campus
        Collection<Space> campus = degree.getCampus(executionYearToShow);
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

    public ActionForward showAccessRequirements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareInfo(request);

        return mapping.findForward("showAccessRequirements");
    }

    public ActionForward showProfessionalStatus(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareInfo(request);

        return mapping.findForward("showProfessionalStatus");
    }

    public ActionForward showCurricularPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Degree degree = getDegree(request);

        String degreeCurricularPlanId = FenixContextDispatchAction.getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Boolean inEnglish = FenixContextDispatchAction.getFromRequestBoolean("inEnglish", request);
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);

        String executionDegreeId = FenixContextDispatchAction.getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeID", executionDegreeId);

        String index = FenixContextDispatchAction.getFromRequest("index", request);
        request.setAttribute("index", index);

        final ActionErrors errors = new ActionErrors();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan =
                getInfoDegreeCurricularPlan(executionDegreeId, degree.getExternalId(), degreeCurricularPlanId, mapping, request,
                        errors);
        if (infoDegreeCurricularPlan == null) {
            addErrorMessage(request, "impossibleDegreeSite", "error.impossibleCurricularPlan");
        } else {
            request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        }

        return mapping.findForward("showCurricularPlans");
    }

    public ActionForward viewDegreeEvaluation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Degree degree = getDegree(request);

        String executionDegreeId = FenixContextDispatchAction.getFromRequest("executionDegreeID", request);
        request.setAttribute("executionDegreeId", executionDegreeId);

        String degreeCurricularPlanId = FenixContextDispatchAction.getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Boolean inEnglish = FenixContextDispatchAction.getFromRequestBoolean("inEnglish", request);
        request.setAttribute("inEnglish", inEnglish);

        Collection<InfoOldInquiriesSummary> allSummariesDegree = ReadOldIquiriesSummaryByDegreeID.run(degree.getExternalId());

        List<InfoExecutionPeriod> infoExecutionPeriods = ReadExecutionPeriods.run();
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
            @Override
            public int compare(Object o1, Object o2) {
                InfoExecutionPeriod ep1 = (InfoExecutionPeriod) o1;
                InfoExecutionPeriod ep2 = (InfoExecutionPeriod) o2;
                return ep2.getBeginDate().compareTo(ep1.getBeginDate());
            }
        });
        request.setAttribute("executionPeriodList", executionPeriodList);

        final ActionErrors errors = new ActionErrors();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan =
                getInfoDegreeCurricularPlan(executionDegreeId, degree.getExternalId(), degreeCurricularPlanId, mapping, request,
                        errors);
        if (infoDegreeCurricularPlan == null) {
            addErrorMessage(request, "impossibleDegreeSite", "error.impossibleDegreeSite");
            return new ActionForward(mapping.getInput());
        }
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);

        // Getting the summary information
        DynaActionForm degreeEvaluationForm = (DynaActionForm) actionForm;
        String executionPeriodId = (String) degreeEvaluationForm.get("executionPeriodId");

        if (!StringUtils.isEmpty(executionPeriodId)) {
            request.setAttribute("searchExecutionPeriodId", executionPeriodId);

            List<InfoOldInquiriesSummary> oldInquiriesSummaries = new ArrayList<InfoOldInquiriesSummary>();
            for (InfoOldInquiriesSummary iois : allSummariesDegree) {
                if (iois.getExecutionPeriod().getExternalId().equals(executionPeriodId)) {
                    if ((iois.getNumberEnrollments() > 0) && (iois.getNumberApproved() >= 0) && (iois.getNumberEvaluated() > 0)) {
                        oldInquiriesSummaries.add(iois);
                    }
                }
            }

            if (!oldInquiriesSummaries.isEmpty()) {
                Collections.sort(oldInquiriesSummaries);
                request.setAttribute("oldInquiriesSummaries", oldInquiriesSummaries);

                int denominatorCourses = 0, denominatorTeachers = 0;
                double numeratorCourses = 0, numeratorTeachers = 0;

                for (InfoOldInquiriesSummary iois : oldInquiriesSummaries) {
                    if ((iois.getNumberAnswers() != null) && (iois.getNumberAnswers() > 0)
                            && (iois.getNumberEnrollments() != null) && (iois.getNumberEnrollments() > 0)) {
                        iois.setRepresentationQuota(Double.valueOf((iois.getNumberAnswers() / iois.getNumberEnrollments()) * 100));
                    }

                    if ((iois.getAverage2_8() != null) && (iois.getAverage2_8() >= 0) && (iois.getRepresentationQuota() > 10)) {
                        numeratorCourses += iois.getAverage2_8();
                        denominatorCourses++;
                    }
                    if ((iois.getAverage3_11() != null) && (iois.getAverage3_11() >= 0) && (iois.getRepresentationQuota() > 10)) {
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

    private InfoDegreeCurricularPlan getInfoDegreeCurricularPlan(String executionDegreeId, String degreeId,
            String degreeCurricularPlanId, ActionMapping mapping, HttpServletRequest request, ActionErrors errors) {

        if (executionDegreeId != null) {
            // if came in the request a executionDegreeId that it is
            // necessary find the corresponding degree curricular plan
            return getByExecutionDegreeId(executionDegreeId, request);
        }

        return getByDegreeIdOrDegreeCurricularPlanId(degreeId, degreeCurricularPlanId, request);
    }

    private InfoDegreeCurricularPlan getByExecutionDegreeId(String executionDegreeId, HttpServletRequest request) {
        try {

            final InfoExecutionDegree infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegreeId);
            if (infoExecutionDegree == null || infoExecutionDegree.getInfoDegreeCurricularPlan() == null) {
                return null;
            }

            request.setAttribute(PresentationConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
            request.setAttribute("executionDegreeID", infoExecutionDegree.getExternalId());
            return infoExecutionDegree.getInfoDegreeCurricularPlan();
        } catch (Exception e) {
            return null;
        }
    }

    private InfoDegreeCurricularPlan getByDegreeIdOrDegreeCurricularPlanId(String degreeId, String degreeCurricularPlanId,
            HttpServletRequest request) {
        final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlanList;
        try {

            infoDegreeCurricularPlanList = ReadDegreeCurricularPlansByDegree.run(degreeId);
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

        // if came in the request a degreeCurricularPlanId that it is necessary
        // find information about this degree curricular plan
        if (degreeCurricularPlanId != null) {
            for (InfoDegreeCurricularPlan infoDegreeCurricularPlanElem : infoDegreeCurricularPlanList) {
                if (infoDegreeCurricularPlanElem.getExternalId().equals(degreeCurricularPlanId)) {
                    request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlanElem);
                    return infoDegreeCurricularPlanElem;
                }
            }
        } else {
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoDegreeCurricularPlanList.iterator().next();
            request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
            return infoDegreeCurricularPlan;
        }

        return null;
    }

    public static Degree getDegree(HttpServletRequest request) {
        final DegreeSite site = SiteMapper.getSite(request);
        Degree degree = null;
        if (site != null) {
            degree = site.getDegree();
        } else {
            String degreeId = FenixContextDispatchAction.getFromRequest("degreeID", request);
            degree = FenixFramework.getDomainObject(degreeId);
        }
        if (degree != null) {
            request.setAttribute("degreeID", degree.getExternalId());
            request.setAttribute("degree", degree);
            OldCmsSemanticURLHandler.selectSite(request, degree.getSite());
        }
        return degree;
    }

}