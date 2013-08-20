package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.elections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections.CreateDelegateVotingPeriod;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.NewRoundElectionBean;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.YearDelegateElectionsPeriodsByDegreeBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionResultsByStudentDTO;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@Mapping(module = "pedagogicalCouncil", path = "/electionsPeriodsManagement", scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(name = "showPossibleDelegates", path = "/pedagogicalCouncil/delegates/showPossibleDelegates.jsp",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "deleteYearDelegateVotingPeriods",
                        path = "/editElectionsPeriods.do?method=prepareDeleteYearDelegateElectionsPeriods&forwardTo=createEditVotingPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "createYearDelegateCandidacyPeriod",
                        path = "/createElectionsPeriods.do?method=prepareCreateYearDelegateCandidacyPeriod&forwardTo=createEditCandidacyPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(name = "createEditVotingPeriods", path = "/pedagogicalCouncil/elections/createEditVotingPeriods.jsp",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.votesinyeardelegates")),
                @Forward(
                        name = "editYearDelegateVotingPeriods",
                        path = "/editElectionsPeriods.do?method=prepareEditYearDelegateElectionsPeriods&forwardTo=createEditVotingPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "createYearDelegateVotingPeriods",
                        path = "/createElectionsPeriods.do?method=prepareCreateYearDelegateElectionsPeriods&forwardTo=createEditVotingPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "createYearDelegateCandidacyPeriods",
                        path = "/createElectionsPeriods.do?method=prepareCreateYearDelegateElectionsPeriods&forwardTo=createEditCandidacyPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "createYearDelegateVotingPeriod",
                        path = "/createElectionsPeriods.do?method=prepareCreateYearDelegateVotingPeriod&forwardTo=createEditVotingPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(name = "showCandidates", path = "/pedagogicalCouncil/elections/showCandidates.jsp",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "deleteYearDelegateCandidacyPeriods",
                        path = "/editElectionsPeriods.do?method=prepareDeleteYearDelegateElectionsPeriods&forwardTo=createEditCandidacyPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(name = "showVotingResults", path = "/pedagogicalCouncil/elections/showVotingResults.jsp",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(name = "showCandidacyPeriods", path = "/pedagogicalCouncil/elections/showCandidacyPeriods.jsp",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.applicationresults")),
                @Forward(
                        name = "editYearDelegateCandidacyPeriod",
                        path = "/editElectionsPeriods.do?method=prepareEditYearDelegateCandidacyPeriod&forwardTo=createEditCandidacyPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(name = "showVotingPeriods", path = "/pedagogicalCouncil/elections/showVotingPeriods.jsp",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.votingresults")),
                @Forward(name = "secondRoundElections", path = "/pedagogicalCouncil/elections/secondRoundElections.jsp",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(name = "createEditCandidacyPeriods",
                        path = "/pedagogicalCouncil/elections/createEditCandidacyPeriods.jsp", tileProperties = @Tile(
                                title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "editYearDelegateCandidacyPeriods",
                        path = "/editElectionsPeriods.do?method=prepareEditYearDelegateElectionsPeriods&forwardTo=createEditCandidacyPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")),
                @Forward(
                        name = "editYearDelegateVotingPeriod",
                        path = "/editElectionsPeriods.do?method=prepareEditYearDelegateVotingPeriod&forwardTo=createEditVotingPeriods",
                        tileProperties = @Tile(title = "private.pedagogiccouncil.delegates.candidacies")) })
