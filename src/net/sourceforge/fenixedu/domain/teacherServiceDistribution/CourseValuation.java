package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;

import org.apache.commons.collections.Predicate;

public abstract class CourseValuation extends CourseValuation_Base {
	    
    public  CourseValuation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
        
    public abstract List<ValuationCurricularCourse> getAssociatedValuationCurricularCourses();

    private Integer getRealFirstTimeEnrolledStudentsNumberByExecutionPeriod(ExecutionPeriod executionPeriod) {
    	int firstTimeEnrolledStudents = 0;

    	for(ValuationCurricularCourse valuationCurricularCourse : getAssociatedValuationCurricularCourses()) {
    		firstTimeEnrolledStudents += valuationCurricularCourse.getFirstTimeEnrolmentStudentNumber(executionPeriod);
    	}
    	
    	return firstTimeEnrolledStudents;    	
    }
    
    private Integer getRealSecondTimeEnrolledStudentsNumberByExecutionPeriod(ExecutionPeriod executionPeriod) {
    	Integer secondTimeEnrolledStudentsNumber = 0;
    	
    	if(executionPeriod != null) {
	    	for(ValuationCurricularCourse valuationCurricularCourse : getAssociatedValuationCurricularCourses()) {
	    		secondTimeEnrolledStudentsNumber += valuationCurricularCourse.getSecondTimeEnrolmentStudentNumber(executionPeriod);
	    	}
    	}
    	    	
    	return secondTimeEnrolledStudentsNumber;   	    	
    }
    
    public Integer getRealFirstTimeEnrolledStudentsNumberLastYear()  {
    	return getRealFirstTimeEnrolledStudentsNumberByExecutionPeriod(getLastYearExecutionPeriod());
    }
    
    public Integer getRealSecondTimeEnrolledStudentsNumberLastYear() {
    	return getRealSecondTimeEnrolledStudentsNumberByExecutionPeriod(getLastYearExecutionPeriod());   	
    }
        
    public Integer getRealFirstTimeEnrolledStudentsNumber() {
    	return getRealFirstTimeEnrolledStudentsNumberByExecutionPeriod(getExecutionPeriod());
    }
    
    public Integer getRealSecondTimeEnrolledStudentsNumber() {
    	return getRealSecondTimeEnrolledStudentsNumberByExecutionPeriod(getExecutionPeriod());   	
    }
           
    public Integer getFirstTimeEnrolledStudents() {  	
    	if(getFirstTimeEnrolledStudentsType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getFirstTimeEnrolledStudentsManual();
    	} else if(getFirstTimeEnrolledStudentsType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealFirstTimeEnrolledStudentsNumberLastYear();
        } else if(getFirstTimeEnrolledStudentsType() == ValuationValueType.REAL_VALUE) {
        	return getRealFirstTimeEnrolledStudentsNumber();
        }
    	
    	return 0;
    }
    
    public Integer getSecondTimeEnrolledStudents() {
    	if(getSecondTimeEnrolledStudentsType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getSecondTimeEnrolledStudentsManual();
    	} else if(getSecondTimeEnrolledStudentsType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealSecondTimeEnrolledStudentsNumberLastYear();
        } else if(getSecondTimeEnrolledStudentsType() == ValuationValueType.REAL_VALUE) {
        	return getRealSecondTimeEnrolledStudentsNumber();
        }
    	
    	return 0;
    }
    
    public Integer getTotalEnrolledStudents() {
    	ValuationValueType firstTimeType = getFirstTimeEnrolledStudentsType();
    	
    	if(firstTimeType == getSecondTimeEnrolledStudentsType()){
    		if(firstTimeType ==  ValuationValueType.REAL_VALUE){
    			return getTotalEnrolledStudentsNumberByExecutionPeriod(getExecutionPeriod());
    		}
    		if(firstTimeType == ValuationValueType.LAST_YEAR_REAL_VALUE){
    			return getTotalEnrolledStudentsNumberByExecutionPeriod(getLastYearExecutionPeriod());
    		}
    	} 
    	
    	return getFirstTimeEnrolledStudents() + getSecondTimeEnrolledStudents();
    }
       
    public Integer getTotalNumberOfTheoreticalStudents() {
    	Double totalNumberOfStudents = 
    		(getFirstTimeEnrolledStudents() * getWeightFirstTimeEnrolledStudentsPerTheoShift()) +
    		(getSecondTimeEnrolledStudents() * getWeightSecondTimeEnrolledStudentsPerTheoShift());
    	
    	totalNumberOfStudents = StrictMath.ceil(totalNumberOfStudents);
    	return totalNumberOfStudents.intValue();
    }

    public Integer getTotalNumberOfPraticalStudents() {
    	Double totalNumberOfStudents = 
    		(getFirstTimeEnrolledStudents() * getWeightFirstTimeEnrolledStudentsPerPratShift()) +
    		(getSecondTimeEnrolledStudents() * getWeightSecondTimeEnrolledStudentsPerPratShift());
    	
    	totalNumberOfStudents = StrictMath.ceil(totalNumberOfStudents);
    	return totalNumberOfStudents.intValue();
    }

