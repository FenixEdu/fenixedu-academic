package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurricularCourseValuationGroup extends CurricularCourseValuationGroup_Base {
    
    private  CurricularCourseValuationGroup() {
        super();
    }

	public CurricularCourseValuationGroup(List<CurricularCourseValuation> curricularCourseValuationList) {
		this();
		
		if(curricularCourseValuationList == null || curricularCourseValuationList.isEmpty()){
			throw new NullPointerException();
		}
		
		ValuationPhase valuationPhase = curricularCourseValuationList.get(0).getValuationPhase();
		for (CurricularCourseValuation courseValuation : curricularCourseValuationList) {
			if(courseValuation.getValuationPhase() != valuationPhase) {
				throw new NullPointerException();
			}
		}
		
		this.setValuationPhase(valuationPhase);
		this.getCurricularCourseValuations().addAll(curricularCourseValuationList);
		this.setOjbConcreteClass(getClass().getName());
		this.setExecutionPeriod(curricularCourseValuationList.get(0).getExecutionPeriod());
	}

	@Override
	public List<ValuationCurricularCourse> getAssociatedValuationCurricularCourses() {
		List<ValuationCurricularCourse> valuationCurricularCourseList = new ArrayList<ValuationCurricularCourse>();
		
		for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
			valuationCurricularCourseList.add(curricularCourseValuation.getValuationCurricularCourse());
		}
		
		return valuationCurricularCourseList;
	}

	public String getAssociatedCurricularCoursesDescription() {
		StringBuilder sb = new StringBuilder();
		
		for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
			sb.append(curricularCourseValuation.getValuationCurricularCourse().getDegreeCurricularPlan().getDegree().getSigla());
			sb.append("+");
		}
		
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}

    public String getGroupName() {
    	return getName() + " - " + getAssociatedCurricularCoursesDescription();
	}	
    
	@Override
    public Integer getTotalNumberOfTheoreticalStudents() {
		if(getUsingCurricularCourseValuations() == true) {
			Integer totalNumberOfTheoreticalStudents = 0;
			
			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
				totalNumberOfTheoreticalStudents += curricularCourseValuation.getTotalNumberOfTheoreticalStudents();
			}
			
			return totalNumberOfTheoreticalStudents;
		}
		
		return super.getTotalNumberOfTheoreticalStudents();
    }

	@Override
    public Integer getTotalNumberOfPraticalStudents() {
		if(getUsingCurricularCourseValuations() == true) {
			Integer totalNumberOfPraticalStudents = 0;
			
			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
				totalNumberOfPraticalStudents += curricularCourseValuation.getTotalNumberOfPraticalStudents();
			}
			
			return totalNumberOfPraticalStudents;
		}
		
		return super.getTotalNumberOfPraticalStudents();
    }

	@Override
    public Integer getTotalNumberOfTheoPratStudents() {
		if(getUsingCurricularCourseValuations() == true) {
			Integer totalNumberOfTheoreticalPraticalStudents = 0;
			
			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
				totalNumberOfTheoreticalPraticalStudents += curricularCourseValuation.getTotalNumberOfTheoPratStudents();
			}
			
			return totalNumberOfTheoreticalPraticalStudents;
		}
		
		return super.getTotalNumberOfTheoPratStudents();
	}

	@Override
    public Integer getTotalNumberOfLaboratorialStudents() {
		if(getUsingCurricularCourseValuations() == true) {
			Integer totalNumberOfLaboratorialStudents = 0;
			
			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
				totalNumberOfLaboratorialStudents += curricularCourseValuation.getTotalNumberOfLaboratorialStudents();
			}
			
			return totalNumberOfLaboratorialStudents;
		}
		
		return super.getTotalNumberOfLaboratorialStudents();
    }

	
	@Override
    public Integer getFirstTimeEnrolledStudents() {
    	if(getFirstTimeEnrolledStudentsType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Integer firstTimeEnrolledStudents = 0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				firstTimeEnrolledStudents += curricularCourseValuation.getFirstTimeEnrolledStudents();
				}
    			
    			return firstTimeEnrolledStudents;
    		}
    	}
    	
    	return super.getFirstTimeEnrolledStudents();
    }

	@Override
    public Integer getSecondTimeEnrolledStudents() {
    	if(getSecondTimeEnrolledStudentsType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Integer secondOrMoreTimeEnrolledStudents = 0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				secondOrMoreTimeEnrolledStudents += curricularCourseValuation.getSecondTimeEnrolledStudents();
				}
    			
    			return secondOrMoreTimeEnrolledStudents;
    		}
    	}
    	
    	return super.getSecondTimeEnrolledStudents();
    }
	
	@Override
    public Integer getStudentsPerTheoreticalShift() {
    	if(getStudentsPerTheoreticalShiftType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double studentsPerTheoreticalShift = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				studentsPerTheoreticalShift += curricularCourseValuation.getStudentsPerTheoreticalShift();
    			}
    			
    			studentsPerTheoreticalShift /= getCurricularCourseValuations().size();
    			
    			studentsPerTheoreticalShift = StrictMath.ceil(studentsPerTheoreticalShift);
    			
    			return studentsPerTheoreticalShift.intValue();
    		}
    	}
    	
    	return super.getStudentsPerTheoreticalShift();
    }
    
	@Override
    public Integer getStudentsPerPraticalShift() {
    	if(getStudentsPerPraticalShiftType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double studentsPerPraticalShift = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				studentsPerPraticalShift += curricularCourseValuation.getStudentsPerPraticalShift();
    			}
    			
    			studentsPerPraticalShift /= getCurricularCourseValuations().size();
    			
    			studentsPerPraticalShift = StrictMath.ceil(studentsPerPraticalShift);
    			
    			return studentsPerPraticalShift.intValue();
    		}
    	}
    	
    	return super.getStudentsPerPraticalShift();
    }
    
	@Override
    public Integer getStudentsPerTheoPratShift() {
    	if(getStudentsPerTheoPratShiftType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double studentsPerTheoreticalPraticalShift = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				studentsPerTheoreticalPraticalShift += curricularCourseValuation.getStudentsPerTheoPratShift();
    			}
    			
    			studentsPerTheoreticalPraticalShift /= getCurricularCourseValuations().size();
    			
    			studentsPerTheoreticalPraticalShift = StrictMath.ceil(studentsPerTheoreticalPraticalShift);
    			
    			return studentsPerTheoreticalPraticalShift.intValue();
    		}
    	}
    	
    	return super.getStudentsPerTheoPratShift();
    }
	
	@Override
    public Integer getStudentsPerLaboratorialShift() {
    	if(getStudentsPerLaboratorialShiftType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double studentsPerLaboratorialShift = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				studentsPerLaboratorialShift += curricularCourseValuation.getStudentsPerLaboratorialShift();
    			}
    			
    			studentsPerLaboratorialShift /= getCurricularCourseValuations().size();
    			
    			studentsPerLaboratorialShift = StrictMath.ceil(studentsPerLaboratorialShift);
    			
    			return studentsPerLaboratorialShift.intValue();
    		}
    	}
    	
    	return super.getStudentsPerLaboratorialShift();
    }

    public Double getTheoreticalHours() {
    	if(getTheoreticalHoursType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double theoreticalHours = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				theoreticalHours += curricularCourseValuation.getTheoreticalHours();
    			}
    			
    			return theoreticalHours;
    		}
    	}
    	
    	return super.getTheoreticalHours();
    }
    

    public Double getPraticalHours() {
    	if(getPraticalHoursType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double praticalHours = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				praticalHours += curricularCourseValuation.getPraticalHours();
    			}
    			
    			return praticalHours;
    		}
    	}
    	
    	return super.getPraticalHours();
    }

    public Double getTheoreticalPraticalHours() {
    	if(getTheoPratHoursType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double theoreticalPraticalHours = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				theoreticalPraticalHours += curricularCourseValuation.getTheoPratHours();
    			}
    			
    			return theoreticalPraticalHours;
    		}
    	}
    	
    	return super.getTheoPratHours();
    }

    public Double getLaboratorialHours() {
    	if(getLaboratorialHoursType() == ValuationValueType.MANUAL_VALUE) {
    		if(getUsingCurricularCourseValuations() == true) {
    			Double laboratorialHours = 0.0;
    			
    			for (CurricularCourseValuation curricularCourseValuation : getCurricularCourseValuations()) {
    				laboratorialHours += curricularCourseValuation.getLaboratorialHours();
    			}
    			
    			return laboratorialHours;
    		}
    	}
    	
    	return super.getLaboratorialHours();
    }

	public static CurricularCourseValuationGroup createAndCopyFromCurricularCoursevValuationGroup(
			List<CurricularCourseValuation> newAssociatedCurricularCourseValuations, 
			CurricularCourseValuationGroup oldCurricularCourseValuationGroup) {
		CurricularCourseValuationGroup curricularCourseValuationGroup = new CurricularCourseValuationGroup(newAssociatedCurricularCourseValuations);
		fillCourseValuationFromAnotherCourseValuation(oldCurricularCourseValuationGroup, curricularCourseValuationGroup);
		
		return curricularCourseValuationGroup;
	}
    
    public ValuationPhase getValuationPhase() {
    	return getCurricularCourseValuations().get(0).getValuationPhase();
    }
    
    @Override
	public void delete(){
		for(CurricularCourseValuation courseValuation : getCurricularCourseValuations()){
			courseValuation.deleteValuationOnly();
		}
		super.delete();
	}
}
