package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;

public class ValuationTeacherDTOEntry extends DataTranferObject {
	private List<ValuationTeacher> valuationTeacherList;
	private List<ExecutionPeriod> executionPeriodList = null;
	
	public ValuationTeacherDTOEntry(ValuationTeacher valuationTeacher, List<ExecutionPeriod> executionPeriodList) {
		this.valuationTeacherList = new ArrayList<ValuationTeacher>();
		this.valuationTeacherList.add(valuationTeacher);
		
		this.executionPeriodList = executionPeriodList;
	}
	
	public List<ProfessorshipValuationDTOEntry> getProfessorshipValuationDTOEntries() {
		List<ProfessorshipValuationDTOEntry> professorshipValuationDTOEntryList = new ArrayList<ProfessorshipValuationDTOEntry>();
		
		for(ValuationTeacher valuationTeacher : valuationTeacherList) {
			for (ProfessorshipValuation professorshipValuation : valuationTeacher.getActiveProfessorshipValuations()) {
				if(executionPeriodList.contains(professorshipValuation.getExecutionPeriod()))
					professorshipValuationDTOEntryList.add(new ProfessorshipValuationDTOEntry(professorshipValuation, executionPeriodList));
			}
		}
		
		return professorshipValuationDTOEntryList;
	}
	
	public Boolean getIsRealTeacher() {
		return valuationTeacherList.get(0).getIsRealTeacher();
	}
	
	
	public Integer getTeacherNumber() {
		return valuationTeacherList.get(0).getTeacherNumber();
	}
	
	public Category getCategory() {
		return valuationTeacherList.get(0).getCategory();
	}
	
	public String getName() {
		return valuationTeacherList.get(0).getName();
	}
	
	public List<ExecutionPeriod> getExecutionPeriodList() {
		return executionPeriodList;
	}
	
    public Double getTotalTheoreticalHoursLectured() {
    	Double totalTheoreticalHoursLectured = 0d;
    	
    	for(ValuationTeacher valuationTeacher : valuationTeacherList) {
    		totalTheoreticalHoursLectured += valuationTeacher.getTotalTheoreticalHoursLectured(executionPeriodList);
    	}
    	
    	return totalTheoreticalHoursLectured;
    }
    
    public Double getTotalPraticalHoursLectured() {
    	Double totalPraticalHoursLectured = 0d;
    	
    	for(ValuationTeacher valuationTeacher : valuationTeacherList) {
    		totalPraticalHoursLectured += valuationTeacher.getTotalPraticalHoursLectured(executionPeriodList);
    	}
    	
    	return totalPraticalHoursLectured;
    }
    
    public Double getTotalTheoPratHoursLectured() {
    	Double totalTheoPratHoursLectured = 0d;
    	
    	for(ValuationTeacher valuationTeacher : valuationTeacherList) {
    		totalTheoPratHoursLectured += valuationTeacher.getTotalTheoPratHoursLectured(executionPeriodList);
    	}
    	
    	return totalTheoPratHoursLectured;
    }
    
    public Double getTotalLaboratorialHoursLectured() {
    	Double totalLaboratorialHoursLectured = 0d;
    	
    	for(ValuationTeacher valuationTeacher : valuationTeacherList) {
    		totalLaboratorialHoursLectured += valuationTeacher.getTotalLaboratorialHoursLectured(executionPeriodList);
    	}
    	
    	return totalLaboratorialHoursLectured;
    }
    
    public Double getTotalHoursLectured() {
    	return getTotalTheoreticalHoursLectured() + getTotalPraticalHoursLectured() + 
    		getTotalTheoPratHoursLectured() + getTotalLaboratorialHoursLectured();
    }
        
    public Integer getRequiredHours() {
    	return valuationTeacherList.get(0).getRequiredHours(executionPeriodList);
    }
    
    public Double getAvailability() {
    	return getRequiredHours() - getTotalHoursLecturedPlusExtraCredits();
    }

	public Double getServiceExemptionCredits() {
		return valuationTeacherList.get(0).getServiceExemptionCredits(executionPeriodList);
	}
	
	public Double getManagementFunctionsCredits() {
		return valuationTeacherList.get(0).getManagementFunctionsCredits(executionPeriodList);
	}
		
	public Double getRequiredTeachingHours() {
		return getRequiredHours() - getExtraCreditsValue();
	}
	
	public void addExecutionPeriodList(List<ExecutionPeriod> executionPeriodList) {
		for(ExecutionPeriod executionPeriod : executionPeriodList) {
			if(!this.executionPeriodList.contains(executionPeriod)) {
				this.executionPeriodList.add(executionPeriod);
			}
		}
	}
	
	public void addValuationTeacher(ValuationTeacher valuationTeacher) {
		this.valuationTeacherList.add(valuationTeacher);
	}

	public List<ValuationTeacher> getValuationTeachers() {
		return valuationTeacherList;
	}
	
	public List<TeacherServiceExemption> getServiceExemptions() {
		return valuationTeacherList.get(0).getServiceExemptions(executionPeriodList);
	}
	
	public List<PersonFunction> getManagementFunctions() {
		return valuationTeacherList.get(0).getManagementFunctions(executionPeriodList);
	}
	
	public ProfessorshipValuationDTOEntry getProfeshipValuationDTOEntryByCourseValuationDTOEntry(final CourseValuationDTOEntry courseValuationDTOEntry) {
		return (ProfessorshipValuationDTOEntry) CollectionUtils.find(getProfessorshipValuationDTOEntries(), new Predicate() {

			public boolean evaluate(Object arg0) {
				ProfessorshipValuationDTOEntry professorshipValuationDTOEntry = (ProfessorshipValuationDTOEntry) arg0;		
				return professorshipValuationDTOEntry.getCourseValuationDTOEntry().getCourseValuation() == courseValuationDTOEntry.getCourseValuation();
			}
			
		});
	}
	
	public String getAcronym() {
		return valuationTeacherList.get(0).getAcronym();
	}
	
	public Double getTotalHoursLecturedPlusExtraCredits() {
		return valuationTeacherList.get(0).getTotalHoursLecturedPlusExtraCredits(executionPeriodList);
	}
	
	public Boolean getUsingExtraCredits() {
		return valuationTeacherList.get(0).getUsingExtraCredits();
	}
	
	public String getExtraCreditsName() {
		return valuationTeacherList.get(0).getExtraCreditsName();
	}
	
	public Double getExtraCreditsValue() {
		return valuationTeacherList.get(0).getExtraCreditsValue();
	}
	
	public String getEmailUserId() {
		return valuationTeacherList.get(0).getEmailUserId();
	}
	
}