public class ElectionsPeriodsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        ElectionPeriodBean bean = new ElectionPeriodBean();
        bean.setDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE); // default
        bean.setExecutionYear(currentExecutionYear); // default

        request.setAttribute("electionPeriodBean", bean);
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward selectDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String forwardTo = (String) getFromRequest(request, "forwardTo");
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        ElectionPeriodBean bean = (ElectionPeriodBean) getFromRequest(request, "electionPeriodBean");

        if (bean == null) {
            final Degree degree = AbstractDomainObject.fromExternalId(request.getParameter("degreeOID"));

            bean = new ElectionPeriodBean();
            bean.setDegree(degree);
            bean.setDegreeType(degree.getDegreeType());
            bean.setExecutionYear(currentExecutionYear); // default
        } else {
            if (bean.getExecutionYear() == null) {
                bean.setExecutionYear(currentExecutionYear); // default
            }
            if (bean.getDegreeType() == null) {
                bean.setDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE); // default
            }
        }

        List<YearDelegateElectionsPeriodsByDegreeBean> electionsByDegree =
                new ArrayList<YearDelegateElectionsPeriodsByDegreeBean>();
        for (Degree degree : Degree.readAllByDegreeType(bean.getDegreeType())) {
            List<YearDelegateElection> elections = degree.getYearDelegateElectionsGivenExecutionYear(bean.getExecutionYear());
            YearDelegateElectionsPeriodsByDegreeBean electionBean =
                    new YearDelegateElectionsPeriodsByDegreeBean(degree, bean.getExecutionYear(), elections);
            electionsByDegree.add(electionBean);
        }

        request.setAttribute("electionPeriodBean", bean);
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        request.setAttribute("electionsByDegreeBean", electionsByDegree);
        return mapping.findForward(forwardTo);
    }

    public ActionForward showCandidates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        final YearDelegateElection yearDelegateElection =
                (YearDelegateElection) AbstractDomainObject.fromExternalId(request.getParameter("selectedCandidacyPeriod"));
        List<Student> candidates = yearDelegateElection.getCandidates();

        final ExecutionYear executionYear = yearDelegateElection.getExecutionYear();

        ElectionPeriodBean bean = new ElectionPeriodBean();
        bean.setCurricularYear(yearDelegateElection.getCurricularYear());
        bean.setDegree(yearDelegateElection.getDegree());
        bean.setDegreeType(yearDelegateElection.getDegree().getDegreeType());
        bean.setElection(yearDelegateElection);
        bean.setExecutionYear(executionYear);

        if (request.getParameter("showPhotos") != null) {
            request.setAttribute("candidatesWithPhotos", candidates);
        } else {
            request.setAttribute("candidatesWithoutPhotos", candidates);
        }

        request.setAttribute("currentExecutionYear", currentExecutionYear);
        request.setAttribute("electionPeriodBean", bean);
        return mapping.findForward("showCandidates");
    }

    public ActionForward showVotingResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String forwardTo = (String) getFromRequest(request, "forwardTo");

        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        final YearDelegateElection yearDelegateElection =
                (YearDelegateElection) AbstractDomainObject.fromExternalId((String) getFromRequest(request,
                        "selectedVotingPeriod"));

        final ExecutionYear executionYear = yearDelegateElection.getExecutionYear();

        List<DelegateElectionResultsByStudentDTO> electionResultsByStudentDTOList =
                yearDelegateElection.getLastVotingPeriod().getDelegateElectionResults();

        ElectionPeriodBean bean = new ElectionPeriodBean();
        bean.setCurricularYear(yearDelegateElection.getCurricularYear());
        bean.setDegree(yearDelegateElection.getDegree());
        bean.setDegreeType(yearDelegateElection.getDegree().getDegreeType());
        bean.setElection(yearDelegateElection);
        bean.setExecutionYear(executionYear);

        request.setAttribute("currentExecutionYear", currentExecutionYear);
        request.setAttribute("electionPeriodBean", bean);
        request.setAttribute("votingResultsByStudent", electionResultsByStudentDTOList);
        return mapping.findForward(forwardTo);
    }

    public ActionForward manageYearDelegateCandidacyPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ElectionPeriodBean bean = (ElectionPeriodBean) getFromRequest(request, "electionPeriodBean");
        request.setAttribute("electionPeriodBean", bean);

        if (request.getParameter("edit") != null) {
            return mapping.findForward("editYearDelegateCandidacyPeriods");
        }

        if (request.getParameter("create") != null) {
            return mapping.findForward("createYearDelegateCandidacyPeriods");
        }

        if (request.getParameter("delete") != null) {
            return mapping.findForward("deleteYearDelegateCandidacyPeriods");
        }

        request.setAttribute("forwardTo", "createEditCandidacyPeriods");
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward manageSingleYearDelegateCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DelegateElection election = AbstractDomainObject.fromExternalId(request.getParameter("selectedPeriod"));

        request.setAttribute("selectedPeriod", election);

        if (!election.getCandidacyPeriod().isPastPeriod()) {
            return mapping.findForward("editYearDelegateCandidacyPeriod");
        } else {
            return mapping.findForward("createYearDelegateCandidacyPeriod");
        }
    }

    public ActionForward manageYearDelegateVotingPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ElectionPeriodBean bean = (ElectionPeriodBean) getFromRequest(request, "electionPeriodBean");
        request.setAttribute("electionPeriodBean", bean);

        if (request.getParameter("edit") != null) {
            return mapping.findForward("editYearDelegateVotingPeriods");
        }

        if (request.getParameter("create") != null) {
            return mapping.findForward("createYearDelegateVotingPeriods");
        }

        if (request.getParameter("delete") != null) {
            return mapping.findForward("deleteYearDelegateVotingPeriods");
        }

        request.setAttribute("forwardTo", "createEditVotingPeriods");
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward manageSingleYearDelegateVotingPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DelegateElection election = AbstractDomainObject.fromExternalId(request.getParameter("selectedPeriod"));

        request.setAttribute("selectedPeriod", election);

        if (election.hasLastVotingPeriod() && !election.getLastVotingPeriod().isPastPeriod()) {
            return mapping.findForward("editYearDelegateVotingPeriod");
        } else {
            return mapping.findForward("createYearDelegateVotingPeriod");
        }
    }

    public ActionForward secondRoundElections(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DelegateElection election = AbstractDomainObject.fromExternalId(request.getParameter("selectedPeriod"));

        List<Student> candidatesHadVoted = new LinkedList<Student>();

        List<Student> candidatesHadNotVoted = new LinkedList<Student>();

        ElectionPeriodBean bean = new ElectionPeriodBean();
        bean.setDegree(election.getDegree());
        bean.setDegreeType(election.getDegree().getDegreeType());
        bean.setCurricularYear(((YearDelegateElection) election).getCurricularYear());
        bean.setElection(election);

        DelegateElectionVotingPeriod votingPeriod = election.getLastVotingPeriod();

        request.setAttribute("electionPeriodBean", bean);
        request.setAttribute("newElectionPeriodBean", bean);
        request.setAttribute("secondRoundElectionsCandidatesBean", new NewRoundElectionBean(candidatesHadVoted, election));
        request.setAttribute("secondRoundElectionsNotCandidatesBean", new NewRoundElectionBean(candidatesHadNotVoted, election));

        if (!votingPeriod.isFirstRoundElections()) {
            return mapping.findForward("secondRoundElections");
        }

        for (Student candidate : election.getCandidaciesHadVoted(votingPeriod)) {
            candidatesHadVoted.add(candidate);
        }

        for (Student candidate : election.getNotCandidaciesHadVoted(votingPeriod)) {
            candidatesHadNotVoted.add(candidate);
        }

        return mapping.findForward("secondRoundElections");
    }

    public ActionForward addCandidatesToSecondRoundElections(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        NewRoundElectionBean secondRoundElectionsCandidatesBean = getRenderedObject("secondRoundElectionsCandidatesBean");
        NewRoundElectionBean secondRoundElectionsNotCandidatesBean = getRenderedObject("secondRoundElectionsNotCandidatesBean");

        ElectionPeriodBean newElectionPeriodBean = getRenderedObject("newElectionPeriodBean");

        CreateDelegateVotingPeriod.run(newElectionPeriodBean, secondRoundElectionsCandidatesBean,
                secondRoundElectionsNotCandidatesBean);

        return prepare(mapping, actionForm, request, response);
    }

    public ActionForward exportResultsToFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String degreeTypeString = (String) getFromRequest(request, "degreeType");
        DegreeType degreeType = DegreeType.valueOf(degreeTypeString);
        ExecutionYear executionYear = getDomainObject(request, "executionYearOID");
        StyledExcelSpreadsheet spreadsheet =
                YearDelegateElection.exportElectionsResultsToFile(Degree.readAllByDegreeType(degreeType), executionYear);

        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        response.setContentType("application/txt");
        final String filename =
                String.format("electionsResults_%s_%s.xls", degreeType.getLocalizedName(),
                        executionYear.getYear().replace("/", "-"));
        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        writer.flush();
        response.flushBuffer();
        return null;
    }

    /*
     * AUXIALIARY METHODS
     */

    @Override
    protected Object getFromRequest(HttpServletRequest request, String id) {
        if (RenderUtils.getViewState(id) != null) {
            return RenderUtils.getViewState(id).getMetaObject().getObject();
        } else if (request.getParameter(id) != null) {
            return request.getParameter(id);
        } else {
            return request.getAttribute(id);
        }
    }

    protected Object getCheckboxesValues(HttpServletRequest request, String id) {
        if (request.getParameterValues(id) != null) {
            return Arrays.asList(request.getParameterValues(id));
        } else {
            return getFromRequest(request, id);
        }
    }
}
