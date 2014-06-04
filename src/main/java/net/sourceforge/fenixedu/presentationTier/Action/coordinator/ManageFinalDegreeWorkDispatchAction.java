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
 * Created on 2004/03/09
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.AddExecutionDegreeToScheduling;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.AttributeFinalDegreeWork;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ChangeStatusOfFinalDegreeWorkProposals;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.PublishAprovedFinalDegreeWorkProposals;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadFinalDegreeWorkProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadFinalDegreeWorkProposalSubmisionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.RemoveExecutionDegreeToScheduling;
import net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice.DeleteFinalDegreeWorkProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice.DeleteGroupProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice.DeleteGroupProposalAttribution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfPeriodException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionDegreesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.SubmitFinalWorkProposal;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.CandidacyAttributionType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ProposalsFilterBean.AttributionFilter;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ProposalsFilterBean.StatusCountPair;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ProposalsFilterBean.WithCandidatesFilter;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.utils.CommonServiceRequests;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import net.sourceforge.fenixedu.presentationTier.util.CollectionFilter;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;
import pt.utl.ist.fenix.tools.util.CollectionUtils;
import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

/**
 * @author Luis Cruz
 */

@Mapping(path = "/manageFinalDegreeWork", module = "coordinator", formBean = "finalDegreeWorkCandidacyRequirements",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "error", path = "/coordinator/welcomeScreen.jsp"),
        @Forward(name = "show-choose-execution-degree-page",
                path = "/coordinator/finalDegreeWork/showFinalDegreeChooseExecutionDegree_bd.jsp"),
        @Forward(name = "show-final-degree-work-list", path = "/coordinator/finalDegreeWork/showFinalDegreeWorkList_bd.jsp"),
        @Forward(name = "show-final-degree-work-info", path = "/coordinator/finalDegreeWork/showFinalDegreeWorkInfo.jsp"),
        @Forward(name = "show-proposals", path = "/coordinator/finalDegreeWork/showProposals.jsp"),
        @Forward(name = "show-proposal", path = "/coordinator/finalDegreeWork/showProposal.jsp"),
        @Forward(name = "edit-proposal", path = "/coordinator/finalDegreeWork/editProposal.jsp"),
        @Forward(name = "show-candidates", path = "/coordinator/finalDegreeWork/showCandidates.jsp"),
        @Forward(name = "show-final-degree-work-proposal",
                path = "/coordinator/finalDegreeWork/showFinalDegreeWorkProposal_bd.jsp"),
        @Forward(name = "prepare-show-final-degree-work-proposal",
                path = "/coordinator/manageFinalDegreeWork.do?method=prepare&page=0"),
        @Forward(name = "show-student-curricular-plan", path = "/coordinator/viewStudentCurriculum.do?method=prepare"),
        @Forward(name = "edit-final-degree-periods", path = "/coordinator/finalDegreeWork/editFinalDegreePeriods.jsp"),
        @Forward(name = "edit-final-degree-requirements", path = "/coordinator/finalDegreeWork/editFinalDegreeRequirements.jsp"),
        @Forward(name = "detailed-proposal-list", path = "/coordinator/finalDegreeWork/detailedProposalList_bd.jsp") })
@Exceptions({
        @ExceptionHandling(key = "error.final.degree.work.scheduling.has.proposals", handler = FenixErrorExceptionHandler.class,
                type = AddExecutionDegreeToScheduling.SchedulingContainsProposalsException.class),
        @ExceptionHandling(key = "error.final.degree.work.scheduling.has.proposals", handler = FenixErrorExceptionHandler.class,
                type = RemoveExecutionDegreeToScheduling.SchedulingContainsProposalsException.class) })
public class ManageFinalDegreeWorkDispatchAction extends FenixDispatchAction {

