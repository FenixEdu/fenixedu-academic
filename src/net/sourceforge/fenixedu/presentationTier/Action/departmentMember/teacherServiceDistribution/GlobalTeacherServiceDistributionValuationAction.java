package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.CourseValuationDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTeacherServiceDistributionValuationForm;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.utl.fenix.utils.Pair;

public class GlobalTeacherServiceDistributionValuationAction extends FenixDispatchAction {
	private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS = 0;
	private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES = 1;
	private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES = 2;

	private static final Integer NOT_SELECTED_EXECUTION_PERIOD = -1;
	private static final Integer NOT_SELECTED_DEPARTMENT = -1;
	

	public ActionForward prepareForGlobalTeacherServiceDistributionValuation(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		
		GlobalTeacherServiceDistributionValuationForm globalForm = (GlobalTeacherServiceDistributionValuationForm) form;
		
		List<ExecutionYear> executionYearList = getNotClosedExecutionYears(userView);
		Collections.sort(executionYearList, new BeanComparator("year"));
				
		ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, globalForm);
		
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		if(selectedExecutionYear != null) {
			executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
			Collections.sort(executionPeriodList, new BeanComparator("semester"));
		}
			
		List<TeacherServiceDistribution> teacherServiceDistributionList;
		ExecutionPeriod  selectedExecutionPeriod = getSelectedExecutionPeriod(userView, globalForm);
		Department selectedDepartment = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
		
		if(selectedExecutionPeriod != null) {
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionPeriod(selectedExecutionPeriod);	
		} else if(selectedExecutionYear != null){
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionYear(selectedExecutionYear);
		} else {
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributions();
		}
				
		teacherServiceDistributionList = selectOnlyTeacherServiceDistributionsWithPublishedPhases(teacherServiceDistributionList);
		Collections.sort(teacherServiceDistributionList, new BeanComparator("name"));
		
		request.setAttribute("executionYearList", executionYearList);
		request.setAttribute("executionPeriodsList", executionPeriodList);
		request.setAttribute("teacherServiceDistributionList", teacherServiceDistributionList);
		
