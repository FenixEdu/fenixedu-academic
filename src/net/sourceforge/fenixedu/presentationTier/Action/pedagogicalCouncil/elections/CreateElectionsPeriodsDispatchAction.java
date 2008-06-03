package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.elections;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateElectionsPeriodsDispatchAction extends ElectionsPeriodsManagementDispatchAction {
	
	/*
	 * Prepare create single election period (voting or candidacy period)
	 */
	private ActionForward prepareCreateYearDelegateElectionPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, 
			HttpServletResponse response, Degree degree, CurricularYear curricularYear, DelegateElection election) throws Exception {
		final String forwardTo = (String)getFromRequest(request, "forwardTo");
		
		ElectionPeriodBean bean = new ElectionPeriodBean();
		bean.setDegree(degree);
		bean.setDegreeType(degree.getDegreeType());
		bean.setCurricularYear(curricularYear);
		
		if(election != null)
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
		
		if(getFromRequest(request, "selectedPeriod") != null) {
			final Integer electionOID = Integer.parseInt(request.getParameter("selectedPeriod"));
			final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);
			
			Degree degree = election.getDegree();
			CurricularYear curricularYear = ((YearDelegateElection)election).getCurricularYear();
			
			return prepareCreateYearDelegateElectionPeriod(mapping, actionForm, request, response, degree, curricularYear, null);
		}
		else {
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
		
		final String forwardTo = (String)getFromRequest(request, "forwardTo");
		
		Integer electionOID = Integer.parseInt(request.getParameter("selectedPeriod"));
		final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);
		
		Degree degree = election.getDegree();
		CurricularYear curricularYear = ((YearDelegateElection)election).getCurricularYear();
		
		return prepareCreateYearDelegateElectionPeriod(mapping, actionForm, request, response, degree, curricularYear, election);
	}
	
	/*
	 * Prepare create multiple elections periods (candidacy or voting periods)
	 */
	public ActionForward prepareCreateYearDelegateElectionsPeriods(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String forwardTo = (String)getFromRequest(request, "forwardTo");
		final List<String> selectedDegrees = (List<String>)getCheckboxesValues(request, "selectedDegrees");
		request.setAttribute("selectedDegrees", selectedDegrees);
		
		ElectionPeriodBean bean = (ElectionPeriodBean)getFromRequest(request, "electionPeriodBean");
		RenderUtils.invalidateViewState("electionPeriodBean");
		
		if(selectedDegrees != null) {
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
		
		final List<String> selectedDegrees = (List<String>)getCheckboxesValues(request, "selectedDegrees");
		
		ElectionPeriodBean newElectionPeriodBean = (ElectionPeriodBean)getFromRequest(request, "newElectionPeriodBean");
		RenderUtils.invalidateViewState("newElectionPeriodBean");
		RenderUtils.invalidateViewState("electionPeriodBean");
		
		return createYearDelegateElectionPeriods(mapping, actionForm, request, response, selectedDegrees,
				"CreateDelegateCandidacyPeriod", "createEditCandidacyPeriods", newElectionPeriodBean);
	}

	/*
	 * Prepare create multiple elections voting periods
	 */
	public ActionForward createYearDelegateVotingPeriods(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final List<String> selectedDegrees = (List<String>)getCheckboxesValues(request, "selectedDegrees");
		
		ElectionPeriodBean newElectionPeriodBean = (ElectionPeriodBean)getFromRequest(request, "newElectionPeriodBean");
		RenderUtils.invalidateViewState("newElectionPeriodBean");
		RenderUtils.invalidateViewState("electionPeriodBean");
		
		return createYearDelegateElectionPeriods(mapping, actionForm, request, response, selectedDegrees,
				"CreateDelegateVotingPeriod", "createEditVotingPeriods", newElectionPeriodBean);
	}
	
	/*
	 * Create single or multiple elections periods
	 */
	public ActionForward createYearDelegateElectionPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, List<String> selectedDegrees, String service, String forwardTo, 
				ElectionPeriodBean newElectionPeriodBean) throws Exception {
		
		if(selectedDegrees == null){
			try {
				executeService(request, service, new Object[] { newElectionPeriodBean });
				
			} catch (FenixServiceException ex) {
				addActionMessage(request, ex.getMessage(), ex.getArgs());
			}
		}
		else {
			for (String degreeOID : selectedDegrees) {
				try {
					executeService(request, service, new Object[] { newElectionPeriodBean,  degreeOID});
				} catch (FenixServiceException ex) {
					addActionMessage(request, ex.getMessage(), ex.getArgs());
				}
			}
		}
		
		newElectionPeriodBean.setCurricularYear(null);
		newElectionPeriodBean.setStartDate(null);
		newElectionPeriodBean.setEndDate(null);
		newElectionPeriodBean.setDegree(null);
		
		request.setAttribute("forwardTo", forwardTo);
		request.setAttribute("electionPeriodBean", newElectionPeriodBean);
		
		return selectDegreeType(mapping, actionForm, request, response);
	}
}