    public Integer getTotalNumberOfTheoPratStudents() {
    	Double totalNumberOfStudents = 
    		(getFirstTimeEnrolledStudents() * getWeightFirstTimeEnrolledStudentsPerTheoPratShift()) +
    		(getSecondTimeEnrolledStudents() * getWeightSecondTimeEnrolledStudentsPerTheoPratShift());
    	
    	totalNumberOfStudents = StrictMath.ceil(totalNumberOfStudents);
    	return totalNumberOfStudents.intValue();
    }

    public Integer getTotalNumberOfLaboratorialStudents() {
    	Double totalNumberOfStudents = 
    		(getFirstTimeEnrolledStudents() * getWeightFirstTimeEnrolledStudentsPerLabShift()) +
    		(getSecondTimeEnrolledStudents() * getWeightSecondTimeEnrolledStudentsPerLabShift());
    	
    	totalNumberOfStudents = StrictMath.ceil(totalNumberOfStudents);
    	return totalNumberOfStudents.intValue();
    }

    
    public Double getWeightFirstTimeEnrolledStudentsPerTheoShift() {
    	if(getWeightFirstTimeEnrolledStudentsPerTheoShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightFirstTimeEnrolledStudentsPerTheoShiftManual();
    	} else if(getWeightFirstTimeEnrolledStudentsPerTheoShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightFirstTimeEnrolledStudentsPerTheoShift();
    	}
    	
    	return 0.0;
    }

    public Double getWeightFirstTimeEnrolledStudentsPerPratShift() {
    	if(getWeightFirstTimeEnrolledStudentsPerPratShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightFirstTimeEnrolledStudentsPerPratShiftManual();
    	} else if(getWeightFirstTimeEnrolledStudentsPerPratShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightFirstTimeEnrolledStudentsPerPratShift();
    	}
    	
    	return 0.0;
    }

    public Double getWeightFirstTimeEnrolledStudentsPerTheoPratShift() {
    	if(getWeightFirstTimeEnrolledStudentsPerTheoPratShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual();
    	} else if(getWeightFirstTimeEnrolledStudentsPerTheoPratShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightFirstTimeEnrolledStudentsPerTheoPratShift();
    	}
    	
    	return 0.0;
    }

    public Double getWeightFirstTimeEnrolledStudentsPerLabShift() {
    	if(getWeightFirstTimeEnrolledStudentsPerLabShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightFirstTimeEnrolledStudentsPerLabShiftManual();
    	} else if(getWeightFirstTimeEnrolledStudentsPerLabShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightFirstTimeEnrolledStudentsPerLabShift();
    	}
    	
    	return 0.0;
    }

    public Double getWeightSecondTimeEnrolledStudentsPerTheoShift() {
    	if(getWeightSecondTimeEnrolledStudentsPerTheoShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightSecondTimeEnrolledStudentsPerTheoShiftManual();
    	} else if(getWeightSecondTimeEnrolledStudentsPerTheoShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightSecondTimeEnrolledStudentsPerTheoShift();
    	}
    	
    	return 0.0;
    }
   
    public Double getWeightSecondTimeEnrolledStudentsPerPratShift() {
    	if(getWeightSecondTimeEnrolledStudentsPerPratShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightSecondTimeEnrolledStudentsPerPratShiftManual();
    	} else if(getWeightSecondTimeEnrolledStudentsPerPratShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightSecondTimeEnrolledStudentsPerPratShift();
    	}
    	
    	return 0.0;
    }

    public Double getWeightSecondTimeEnrolledStudentsPerTheoPratShift() {
    	if(getWeightSecondTimeEnrolledStudentsPerTheoPratShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual();
    	} else if(getWeightSecondTimeEnrolledStudentsPerTheoPratShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightSecondTimeEnrolledStudentsPerTheoPratShift();
    	}
    	
    	return 0.0;
    }

    public Double getWeightSecondTimeEnrolledStudentsPerLabShift() {
    	if(getWeightSecondTimeEnrolledStudentsPerLabShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getWeightSecondTimeEnrolledStudentsPerLabShiftManual();
    	} else if(getWeightSecondTimeEnrolledStudentsPerLabShiftType() == ValuationValueType.OMISSION_VALUE) {
    		return getValuationPhase().getWeightSecondTimeEnrolledStudentsPerLabShift();
    	}
    	
    	return 0.0;
    }
    

    private Double getRealHoursByShiftAndExecutionPeriod(ShiftType shiftType, ExecutionPeriod executionPeriod) {
    	Double hours = 0d;
    	
    	for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesByExecutionPeriod(executionPeriod)) {
    		hours += executionCourse.getTotalHours(shiftType);
		}
    	