		return mapping.findForward("showGlobalTeacherServiceDistributions");
	}
	
	

	@SuppressWarnings("unchecked")
	private List<TeacherServiceDistribution> selectOnlyTeacherServiceDistributionsWithPublishedPhases(List<TeacherServiceDistribution> teacherServiceDistributionList) {
		return (List<TeacherServiceDistribution>) CollectionUtils.select(teacherServiceDistributionList, new Predicate() {

			public boolean evaluate(Object arg0) {
				TeacherServiceDistribution teacherServiceDistribution = (TeacherServiceDistribution) arg0;
				return !teacherServiceDistribution.getOrderedPublishedValuationPhases().isEmpty();
			}
			
		});
	}

	public ActionForward changeToViewTeachers(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		GlobalTeacherServiceDistributionValuationForm globalForm = (GlobalTeacherServiceDistributionValuationForm) form;
		globalForm.setViewType(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS);
		
		return viewGlobalTeacherServiceDistributionValuation(mapping, form, request, response);
	}

	public ActionForward changeToViewCourses(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		GlobalTeacherServiceDistributionValuationForm globalForm = (GlobalTeacherServiceDistributionValuationForm) form;
		globalForm.setViewType(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES);
		
		return viewGlobalTeacherServiceDistributionValuation(mapping, form, request, response);
	}

	public ActionForward changeToViewTeacherAndCourses(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		GlobalTeacherServiceDistributionValuationForm globalForm = (GlobalTeacherServiceDistributionValuationForm) form;
		globalForm.setViewType(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES);
		
		return viewGlobalTeacherServiceDistributionValuation(mapping, form, request, response);
	}
	
	public ActionForward viewGlobalTeacherServiceDistributionValuation(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		GlobalTeacherServiceDistributionValuationForm globalForm = (GlobalTeacherServiceDistributionValuationForm) form;
		List<TeacherServiceDistribution> selectedTeacherServiceDistributionList = retrieveSelectedTeacherServiceDistributions(userView, globalForm);
		List<ExecutionPeriod> executionPeriodList = retrieveExecutionPeriodsFromSelectedTeacherServiceDistributions(selectedTeacherServiceDistributionList);
		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, globalForm);
		
		Map<Integer, Pair<Integer, Integer>> teacherServiceDistributionMap = new HashMap<Integer, Pair<Integer, Integer>>();
		
		for (TeacherServiceDistribution teacherServiceDistribution : selectedTeacherServiceDistributionList) {
			ValuationPhase selectedValuationPhase = getSelectedValuationPhaseFromGlobalForm(userView, globalForm, teacherServiceDistribution);
			ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(userView, globalForm, teacherServiceDistribution);
			
			teacherServiceDistributionMap.put(selectedValuationPhase.getIdInternal(), new Pair<Integer, Integer>(selectedValuationGrouping.getIdInternal(), (selectedExecutionPeriod != null) ? selectedExecutionPeriod.getIdInternal() : NOT_SELECTED_EXECUTION_PERIOD));
		}
		
		List<CourseValuationDTOEntry> courseValuationDTOEntryList = null;
		List<ValuationTeacherDTOEntry> valuationTeacherDTOEntryList = null;
		Integer viewType = globalForm.getViewType();
		
		if(viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES) || viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
			courseValuationDTOEntryList = 
				(List<CourseValuationDTOEntry>) ServiceUtils.executeService(userView, "ReadCourseValuationsFromTeacherServiceDistributions", new Object[] {teacherServiceDistributionMap});
		}
		
		if(viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS) || viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
			valuationTeacherDTOEntryList =
				(List<ValuationTeacherDTOEntry>) ServiceUtils.executeService(userView, "ReadValuationTeachersFromTeacherServiceDistributions", new Object[] {teacherServiceDistributionMap});			
		}

		String comparatorStr = "name";
		if(viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
			comparatorStr = "acronym";
		}
		
		if(courseValuationDTOEntryList != null && !courseValuationDTOEntryList.isEmpty()){
			Collections.sort(courseValuationDTOEntryList, new BeanComparator("courseValuation." + comparatorStr));
		}
		if(valuationTeacherDTOEntryList != null && !valuationTeacherDTOEntryList.isEmpty()){
			Collections.sort(valuationTeacherDTOEntryList, new BeanComparator(comparatorStr));
		}

		request.setAttribute("courseValuationDTOEntryList", courseValuationDTOEntryList);
		request.setAttribute("valuationTeacherDTOEntryList", valuationTeacherDTOEntryList);
		request.setAttribute("executionPeriodList", executionPeriodList);
		
		if(viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)) {
			return mapping.findForward("showGlobalTeacherServiceDistributionsValuationByCourses");
		} else if(viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
			return mapping.findForward("showGlobalTeacherServiceDistributionsValuationByTeachersAndCourses");
		} else if(viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)) {
			return mapping.findForward("showGlobalTeacherServiceDistributionsValuationByTeachers");
		} else {
			return mapping.findForward("showGlobalTeacherServiceDistributionsValuationByCourses");
		}
		
	}


	private ValuationGrouping getSelectedValuationGrouping(IUserView userView, GlobalTeacherServiceDistributionValuationForm globalForm, TeacherServiceDistribution teacherServiceDistribution) throws FenixFilterException, FenixServiceException {
		Integer valuationGroupingId = Integer.parseInt((String) globalForm.getValuationGrouping(teacherServiceDistribution.getIdInternal().toString()));
		ValuationGrouping selectedValuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		
		return selectedValuationGrouping;
	}

	private ValuationPhase getSelectedValuationPhaseFromGlobalForm(IUserView userView, GlobalTeacherServiceDistributionValuationForm globalForm, TeacherServiceDistribution teacherServiceDistribution) throws FenixFilterException, FenixServiceException {
		Integer valuationPhaseId = Integer.parseInt((String) globalForm.getValuationPhase(teacherServiceDistribution.getIdInternal().toString()));
		ValuationPhase selectedValuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		
		return selectedValuationPhase;
	}

	private List<TeacherServiceDistribution> retrieveSelectedTeacherServiceDistributions(IUserView userView, GlobalTeacherServiceDistributionValuationForm globalForm) throws FenixFilterException, FenixServiceException {
		List<TeacherServiceDistribution> selectedTeacherServiceDistributionList = new ArrayList<TeacherServiceDistribution>();
		
		List<String> selectedTeacherServiceDistributionIdList = globalForm.getSelectedTeacherServiceDistributions();
		for (String selectedTeacherServiceDistributionIdStr : selectedTeacherServiceDistributionIdList) {
			Integer selectedTeacherServiceDistributionId = Integer.parseInt(selectedTeacherServiceDistributionIdStr);
			
			TeacherServiceDistribution selectedTeacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(selectedTeacherServiceDistributionId);
			
			if(selectedTeacherServiceDistribution != null) {
				selectedTeacherServiceDistributionList.add(selectedTeacherServiceDistribution);
			}
		}
		
		return selectedTeacherServiceDistributionList;
	}

	private void fillDynamicGlobalFormWithTeacherServiceDistributions(GlobalTeacherServiceDistributionValuationForm globalForm, List<TeacherServiceDistribution> teacherServiceDistributionList) {
		for (TeacherServiceDistribution teacherServiceDistribution : teacherServiceDistributionList) {
			globalForm.setTeacherServiceDistribution(teacherServiceDistribution.getIdInternal().toString(), Boolean.FALSE);
			globalForm.setValuationPhase(teacherServiceDistribution.getIdInternal().toString(), teacherServiceDistribution.getCurrentValuationPhase().getIdInternal());
			globalForm.setValuationGrouping(teacherServiceDistribution.getIdInternal().toString(), teacherServiceDistribution.getCurrentValuationPhase().getRootValuationGrouping());
		}
	}

	private List<ExecutionYear> getNotClosedExecutionYears(IUserView userView) throws FenixFilterException, FenixServiceException {
		List<ExecutionYear> notClosedExecutionYearList = new ArrayList<ExecutionYear>();
		Collection<ExecutionYear> executionYearList = rootDomainObject.readAllDomainObjects(ExecutionYear.class);
		
		for (ExecutionYear year : executionYearList) {
			if(!year.getState().equals(PeriodState.CLOSED)) {
				notClosedExecutionYearList.add(year);
			}
		}
		
		return notClosedExecutionYearList;
	}

	private ExecutionYear getSelectedExecutionYear(IUserView userView, GlobalTeacherServiceDistributionValuationForm globalForm) throws FenixServiceException, FenixFilterException {
		Integer selectedExecutionYearId = globalForm.getExecutionYear();	
		
		return rootDomainObject.readExecutionYearByOID(selectedExecutionYearId);
	}

	private ExecutionYear getCurrentExecutionYear(List<ExecutionYear> executionYearList) {
		return (ExecutionYear) CollectionUtils.find(executionYearList, new Predicate() {
			public boolean evaluate(Object arg0) {
				ExecutionYear executionYear = (ExecutionYear) arg0;
				return executionYear.getState().equals(PeriodState.CURRENT);
			}
		});
	}	

	private ExecutionPeriod getSelectedExecutionPeriod(IUserView userView, GlobalTeacherServiceDistributionValuationForm globalForm) throws FenixServiceException, FenixFilterException {
		Integer selectedExecutionPeriodId = globalForm.getExecutionPeriod();
		
		return rootDomainObject.readExecutionPeriodByOID(selectedExecutionPeriodId);
	}

	private List<ExecutionPeriod> retrieveExecutionPeriodsFromSelectedTeacherServiceDistributions(List<TeacherServiceDistribution> teacherServiceDistributionList) {
		Set<ExecutionPeriod> executionPeriodSet = new HashSet<ExecutionPeriod>();
		
		for(TeacherServiceDistribution teacherServiceDistribution : teacherServiceDistributionList) {
			executionPeriodSet.addAll(teacherServiceDistribution.getExecutionPeriods());
		}
		
		return new ArrayList<ExecutionPeriod>(executionPeriodSet);
	}

}
