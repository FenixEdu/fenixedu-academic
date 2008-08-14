package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.elections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.YearDelegateElectionsPeriodsByDegreeBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionResultsByStudentDTO;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
	    Integer degreeOID = Integer.parseInt(request.getParameter("degreeOID"));
	    final Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

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

	List<YearDelegateElectionsPeriodsByDegreeBean> electionsByDegree = new ArrayList<YearDelegateElectionsPeriodsByDegreeBean>();
	for (Degree degree : Degree.readAllByDegreeType(bean.getDegreeType())) {
	    List<YearDelegateElection> elections = degree.getYearDelegateElectionsGivenExecutionYear(bean.getExecutionYear());
	    YearDelegateElectionsPeriodsByDegreeBean electionBean = new YearDelegateElectionsPeriodsByDegreeBean(degree, bean
		    .getExecutionYear(), elections);
	    electionsByDegree.add(electionBean);
	}

	request.setAttribute("electionPeriodBean", bean);
	request.setAttribute("currentExecutionYear", currentExecutionYear);
	request.setAttribute("electionsByDegreeBean", electionsByDegree);
	return mapping.findForward(forwardTo);
    }

    public ActionForward showCandidates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Integer electionOID = Integer.parseInt(request.getParameter("selectedCandidacyPeriod"));
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	final YearDelegateElection yearDelegateElection = (YearDelegateElection) rootDomainObject
		.readDelegateElectionByOID(electionOID);
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

	Integer electionOID = Integer.parseInt((String) getFromRequest(request, "selectedVotingPeriod"));
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	final YearDelegateElection yearDelegateElection = (YearDelegateElection) rootDomainObject
		.readDelegateElectionByOID(electionOID);

	final ExecutionYear executionYear = yearDelegateElection.getExecutionYear();

	List<DelegateElectionResultsByStudentDTO> electionResultsByStudentDTOList = yearDelegateElection
		.getDelegateElectionResults();

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

	if (request.getParameter("edit") != null)
	    return mapping.findForward("editYearDelegateCandidacyPeriods");

	if (request.getParameter("create") != null)
	    return mapping.findForward("createYearDelegateCandidacyPeriods");

	if (request.getParameter("delete") != null)
	    return mapping.findForward("deleteYearDelegateCandidacyPeriods");

	request.setAttribute("forwardTo", "createEditCandidacyPeriods");
	return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward manageSingleYearDelegateCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer electionOID = Integer.parseInt(request.getParameter("selectedPeriod"));
	final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);

	request.setAttribute("selectedPeriod", election);

	if (!election.getCandidacyPeriod().isPastPeriod())
	    return mapping.findForward("editYearDelegateCandidacyPeriod");
	else
	    return mapping.findForward("createYearDelegateCandidacyPeriod");
    }

    public ActionForward manageYearDelegateVotingPeriods(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ElectionPeriodBean bean = (ElectionPeriodBean) getFromRequest(request, "electionPeriodBean");
	request.setAttribute("electionPeriodBean", bean);

	if (request.getParameter("edit") != null)
	    return mapping.findForward("editYearDelegateVotingPeriods");

	if (request.getParameter("create") != null)
	    return mapping.findForward("createYearDelegateVotingPeriods");

	if (request.getParameter("delete") != null)
	    return mapping.findForward("deleteYearDelegateVotingPeriods");

	request.setAttribute("forwardTo", "createEditVotingPeriods");
	return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward manageSingleYearDelegateVotingPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer electionOID = Integer.parseInt(request.getParameter("selectedPeriod"));
	final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);

	request.setAttribute("selectedPeriod", election);

	if (election.hasVotingPeriod() && !election.getVotingPeriod().isPastPeriod())
	    return mapping.findForward("editYearDelegateVotingPeriod");
	else
	    return mapping.findForward("createYearDelegateVotingPeriod");
    }

    /*
     * AUXIALIARY METHODS
     */

    protected Object getFromRequest(HttpServletRequest request, String id) {
	if (RenderUtils.getViewState(id) != null)
	    return RenderUtils.getViewState(id).getMetaObject().getObject();
	else if (request.getParameter(id) != null)
	    return request.getParameter(id);
	else
	    return request.getAttribute(id);
    }

    protected Object getCheckboxesValues(HttpServletRequest request, String id) {
	if (request.getParameterValues(id) != null)
	    return Arrays.asList(request.getParameterValues(id));
	else
	    return getFromRequest(request, id);
    }
}
