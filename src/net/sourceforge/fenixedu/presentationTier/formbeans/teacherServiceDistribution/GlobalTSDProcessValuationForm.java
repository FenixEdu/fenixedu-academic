package net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class GlobalTSDProcessValuationForm extends ActionForm {
	private final Map tsdProcessMap = new LinkedHashMap();
	private final Map tsdProcessPhaseMap = new LinkedHashMap();
	private final Map tsdMap = new LinkedHashMap();
	
	private Integer executionYear = null;
	private Integer executionPeriod = null;
	private Integer department = null;
	private Integer viewType = null;
	
	private Boolean viewCurricularInformation = null;
	private Boolean viewStudentsEnrolments = null;
	private Boolean viewShiftHours = null;
	private Boolean viewStudentsEnrolmentsPerShift = null;
	
	private String[] shiftTypeArray = null;
	
	public void setTSDProcess(String key, Object value) {
		tsdProcessMap.put(key, value);
	}
	
	public Object getTSDProcess(String key) {
		return tsdProcessMap.get(key);
	}
	
	public void setTSDProcessPhase(String key, Object value) {
		tsdProcessPhaseMap.put(key, value);
	}
	
	public Object getTSDProcessPhase(String key) {
		return tsdProcessPhaseMap.get(key);
	}
	
	public void setTeacherServiceDistribution(String key, Object value) {
		tsdMap.put(key, value);
	}
	
	public Object getTeacherServiceDistribution(String key) {
		return tsdMap.get(key);
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
	
	public List<String> getSelectedTSDProcesss() {
		List<String> selectedTSDProcessList = new ArrayList<String>();
				
		for (Object tsdProcessIdObj : tsdProcessMap.keySet()) {
			Boolean value = Boolean.parseBoolean((String) tsdProcessMap.get(tsdProcessIdObj));
			
			if(value) {
				selectedTSDProcessList.add((String) tsdProcessIdObj);
			}
		}
		
		return selectedTSDProcessList;
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

	public String[] getShiftTypeArray() {
		return shiftTypeArray;
	}

	public void setShiftTypeArray(String[] shiftTypeArray) {
		this.shiftTypeArray = shiftTypeArray;
	}
	
}