    	return hours;
    }
    
    public Double getRealTheoreticalHoursLastYear() {
    	ExecutionPeriod executionPeriodLastYear = getLastYearExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.TEORICA, executionPeriodLastYear);
    }
    
    public Double getRealPraticalHoursLastYear() {
    	ExecutionPeriod executionPeriodLastYear = getLastYearExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.PRATICA, executionPeriodLastYear);
    }
 
    public Double getRealTheoPratHoursLastYear() {
    	ExecutionPeriod executionPeriodLastYear = getLastYearExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.TEORICO_PRATICA, executionPeriodLastYear);
    }
        
    public Double getRealLaboratorialHoursLastYear() {
    	ExecutionPeriod executionPeriodLastYear = getLastYearExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.LABORATORIAL, executionPeriodLastYear);
    }

    public Double getRealTheoreticalHours() {
    	ExecutionPeriod executionPeriod = getExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.TEORICA, executionPeriod);
    }
    
    public Double getRealPraticalHours() {
    	ExecutionPeriod executionPeriod = getExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.PRATICA, executionPeriod);
    }
 
    public Double getRealTheoPratHours() {
    	ExecutionPeriod executionPeriod = getExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.TEORICO_PRATICA, executionPeriod);
    }
        
    public Double getRealLaboratorialHours() {
    	ExecutionPeriod executionPeriod = getExecutionPeriod();
    	return getRealHoursByShiftAndExecutionPeriod(ShiftType.LABORATORIAL, executionPeriod);
    }
    
    public Double getTheoreticalHoursCalculated() {
    	if(getStudentsPerTheoreticalShiftType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0.0;
    	}
    	
    	if(getStudentsPerTheoreticalShift().equals(0)) {
    		return 0.0;
    	}
    	
    	Double numberOfTheoreticalShifts = StrictMath.ceil(((new Double (getTotalNumberOfTheoreticalStudents()) / getStudentsPerTheoreticalShift())));    	
    	
    	return numberOfTheoreticalShifts * getTheoreticalHoursPerShift();
    }
    
    public Double getPraticalHoursCalculated() {
    	if(getStudentsPerPraticalShiftType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0.0;
    	}
    	
    	if(getStudentsPerPraticalShift().equals(0)) {
    		return 0.0;
    	}
    	
    	Double numberOfPraticalShifts = StrictMath.ceil(((new Double (getTotalNumberOfTheoreticalStudents()) / getStudentsPerPraticalShift())));
    	
    	return numberOfPraticalShifts * getPraticalHoursPerShift();
    }

    public Double getTheoPratHoursCalculated() {
    	if(getStudentsPerTheoPratShiftType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0.0;
    	}
    	
    	if(getStudentsPerTheoPratShift().equals(0)) {
    		return 0.0;
    	}
    	
    	Double numberOfTheoreticalPraticalShifts = StrictMath.ceil(((new Double (getTotalNumberOfTheoPratStudents()) / getStudentsPerTheoPratShift())));
    	
    	return numberOfTheoreticalPraticalShifts * getTheoPratHoursPerShift();
    }

    public Double getLaboratorialHoursCalculated() {
    	if(getStudentsPerLaboratorialShiftType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0.0;
    	}
    	
    	if(getStudentsPerLaboratorialShift().equals(0)) {
    		return 0.0;
    	}
    	
    	Double numberOfLaboratorialShifts = StrictMath.ceil(((new Double (getTotalNumberOfLaboratorialStudents()) / getStudentsPerLaboratorialShift())));
    	
    	return numberOfLaboratorialShifts * getLaboratorialHoursPerShift();
    }
    
    public Double getTheoreticalHours() {
    	if(getTheoreticalHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getTheoreticalHoursManual();
    	} else if(getTheoreticalHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealTheoreticalHoursLastYear();
        } else if(getTheoreticalHoursType() == ValuationValueType.REAL_VALUE) {
        	return getRealTheoreticalHours();
        } else if(getTheoreticalHoursType() == ValuationValueType.CALCULATED_VALUE) {
        	return getTheoreticalHoursCalculated();
        }
    	
    	return 0.0;
    }
    
    public Double getPraticalHours() {
    	if(getPraticalHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getPraticalHoursManual();
    	} else if(getPraticalHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealPraticalHoursLastYear();
        } else if(getPraticalHoursType() == ValuationValueType.REAL_VALUE) {
        	return getRealPraticalHours();
        } else if(getPraticalHoursType() == ValuationValueType.CALCULATED_VALUE) {
        	return getPraticalHoursCalculated();
        }
    	
    	return 0.0;
    }

    public Double getTheoPratHours() {
    	if(getTheoPratHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getTheoPratHoursManual();
    	} else if(getTheoPratHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealTheoPratHoursLastYear();
        } else if(getTheoPratHoursType() == ValuationValueType.REAL_VALUE) {
        	return getRealTheoPratHours();
        } else if(getTheoPratHoursType() == ValuationValueType.CALCULATED_VALUE) {
        	return getTheoPratHoursCalculated();
        }
    	
    	return 0.0;
    }

    public Double getLaboratorialHours() {
    	if(getLaboratorialHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getLaboratorialHoursManual();
    	} else if(getLaboratorialHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealLaboratorialHoursLastYear();
        } else if(getLaboratorialHoursType() == ValuationValueType.REAL_VALUE) {
        	return getRealLaboratorialHours();
        } else if(getLaboratorialHoursType() == ValuationValueType.CALCULATED_VALUE) {
        	return getLaboratorialHoursCalculated();
        }
    	
    	return 0.0;
    }
    
    public Double getTotalHours() {
    	return getTheoreticalHours() + getPraticalHours() + getTheoPratHours() + getLaboratorialHours();
    }
 
    
	private Integer getRealStudentsByShiftAndExecutionPeriod(ShiftType shiftType, ExecutionPeriod executionPeriod) {
		Double numberOfShifts = 0.0;
		Double totalNumberOfStudents = 0d;
		
		for(ValuationCurricularCourse valuationCurricularCourse : getAssociatedValuationCurricularCourses()) {
			List<ExecutionCourse> executionCourseList = valuationCurricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod);
			
			for (ExecutionCourse executionCourse : executionCourseList) {
				numberOfShifts += (executionCourse.getNumberOfShifts(shiftType) * executionCourse.getCurricularCourseEnrolmentsWeight(valuationCurricularCourse.getCurricularCourse()));
			}
			
			totalNumberOfStudents += valuationCurricularCourse.getTotalEnrolmentStudentNumber(executionPeriod);
		}
		
		numberOfShifts = StrictMath.ceil(numberOfShifts);
		
		if(numberOfShifts > 0d) {
			return ((Double) StrictMath.ceil((totalNumberOfStudents / numberOfShifts))).intValue();
		} else {
			return 0;
		}		
	}
	
	public Integer getRealStudentsPerTheoreticalShiftLastYear() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.TEORICA, getLastYearExecutionPeriod());
	}
	
	public Integer getRealStudentsPerPraticalShiftLastYear() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.PRATICA, getLastYearExecutionPeriod());
	}
	
	public Integer getRealStudentsPerTheoPratShiftLastYear() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.TEORICO_PRATICA, getLastYearExecutionPeriod());	}
	
	public Integer getRealStudentsPerLaboratorialShiftLastYear() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.LABORATORIAL, getLastYearExecutionPeriod());	}
	
	public Integer getRealStudentsPerTheoreticalShift() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.TEORICA, getExecutionPeriod());	
	}
	
	public Integer getRealStudentsPerPraticalShift() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.PRATICA, getExecutionPeriod());	
	}
	
	public Integer getRealStudentsPerTheoPratShift() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.TEORICO_PRATICA, getExecutionPeriod());
	}
	
	public Integer getRealStudentsPerLaboratorialShift() {
		return getRealStudentsByShiftAndExecutionPeriod(ShiftType.LABORATORIAL, getExecutionPeriod());
	}
    
    public Integer getStudentsPerTheoreticalShiftCalculated() {
    	if(getTheoreticalHoursType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0;
    	} else {
    		if(getTheoreticalHoursPerShift().equals(0.0)) {
    			return 0;
    		}
    		
    		Double numberOfTheoreticalShifts = (getTheoreticalHours() / getTheoreticalHoursPerShift());
    		numberOfTheoreticalShifts = StrictMath.ceil(numberOfTheoreticalShifts);
    		
    		if(numberOfTheoreticalShifts.equals(0.0)) {
    			return 0;
    		}
    		
    		Double studentsPerTheoreticalShift = getTotalNumberOfTheoreticalStudents() / numberOfTheoreticalShifts;
    		studentsPerTheoreticalShift = StrictMath.ceil(studentsPerTheoreticalShift);
    		
    		return studentsPerTheoreticalShift.intValue();
    	}
    }

    public Integer getStudentsPerPraticalShiftCalculated() {
    	if(getPraticalHoursType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0;
    	} else {
    		if(getPraticalHoursPerShift().equals(0.0)) {
    			return 0;
    		}
    		
    		Double numberOfPraticalShifts = (getPraticalHours() / getPraticalHoursPerShift());
    		numberOfPraticalShifts = StrictMath.ceil(numberOfPraticalShifts);
    		
    		if(numberOfPraticalShifts.equals(0.0)) {
    			return 0;
    		}
    		
    		Double studentsPerPraticalShift = getTotalNumberOfPraticalStudents() / numberOfPraticalShifts;
    		studentsPerPraticalShift = StrictMath.ceil(studentsPerPraticalShift);
    		
    		return studentsPerPraticalShift.intValue();
    	}
    }

    public Integer getStudentsPerTheoPratShiftCalculated() {
    	if(getTheoPratHoursType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0;
    	} else {
    		if(getTheoPratHoursPerShift().equals(0.0)) {
    			return 0;
    		}
    		
    		Double numberOfTheoreticalPraticalShifts = (getTheoPratHours() / getTheoPratHoursPerShift());
    		numberOfTheoreticalPraticalShifts = StrictMath.ceil(numberOfTheoreticalPraticalShifts);
    		
    		if(numberOfTheoreticalPraticalShifts.equals(0.0)) {
    			return 0;
    		}
    		
    		Double studentsPerTheoreticalPraticalShift = getTotalNumberOfTheoPratStudents() / numberOfTheoreticalPraticalShifts;
    		studentsPerTheoreticalPraticalShift = StrictMath.ceil(studentsPerTheoreticalPraticalShift);
    		
    		return studentsPerTheoreticalPraticalShift.intValue();
    	}
    }

    public Integer getStudentsPerLaboratorialShiftCalculated() {
    	if(getLaboratorialHoursType() == ValuationValueType.CALCULATED_VALUE) {
    		return 0;
    	} 

    	if(getLaboratorialHoursPerShift().equals(0.0)) {
			return 0;
		}
		
		Double numberOfLaboratorialShifts = (getLaboratorialHours() / getLaboratorialHoursPerShift());
		numberOfLaboratorialShifts = StrictMath.ceil(numberOfLaboratorialShifts);
		
		if(numberOfLaboratorialShifts.equals(0.0)) {
			return 0;
		}
		
		Double studentsPerLaboratorialShift = getTotalNumberOfLaboratorialStudents() / numberOfLaboratorialShifts;
		studentsPerLaboratorialShift = StrictMath.ceil(studentsPerLaboratorialShift);
		
		return studentsPerLaboratorialShift.intValue();
    }

    public Integer getStudentsPerTheoreticalShift() {
    	if(getStudentsPerTheoreticalShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getStudentsPerTheoreticalShiftManual();
    	} else if(getStudentsPerTheoreticalShiftType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealStudentsPerTheoreticalShiftLastYear();
        } else if(getStudentsPerTheoreticalShiftType() == ValuationValueType.REAL_VALUE) {
        	return getRealStudentsPerTheoreticalShift();
        } else if(getStudentsPerTheoreticalShiftType() == ValuationValueType.CALCULATED_VALUE) {
        	return getStudentsPerTheoreticalShiftCalculated();
        } else if(getStudentsPerTheoreticalShiftType() == ValuationValueType.OMISSION_VALUE) {
        	return getValuationPhase().getStudentsPerTheoreticalShift();
        }
    	
    	return 0;
    }

    public Integer getStudentsPerPraticalShift() {
    	if(getStudentsPerPraticalShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getStudentsPerPraticalShiftManual();
    	} else if(getStudentsPerPraticalShiftType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealStudentsPerPraticalShiftLastYear();
        } else if(getStudentsPerPraticalShiftType() == ValuationValueType.REAL_VALUE) {
        	return getRealStudentsPerPraticalShift();
        } else if(getStudentsPerPraticalShiftType() == ValuationValueType.CALCULATED_VALUE) {
        	return getStudentsPerPraticalShiftCalculated();
        } else if(getStudentsPerPraticalShiftType() == ValuationValueType.OMISSION_VALUE) {
        	return getValuationPhase().getStudentsPerPraticalShift();
        }
    	
    	return 0;
    }

    public Integer getStudentsPerTheoPratShift() {
    	if(getStudentsPerTheoPratShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getStudentsPerTheoPratShiftManual();
    	} else if(getStudentsPerTheoPratShiftType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealStudentsPerTheoPratShiftLastYear();
        } else if(getStudentsPerTheoPratShiftType() == ValuationValueType.REAL_VALUE) {
        	return getRealStudentsPerTheoPratShift();
        } else if(getStudentsPerTheoPratShiftType() == ValuationValueType.CALCULATED_VALUE) {
        	return getStudentsPerTheoPratShiftCalculated();
        } else if(getStudentsPerTheoPratShiftType() == ValuationValueType.OMISSION_VALUE) {
        	return getValuationPhase().getStudentsPerTheoPratShift();
        }
    	
    	return 0;
    }

    public Integer getStudentsPerLaboratorialShift() {
    	if(getStudentsPerLaboratorialShiftType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getStudentsPerLaboratorialShiftManual();
    	} else if(getStudentsPerLaboratorialShiftType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getRealStudentsPerLaboratorialShiftLastYear();
        } else if(getStudentsPerLaboratorialShiftType() == ValuationValueType.REAL_VALUE) {
        	return getRealStudentsPerLaboratorialShift();
        } else if(getStudentsPerLaboratorialShiftType() == ValuationValueType.CALCULATED_VALUE) {
        	return getStudentsPerLaboratorialShiftCalculated();
        } else if(getStudentsPerLaboratorialShiftType() == ValuationValueType.OMISSION_VALUE) {
        	return getValuationPhase().getStudentsPerLaboratorialShift();
        }
    	
    	return 0;
    }

    
    
    
    
    
    public ProfessorshipValuation getProfessorshipValuationByValuationTeacher(final ValuationTeacher valuationTeacher) {
    	return (ProfessorshipValuation) CollectionUtils.find(getProfessorshipValuations(), new Predicate() {

			public boolean evaluate(Object arg0) {
				ProfessorshipValuation professorshipValuation = (ProfessorshipValuation) arg0;
				
				return professorshipValuation.getValuationTeacher() == valuationTeacher;
			}
    	});
    }

    
    
    
	public List<ExecutionCourse> getAssociatedExecutionCoursesByExecutionPeriod(ExecutionPeriod executionPeriod) {
		Set<ExecutionCourse> executionCourseSet = new HashSet<ExecutionCourse>();
		
		for(ValuationCurricularCourse valuationCurricularcourse : getAssociatedValuationCurricularCourses()) {
			executionCourseSet.addAll(valuationCurricularcourse.getExecutionCoursesByExecutionPeriod(executionPeriod));
		}
		
		List<ExecutionCourse> executionCourseList = new ArrayList<ExecutionCourse>();
		executionCourseList.addAll(executionCourseSet);
		
		return executionCourseList;
	}
        
    public List<ExecutionCourse> getAssociatedExecutionCoursesLastYear() {
    	return getAssociatedExecutionCoursesByExecutionPeriod(getLastYearExecutionPeriod());
    }
    
    public List<ExecutionCourse> getAssociatedExecutionCourses() {
    	return getAssociatedExecutionCoursesByExecutionPeriod(getExecutionPeriod());
    }

    
    
	public ExecutionPeriod getLastYearExecutionPeriod() {
		ExecutionYear executionYear = getExecutionPeriod().getExecutionYear();
		
		ExecutionYear lastExecutionYear = executionYear.getPreviousExecutionYear();

		if(lastExecutionYear != null) {
			for (ExecutionPeriod executionPeriod : lastExecutionYear.getExecutionPeriods()) {
				if(executionPeriod.getSemester().equals(getExecutionPeriod().getSemester())) {
					return executionPeriod;
				}
			}
		}
		
		return null;
	}
	    
    private Double getTotalHoursLecturedByShiftType(ShiftType shiftType) {
    	Double totalHours = 0d;
    	
    	for (ProfessorshipValuation professorshipValuation : getProfessorshipValuations()) {
			if(shiftType == ShiftType.TEORICA) {
				totalHours += professorshipValuation.getTheoreticalHours();
			} else if(shiftType == ShiftType.PRATICA) {
				totalHours += professorshipValuation.getPraticalHours();
			} else if(shiftType == ShiftType.TEORICO_PRATICA) {
				totalHours += professorshipValuation.getTheoPratHours();
			} else if(shiftType == ShiftType.LABORATORIAL) {
				totalHours += professorshipValuation.getLaboratorialHours();
			}
		}
    	
    	return totalHours;
    }
    
    public Double getTotalTheoreticalHoursLectured() {
    	return getTotalHoursLecturedByShiftType(ShiftType.TEORICA);
    }
    
    public Double getTotalPraticalHoursLectured() {
    	return getTotalHoursLecturedByShiftType(ShiftType.PRATICA);
    }
    
    public Double getTotalTheoPratHoursLectured() {
    	return getTotalHoursLecturedByShiftType(ShiftType.TEORICO_PRATICA);
    }
    
    public Double getTotalLaboratorialHoursLectured() {
    	return getTotalHoursLecturedByShiftType(ShiftType.LABORATORIAL);
    }
    
    public Double getTotalHoursLectured() {
    	return getTotalTheoreticalHoursLectured() +
    		getTotalPraticalHoursLectured() +
    		getTotalTheoPratHoursLectured() +
    		getTotalLaboratorialHoursLectured();
    }
    
    public Double getTotalHoursNotLectured() {
    	return getTotalHours() - getTotalHoursLectured();
    }

    
	public List<String> getCampus() {
		Set<String> campusSet = new LinkedHashSet<String>();
		ExecutionPeriod executionPeriod = getExecutionPeriod();
		
		for (ValuationCurricularCourse valuationCurricularCourse : getAssociatedValuationCurricularCourses()) {
			if(!valuationCurricularCourse.getIsRealCurricularCourse()) continue;
			CurricularCourse curricularCourse = valuationCurricularCourse.getCurricularCourse();
			for (ExecutionDegree executionDegree : curricularCourse.getDegreeCurricularPlan().getExecutionDegrees()) {
				if (executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
					campusSet.add(executionDegree.getCampus().getName());
					break;
				}
			}
		}
		
		List<String> campusList = new ArrayList<String>();
		campusList.addAll(campusSet);
		
		return campusList;
	}
		
	public ValuationCompetenceCourse getValuationCompetenceCourse() {
		return getAssociatedValuationCurricularCourses().get(0).getValuationCompetenceCourse();
	}
	
	public String getName() {
		return getValuationCompetenceCourse().getName();
	}

	public Double getTheoreticalHoursPerShift() {
		return getAssociatedValuationCurricularCourses().get(0).getTheoreticalHours();
	}

	public Double getPraticalHoursPerShift() {
		return getAssociatedValuationCurricularCourses().get(0).getPraticalHours();	
	}

	public Double getTheoPratHoursPerShift() {
		return getAssociatedValuationCurricularCourses().get(0).getTheoPratHours();	
	}

	public Double getLaboratorialHoursPerShift() {
		return getAssociatedValuationCurricularCourses().get(0).getLaboratorialHours();
	}
	
    public void delete(){		
		for(ProfessorshipValuation professorshipValuation : getProfessorshipValuations()){
			professorshipValuation.delete();
		}		
		removeValuationPhase();
		removeRootDomainObject();
		removeExecutionPeriod();
		super.deleteDomainObject();
	}

	public String getAcronym() {
		
		ValuationCurricularCourse valuationCurricularCourse = null;
		if(getAssociatedValuationCurricularCourses().size() > 0) {
			valuationCurricularCourse = getAssociatedValuationCurricularCourses().get(0);
		}
		
		if(valuationCurricularCourse != null) {
			return valuationCurricularCourse.getAcronym();
		} else {
			return getName();
		}
	}

	public static CourseValuation createAndCopyFromCourseValuation(
			ValuationPhase valuationPhase, 
			CourseValuation courseValuation,
			Map<ValuationCompetenceCourse, ValuationCompetenceCourse> oldAndNewValuationCompetenceCourseMap,
			Map<ValuationCurricularCourse, ValuationCurricularCourse> oldAndNewValuationCurricularCourseMap,
			Map<ExecutionPeriod, ExecutionPeriod> oldAndNewExecutionPeriodMap) {
		CourseValuation newCourseValuation = null;
		
		if(courseValuation instanceof CompetenceCourseValuation) {
			ValuationCompetenceCourse valuationCompetenceCourse = oldAndNewValuationCompetenceCourseMap.get(courseValuation.getValuationCompetenceCourse());
			newCourseValuation = new CompetenceCourseValuation(valuationCompetenceCourse, valuationPhase, courseValuation.getExecutionPeriod());
		} else if(courseValuation instanceof CurricularCourseValuation) {
			ValuationCurricularCourse valuationCurricularCourse = oldAndNewValuationCurricularCourseMap.get(((CurricularCourseValuation) courseValuation).getValuationCurricularCourse());
			newCourseValuation = new CurricularCourseValuation(valuationCurricularCourse, valuationPhase, courseValuation.getExecutionPeriod());
		} else if(courseValuation instanceof CurricularCourseValuationGroup) {
			List<CurricularCourseValuation> associatedCurricularCourseValuations = createAndGetAssociatedCurricularCourseValuation(valuationPhase, ((CurricularCourseValuationGroup) courseValuation).getCurricularCourseValuations(), oldAndNewValuationCurricularCourseMap);
			newCourseValuation = new CurricularCourseValuationGroup(associatedCurricularCourseValuations);
		}
		
		fillCourseValuationFromAnotherCourseValuation(courseValuation, newCourseValuation);
		
		return newCourseValuation;
	}

	protected static void fillCourseValuationFromAnotherCourseValuation(CourseValuation courseValuation, CourseValuation newCourseValuation) {
		newCourseValuation.setFirstTimeEnrolledStudentsManual(courseValuation.getFirstTimeEnrolledStudents());
		newCourseValuation.setFirstTimeEnrolledStudentsType(ValuationValueType.MANUAL_VALUE);
		newCourseValuation.setSecondTimeEnrolledStudentsManual(courseValuation.getSecondTimeEnrolledStudents());
		newCourseValuation.setSecondTimeEnrolledStudentsType(ValuationValueType.MANUAL_VALUE);
		newCourseValuation.setStudentsPerTheoreticalShiftManual(courseValuation.getStudentsPerTheoreticalShift());
		newCourseValuation.setStudentsPerTheoreticalShiftType(ValuationValueType.MANUAL_VALUE);
		newCourseValuation.setStudentsPerPraticalShiftManual(courseValuation.getStudentsPerPraticalShift());
		newCourseValuation.setStudentsPerPraticalShiftType(ValuationValueType.MANUAL_VALUE);
		newCourseValuation.setStudentsPerTheoPratShiftManual(courseValuation.getStudentsPerTheoPratShift());
		newCourseValuation.setStudentsPerTheoPratShiftType(ValuationValueType.MANUAL_VALUE);
		newCourseValuation.setStudentsPerLaboratorialShiftManual(courseValuation.getStudentsPerLaboratorialShift());
		newCourseValuation.setStudentsPerLaboratorialShiftType(ValuationValueType.MANUAL_VALUE);
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftManual(courseValuation.getWeightFirstTimeEnrolledStudentsPerTheoShift());
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoShiftType(courseValuation.getWeightFirstTimeEnrolledStudentsPerTheoShiftType());
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftManual(courseValuation.getWeightFirstTimeEnrolledStudentsPerPratShift());
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerPratShiftType(courseValuation.getWeightFirstTimeEnrolledStudentsPerPratShiftType());
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual(courseValuation.getWeightFirstTimeEnrolledStudentsPerTheoPratShift());
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerTheoPratShiftType(courseValuation.getWeightFirstTimeEnrolledStudentsPerTheoPratShiftType());
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftManual(courseValuation.getWeightFirstTimeEnrolledStudentsPerLabShift());
		newCourseValuation.setWeightFirstTimeEnrolledStudentsPerLabShiftType(courseValuation.getWeightFirstTimeEnrolledStudentsPerLabShiftType());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftManual(courseValuation.getWeightSecondTimeEnrolledStudentsPerTheoPratShift());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoShiftType(courseValuation.getWeightSecondTimeEnrolledStudentsPerTheoShiftType());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftManual(courseValuation.getWeightSecondTimeEnrolledStudentsPerPratShift());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerPratShiftType(courseValuation.getWeightSecondTimeEnrolledStudentsPerPratShiftType());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual(courseValuation.getWeightSecondTimeEnrolledStudentsPerTheoPratShift());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerTheoPratShiftType(courseValuation.getWeightSecondTimeEnrolledStudentsPerTheoPratShiftType());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftManual(courseValuation.getWeightSecondTimeEnrolledStudentsPerLabShift());
		newCourseValuation.setWeightSecondTimeEnrolledStudentsPerLabShiftType(courseValuation.getWeightSecondTimeEnrolledStudentsPerLabShiftType());
		newCourseValuation.setTheoreticalHoursManual(courseValuation.getTheoreticalHoursManual());
		newCourseValuation.setTheoreticalHoursType(courseValuation.getTheoreticalHoursType());
		newCourseValuation.setPraticalHoursManual(courseValuation.getPraticalHoursManual());
		newCourseValuation.setPraticalHoursType(courseValuation.getPraticalHoursType());
		newCourseValuation.setTheoPratHoursManual(courseValuation.getTheoPratHoursManual());
		newCourseValuation.setTheoPratHoursType(courseValuation.getTheoPratHoursType());
		newCourseValuation.setLaboratorialHoursManual(courseValuation.getLaboratorialHoursManual());
		newCourseValuation.setLaboratorialHoursType(courseValuation.getLaboratorialHoursType());
		newCourseValuation.setIsActive(courseValuation.getIsActive());
		
		if(courseValuation instanceof CurricularCourseValuationGroup) {
			((CurricularCourseValuationGroup) newCourseValuation).setUsingCurricularCourseValuations(((CurricularCourseValuationGroup) courseValuation).getUsingCurricularCourseValuations());
		}
	}

	private static List<CurricularCourseValuation> createAndGetAssociatedCurricularCourseValuation(
			ValuationPhase valuationPhase,
			List<CurricularCourseValuation> oldCurricularCourseValuations, 
			Map<ValuationCurricularCourse, ValuationCurricularCourse> oldAndNewValuationCurricularCourseMap) {
		List<CurricularCourseValuation> curricularCourseValuationList = new ArrayList<CurricularCourseValuation>();
		
		for(CurricularCourseValuation oldCurricularCourseValuation : oldCurricularCourseValuations) {
			ValuationCurricularCourse newValuationCurricularCourse = oldAndNewValuationCurricularCourseMap.get(oldCurricularCourseValuation.getValuationCurricularCourse());
			CurricularCourseValuation newCurricularCourseValuation = newValuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(valuationPhase, oldCurricularCourseValuation.getExecutionPeriod());
			
			if(newCurricularCourseValuation == null) {
				newCurricularCourseValuation = new CurricularCourseValuation(newValuationCurricularCourse, valuationPhase, oldCurricularCourseValuation.getExecutionPeriod());
			}
			
			curricularCourseValuationList.add(newCurricularCourseValuation);
		}
		
		return curricularCourseValuationList;
	} 	
	
	public Boolean getHavePermissionToValuate(Person person) {
		TeacherServiceDistribution teacherServiceDistribution = getValuationPhase().getTeacherServiceDistribution(); 
		return getValuationCompetenceCourse().getHavePermissionToValuate(teacherServiceDistribution, person);
	}
	
	
	private Integer getTotalEnrolledStudentsNumberByExecutionPeriod(ExecutionPeriod executionPeriod) {
    	Integer totalEnrolledStudentsNumber = 0;
    	
    	if(executionPeriod != null) {
	    	for(ValuationCurricularCourse valuationCurricularCourse : getAssociatedValuationCurricularCourses()) {
	    		totalEnrolledStudentsNumber += valuationCurricularCourse.getTotalEnrolmentStudentNumber(executionPeriod);
	    	}
    	}
    	    	
    	return totalEnrolledStudentsNumber;   	    	
    }    
}
