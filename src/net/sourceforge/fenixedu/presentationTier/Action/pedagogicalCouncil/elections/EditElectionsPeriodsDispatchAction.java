package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.elections;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class EditElectionsPeriodsDispatchAction extends ElectionsPeriodsManagementDispatchAction {
	
	private ActionForward prepareEditYearDelegateElectionPeriod(ActionMapping mapping, ActionForm actionForm, 
			HttpServletRequest request, HttpServletResponse response, DelegateElection election, YearMonthDay startDate,
				YearMonthDay endDate) throws Exception {
		final String forwardTo = (String)getFromRequest(request, "forwardTo");
		
		ElectionPeriodBean bean = new ElectionPeriodBean();
		bean.setDegree(election.getDegree());
		bean.setDegreeType(election.getDegree().getDegreeType());
		bean.setStartDate(startDate);
		bean.setEndDate(endDate);
		
		bean.setCurricularYear(((YearDelegateElection)election).getCurricularYear());
		
		bean.setElection(election);
		
		request.setAttribute("forwardTo", forwardTo);
		request.setAttribute("electionPeriodBean", bean);
		request.setAttribute("editElectionBean", bean);

		return selectDegreeType(mapping, actionForm, request, response);
	}
	
	public ActionForward prepareEditYearDelegateCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Integer electionOID = Integer.parseInt((String)getFromRequest(request, "selectedPeriod"));
		final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);
		
		YearMonthDay startDate = election.getCandidacyStartDate();
		YearMonthDay endDate = election.getCandidacyEndDate();
		
		return prepareEditYearDelegateElectionPeriod(mapping, actionForm, request, response, election, startDate, endDate);
	}
	
	public ActionForward prepareEditYearDelegateVotingPeriod(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Integer electionOID = Integer.parseInt((String)getFromRequest(request, "selectedPeriod"));
		final DelegateElection election = rootDomainObject.readDelegateElectionByOID(electionOID);
		
		YearMonthDay startDate = election.getVotingStartDate();
		YearMonthDay endDate = election.getVotingEndDate();
		
		return prepareEditYearDelegateElectionPeriod(mapping, actionForm, request, response, election, startDate, endDate);
	}
	
	public ActionForward prepareEditYearDelegateElectionsPeriods(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String forwardTo = (String)getFromRequest(request, "forwardTo");
		final List<String> selectedDegrees = (List<String>)getCheckboxesValues(request, "selectedDegrees");
		request.setAttribute("selectedDegrees", selectedDegrees);
		
		ElectionPeriodBean bean = (ElectionPeriodBean)getFromRequest(request, "electionPeriodBean");
		RenderUtils.invalidateViewState("electionPeriodBean");
		
		if(selectedDegrees != null) {
			request.setAttribute("selectedDegrees", selectedDegrees);
			request.setAttribute("editElectionBean", bean);
		}
		
		request.setAttribute("forwardTo", forwardTo);
		request.setAttribute("electionPeriodBean", bean);
		
		return selectDegreeType(mapping, actionForm, request, response);
	}
	
	public ActionForward prepareDeleteYearDelegateElectionsPeriods(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String forwardTo = (String)getFromRequest(request, "forwardTo");
		final List<String> selectedDegrees = (List<String>)getCheckboxesValues(request, "selectedDegrees");
		
		ElectionPeriodBean bean = (ElectionPeriodBean)getFromRequest(request, "electionPeriodBean");
		RenderUtils.invalidateViewState("electionPeriodBean");
		
		if(selectedDegrees != null) {
			request.setAttribute("selectedDegrees", selectedDegrees);
			request.setAttribute("deleteElectionBean", bean);
		}
		
		if(selectedDegrees == null && request.getParameter("deleteVotingPeriod") != null) {
			request.setAttribute("deleteElectionBean", bean);
		}
		
		request.setAttribute("forwardTo", forwardTo);
		request.setAttribute("electionPeriodBean", bean);
		
		return selectDegreeType(mapping, actionForm, request, response);
	}
	
	public ActionForward editYearDelegateCandidacyPeriods(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final List<String> selectedDegrees = (List<String>)getCheckboxesValues(request, "selectedDegrees");
		
		ElectionPeriodBean editElectionBean = (ElectionPeriodBean)getFromRequest(request, "editElectionBean");
		RenderUtils.invalidateViewState("editElectionBean");
		RenderUtils.invalidateViewState("electionPeriodBean");
		
		request.setAttribute("forwardTo", "createEditCandidacyPeriods");
		request.setAttribute("electionPeriodBean", editElectionBean);
		
		if(request.getParameter("deleteVotingPeriod") != null) {
			return prepareDeleteYearDelegateElectionsPeriods(mapping, actionForm, request, response);
		}
		
		if(request.getParameter("delete") != null) {
			return editYearDelegateElectionPeriods(mapping, actionForm, request, response, selectedDegrees, 
					"DeleteDelegateCandidacyPeriod", editElectionBean);
		}
		
		return editYearDelegateElectionPeriods(mapping, actionForm, request, response, selectedDegrees, 
				"EditDelegateCandidacyPeriod", editElectionBean);
	}
	
	public ActionForward editYearDelegateVotingPeriods(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		final List<String> selectedDegrees = (List<String>)getCheckboxesValues(request, "selectedDegrees");
		
		ElectionPeriodBean editElectionBean = (ElectionPeriodBean)getFromRequest(request, "editElectionBean");
		RenderUtils.invalidateViewState("editElectionBean");
		RenderUtils.invalidateViewState("electionPeriodBean");
		
		request.setAttribute("forwardTo", "createEditVotingPeriods");
		request.setAttribute("electionPeriodBean", editElectionBean);
		
		if(request.getParameter("deleteVotingPeriod") != null) {
			return prepareDeleteYearDelegateElectionsPeriods(mapping, actionForm, request, response);
		}
		
		if(request.getParameter("delete") != null) {
			return editYearDelegateElectionPeriods(mapping, actionForm, request, response, selectedDegrees, 
					"DeleteDelegateVotingPeriod", editElectionBean);
		}
		
		return editYearDelegateElectionPeriods(mapping, actionForm, request, response, selectedDegrees,
					"EditDelegateVotingPeriod", editElectionBean);
	}
	
	public ActionForward editYearDelegateElectionPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, List<String> selectedDegrees, String service, ElectionPeriodBean editElectionBean) throws Exception {
		
		if(selectedDegrees == null){
			try {
				executeService(request, service, new Object[] { editElectionBean });
				
			} catch (FenixServiceException ex) {
				addActionMessage(request, ex.getMessage(), ex.getArgs());
			}
		}
		else {
			for (String degreeOID : selectedDegrees) {
				try {	
					executeService(request, service, new Object[] { editElectionBean,  degreeOID});
				} catch (FenixServiceException ex) {
					addActionMessage(request, ex.getMessage(), ex.getArgs());
				}
			}
		}
		
		editElectionBean.setCurricularYear(null);
		editElectionBean.setStartDate(null);
		editElectionBean.setEndDate(null);
		editElectionBean.setDegree(null);
		editElectionBean.setElection(null);
		
		return selectDegreeType(mapping, actionForm, request, response);
	}
	
}
