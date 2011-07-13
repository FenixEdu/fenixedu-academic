package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.elections;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections.CreateDelegateCandidacyPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections.CreateDelegateVotingPeriod;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "pedagogicalCouncil", path = "/createElectionsPeriods", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "createEditVotingPeriods", path = "/pedagogicalCouncil/elections/createEditVotingPeriods.jsp"),
		@Forward(name = "createEditCandidacyPeriods", path = "/pedagogicalCouncil/elections/createEditCandidacyPeriods.jsp") })
public class CreateElectionsPeriodsDispatchAction extends ElectionsPeriodsManagementDispatchAction {
    private static final String ELECTIONS_PACKAGE = "net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.elections";

    /*
     * Prepare create single election period (voting or candidacy period)
     */
    private ActionForward prepareCreateYearDelegateElectionPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response, Degree degree, CurricularYear curricularYear,
	    DelegateElection election) throws Exception {
	final String forwardTo = (String) getFromRequest(request, "forwardTo");

	ElectionPeriodBean bean = new ElectionPeriodBean();
	bean.setDegree(degree);
	bean.setDegreeType(degree.getDegreeType());
	bean.setCurricularYear(curricularYear);

	if (election != null)
	    bean.setElection(election);

	request.setAttribute("forwardTo", forwardTo);
	request.setAttribute("electionPeriodBean", bean);
	request.setAttribute("newElectionPeriodBean", bean);

	return selectDegreeType(mapping, actionForm, request, response);
    }

    /*
     * Prepare create single election candidacy period
     */
    public ActionForward prepareCreateYearDelegateCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	if (getFromRequest(request, "selectedPeriod") != null) {
	    final Integer electionOID = Integer.parseInt(request.getParameter("selectedPeriod"));
	    final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);

	    Degree degree = election.getDegree();
	    CurricularYear curricularYear = ((YearDelegateElection) election).getCurricularYear();

	    return prepareCreateYearDelegateElectionPeriod(mapping, actionForm, request, response, degree, curricularYear, null);
	} else {
	    final Integer year = Integer.parseInt(request.getParameter("selectedYear"));
	    final CurricularYear curricularYear = CurricularYear.readByYear(year);
	    final Integer degreeOID = Integer.parseInt(request.getParameter("selectedDegree"));
	    final Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

	    return prepareCreateYearDelegateElectionPeriod(mapping, actionForm, request, response, degree, curricularYear, null);
	}
    }

    /*
     * Prepare create single election voting period
     */
    public ActionForward prepareCreateYearDelegateVotingPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final String forwardTo = (String) getFromRequest(request, "forwardTo");

	Integer electionOID = Integer.parseInt(request.getParameter("selectedPeriod"));
	final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);

	Degree degree = election.getDegree();
	CurricularYear curricularYear = ((YearDelegateElection) election).getCurricularYear();

	return prepareCreateYearDelegateElectionPeriod(mapping, actionForm, request, response, degree, curricularYear, election);
    }

    /*
     * Prepare create multiple elections periods (candidacy or voting periods)
     */
    public ActionForward prepareCreateYearDelegateElectionsPeriods(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final String forwardTo = (String) getFromRequest(request, "forwardTo");
	final List<String> selectedDegrees = (List<String>) getCheckboxesValues(request, "selectedDegrees");
	request.setAttribute("selectedDegrees", selectedDegrees);

	ElectionPeriodBean bean = (ElectionPeriodBean) getFromRequest(request, "electionPeriodBean");
	RenderUtils.invalidateViewState("electionPeriodBean");

	if (selectedDegrees != null) {
	    request.setAttribute("selectedDegrees", selectedDegrees);
	    request.setAttribute("newElectionPeriodBean", bean);
	}

	request.setAttribute("forwardTo", forwardTo);
	request.setAttribute("electionPeriodBean", bean);

	return selectDegreeType(mapping, actionForm, request, response);
    }

    /*
     * Prepare create multiple elections candidacy periods
     */
    public ActionForward createYearDelegateCandidacyPeriods(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final List<String> selectedDegrees = (List<String>) getCheckboxesValues(request, "selectedDegrees");

	ElectionPeriodBean newElectionPeriodBean = (ElectionPeriodBean) getFromRequest(request, "newElectionPeriodBean");
	RenderUtils.invalidateViewState("newElectionPeriodBean");
	RenderUtils.invalidateViewState("electionPeriodBean");

	try {
	    if (selectedDegrees == null) {
		CreateDelegateCandidacyPeriod.run(newElectionPeriodBean);
	    } else {
		for (String degreeOID : selectedDegrees) {
		    CreateDelegateCandidacyPeriod.run(newElectionPeriodBean, degreeOID);
		}
	    }
	    newElectionPeriodBean.setCurricularYear(null);
	    newElectionPeriodBean.setStartDate(null);
	    newElectionPeriodBean.setEndDate(null);
	    newElectionPeriodBean.setDegree(null);
	} catch (FenixServiceException ex) {
	    addActionMessage(request, ex.getMessage(), ex.getArgs());
	    request.setAttribute("newElectionPeriodBean", newElectionPeriodBean);
	    request.setAttribute("selectedDegrees", selectedDegrees);
	}

	request.setAttribute("forwardTo", "createEditCandidacyPeriods");
	request.setAttribute("electionPeriodBean", newElectionPeriodBean);

	return selectDegreeType(mapping, actionForm, request, response);
    }

    /*
     * Prepare create multiple elections voting periods
     */
    public ActionForward createYearDelegateVotingPeriods(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final List<String> selectedDegrees = (List<String>) getCheckboxesValues(request, "selectedDegrees");

	ElectionPeriodBean newElectionPeriodBean = (ElectionPeriodBean) getFromRequest(request, "newElectionPeriodBean");
	RenderUtils.invalidateViewState("newElectionPeriodBean");
	RenderUtils.invalidateViewState("electionPeriodBean");

	try {
	    if (selectedDegrees == null) {
		CreateDelegateVotingPeriod.run(newElectionPeriodBean);
	    } else {
		for (String degreeOID : selectedDegrees) {
		    CreateDelegateVotingPeriod.run(newElectionPeriodBean, degreeOID);
		}
	    }
	    newElectionPeriodBean.setCurricularYear(null);
	    newElectionPeriodBean.setStartDate(null);
	    newElectionPeriodBean.setEndDate(null);
	    newElectionPeriodBean.setDegree(null);
	} catch (FenixServiceException ex) {
	    addActionMessage(request, ex.getMessage(), ex.getArgs());
	    request.setAttribute("newElectionPeriodBean", newElectionPeriodBean);
	    request.setAttribute("selectedDegrees", selectedDegrees);
	}

	request.setAttribute("electionPeriodBean", newElectionPeriodBean);
	request.setAttribute("forwardTo", "createEditVotingPeriods");
	return selectDegreeType(mapping, actionForm, request, response);
    }
}
