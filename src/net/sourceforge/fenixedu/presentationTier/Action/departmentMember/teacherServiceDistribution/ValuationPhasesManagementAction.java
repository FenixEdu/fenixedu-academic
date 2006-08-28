package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ValuationPhasesManagementAction extends FenixDispatchAction {
	private static final Integer COPY_LAST_YEAR_REAL_DATA = 0;
	private static final Integer COPY_PREVIOUS_PHASE_DATA = 1;
	private static final Integer SUCCESSFUL_VALUATION = 2;
	
	private static final Integer NOT_SELECTED_EXECUTION_PERIOD = -1;
	private static final Integer NOT_SELECTED_EXECUTION_YEAR = -1;
	private static final Integer NOT_SELECTED_DEPARTMENT = -1;
	private static final Integer NOT_SELECTED_DISTRIBUTION = -1;
	
	public ActionForward prepareForValuationPhasesManagement(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
				
		initializeVariables(dynaForm);
		
		return loadValuationPhases(mapping, form, request, response);
	}
	
	
	public ActionForward createValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		
		String name = (String) dynaForm.get("name");
		
		Object[] parameters = new Object[] {
				teacherServiceDistribution.getIdInternal(),
				name
		};
		
		ServiceUtils.executeService(userView, "CreateValuationPhase", parameters );
		
		
		return loadValuationPhases(mapping, form, request, response);
	}
	
	public ActionForward  setCurrentValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);
		
		ServiceUtils.executeService(userView, "SetCurrentValuationPhase", new Object[] { selectedValuationPhase.getIdInternal() });
		
		return loadValuationPhases(mapping, form, request, response);
	}
	
	public ActionForward closeValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);
		
		ServiceUtils.executeService(userView, "CloseValuationPhase", new Object[] { selectedValuationPhase.getIdInternal() });
		
		return loadValuationPhases(mapping, form, request, response);		
	}

	public ActionForward openValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);
		
		ServiceUtils.executeService(userView, "OpenValuationPhase", new Object[] { selectedValuationPhase.getIdInternal() });
		
		return loadValuationPhases(mapping, form, request, response);		
	}

	public ActionForward deleteValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);
		
		ServiceUtils.executeService(userView, "DeleteValuationPhase", new Object[] { selectedValuationPhase.getIdInternal() });
		
		return loadValuationPhases(mapping, form, request, response);		
	}

	public ActionForward setPublishedStateOnValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);
		Boolean isPublished = (Boolean) dynaForm.get("isPublished");
		
		Object[] arguments = new Object[] {
				selectedValuationPhase.getIdInternal(),
				isPublished
		};
		
		ServiceUtils.executeService(userView, "SetPublishedStateOnValuationPhase", arguments);
		
		return loadValuationPhases(mapping, form, request, response);
	}

	
	public ActionForward loadValuationPhases(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		
		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		request.setAttribute("valuationPhaseList", teacherServiceDistribution.getOrderedValuationPhases());
		
		return mapping.findForward("showValuationPhasesManagementForm");
	}
	
	
	public ActionForward prepareForCurrentValuationPhaseDataManagement(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		Integer teacherServiceDistributionId = getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		
		dynaForm.set("dataManagementOption", COPY_LAST_YEAR_REAL_DATA);
		
		return showValuationPhaseDataManagementOptions(mapping, form, request, response);
	}
	
	public ActionForward showValuationPhaseDataManagementOptions(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		
		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		
		return mapping.findForward("valuationPhaseDataManagementOptions");
	}
	
	
	public ActionForward manageCurrentValuationPhaseData(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		
		Integer dataManagementOption = (Integer) dynaForm.get("dataManagementOption");
		
		if(dataManagementOption.equals(COPY_LAST_YEAR_REAL_DATA)) {
			ServiceUtils.executeService(userView, "CopyLastYearRealDataToValuationPhase", 
					new Object[] { teacherServiceDistribution.getCurrentValuationPhase().getIdInternal() });
		} else if(dataManagementOption.equals(COPY_PREVIOUS_PHASE_DATA)) {
			Integer valuationPhaseId = (Integer) dynaForm.get("valuationPhase");
			ServiceUtils.executeService(userView, "CopyValuationPhaseDataToValuationPhase", 
					new Object[] { valuationPhaseId, teacherServiceDistribution.getCurrentValuationPhase().getIdInternal() });
		}
		
		dynaForm.set("dataManagementOption", SUCCESSFUL_VALUATION);
		return showValuationPhaseDataManagementOptions(mapping, form, request, response);
	}
	
	public ActionForward prepareForOmissionValuesValuation(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		Integer teacherServiceDistributionId = getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		
		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		request.setAttribute("currentValuationPhase", teacherServiceDistribution.getCurrentValuationPhase());
		
		return mapping.findForward("showOmissionValuesValuationForm");
	}
	
	public ActionForward resetSelectedTeacherServiceDistribution(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
            HttpServletResponse response) throws Exception {
	
		DynaActionForm dynaForm = (DynaActionForm) form;
		dynaForm.set("selectedTeacherServiceDistribution", null);
				
		return prepareToChooseValuationPhase(mapping, form, request, response);
	}
	
	
	public ActionForward prepareToChooseValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
		Collections.sort(executionYearList, new BeanComparator("year"));
				
		ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);
		
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		if(selectedExecutionYear != null) {
			executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
		}
				
		Department selectedDepartment = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
		
		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		
		List<TeacherServiceDistribution> teacherServiceDistributionList;
		
		ExecutionPeriod  selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm);
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		TeacherServiceDistribution selectedTeacherServiceDistribution= getSelectedTeacherServiceDistribution(userView, dynaForm);
				
	
		if(selectedExecutionYear == null){
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributions();
		} else if(selectedExecutionPeriod != null){
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionPeriod(selectedExecutionPeriod);
		} else {
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionYear(selectedExecutionYear);
		}
		
		List<ValuationPhase> valuationPhases = new ArrayList<ValuationPhase>();
		
		if(selectedTeacherServiceDistribution != null){
			request.setAttribute("selectedTeacherServiceDistribution", selectedTeacherServiceDistribution);
			if(selectedTeacherServiceDistribution == teacherServiceDistribution){
				valuationPhases.addAll(teacherServiceDistribution.getPreviousValuationPhases(
						teacherServiceDistribution.getCurrentValuationPhase()));
			} else {
				valuationPhases.addAll(selectedTeacherServiceDistribution.getOrderedValuationPhases());
			}
		}

		request.setAttribute("executionYearList", executionYearList);
		request.setAttribute("executionPeriodsList", executionPeriodList);
		request.setAttribute("teacherServiceDistributionList", teacherServiceDistributionList);
		request.setAttribute("valuationPhaseList", valuationPhases);
		
		return showValuationPhaseDataManagementOptions(mapping, form, request, response);
	}
		
	private void initializeVariables(DynaActionForm dynaForm) {
		dynaForm.set("valuationPhase", null);
	}

	private Integer getFromRequestAndSetOnFormTeacherServiceDistributionId(HttpServletRequest request, DynaActionForm dynaForm) {
		Integer teacherServiceDistributionId = new Integer(request.getParameter("teacherServiceDistribution"));
		dynaForm.set("teacherServiceDistribution", teacherServiceDistributionId);
		return teacherServiceDistributionId;
	}

	private TeacherServiceDistribution getTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		Integer teacherServiceDistributionId = (Integer) dynaForm.get("teacherServiceDistribution");
		TeacherServiceDistribution teacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId);

		return teacherServiceDistribution;
	}

	private ValuationPhase getSelectedValuationPhase(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		Integer selectedValuationPhaseId = (Integer) dynaForm.get("valuationPhase");
		ValuationPhase selectedValuationPhase = rootDomainObject.readValuationPhaseByOID(selectedValuationPhaseId);

		return selectedValuationPhase;
	}

	private ExecutionYear getSelectedExecutionYear(IUserView userView, DynaActionForm dynaForm, List<ExecutionYear> executionYearList) throws FenixServiceException, FenixFilterException {
		Integer selectedExecutionYearId = (Integer) dynaForm.get("executionYear");
		
		ExecutionYear selectedExecutionYear = rootDomainObject.readExecutionYearByOID(selectedExecutionYearId);
		
		if(selectedExecutionYear == null) {
			if(executionYearList != null && !executionYearList.isEmpty()) {
				return ExecutionYear.readCurrentExecutionYear();
			} else {
				return null;
			}
		}
		
		return selectedExecutionYear;
	}
	
	private TeacherServiceDistribution getSelectedTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		String selectedTSDId = (String) dynaForm.get("selectedTeacherServiceDistribution");
		
		return (selectedTSDId == null || selectedTSDId.equals("")) ? null : rootDomainObject.readTeacherServiceDistributionByOID(Integer.parseInt(selectedTSDId)); 
	}
	
	private ExecutionPeriod getSelectedExecutionPeriod(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriod");
		
		if(selectedExecutionPeriodId == NOT_SELECTED_EXECUTION_PERIOD) {
			return null;
		}
		
		ExecutionPeriod selectedExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(selectedExecutionPeriodId);
		
		return selectedExecutionPeriod;
	}
}
