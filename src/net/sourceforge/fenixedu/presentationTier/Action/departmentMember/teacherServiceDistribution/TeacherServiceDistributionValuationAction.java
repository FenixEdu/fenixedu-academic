package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.CourseValuationDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationGroupingDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.teacherServiceDistribution.report.TeacherServiceDistributionChart;
import net.sourceforge.fenixedu.util.teacherServiceDistribution.report.TeacherServiceDistributionSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.utl.fenix.utils.Pair;

public class TeacherServiceDistributionValuationAction extends
		FenixDispatchAction {
	
	private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS = 0;
	private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES = 1;
	private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES = 2;
	private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS = 4;
	
	
	public ActionForward prepareForTeacherServiceDistributionValuation(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		DynaActionForm dynaForm = (DynaActionForm) form;
		
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		initializeVariables(dynaForm);
		
		return loadTeacherServiceDistribution(mapping, form, request, response);
	}
	
	public ActionForward loadValuationPhase(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);
		dynaForm.set("valuationGrouping", selectedValuationPhase.getRootValuationGrouping().getIdInternal());
		
		return loadTeacherServiceDistribution(mapping, form, request, response);
	}
	
	public ActionForward changeToViewCourses(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES);
		
		return loadTeacherServiceDistribution(mapping, form, request, response);
	}

	public ActionForward changeToViewTeachers(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS);
		
		return loadTeacherServiceDistribution(mapping, form, request, response);
	}

	public ActionForward changeToViewTeacherAndCourses(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;
		dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES);
		
		return loadTeacherServiceDistribution(mapping, form, request, response);
	}
	
	public ActionForward changeToViewCharts(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;
		dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS);
		
		return loadTeacherServiceDistribution(mapping, form, request, response);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward loadTeacherServiceDistribution(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		TeacherServiceDistribution teacherServiceDistribution =  getTeacherServiceDistribution(userView, dynaForm);
		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);
		
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>(teacherServiceDistribution.getExecutionPeriods());
		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
		List<ExecutionPeriod> selectedExecutionPeriodList = new ArrayList<ExecutionPeriod>();		
		if(selectedExecutionPeriod == null) {
			selectedExecutionPeriodList.addAll(executionPeriodList);
		} else {
			selectedExecutionPeriodList.add(selectedExecutionPeriod);
		}
		
		if(selectedValuationPhase == null) {
			selectedValuationPhase = teacherServiceDistribution.getCurrentValuationPhase();
			dynaForm.set("valuationPhase", selectedValuationPhase.getIdInternal());
		}
		ValuationGrouping rootValuationGrouping = selectedValuationPhase.getRootValuationGrouping();
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(userView, dynaForm, rootValuationGrouping);
				
		List<CourseValuationDTOEntry> courseValuationDTOEntryList = null;
		List<ValuationTeacherDTOEntry> valuationTeacherDTOEntryList = null;
		if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)) {
			courseValuationDTOEntryList = getCourseValuationDTOEntries(userView, selectedValuationPhase, selectedValuationGrouping, selectedExecutionPeriod);
			List<ValuationCompetenceCourse> nonValuatedCompetenceCourses = selectedValuationGrouping.getValuationCompetenceCourseWithoutCourseValuations(selectedExecutionPeriodList);
			if(!nonValuatedCompetenceCourses.isEmpty()) {
				Collections.sort(nonValuatedCompetenceCourses, new BeanComparator("name"));
				request.setAttribute("nonValuatedCompetenceCourses", nonValuatedCompetenceCourses);
			}
		} else if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
			courseValuationDTOEntryList = getCourseValuationDTOEntries(userView, selectedValuationPhase, selectedValuationGrouping, selectedExecutionPeriod);
			valuationTeacherDTOEntryList = getValuationTeacherDTOEntries(userView, selectedValuationPhase, selectedValuationGrouping, selectedExecutionPeriod);
		} else if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)) {
			valuationTeacherDTOEntryList = getValuationTeacherDTOEntries(userView, selectedValuationPhase, selectedValuationGrouping, selectedExecutionPeriod);
		}
		
		String comparatorStr = "name";
		if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
			comparatorStr = "acronym";
		}
		
		if(courseValuationDTOEntryList != null){
			Collections.sort(courseValuationDTOEntryList, new BeanComparator("courseValuation." + comparatorStr));
		}
		if(valuationTeacherDTOEntryList != null){
			Collections.sort(valuationTeacherDTOEntryList, new BeanComparator(comparatorStr));
		}
		
		request.setAttribute("courseValuationDTOEntryList", courseValuationDTOEntryList);
		request.setAttribute("valuationTeacherDTOEntryList", valuationTeacherDTOEntryList);
		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		request.setAttribute("valuationPhaseList", teacherServiceDistribution.getOrderedValuationPhases());
		request.setAttribute("valuationGroupingOptionEntryList", ValuationGroupingDTOEntry.getValuationGroupingOptionEntries(selectedValuationPhase));
		request.setAttribute("executionPeriodList", executionPeriodList);
		request.setAttribute("selectedExecutionPeriodList", selectedExecutionPeriodList);
			
		
		setInformationTableParameters(dynaForm, request);
		
		if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)) {
			return mapping.findForward("showTeacherServiceDistributionValuation");
		} else if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
			return mapping.findForward("showTeacherServiceDistributionValuationByTeachersAndCourses");
		} else if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)) {
			return mapping.findForward("showTeacherServiceDistributionValuationByTeachers");
		} else if(getViewType(dynaForm).equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS)) {
			return mapping.findForward("showTeacherServiceDistributionValuationByCharts");
		} else {
			return mapping.findForward("showTeacherServiceDistributionValuation");
		}
	}
	
	
	public ActionForward exportTeacherServiceDistributionValuationToExcel(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution =  getTeacherServiceDistribution(userView, dynaForm);
		ValuationPhase selectedValuationPhase = getSelectedValuationPhase(userView, dynaForm);

		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>(teacherServiceDistribution.getExecutionPeriods());
		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
		List<ExecutionPeriod> selectedExecutionPeriodList = new ArrayList<ExecutionPeriod>();		
		if(selectedExecutionPeriod == null) {
			selectedExecutionPeriodList.addAll(executionPeriodList);
		} else {
			selectedExecutionPeriodList.add(selectedExecutionPeriod);
		}
		
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(userView, dynaForm, selectedValuationPhase.getRootValuationGrouping());
				
		List<CourseValuationDTOEntry> courseValuationDTOEntryList = getCourseValuationDTOEntries(userView, selectedValuationPhase, selectedValuationGrouping, selectedExecutionPeriod);
		List<ValuationTeacherDTOEntry> valuationTeacherDTOEntryList = getValuationTeacherDTOEntries(userView, selectedValuationPhase, selectedValuationGrouping, selectedExecutionPeriod);
		
        try {
			String filename = teacherServiceDistribution.getName() + "-" + teacherServiceDistribution.getExecutionYear().getYear() + "-" + selectedValuationPhase.getName();
			
			if(selectedValuationGrouping != null) {
				filename += "-" + selectedValuationGrouping.getName();
			}
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename.replace(" ", "") + ".xls");
	
			ServletOutputStream writer = response.getOutputStream();
			
	        exportToXls(
	        	courseValuationDTOEntryList, 
	        	valuationTeacherDTOEntryList,
	        	filename,
	        	writer);
	
	        writer.flush();
	        response.flushBuffer();
        } catch (IOException e) {
            throw new FenixServiceException();
        }
        
        return null;
	}


	private ActionForward generateTeacherServiceDistributionValuationChart(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response,
			TeacherServiceDistributionChart teacherServiceDistributionChart)  throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;
		Integer valuationGroupingId = new Integer(request.getParameter("valuationGrouping"));
		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		
		Integer executionPeriodId = new Integer(request.getParameter("executionPeriod"));
		ExecutionPeriod selectedExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		if(selectedExecutionPeriod != null) {
			executionPeriodList.add(selectedExecutionPeriod);
		} else {
			executionPeriodList.addAll(valuationGrouping.getValuationPhase().getTeacherServiceDistribution().getExecutionPeriods());
		}
		
		try {
			response.setContentType("image/png");
			ServletOutputStream writer = response.getOutputStream();
			
			
			teacherServiceDistributionChart.setValuationGrouping(valuationGrouping);
			teacherServiceDistributionChart.setExecutionPeriodList(executionPeriodList);
			
	        teacherServiceDistributionChart.execute(writer);
	        
	        writer.flush();
	        response.flushBuffer();			
        } catch (IOException e) {
            throw new FenixServiceException();
        }
        
        return null;		
	}
	
	public ActionForward generateValuatedHoursPerGrouping(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		return generateTeacherServiceDistributionValuationChart(mapping, form, request, response, TeacherServiceDistributionChart.generateValuatedHoursPerGroupingChart());
	}

	public ActionForward generateValuatedNumberStudentsPerGrouping(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		return generateTeacherServiceDistributionValuationChart(mapping, form, request, response, TeacherServiceDistributionChart.generateValuatedNumberStudentsPerGroupingChart());	
	}

	
	public ActionForward generateValuatedHoursPerGroupingPieChart(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		return generateTeacherServiceDistributionValuationChart(mapping, form, request, response, TeacherServiceDistributionChart.generateValuatedHoursPerGroupingPieChart());
	}
	
	
	public ActionForward generateValuatedNumberStudentsPerGroupingPieChart(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		return generateTeacherServiceDistributionValuationChart(mapping, form, request, response, TeacherServiceDistributionChart.generateValuatedNumberStudentsPerGroupingPieChart());
	}
	
	private void exportToXls(
    		final List<CourseValuationDTOEntry> courseValuationDTOEntryList,
    		final List<ValuationTeacherDTOEntry> valuationTeacherDTOEntryList,
    		final String name,
            OutputStream outputStream) throws IOException {
        final TeacherServiceDistributionSpreadsheet spreadsheet = new TeacherServiceDistributionSpreadsheet(
        		courseValuationDTOEntryList, 
        		valuationTeacherDTOEntryList,
        		name);
        
        spreadsheet.exportToXLSSheet(outputStream);
    }
    

	private ValuationGrouping getSelectedValuationGrouping(IUserView userView, DynaActionForm dynaForm, ValuationGrouping rootValuationGrouping) throws FenixFilterException, FenixServiceException {
		Integer selectedValuationGroupingId = (Integer) dynaForm.get("valuationGrouping");
		ValuationGrouping selectedValuationGrouping = rootDomainObject.readValuationGroupingByOID(selectedValuationGroupingId);
		
		if(selectedValuationGrouping == null) return rootValuationGrouping;
		
		return selectedValuationGrouping;
	}

	private ValuationPhase getSelectedValuationPhase(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		Integer selectedValuationPhaseId = (Integer) dynaForm.get("valuationPhase");
		ValuationPhase selectedValuationPhase = rootDomainObject.readValuationPhaseByOID(selectedValuationPhaseId);
		
		return selectedValuationPhase;
	}

	private TeacherServiceDistribution getTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		Integer teacherServiceDistributionId = (Integer) dynaForm.get("teacherServiceDistribution");
		TeacherServiceDistribution teacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId);

		return teacherServiceDistribution;
	}
	
	private Integer getViewType(DynaActionForm dynaForm) {
		Integer viewType = (Integer) dynaForm.get("viewType");
		return viewType;
	}

	private Integer getFromRequestAndSetOnFormTeacherServiceDistributionId(HttpServletRequest request, DynaActionForm dynaForm) {
		Integer teacherServiceDistributionId = new Integer(request.getParameter("teacherServiceDistribution"));
		dynaForm.set("teacherServiceDistribution", teacherServiceDistributionId);
		return teacherServiceDistributionId;
	}

	private void initializeVariables(DynaActionForm dynaForm) {
		dynaForm.set("valuationGrouping", null);
		dynaForm.set("valuationPhase", null);
		dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES);
		dynaForm.set("viewCurricularInformation", false);
		dynaForm.set("viewStudentsEnrolments", false);
		dynaForm.set("viewShiftHours", false);
		dynaForm.set("viewStudentsEnrolmentsPerShift", false);
	}
	
	@SuppressWarnings("unchecked")
	private List<ValuationTeacherDTOEntry> getValuationTeacherDTOEntries(IUserView userView, ValuationPhase selectedValuationPhase, ValuationGrouping selectedValuationGrouping, ExecutionPeriod executionPeriod) throws FenixFilterException, FenixServiceException {
		Map<Integer, Pair<Integer, Integer>> teacherServiceDistributionIdMap = new HashMap<Integer, Pair<Integer, Integer>>();
		teacherServiceDistributionIdMap.put(selectedValuationPhase.getIdInternal(), new Pair<Integer, Integer>(selectedValuationGrouping.getIdInternal(), (executionPeriod == null) ? 0 : executionPeriod.getIdInternal()));
		
		return (List<ValuationTeacherDTOEntry>) ServiceUtils.executeService(userView, "ReadValuationTeachersFromTeacherServiceDistributions", new Object[] {teacherServiceDistributionIdMap });
	}

	@SuppressWarnings("unchecked")
	private List<CourseValuationDTOEntry> getCourseValuationDTOEntries(IUserView userView, ValuationPhase selectedValuationPhase, ValuationGrouping selectedValuationGrouping, ExecutionPeriod executionPeriod) throws FenixFilterException, FenixServiceException {
		Map<Integer, Pair<Integer, Integer>> teacherServiceDistributionIdMap = new HashMap<Integer, Pair<Integer, Integer>>();
		teacherServiceDistributionIdMap.put(selectedValuationPhase.getIdInternal(), new Pair<Integer, Integer>(selectedValuationGrouping.getIdInternal(), (executionPeriod == null) ? 0 : executionPeriod.getIdInternal()));
		
		return (List<CourseValuationDTOEntry>) ServiceUtils.executeService(userView, "ReadCourseValuationsFromTeacherServiceDistributions", new Object[] {teacherServiceDistributionIdMap });
	}
	
	private void setInformationTableParameters(DynaActionForm dynaForm, HttpServletRequest request) {
		Boolean viewCurricularInformation = (Boolean) dynaForm.get("viewCurricularInformation");
		Boolean viewStudentsEnrolments = (Boolean) dynaForm.get("viewStudentsEnrolments");
		Boolean viewShiftHours = (Boolean) dynaForm.get("viewShiftHours");
		Boolean viewStudentsEnrolmentsPerShift = (Boolean) dynaForm.get("viewStudentsEnrolmentsPerShift");
		
		request.setAttribute("viewCurricularInformation", viewCurricularInformation);
		request.setAttribute("viewStudentsEnrolments", viewStudentsEnrolments);
		request.setAttribute("viewShiftHours", viewShiftHours);
		request.setAttribute("viewStudentsEnrolmentsPerShift", viewStudentsEnrolmentsPerShift);
	}
	
	private ExecutionPeriod getSelectedExecutionPeriod(IUserView userView, DynaActionForm dynaForm, List<ExecutionPeriod> executionPeriodList) throws FenixServiceException, FenixFilterException {
		Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriod");
		ExecutionPeriod selectedExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(selectedExecutionPeriodId);
		
		if(selectedExecutionPeriod == null) {
			if(executionPeriodList != null && executionPeriodList.size() > 0) {
				return executionPeriodList.get(0);
			} else {
				return null;
			}
		}
		
		return selectedExecutionPeriod;
	}

		
	public ActionForward prepareForTeacherServiceDistributionValuationByTeachers(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		initializeVariables(dynaForm);
		
		return changeToViewTeachers(mapping, form, request, response);
	}
}