    @Mapping(path = "/finalDegreeWorkProposal", module = "coordinator",
            input = "/finalDegreeWorkProposal.do?method=createNewFinalDegreeWorkProposal", formBean = "finalDegreeWorkProposal",
            functionality = DegreeCoordinatorIndex.class)
    @Forwards(@Forward(name = "show-final-degree-work-list",
            path = "/coordinator/manageFinalDegreeWork.do?method=showProposal&page=0"))
    public static class FinalDegreeWorkProposalDA extends ManageFinalDegreeWorkDispatchAction {
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        final String executionDegreePlanOID = newFindExecutionDegreeID(request);
        if (executionDegreePlanOID != null) {
            request.setAttribute("executionDegreeOID", executionDegreePlanOID);
            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreePlanOID);
            request.setAttribute("executionDegree", executionDegree);
        }
        return super.execute(mapping, actionForm, request, response);
    }

    private static String newFindExecutionDegreeID(HttpServletRequest request) {
        String executionDegreePlanOID = request.getParameter("executionDegreeOID");
        if (executionDegreePlanOID == null) {
            executionDegreePlanOID = (String) request.getAttribute("executionDegreeOID");
        }
        return executionDegreePlanOID;
    }

    public ActionForward showChooseExecutionDegreeForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        // keep degreeCurricularPlan in request
        final String degreeCurricularPlanOID = (String) getFromRequest(request, "degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        DegreeCurricularPlan degreeCurricularPlan = getDomainObject(request, "degreeCurricularPlanID");

        final List<InfoExecutionDegree> infoExecutionDegrees =
                ReadExecutionDegreesByDegreeCurricularPlan.run(degreeCurricularPlan);
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        return mapping.findForward("show-choose-execution-degree-page");
    }

    public ActionForward showChooseExecutionDegreeFormForDepartment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final User userView = getUserView(request);
        final Department department = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();

        final Set<ExecutionDegree> executionDegrees =
                new TreeSet<ExecutionDegree>(
                        ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR);
        if (department != null) {
            addAllExecutionDegrees(executionDegrees, department.getCompetenceCoursesSet());
            addAllExecutionDegrees(executionDegrees, department.getDepartmentUnit());
        }

        request.setAttribute("executionDegrees", executionDegrees);

        return mapping.findForward("show-choose-execution-degree-page");
    }

    private void addAllExecutionDegrees(final Set<ExecutionDegree> executionDegrees, final Unit unit) {
        if (unit.isCompetenceCourseGroupUnit()) {
            final CompetenceCourseGroupUnit competenceCourseGroupUnit = (CompetenceCourseGroupUnit) unit;
            addAllExecutionDegrees(executionDegrees, competenceCourseGroupUnit.getCompetenceCoursesSet());
        }
        for (final Accountability accountability : unit.getChildsSet()) {
            final Party party = accountability.getChildParty();
            if (party.isUnit()) {
                addAllExecutionDegrees(executionDegrees, (Unit) party);
            }
        }
    }

    private void addAllExecutionDegrees(final Set<ExecutionDegree> executionDegrees, final Set<CompetenceCourse> competenceCourses) {
        for (final CompetenceCourse competenceCourse : competenceCourses) {
            for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
                if (curricularCourse.getType() == CurricularCourseType.TFC_COURSE || competenceCourse.isDissertation()) {
                    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                    executionDegrees.addAll(degreeCurricularPlan.getExecutionDegrees());
                }
            }
        }
    }

    public ActionForward deleteProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final Proposal proposal = getDomainObject(request, "proposalOID");
        DeleteFinalDegreeWorkProposal.run(proposal);
        return showProposals(mapping, form, request, response);
    }

    public ActionForward deleteAttributions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final GroupProposal groupProposal = getDomainObject(request, "groupProposalOID");
        DeleteGroupProposalAttribution.run(groupProposal);
        return showCandidates(mapping, form, request, response);
    }

    public ActionForward deleteCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final GroupProposal groupProposal = getDomainObject(request, "groupProposalOID");
        DeleteGroupProposal.run(groupProposal);
        return showCandidates(mapping, form, request, response);
    }

    public ActionForward finalDegreeWorkInfoWithNewScheduling(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        Scheduleing.newInstance(getExecutionDegree(request));
        return finalDegreeWorkInfo(mapping, actionForm, request, response);
    }

    public ActionForward finalDegreeWorkInfo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ExecutionDegree executionDegree = getExecutionDegree(request);
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        net.sourceforge.fenixedu.domain.ExecutionYear executionYear = executionDegree.getExecutionYear().getNextExecutionYear();
        if (executionYear != null) {
            request.setAttribute("executionYearId", executionYear.getExternalId());
        }
        request.setAttribute("summary", new AllSummaryBean(executionDegree, degreeCurricularPlan));
        return mapping.findForward("show-final-degree-work-info");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        return getDomainObject(request, "degreeCurricularPlanID");
    }

    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        return getDomainObject(request, "executionDegreeOID");
    }

    public ActionForward showCandidates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final ExecutionDegree executionDegree = getExecutionDegree(request);

        CandidaciesSummaryBean summaryBean = new CandidaciesSummaryBean(executionDegree, degreeCurricularPlan);
        final CandidacyFilterBean filterBean = new CandidacyFilterBean(CandidacyAttributionType.TOTAL);
        filterBean.setFromRequest(request);
        Set<FinalDegreeWorkGroup> groups;
        if (executionDegree.hasScheduling()) {
            groups = executionDegree.getScheduling().getAssociatedFinalDegreeWorkGroups();
        } else {
            groups = Collections.emptySet();
        }
        final CollectionFilter<FinalDegreeWorkGroup> collectionFilter;
        collectionFilter =
                new CollectionFilter<FinalDegreeWorkGroup>(filterBean.getPredicates(),
                        FinalDegreeWorkGroup.COMPARATOR_BY_STUDENT_NUMBERS);

        final Set<FinalDegreeWorkGroup> filteredGroups = collectionFilter.filter(groups);

        final CollectionPager<FinalDegreeWorkGroup> pager = new CollectionPager<FinalDegreeWorkGroup>(filteredGroups, 45);

        request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);

        request.setAttribute("method", "showCandidates");
        request.setAttribute("filterBean", filterBean);
        request.setAttribute("filter", filterBean.getStatus());
        request.setAttribute("pageNumber", page);
        request.setAttribute("candidaciesCount", filteredGroups.size());
        request.setAttribute("candidacies", pager.getPage(page));
        request.setAttribute("summary", summaryBean);
        return mapping.findForward("show-candidates");
    }

    public ActionForward showCandidatesWithoutDissertationEnrolments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final ExecutionDegree executionDegree = getExecutionDegree(request);
        Set<FinalDegreeWorkGroup> groups;
        if (executionDegree.hasScheduling()) {
            groups = executionDegree.getScheduling().getAssociatedFinalDegreeWorkGroups();
        } else {
            groups = Collections.emptySet();
        }
        final CollectionFilter<FinalDegreeWorkGroup> collectionFilter;
        collectionFilter = new CollectionFilter<FinalDegreeWorkGroup>(FinalDegreeWorkGroup.WITHOUT_DISSERTATION_PREDICATE);
        final Set<FinalDegreeWorkGroup> filteredGroups = collectionFilter.filter(groups);
        final CollectionPager<FinalDegreeWorkGroup> pager = new CollectionPager<FinalDegreeWorkGroup>(filteredGroups, 45);
        request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));
        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);

        request.setAttribute("method", "showCandidatesWithoutDissertationEnrolments");
        request.setAttribute("pageNumber", page);
        request.setAttribute("candidaciesCount", filteredGroups.size());
        request.setAttribute("candidacies", pager.getPage(page));
        request.setAttribute("filterBean",
                RenderUtils.getFormatedResourceString("APPLICATION_RESOURCES", "message.candidatesWithoutDissertationEnrolment"));
        return mapping.findForward("show-candidates");
    }

    public ActionForward showProposals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final ExecutionDegree executionDegree = getExecutionDegree(request);

        final ProposalsSummaryBean proposalsSummaryBean = new ProposalsSummaryBean(executionDegree, degreeCurricularPlan);

        ProposalsFilterBean filterBean;
        if (request.getParameter("proposalStatusType") == null) {
            filterBean = getRenderedObject("filterBean");
            if (filterBean == null) {
                filterBean = new ProposalsFilterBean(proposalsSummaryBean);
            }
        } else {
            filterBean = readFilterBean(request, proposalsSummaryBean);
        }
        filterBean.setFromRequest(request);
        // Collection<Proposal> proposals =
        // executionDegree.getProposals(filterBean);

        Collection<Proposal> proposals =
                new CollectionFilter<Proposal>(filterBean.getPredicates(), Proposal.COMPARATOR_BY_PROPOSAL_NUMBER)
                        .filter(executionDegree.getProposals());
        String sortBy = request.getParameter("sortBy");
        if (sortBy == null) {
            sortBy = "proposalNumber|ascending";
        }
        request.setAttribute("sortBy", sortBy);
        String attrib = sortBy.substring(0, sortBy.indexOf("|"));
        String order = sortBy.substring(sortBy.indexOf("|") + 1, sortBy.length());
        List<Proposal> sortedProposals = sortProposals(proposals, attrib, order);

        final CollectionPager<Proposal> pager = new CollectionPager<Proposal>(sortedProposals, 45);

        request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);

        request.setAttribute("filterBean", filterBean);
        request.setAttribute("pageNumber", page);
        request.setAttribute("proposals", pager.getPage(page));
        request.setAttribute("countProposals", sortedProposals.size());
        return mapping.findForward("show-proposals");
    }

    private List<Proposal> sortProposals(Collection<Proposal> proposals, String attrib, String order) {
        List<Proposal> result = new ArrayList<Proposal>();
        result.addAll(proposals);
        Comparator<Proposal> comparator = null;
        if (attrib.equalsIgnoreCase("proposalStatus")) {
            comparator = Proposal.COMPARATOR_BY_STATUS;
        } else if (attrib.equalsIgnoreCase("numberOfCandidates")) {
            comparator = Proposal.COMPARATOR_BY_NUMBER_OF_CANDIDATES;
        } else {
            comparator = Proposal.COMPARATOR_BY_PROPOSAL_NUMBER;
        }
        Collections.sort(result, comparator);
        if (order.equalsIgnoreCase("descending")) {
            Collections.reverse(result);
        }
        return result;
    }

    private ProposalsFilterBean readFilterBean(HttpServletRequest request, ProposalsSummaryBean summary) {
        ProposalsFilterBean proposalsFilterBean = new ProposalsFilterBean(summary);
        String proposalStatusString = request.getParameter("proposalStatusType");
        String attributionFilterString = request.getParameter("attributionFilter");
        String withCandidatesFilterString = request.getParameter("withCandidatesFilter");
        int count;

        ProposalStatusType proposalStatusType;
        if (proposalStatusString.equalsIgnoreCase("TOTAL")) {
            proposalStatusType = ProposalStatusType.TOTAL;
            count = summary.getTotalProposalsCount();
        } else if (proposalStatusString.equalsIgnoreCase("FOR_APPROVAL")) {
            proposalStatusType = ProposalStatusType.FOR_APPROVAL;
            count = summary.getForApprovalProposalsCount();
        } else if (proposalStatusString.equalsIgnoreCase("APPROVED")) {
            proposalStatusType = ProposalStatusType.APPROVED;
            count = summary.getApprovedProposalsCount();
        } else {
            proposalStatusType = ProposalStatusType.PUBLISHED;
            count = summary.getPublishedProposalsCount();
        }

        AttributionFilter attributionFilter;
        if (attributionFilterString.equalsIgnoreCase("ATTRIBUTED")) {
            attributionFilter = AttributionFilter.ATTRIBUTED;
        } else if (attributionFilterString.equalsIgnoreCase("NOT_ATTRIBUTED")) {
            attributionFilter = AttributionFilter.NOT_ATTRIBUTED;
        } else {
            attributionFilter = AttributionFilter.ALL;
        }

        WithCandidatesFilter withCandidatesFilter;
        if (withCandidatesFilterString.equalsIgnoreCase("WITH_CANDIDATES")) {
            withCandidatesFilter = WithCandidatesFilter.WITH_CANDIDATES;
        } else if (withCandidatesFilterString.equalsIgnoreCase("WITHOUT_CANDIDATES")) {
            withCandidatesFilter = WithCandidatesFilter.WITHOUT_CANDIDATES;
        } else {
            withCandidatesFilter = WithCandidatesFilter.ALL;
        }
        proposalsFilterBean.setAttribution(attributionFilter);
        proposalsFilterBean.setWithCandidates(withCandidatesFilter);

        proposalsFilterBean.setStatus(new StatusCountPair(proposalStatusType, count));
        return proposalsFilterBean;
    }

    public ActionForward showProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final ExecutionDegree executionDegree = getExecutionDegree(request);
        request.setAttribute("executionDegree", executionDegree);

        String proposalOID = (String) getFromRequest(request, "proposalOID");
        final Proposal proposal = FenixFramework.getDomainObject(proposalOID);
        request.setAttribute("proposal", proposal);
        request.setAttribute("candidacies", proposal.getCandidacies());
        request.setAttribute("candidaciesCount", proposal.getCandidacies().size());
        request.setAttribute("method", "");
        return mapping.findForward("show-proposal");
    }

    public ActionForward editProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final ExecutionDegree executionDegree = getExecutionDegree(request);
        request.setAttribute("executionDegree", executionDegree);

        String proposalOID = (String) getFromRequest(request, "proposalOID");
        final Proposal proposal = FenixFramework.getDomainObject(proposalOID);
        request.setAttribute("proposal", proposal);
        return mapping.findForward("edit-proposal");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {
        User userView = Authenticate.getUser();

        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            request.setAttribute("degreeCurricularPlanID", request.getParameter("degreeCurricularPlanID"));
        }

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOID);
        request.setAttribute("executionDegreeOID", executionDegreeOID);
        request.setAttribute("executionDegree", executionDegree);

        // Load proposals for execution degree
        List finalDegreeWorkProposalHeaders = null;
        try {
            finalDegreeWorkProposalHeaders =
                    ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan
                            .runReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan(executionDegree);

            if (finalDegreeWorkProposalHeaders != null && !finalDegreeWorkProposalHeaders.isEmpty()) {
                Collections.sort(finalDegreeWorkProposalHeaders, new BeanComparator("proposalNumber"));

                request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);
                final CollectionPager<FinalDegreeWorkProposalHeader> collectionPager =
                        new CollectionPager<FinalDegreeWorkProposalHeader>(finalDegreeWorkProposalHeaders, 50);
                request.setAttribute("collectionPager", collectionPager);
                request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));

                final String pageParameter = request.getParameter("pageNumber");
                final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
                request.setAttribute("pageNumber", page);
                request.setAttribute("resultPage", collectionPager.getPage(page));
            }
        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("notAuthorized", new ActionError("error.exception.notAuthorized"));
            saveErrors(request, actionErrors);

            degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

            return mapping.findForward("error");
        }

        // Load proposal submission period
        try {
            InfoScheduleing infoScheduleing = ReadFinalDegreeWorkProposalSubmisionPeriod.run(executionDegree);

            if (infoScheduleing != null) {
                SimpleDateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:mm");

                DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;
                if (infoScheduleing.getStartOfProposalPeriod() != null) {
                    finalDegreeWorkScheduleingForm.set("startOfProposalPeriodDate",
                            dateFormatDate.format(infoScheduleing.getStartOfProposalPeriod()));
                    finalDegreeWorkScheduleingForm.set("startOfProposalPeriodHour",
                            dateFormatHour.format(infoScheduleing.getStartOfProposalPeriod()));
                }
                if (infoScheduleing.getEndOfProposalPeriod() != null) {
                    finalDegreeWorkScheduleingForm.set("endOfProposalPeriodDate",
                            dateFormatDate.format(infoScheduleing.getEndOfProposalPeriod()));
                    finalDegreeWorkScheduleingForm.set("endOfProposalPeriodHour",
                            dateFormatHour.format(infoScheduleing.getEndOfProposalPeriod()));
                }

                List attributedByTeacherList = new ArrayList();
                List attributionsList = new ArrayList();
                if (finalDegreeWorkProposalHeaders != null) {
                    for (int i = 0; i < finalDegreeWorkProposalHeaders.size(); i++) {
                        FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader =
                                (FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeaders.get(i);
                        InfoGroup infoGroup = finalDegreeWorkProposalHeader.getGroupAttributedByTeacher();
                        if (infoGroup != null) {
                            attributedByTeacherList.add(finalDegreeWorkProposalHeader.getExternalId().toString()
                                    + infoGroup.getExternalId().toString());
                        }
                        infoGroup = finalDegreeWorkProposalHeader.getGroupAttributed();
                        if (infoGroup != null) {
                            attributionsList.add(finalDegreeWorkProposalHeader.getExternalId().toString()
                                    + infoGroup.getExternalId().toString());
                        }
                    }

                    String[] attributedByTeacher = new String[attributedByTeacherList.size()];
                    for (int i = 0; i < attributedByTeacherList.size(); i++) {
                        attributedByTeacher[i] = (String) attributedByTeacherList.get(i);
                    }
                    String[] attributions = new String[attributionsList.size()];
                    for (int i = 0; i < attributionsList.size(); i++) {
                        attributions[i] = (String) attributionsList.get(i);
                    }

                    finalDegreeWorkScheduleingForm.set("attributedByTeacher", attributedByTeacher);
                    finalDegreeWorkScheduleingForm.set("attributions", attributions);
                }

                ModuleConfig moduleConfig = mapping.getModuleConfig();

                FormBeanConfig fbc = moduleConfig.findFormBeanConfig("finalDegreeWorkCandidacyPeriod");
                DynaActionFormClass dafc = DynaActionFormClass.createDynaActionFormClass(fbc);
                DynaActionForm finalDegreeWorkCandidacyPeriodForm;
                try {
                    finalDegreeWorkCandidacyPeriodForm = (DynaActionForm) dafc.newInstance();
                } catch (Exception e1) {
                    throw new FenixActionException(e1);
                }
                if (infoScheduleing.getStartOfCandidacyPeriod() != null) {
                    finalDegreeWorkCandidacyPeriodForm.set("startOfCandidacyPeriodDate",
                            dateFormatDate.format(infoScheduleing.getStartOfCandidacyPeriod()));
                    finalDegreeWorkCandidacyPeriodForm.set("startOfCandidacyPeriodHour",
                            dateFormatHour.format(infoScheduleing.getStartOfCandidacyPeriod()));
                }
                if (infoScheduleing.getEndOfCandidacyPeriod() != null) {
                    finalDegreeWorkCandidacyPeriodForm.set("endOfCandidacyPeriodDate",
                            dateFormatDate.format(infoScheduleing.getEndOfCandidacyPeriod()));
                    finalDegreeWorkCandidacyPeriodForm.set("endOfCandidacyPeriodHour",
                            dateFormatHour.format(infoScheduleing.getEndOfCandidacyPeriod()));
                }
                request.setAttribute("finalDegreeWorkCandidacyPeriod", finalDegreeWorkCandidacyPeriodForm);

                FormBeanConfig fbc2 = moduleConfig.findFormBeanConfig("finalDegreeWorkCandidacyRequirements");
                DynaActionFormClass dafc2 = DynaActionFormClass.createDynaActionFormClass(fbc2);
                DynaActionForm finalDegreeWorkCandidacyRequirementsForm;
                try {
                    finalDegreeWorkCandidacyRequirementsForm = (DynaActionForm) dafc2.newInstance();
                } catch (Exception e1) {
                    throw new FenixActionException(e1);
                }
                if (infoScheduleing.getMinimumNumberOfCompletedCourses() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("minimumNumberOfCompletedCourses", infoScheduleing
                            .getMinimumNumberOfCompletedCourses().toString());
                }
                if (infoScheduleing.getMinimumCompletedCreditsFirstCycle() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("minimumCompletedCreditsFirstCycle", infoScheduleing
                            .getMinimumCompletedCreditsFirstCycle().toString());
                }
                if (infoScheduleing.getMinimumCompletedCreditsSecondCycle() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("minimumCompletedCreditsSecondCycle", infoScheduleing
                            .getMinimumCompletedCreditsSecondCycle().toString());
                }
                if (infoScheduleing.getMaximumCurricularYearToCountCompletedCourses() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("maximumCurricularYearToCountCompletedCourses", infoScheduleing
                            .getMaximumCurricularYearToCountCompletedCourses().toString());
                }
                if (infoScheduleing.getMinimumCompletedCurricularYear() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("minimumCompletedCurricularYear", infoScheduleing
                            .getMinimumCompletedCurricularYear().toString());
                }
                if (infoScheduleing.getMinimumNumberOfStudents() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("minimumNumberOfStudents", infoScheduleing
                            .getMinimumNumberOfStudents().toString());
                }
                if (infoScheduleing.getMaximumNumberOfStudents() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("maximumNumberOfStudents", infoScheduleing
                            .getMaximumNumberOfStudents().toString());
                }
                if (infoScheduleing.getMaximumNumberOfProposalCandidaciesPerGroup() != null) {
                    finalDegreeWorkCandidacyRequirementsForm.set("maximumNumberOfProposalCandidaciesPerGroup", infoScheduleing
                            .getMaximumNumberOfProposalCandidaciesPerGroup().toString());
                }
                finalDegreeWorkCandidacyRequirementsForm.set("attributionByTeachers", infoScheduleing.getAttributionByTeachers());
                finalDegreeWorkCandidacyRequirementsForm.set("allowSimultaneousCoorientationAndCompanion",
                        infoScheduleing.getAllowSimultaneousCoorientationAndCompanion());

                request.setAttribute("finalDegreeWorkCandidacyRequirements", finalDegreeWorkCandidacyRequirementsForm);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("show-final-degree-work-list");
    }

    public ActionForward viewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String proposalOID = keepInRequest(request, "proposalOID");
        final Proposal proposal = FenixFramework.getDomainObject(proposalOID);
        final String finalDegreeWorkProposalOIDString = proposal.getExternalId();
        String executionDegreeOID = (String) getFromRequest(request, "executionDegreeOID");
        request.setAttribute("executionDegree", getDomainObject(request, "executionDegreeOID"));

        if (finalDegreeWorkProposalOIDString != null && StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {
            User userView = Authenticate.getUser();

            try {
                InfoProposal infoProposal =
                        ReadFinalDegreeWorkProposal.runReadFinalDegreeWorkProposal(finalDegreeWorkProposalOIDString);

                if (infoProposal != null) {
                    DynaActionForm finalWorkForm = (DynaActionForm) form;

                    if (infoProposal.getExternalId() != null) {
                        finalWorkForm.set("externalId", infoProposal.getExternalId().toString());
                    }
                    finalWorkForm.set("title", infoProposal.getTitle());
                    if (infoProposal.getOrientatorsCreditsPercentage() != null) {
                        finalWorkForm.set("responsibleCreditsPercentage", infoProposal.getOrientatorsCreditsPercentage());
                    }
                    if (infoProposal.getCoorientatorsCreditsPercentage() != null) {
                        finalWorkForm.set("coResponsibleCreditsPercentage", infoProposal.getCoorientatorsCreditsPercentage());
                    }
                    finalWorkForm.set("companionName", infoProposal.getCompanionName());
                    finalWorkForm.set("companionMail", infoProposal.getCompanionMail());
                    if (infoProposal.getCompanionPhone() != null) {
                        finalWorkForm.set("companionPhone", infoProposal.getCompanionPhone().toString());
                    }
                    finalWorkForm.set("framing", infoProposal.getFraming());
                    finalWorkForm.set("objectives", infoProposal.getObjectives());
                    finalWorkForm.set("description", infoProposal.getDescription());
                    finalWorkForm.set("requirements", infoProposal.getRequirements());
                    finalWorkForm.set("deliverable", infoProposal.getDeliverable());
                    finalWorkForm.set("url", infoProposal.getUrl());
                    if (infoProposal.getMaximumNumberOfGroupElements() != null) {
                        finalWorkForm.set("maximumNumberOfGroupElements", infoProposal.getMaximumNumberOfGroupElements()
                                .toString());
                    }
                    if (infoProposal.getMinimumNumberOfGroupElements() != null) {
                        finalWorkForm.set("minimumNumberOfGroupElements", infoProposal.getMinimumNumberOfGroupElements()
                                .toString());
                    }
                    if (infoProposal.getDegreeType() != null) {
                        finalWorkForm.set("degreeType", infoProposal.getDegreeType().getName());
                    }
                    finalWorkForm.set("observations", infoProposal.getObservations());
                    finalWorkForm.set("location", infoProposal.getLocation());

                    finalWorkForm.set("companyAdress", infoProposal.getCompanyAdress());
                    finalWorkForm.set("companyName", infoProposal.getCompanyName());
                    if (infoProposal.getOrientator() != null && infoProposal.getOrientator().getExternalId() != null) {
                        finalWorkForm.set("orientatorOID", infoProposal.getOrientator().getExternalId().toString());
                        finalWorkForm.set("responsableTeacherId", infoProposal.getOrientator().getPerson().getIstUsername());
                        finalWorkForm.set("responsableTeacherName", infoProposal.getOrientator().getNome());
                    }
                    if (infoProposal.getCoorientator() != null && infoProposal.getCoorientator().getExternalId() != null) {
                        finalWorkForm.set("coorientatorOID", infoProposal.getCoorientator().getExternalId().toString());
                        finalWorkForm.set("coResponsableTeacherId", infoProposal.getCoorientator().getPerson().getIstUsername());
                        finalWorkForm.set("coResponsableTeacherName", infoProposal.getCoorientator().getNome());
                    }
                    if (infoProposal.getExecutionDegree() != null && infoProposal.getExecutionDegree().getExternalId() != null) {
                        finalWorkForm.set("degree", infoProposal.getExecutionDegree().getExternalId().toString());
                    }
                    if (infoProposal.getStatus() != null && infoProposal.getStatus().getStatus() != null) {
                        finalWorkForm.set("status", infoProposal.getStatus().getStatus().toString());
                    }

                    if (infoProposal.getBranches() != null && infoProposal.getBranches().size() > 0) {
                        String[] branchList = new String[infoProposal.getBranches().size()];
                        for (int i = 0; i < infoProposal.getBranches().size(); i++) {
                            InfoBranch infoBranch = (infoProposal.getBranches().get(i));
                            if (infoBranch != null && infoBranch.getExternalId() != null) {
                                String brachOIDString = infoBranch.getExternalId().toString();
                                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
                                    branchList[i] = brachOIDString;
                                }
                            }
                        }
                        finalWorkForm.set("branchList", branchList);
                    }

                    final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOID);
                    final Scheduleing scheduleing = executionDegree.getScheduling();
                    final List branches = new ArrayList();
                    for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
                        final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
                        branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
                                degreeCurricularPlan.getExternalId()));
                    }
                    // InfoExecutionDegree infoExecutionDegree =
                    // CommonServiceRequests
                    // .getInfoExecutionDegree(userView,
                    // infoProposal.getExecutionDegree()
                    // .getExternalId());
                    // List branches =
                    // CommonServiceRequests.getBranchesByDegreeCurricularPlan
                    // (userView,
                    // infoExecutionDegree.getInfoDegreeCurricularPlan().
                    // getExternalId());
                    request.setAttribute("branches", branches);

                    request.setAttribute("finalDegreeWorkProposalStatusList", FinalDegreeWorkProposalStatus.getLabelValueList());
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }

        return mapping.findForward("show-final-degree-work-proposal");
    }

    public ActionForward createNewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        User userView = Authenticate.getUser();

        ExecutionDegree executionDegree = getExecutionDegree(request);
        // DynaActionForm finalWorkForm = (DynaActionForm) form;
        // finalWorkForm.set("degree", executionDegreeOID);

        final Scheduleing scheduleing = executionDegree.getScheduling();
        if (scheduleing == null) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.scheduling.not.defined", new ActionError("error.scheduling.not.defined"));
            saveErrors(request, actionErrors);
            return mapping.findForward("show-final-degree-work-proposal");
        }

        final List branches = new ArrayList();
        for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
            final DegreeCurricularPlan degreeCurricularPlanIter = ed.getDegreeCurricularPlan();
            branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
                    degreeCurricularPlanIter.getExternalId()));
        }
        // List branches =
        // CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
        // infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId());
        request.setAttribute("branches", branches);

        request.setAttribute("finalDegreeWorkProposalStatusList", FinalDegreeWorkProposalStatus.getLabelValueList());

        return mapping.findForward("show-final-degree-work-proposal");
    }

    public static class NoScheduleExistsException extends FenixActionException {
    }

    public ActionForward editFinalDegreePeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final ExecutionDegree executionDegree = getExecutionDegree(request);
        if (executionDegree != null) {
            return mapping.findForward("edit-final-degree-periods");
        }
        return mapping.findForward("show-final-degree-work-info");
    }

    public ActionForward editFinalDegreeRequirements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final ExecutionDegree executionDegree = getExecutionDegree(request);
        if (executionDegree != null) {
            return mapping.findForward("edit-final-degree-requirements");
        }
        return mapping.findForward("show-final-degree-work-info");
    }

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {
        DynaActionForm finalWorkForm = (DynaActionForm) form;

        String degreeCurricularPlanID = (String) finalWorkForm.get("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String executionDegreeOID = (String) dynaActionForm.get("executionDegreeOID");
        request.setAttribute("executionDegreeOID", executionDegreeOID);

        keepInRequest(request, "proposalOID");

        String externalId = (String) finalWorkForm.get("externalId");
        String title = (String) finalWorkForm.get("title");
        String companionName = (String) finalWorkForm.get("companionName");
        String companionMail = (String) finalWorkForm.get("companionMail");
        String companionPhone = (String) finalWorkForm.get("companionPhone");
        String framing = (String) finalWorkForm.get("framing");
        String objectives = (String) finalWorkForm.get("objectives");
        String description = (String) finalWorkForm.get("description");
        String requirements = (String) finalWorkForm.get("requirements");
        String deliverable = (String) finalWorkForm.get("deliverable");
        String url = (String) finalWorkForm.get("url");
        // String minimumNumberOfGroupElements = (String)
        // finalWorkForm.get("minimumNumberOfGroupElements");
        // String maximumNumberOfGroupElements = (String)
        // finalWorkForm.get("maximumNumberOfGroupElements");
        String minimumNumberOfGroupElements = "1";
        String maximumNumberOfGroupElements = "1";
        // String degreeType = (String) finalWorkForm.get("degreeType");
        String degreeType = null;
        String observations = (String) finalWorkForm.get("observations");
        String location = (String) finalWorkForm.get("location");
        String companyAdress = (String) finalWorkForm.get("companyAdress");
        String companyName = (String) finalWorkForm.get("companyName");
        String orientatorOID = (String) finalWorkForm.get("orientatorOID");
        String coorientatorOID = (String) finalWorkForm.get("coorientatorOID");
        String degree = (String) finalWorkForm.get("degree");
        String[] branchList = (String[]) finalWorkForm.get("branchList");
        String status = (String) finalWorkForm.get("status");

        Integer min = Integer.valueOf(minimumNumberOfGroupElements);
        Integer max = Integer.valueOf(maximumNumberOfGroupElements);
        if ((min.intValue() > max.intValue()) || (min.intValue() <= 0)) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.numberGroupElements.invalidInterval", new ActionError(
                    "finalWorkInformationForm.numberGroupElements.invalidInterval"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        Integer orientatorCreditsPercentage = (Integer) finalWorkForm.get("responsibleCreditsPercentage");
        Integer coorientatorCreditsPercentage = (Integer) finalWorkForm.get("coResponsibleCreditsPercentage");
        if ((orientatorCreditsPercentage.intValue() < 0) || (coorientatorCreditsPercentage.intValue() < 0)
                || (orientatorCreditsPercentage.intValue() + coorientatorCreditsPercentage.intValue() != 100)) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.invalidCreditsPercentageDistribuition", new ActionError(
                    "finalWorkInformationForm.invalidCreditsPercentageDistribuition"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        final InfoProposalEditor infoFinalWorkProposal = new InfoProposalEditor();
        if (!StringUtils.isEmpty(externalId) && StringUtils.isNumeric(externalId)) {
            infoFinalWorkProposal.setExternalId(externalId);
        }
        infoFinalWorkProposal.setTitle(title);
        infoFinalWorkProposal.setOrientatorsCreditsPercentage(orientatorCreditsPercentage);
        infoFinalWorkProposal.setCoorientatorsCreditsPercentage(coorientatorCreditsPercentage);
        infoFinalWorkProposal.setFraming(framing);
        infoFinalWorkProposal.setObjectives(objectives);
        infoFinalWorkProposal.setDescription(description);
        infoFinalWorkProposal.setRequirements(requirements);
        infoFinalWorkProposal.setDeliverable(deliverable);
        infoFinalWorkProposal.setUrl(url);
        infoFinalWorkProposal.setMinimumNumberOfGroupElements(Integer.valueOf(minimumNumberOfGroupElements));
        infoFinalWorkProposal.setMaximumNumberOfGroupElements(Integer.valueOf(maximumNumberOfGroupElements));
        infoFinalWorkProposal.setObservations(observations);
        infoFinalWorkProposal.setLocation(location);
        final DegreeType dt = (degreeType != null && degreeType.length() > 0) ? DegreeType.valueOf(degreeType) : null;
        infoFinalWorkProposal.setDegreeType(dt);

        infoFinalWorkProposal.setOrientator(new InfoPerson((Person) FenixFramework.getDomainObject(orientatorOID)));
        if (coorientatorOID != null && !coorientatorOID.equals("")) {
            infoFinalWorkProposal.setCoorientator(new InfoPerson((Person) FenixFramework.getDomainObject(coorientatorOID)));
        }
        // final ExecutionDegree executionDegree =
        // FenixFramework.getDomainObject(Integer.valueOf(degree));
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOID);
        if (!(coorientatorOID != null && !coorientatorOID.equals(""))
                || executionDegree.getScheduling().getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
            infoFinalWorkProposal.setCompanionName(companionName);
            infoFinalWorkProposal.setCompanionMail(companionMail);
            if (companionPhone != null && !companionPhone.equals("") && StringUtils.isNumeric(companionPhone)) {
                infoFinalWorkProposal.setCompanionPhone(Integer.valueOf(companionPhone));
            }
            infoFinalWorkProposal.setCompanyAdress(companyAdress);
            infoFinalWorkProposal.setCompanyName(companyName);
        }
        infoFinalWorkProposal.setExecutionDegree(new InfoExecutionDegree(executionDegree));

        if (branchList != null && branchList.length > 0) {
            infoFinalWorkProposal.setBranches(new ArrayList());
            for (String brachOIDString : branchList) {
                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
                    InfoBranch infoBranch = new InfoBranch(FenixFramework.<Branch> getDomainObject(brachOIDString));
                    infoFinalWorkProposal.getBranches().add(infoBranch);
                }
            }
        }

        if (status != null && !status.equals("") && StringUtils.isNumeric(status)) {
            infoFinalWorkProposal.setStatus(new FinalDegreeWorkProposalStatus(Integer.valueOf(status)));
        }

        try {
            SubmitFinalWorkProposal.runSubmitFinalWorkProposal(infoFinalWorkProposal);
            request.setAttribute("proposalOID", infoFinalWorkProposal.getProposalOID());
        } catch (Exception e) {
            if (e instanceof OutOfPeriodException) {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("finalWorkInformationForm.scheduling.invalidInterval", new ActionError(
                        "finalWorkInformationForm.scheduling.invalidInterval"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            } else if (e instanceof DomainException) {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }
            throw new FenixActionException(e);
        }

        return mapping.findForward("show-final-degree-work-list");
    }

    public ActionForward showTeacherName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        DynaActionForm finalWorkForm = (DynaActionForm) form;

        final ExecutionDegree executionDegree = getExecutionDegree(request);
        final Scheduleing scheduleing = executionDegree.getScheduling();

        String alteredField = (String) finalWorkForm.get("alteredField");
        String number = null;

        if ("orientator".equals(alteredField)) {
            number = (String) finalWorkForm.get("responsableTeacherId");
        }

        if ("coorientator".equals(alteredField)) {
            number = (String) finalWorkForm.get("coResponsableTeacherId");
        }

        if (number == null || number.equals("")) {
            if ("orientator".equals(alteredField)) {
                finalWorkForm.set("orientatorOID", "");
                finalWorkForm.set("responsableTeacherName", "");
            } else if ("coorientator".equals(alteredField)) {
                finalWorkForm.set("coorientatorOID", "");
                finalWorkForm.set("coResponsableTeacherName", "");
            }

            return prepareFinalWorkInformation(mapping, form, request, response);
        }

        Person person = null;
        if (number.substring(0, 3).equals("ist")) {
            person = Person.readPersonByUsername(number);
        }
        if (StringUtils.isNumeric(number)) {
            final Integer n = new Integer(number);
            final Employee employee = Employee.readByNumber(n);
            if (employee != null) {
                person = employee.getPerson();
            }
        }
        if (person == null || !(person.hasRole(RoleType.TEACHER))
                || !(person.hasRole(RoleType.RESEARCHER) || !(person.hasAnyProfessorships()))) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.unexistingTeacher", new ActionError(
                    "finalWorkInformationForm.unexistingTeacher"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        if ("orientator".equals(alteredField)) {
            finalWorkForm.set("orientatorOID", person.getExternalId().toString());
            finalWorkForm.set("responsableTeacherName", person.getName());
            request.setAttribute("orientator", person);
        }
        if ("coorientator".equals(alteredField)) {
            finalWorkForm.set("coorientatorOID", person.getExternalId().toString());
            finalWorkForm.set("coResponsableTeacherName", person.getName());
            request.setAttribute("coorientator", person);
            if (!scheduleing.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
                finalWorkForm.set("companionName", "");
                finalWorkForm.set("companionMail", "");
                finalWorkForm.set("companionPhone", "");
                finalWorkForm.set("companyAdress", "");
                finalWorkForm.set("companyName", "");
                finalWorkForm.set("alteredField", "");
            }
        }
        return createNewFinalDegreeWorkProposal(mapping, form, request, response);
    }

    public ActionForward prepareFinalWorkInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        User userView = Authenticate.getUser();

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        finalWorkForm.set("degreeType", DegreeType.DEGREE.toString());

        /*
         * Just put the degreeCurricularPlanID in request as it is
         */
        // Integer degreeCurricularPlanID =
        // Integer.valueOf(Integer.parseInt(request.getParameter("degreeCurricularPlanID")));
        // request.setAttribute("degreeCurricularPlanID",
        // degreeCurricularPlanID);
        request.setAttribute("degreeCurricularPlanID", request.getParameter("degreeCurricularPlanID"));

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(request.getParameter("executionDegreeOID"));
        // InfoExecutionDegree infoExecutionDegree =
        // CommonServiceRequests.getInfoExecutionDegree(userView, Integer
        // .valueOf(degreeId));

        // final ExecutionDegree executionDegree = (ExecutionDegree)
        // readDomainObject(request, ExecutionDegree.class,
        // infoExecutionDegree.getExternalId());
        request.setAttribute("executionDegree", executionDegree);
        // request.setAttribute("executionDegreeOID",
        // executionDegree.getExternalId());
        request.setAttribute("executionDegreeOID", executionDegree.getExternalId());
        final Scheduleing scheduleing = executionDegree.getScheduling();
        final List branches = new ArrayList();
        for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
            final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
            branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
                    degreeCurricularPlan.getExternalId()));
        }
        // List branches =
        // CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
        // infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId());
        request.setAttribute("branches", branches);

        request.setAttribute("finalDegreeWorkProposalStatusList", FinalDegreeWorkProposalStatus.getLabelValueList());

        return mapping.findForward("show-final-degree-work-proposal");
    }

    public ActionForward coorientatorVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String alteredField = (String) finalWorkForm.get("alteredField");
        String companionName = (String) finalWorkForm.get("companionName");
        String companionMail = (String) finalWorkForm.get("companionMail");
        String companionPhone = (String) finalWorkForm.get("companionPhone");
        String companyAdress = (String) finalWorkForm.get("companyAdress");
        String companyName = (String) finalWorkForm.get("companyName");

        final ExecutionDegree executionDegree = getDomainObject(request, "executionDegreeOID");
        final Scheduleing scheduleing = executionDegree.getScheduling();

        if (alteredField.equals("companion") && companionName.equals("") && companionMail.equals("") && companionPhone.equals("")
                && companyAdress.equals("") && companyName.equals("")) {
            finalWorkForm.set("alteredField", "");
        }

        if (alteredField.equals("companion") && companionName.equals("") && companionMail.equals("") && companionPhone.equals("")
                && companyAdress.equals("") && companyName.equals("")
                && !scheduleing.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
            finalWorkForm.set("coorientatorOID", "");
            finalWorkForm.set("coResponsableTeacherName", "");
        } else {
            if (alteredField.equals("companion") || !companionName.equals("") || !companionMail.equals("")
                    || !companionPhone.equals("") || !companyAdress.equals("") || !companyName.equals("")) {
                finalWorkForm.set("alteredField", "companion");
            }

        }

        return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward publishAprovedProposals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        User userView = Authenticate.getUser();

        final ExecutionDegree executionDegree = getExecutionDegree(request);
        final Scheduleing scheduleing = executionDegree.getScheduling();
        if (scheduleing == null) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.scheduling.not.defined", new ActionError("error.scheduling.not.defined"));
            saveErrors(request, actionErrors);
            return finalDegreeWorkInfo(mapping, form, request, response);
        }

        PublishAprovedFinalDegreeWorkProposals.run(executionDegree);
        return showProposals(mapping, form, request, response);
    }

    public ActionForward aproveSelectedProposals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        return changeSelectedProposalsStatus(mapping, actionForm, request, response,
                FinalDegreeWorkProposalStatus.APPROVED_STATUS);
    }

    public ActionForward publishSelectedProposals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        return changeSelectedProposalsStatus(mapping, actionForm, request, response,
                FinalDegreeWorkProposalStatus.PUBLISHED_STATUS);
    }

    public ActionForward publishProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String proposalOID = (String) getFromRequest(request, "proposalOID");
        return changeProposals(mapping, actionForm, request, response, FinalDegreeWorkProposalStatus.PUBLISHED_STATUS,
                new String[] { proposalOID });
    }

    public ActionForward approveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String proposalOID = (String) getFromRequest(request, "proposalOID");
        return changeProposals(mapping, actionForm, request, response, FinalDegreeWorkProposalStatus.APPROVED_STATUS,
                new String[] { proposalOID });
    }

    public ActionForward changeSelectedProposalsStatus(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, FinalDegreeWorkProposalStatus status) throws FenixActionException,
            FenixServiceException {

        String[] selectedProposals = request.getParameterValues("selectedProposals");
        return changeProposals(mapping, actionForm, request, response, status, selectedProposals);
    }

    private ActionForward changeProposals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, FinalDegreeWorkProposalStatus status, String[] selectedProposals)
            throws FenixActionException {
        Set<Proposal> proposals = new HashSet<Proposal>();
        if (selectedProposals != null) {
            for (String proposalOID : selectedProposals) {
                proposals.add((Proposal) FenixFramework.getDomainObject(proposalOID));
            }
        }
        ChangeStatusOfFinalDegreeWorkProposals.run(proposals, status);
        return showProposals(mapping, actionForm, request, response);
    }

    public ActionForward attributeGroupProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        GroupProposal groupProposal = getDomainObject(request, "groupProposalOID");
        AttributeFinalDegreeWork.run(groupProposal);

        return showCandidates(mapping, form, request, response);
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String studentNumber = request.getParameter("studentNumber");
        if (studentNumber != null && !studentNumber.isEmpty()) {
            request.setAttribute("studentNumber", studentNumber);
            final Student student = Student.readStudentByNumber(Integer.valueOf(studentNumber));
            final Registration registration = student.getLastActiveRegistration();
            if (registration != null) {
                request.setAttribute("registrationOID", registration.getExternalId().toString());
            }
        }
        // String degreeCurrucularPlanID =
        // request.getParameter("degreeCurrucularPlanID");
        // request.setAttribute("degreeCurrucularPlanID",
        // Integer.valueOf(degreeCurrucularPlanID));

        return mapping.findForward("show-student-curricular-plan");
    }

    public ActionForward addExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String executionDegreeOIDString = dynaActionForm.getString("executionDegreeOID");
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOIDString);
        final String otherExecutionDegreeIDString = dynaActionForm.getString("otherExecutionDegreeID");
        if (otherExecutionDegreeIDString != null && otherExecutionDegreeIDString.length() > 0) {
            final ExecutionDegree otherExecutionDegree = FenixFramework.getDomainObject(otherExecutionDegreeIDString);
            AddExecutionDegreeToScheduling.runAddExecutionDegreeToScheduling(executionDegree.getScheduling(),
                    otherExecutionDegree);
        }
        dynaActionForm.set("otherExecutionDegreeID", null);
        return prepare(mapping, form, request, response);
    }

    public ActionForward removeExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String executionDegreeOIDString = dynaActionForm.getString("executionDegreeOID");
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOIDString);
        final String otherExecutionDegreeIDString = dynaActionForm.getString("otherExecutionDegreeID");
        final ExecutionDegree otherExecutionDegree = FenixFramework.getDomainObject(otherExecutionDegreeIDString);

        RemoveExecutionDegreeToScheduling.runRemoveExecutionDegreeToScheduling(executionDegree.getScheduling(),
                otherExecutionDegree);

        dynaActionForm.set("otherExecutionDegreeID", null);

        return prepare(mapping, form, request, response);
    }

    public ActionForward proposalsXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        final ExecutionDegree executionDegree = getExecutionDegree(request);
        final ExecutionYear executionYear = executionDegree.getExecutionYear();
        final String yearString = executionYear.getNextYearsYearString().replace('/', '_');

        try {
            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=proposals_" + yearString + ".xls");

            final HSSFWorkbook workbook = new HSSFWorkbook();
            final ExcelStyle excelStyle = new ExcelStyle(workbook);
            final ServletOutputStream writer = response.getOutputStream();

            final Spreadsheet proposalsSpreadsheet = new Spreadsheet("Proposals " + yearString);
            setProposalsHeaders(proposalsSpreadsheet);
            fillProposalsSpreadSheet(executionDegree, proposalsSpreadsheet);
            proposalsSpreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());

            final Spreadsheet groupsSpreadsheet = new Spreadsheet("Groups " + yearString);
            fillGroupsSpreadSheet(executionDegree, groupsSpreadsheet);
            groupsSpreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());

            final Scheduleing scheduleing = executionDegree.getScheduling();

            final Set<ExecutionDegree> allExecutionDegrees = new HashSet<ExecutionDegree>();
            for (final ExecutionDegree otherExecutionDegree : scheduleing.getExecutionDegreesSet()) {
                for (final FinalDegreeWorkGroup group : otherExecutionDegree.getAssociatedFinalDegreeWorkGroupsSet()) {
                    if (!group.getGroupProposalsSet().isEmpty()) {
                        for (final GroupStudent groupStudent : group.getGroupStudentsSet()) {
                            final Registration registration = groupStudent.getRegistration();
                            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
                            final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
                            final ExecutionDegree executionDegreeByYear =
                                    degreeCurricularPlan.getExecutionDegreeByYear(otherExecutionDegree.getExecutionYear());
                            if (executionDegreeByYear != null) {
                                allExecutionDegrees.add(executionDegreeByYear);
                            }
                        }
                    }
                }
            }

            // for (final ExecutionDegree otherExecutionDegree :
            // scheduleing.getExecutionDegreesSet()) {
            for (final ExecutionDegree otherExecutionDegree : allExecutionDegrees) {
                final DegreeCurricularPlan degreeCurricularPlan = otherExecutionDegree.getDegreeCurricularPlan();
                final Spreadsheet studentsSpreadsheet =
                        new Spreadsheet("Alunos " + degreeCurricularPlan.getName() + " " + yearString);
                fillStudentsSpreadSheet(otherExecutionDegree, scheduleing, studentsSpreadsheet);
                studentsSpreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
            }

            workbook.write(writer);

            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    private void setProposalsHeaders(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader("Proposta");
        spreadsheet.setHeader("Estado da Proposta");
        spreadsheet.setHeader("T�tulo");
        spreadsheet.setHeader("Utilizador " + Unit.getInstitutionAcronym() + " Orientador");
        spreadsheet.setHeader("Nome Orientador");
        spreadsheet.setHeader("Utilizador " + Unit.getInstitutionAcronym() + " Coorientador");
        spreadsheet.setHeader("Nome Coorientador");
        spreadsheet.setHeader("Percentagem Cr�ditos Orientador");
        spreadsheet.setHeader("Percentagem Cr�ditos Coorientador");
        spreadsheet.setHeader("Nome do Acompanhante");
        spreadsheet.setHeader("Email do Acompanhante");
        spreadsheet.setHeader("Telefone do Acompanhante");
        spreadsheet.setHeader("Nome da empresa");
        spreadsheet.setHeader("Morada da empresa");
        spreadsheet.setHeader("Enquadramento");
        spreadsheet.setHeader("Objectivos");
        spreadsheet.setHeader("Descri��o");
        spreadsheet.setHeader("Requisitos");
        spreadsheet.setHeader("Resultado esperado");
        spreadsheet.setHeader("URL");
        spreadsheet.setHeader("�rea de Especializa��o");
        spreadsheet.setHeader("N�mero m�nimo de elementos do grupo");
        spreadsheet.setHeader("N�mero m�ximo de elementos do grupo");
        spreadsheet.setHeader("Adequa��o a Disserta��o");
        spreadsheet.setHeader("Observa��es");
        spreadsheet.setHeader("Localiza��o da realiza��o do TFC");
    }

    private void setGroupsHeaders(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader("N�mero");
        spreadsheet.setHeader("Estado da Proposta");
        spreadsheet.setHeader("T�tulo");
        spreadsheet.setHeader("Utilizador " + Unit.getInstitutionAcronym() + " Orientador");
        spreadsheet.setHeader("Nome Orientador");
        spreadsheet.setHeader("Utilizador " + Unit.getInstitutionAcronym() + " Coorientador");
        spreadsheet.setHeader("Nome Coorientador");
        spreadsheet.setHeader("Percentagem Cr�ditos Orientador");
        spreadsheet.setHeader("Percentagem Cr�ditos Coorientador");
        spreadsheet.setHeader("Nome do Acompanhante");
        spreadsheet.setHeader("Email do Acompanhante");
        spreadsheet.setHeader("Telefone do Acompanhante");
        spreadsheet.setHeader("Nome da empresa");
        spreadsheet.setHeader("Morada da empresa");
        spreadsheet.setHeader("Enquadramento");
        spreadsheet.setHeader("Objectivos");
        spreadsheet.setHeader("Descri��o");
        spreadsheet.setHeader("Requisitos");
        spreadsheet.setHeader("Resultado esperado");
        spreadsheet.setHeader("URL");
        spreadsheet.setHeader("�rea de Especializa��o");
        spreadsheet.setHeader("N�mero m�nimo de elementos do grupo");
        spreadsheet.setHeader("N�mero m�ximo de elementos do grupo");
        spreadsheet.setHeader("Adequa��o a Disserta��o");
        spreadsheet.setHeader("Observa��es");
        spreadsheet.setHeader("Localiza��o da realiza��o do TFC");
    }

    private static final MessageResources applicationResources = MessageResources
            .getMessageResources(Bundle.APPLICATION);
    private static final MessageResources enumerationResources = MessageResources
            .getMessageResources(Bundle.ENUMERATION);

    private void fillProposalsSpreadSheet(final ExecutionDegree executionDegree, final Spreadsheet spreadsheet) {
        int maxNumberStudentsPerGroup = 0;

        final Scheduleing scheduleing = executionDegree.getScheduling();
        final SortedSet<Proposal> proposals = new TreeSet<Proposal>(new BeanComparator("proposalNumber"));
        proposals.addAll(scheduleing.getProposalsSet());
        for (final Proposal proposal : proposals) {
            final Row row = spreadsheet.addRow();
            row.setCell(proposal.getProposalNumber().toString());
            if (proposal.getGroupAttributed() != null) {
                row.setCell("Atribuido");
            } else if (proposal.getStatus() != null) {
                row.setCell(proposal.getStatus().getKey());
            } else {
                row.setCell("");
            }
            row.setCell(proposal.getTitle());
            row.setCell(proposal.getOrientator().getIstUsername());
            row.setCell(proposal.getOrientator().getName());
            if (proposal.getCoorientator() != null) {
                row.setCell(proposal.getCoorientator().getIstUsername());
                row.setCell(proposal.getCoorientator().getName());
            } else {
                row.setCell("");
                row.setCell("");
            }
            if (proposal.getOrientatorsCreditsPercentage() != null) {
                row.setCell(proposal.getOrientatorsCreditsPercentage().toString());
            } else {
                row.setCell("");
            }
            if (proposal.getCoorientatorsCreditsPercentage() != null) {
                row.setCell(proposal.getCoorientatorsCreditsPercentage().toString());
            } else {
                row.setCell("");
            }
            row.setCell(proposal.getCompanionName());
            row.setCell(proposal.getCompanionMail());
            if (proposal.getCompanionPhone() != null) {
                row.setCell(proposal.getCompanionPhone().toString());
            } else {
                row.setCell("");
            }
            row.setCell(proposal.getCompanyName());
            row.setCell(proposal.getCompanyAdress());
            row.setCell(proposal.getFraming());
            row.setCell(proposal.getObjectives());
            row.setCell(proposal.getDescription());
            row.setCell(proposal.getRequirements());
            row.setCell(proposal.getDeliverable());
            row.setCell(proposal.getUrl());
            final StringBuilder branches = new StringBuilder();
            boolean appendSeperator = false;
            for (final Branch branch : proposal.getBranchesSet()) {
                if (appendSeperator) {
                    branches.append(", ");
                } else {
                    appendSeperator = true;
                }
                branches.append(branch.getName());
            }
            row.setCell(branches.toString());
            if (proposal.getMinimumNumberOfGroupElements() != null) {
                row.setCell(proposal.getMinimumNumberOfGroupElements().toString());
            } else {
                row.setCell("");
            }
            if (proposal.getMaximumNumberOfGroupElements() != null) {
                row.setCell(proposal.getMaximumNumberOfGroupElements().toString());
            } else {
                row.setCell("");
            }
            if (proposal.getDegreeType() == null) {
                row.setCell(applicationResources.getMessage("label.both"));
            } else {
                row.setCell(enumerationResources.getMessage(proposal.getDegreeType().getName()));
            }
            row.setCell(proposal.getObservations());
            row.setCell(proposal.getLocation());
            if (proposal.getGroupAttributed() != null) {
                int i = 0;
                for (final GroupStudent groupStudent : proposal.getGroupAttributed().getGroupStudentsSet()) {
                    final Registration registration = groupStudent.getRegistration();
                    row.setCell(registration.getNumber().toString());
                    row.setCell(registration.getPerson().getName());
                    maxNumberStudentsPerGroup = Math.max(maxNumberStudentsPerGroup, ++i);
                }
            } else if (proposal.getGroupAttributedByTeacher() != null) {
                int i = 0;
                for (final GroupStudent groupStudent : proposal.getGroupAttributedByTeacher().getGroupStudentsSet()) {
                    final Registration registration = groupStudent.getRegistration();
                    row.setCell(registration.getNumber().toString());
                    row.setCell(registration.getPerson().getName());
                    maxNumberStudentsPerGroup = Math.max(maxNumberStudentsPerGroup, ++i);
                }
            }
        }

        for (int i = 0; i < maxNumberStudentsPerGroup; i++) {
            spreadsheet.setHeader("N�mero aluno " + (i + 1));
            spreadsheet.setHeader("Nome aluno " + (i + 1));
        }
    }

    private void fillGroupsSpreadSheet(final ExecutionDegree executionDegree, final Spreadsheet spreadsheet) {
        final Scheduleing scheduleing = executionDegree.getScheduling();
        final SortedSet<FinalDegreeWorkGroup> groups = scheduleing.getGroupsWithProposalsSortedByStudentNumbers();
        int numberGroups = 0;
        int maxNumStudentsPerGroup = 0;
        for (final FinalDegreeWorkGroup group : groups) {
            final Row row = spreadsheet.addRow();
            row.setCell(Integer.toString(++numberGroups));
            final SortedSet<GroupStudent> groupStudents =
                    CollectionUtils.constructSortedSet(group.getGroupStudentsSet(), GroupStudent.COMPARATOR_BY_STUDENT_NUMBER);
            for (final GroupStudent groupStudent : groupStudents) {
                row.setCell(groupStudent.getRegistration().getNumber().toString());
            }
            maxNumStudentsPerGroup = Math.max(maxNumStudentsPerGroup, groupStudents.size());
        }
        spreadsheet.setHeader("Grupo");
        for (int i = 0; i < maxNumStudentsPerGroup; spreadsheet.setHeader("Aluno " + ++i)) {
            ;
        }
        int groupCounter = 0;
        int maxNumberGroupProposals = 0;
        for (final FinalDegreeWorkGroup group : groups) {
            final Row row = spreadsheet.getRow(groupCounter++);
            for (int i = group.getGroupStudentsSet().size(); i++ < maxNumStudentsPerGroup; row.setCell("")) {
                ;
            }
            final SortedSet<GroupProposal> groupProposals = group.getGroupProposalsSortedByPreferenceOrder();
            for (final GroupProposal groupProposal : groupProposals) {
                row.setCell(groupProposal.getFinalDegreeWorkProposal().getProposalNumber().toString());
            }
            for (int i = groupProposals.size(); i++ < scheduleing.getMaximumNumberOfProposalCandidaciesPerGroup().intValue(); row
                    .setCell("")) {
                ;
            }
            if (group.getProposalAttributed() != null) {
                row.setCell(group.getProposalAttributed().getProposalNumber().toString());
            } else if (group.getProposalAttributedByTeacher() != null) {
                row.setCell(group.getProposalAttributedByTeacher().getProposalNumber().toString());
            } else {
                row.setCell("");
            }
            maxNumberGroupProposals = Math.max(maxNumberGroupProposals, groupProposals.size());
        }
        for (int i = 0; i < maxNumberGroupProposals; spreadsheet.setHeader("Proposta de pref. " + ++i)) {
            ;
        }
        spreadsheet.setHeader("Proposta Atribuida");
    }

    private void fillStudentsSpreadSheet(final ExecutionDegree executionDegree, final Scheduleing scheduleing,
            final Spreadsheet spreadsheet) {
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final ExecutionYear executionYear = executionDegree.getExecutionYear();
        final Integer maximumCurricularYearToCountCompletedCourses =
                scheduleing.getMaximumCurricularYearToCountCompletedCourses();

        spreadsheet.setHeader("Aluno");
        final List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            final Set<CurricularCourseScope> curricularCourseScopes =
                    curricularCourse.getActiveScopesInExecutionYear(executionYear);
            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                if (maximumCurricularYearToCountCompletedCourses != null) {
                    if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() <= maximumCurricularYearToCountCompletedCourses
                            .intValue()) {
                        curricularCourses.add(curricularCourse);
                        spreadsheet.setHeader(curricularCourse.getName());
                    }
                } else {
                    curricularCourses.add(curricularCourse);
                    spreadsheet.setHeader(curricularCourse.getName());
                }
            }
        }

        // final SortedSet<Registration> students = new
        // TreeSet<Registration>(Registration.NUMBER_COMPARATOR);
        for (final ExecutionDegree otherExecutionDegree : scheduleing.getExecutionDegreesSet()) {
            for (final FinalDegreeWorkGroup group : otherExecutionDegree.getAssociatedFinalDegreeWorkGroupsSet()) {
                if (!group.getGroupProposalsSet().isEmpty()) {
                    for (final GroupStudent groupStudent : group.getGroupStudentsSet()) {
                        final Registration registration = groupStudent.getRegistration();
                        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
                        if (studentCurricularPlan.getDegreeCurricularPlan() == degreeCurricularPlan) {
                            final Row row = spreadsheet.addRow();
                            row.setCell(registration.getNumber().toString());
                            for (final CurricularCourse curricularCourse : curricularCourses) {
                                if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
                                    final Grade grade = registration.findGradeForCurricularCourse(curricularCourse);
                                    if (!grade.isEmpty()) {
                                        row.setCell(grade.getValue());
                                    } else {
                                        // FIXME what's the sense in this?...
                                        row.setCell(GradeScale.AP);
                                    }
                                } else {
                                    row.setCell("");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ActionForward detailedProposalList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final ExecutionDegree executionDegree = getExecutionDegree(request);
        final SortedSet<Proposal> proposals = new TreeSet<Proposal>(new BeanComparator("proposalNumber"));
        proposals.addAll(executionDegree.getScheduling().getProposalsSet());
        request.setAttribute("proposals", proposals);
        return mapping.findForward("detailed-proposal-list");
    }

}
