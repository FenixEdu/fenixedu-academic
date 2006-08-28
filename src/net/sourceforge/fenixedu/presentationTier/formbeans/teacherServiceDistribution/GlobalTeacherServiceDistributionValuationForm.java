package net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class GlobalTeacherServiceDistributionValuationForm extends ActionForm {
	private final Map teacherServiceDistributionMap = new LinkedHashMap();
	private final Map valuationPhaseMap = new LinkedHashMap();
	private final Map valuationGroupingMap = new LinkedHashMap();
	
	private Integer executionYear = null;
	private Integer executionPeriod = null;
	private Integer department = null;
	private Integer viewType = null;
	
	private Boolean viewCurricularInformation = null;
	private Boolean viewStudentsEnrolments = null;
	private Boolean viewShiftHours = null;
	private Boolean viewStudentsEnrolmentsPerShift = null;
	
	public void setTeacherServiceDistribution(String key, Object value) {
		teacherServiceDistributionMap.put(key, value);
	}
	
	public Object getTeacherServiceDistribution(String key) {
		return teacherServiceDistributionMap.get(key);
	}
	
	public void setValuationPhase(String key, Object value) {
		valuationPhaseMap.put(key, value);
	}
	
	public Object getValuationPhase(String key) {
		return valuationPhaseMap.get(key);
	}
	
	public void setValuationGrouping(String key, Object value) {
		valuationGroupingMap.put(key, value);
	}
	
	public Object getValuationGrouping(String key) {
		return valuationGroupingMap.get(key);
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public Integer getExecutionPeriod() {
		return executionPeriod;
	}

	public void setExecutionPeriod(Integer executionPeriod) {
		this.executionPeriod = executionPeriod;
	}

	public Integer getExecutionYear() {
		return executionYear;
	}

	public void setExecutionYear(Integer executionYear) {
		this.executionYear = executionYear;
	}
	
	public List<String> getSelectedTeacherServiceDistributions() {
		List<String> selectedTeacherServiceDistributionList = new ArrayList<String>();
				
		for (Object teacherServiceDistributionIdObj : teacherServiceDistributionMap.keySet()) {
			Boolean value = Boolean.parseBoolean((String) teacherServiceDistributionMap.get(teacherServiceDistributionIdObj));
			
			if(value) {
				selectedTeacherServiceDistributionList.add((String) teacherServiceDistributionIdObj);
			}
		}
		
		return selectedTeacherServiceDistributionList;
	}

	public Integer getViewType() {
		return viewType;
	}

	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}

	public Boolean getViewCurricularInformation() {
		return viewCurricularInformation;
	}

	public void setViewCurricularInformation(Boolean viewCurricularInformation) {
		this.viewCurricularInformation = viewCurricularInformation;
	}

	public Boolean getViewShiftHours() {
		return viewShiftHours;
	}

	public void setViewShiftHours(Boolean viewShiftHours) {
		this.viewShiftHours = viewShiftHours;
	}

	public Boolean getViewStudentsEnrolments() {
		return viewStudentsEnrolments;
	}

	public void setViewStudentsEnrolments(Boolean viewStudentsEnrolments) {
		this.viewStudentsEnrolments = viewStudentsEnrolments;
	}

	public Boolean getViewStudentsEnrolmentsPerShift() {
		return viewStudentsEnrolmentsPerShift;
	}

	public void setViewStudentsEnrolmentsPerShift(
			Boolean viewStudentsEnrolmentsPerShift) {
		this.viewStudentsEnrolmentsPerShift = viewStudentsEnrolmentsPerShift;
	}
	
}
