package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ProfessorshipValuation extends ProfessorshipValuation_Base {
    
    private  ProfessorshipValuation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ProfessorshipValuation(CourseValuation courseValuation, ValuationTeacher valuationTeacher) {
		this();
		
		this.setCourseValuation(courseValuation);
		this.setValuationTeacher(valuationTeacher);
	}

	public Double getTotalHours() {
    	return 
    		getTheoreticalHours() + 
    		getPraticalHours() +
    		getTheoPratHours() +
    		getLaboratorialHours();
    }
    
    public Double getTheoreticalHours() {
    	if(getTheoreticalHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getTheoreticalHoursManual();
    	} else if(getTheoreticalHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getValuationTeacher().getRealTheoreticalHours(getCourseValuation().getAssociatedExecutionCoursesLastYear());
        } else if(getTheoreticalHoursType() == ValuationValueType.REAL_VALUE) {
        	return getValuationTeacher().getRealTheoreticalHours(getCourseValuation().getAssociatedExecutionCourses());
        }
    	
    	return 0.0;
    }
    
    public Double getPraticalHours() {
    	if(getPraticalHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getPraticalHoursManual();
    	} else if(getPraticalHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getValuationTeacher().getRealPraticalHours(getCourseValuation().getAssociatedExecutionCoursesLastYear());
        } else if(getPraticalHoursType() == ValuationValueType.REAL_VALUE) {
        	return getValuationTeacher().getRealPraticalHours(getCourseValuation().getAssociatedExecutionCourses());
        }
    	
    	return 0.0;
    }
    
    public Double getTheoPratHours() {
    	if(getTheoPratHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getTheoPratHoursManual();
    	} else if(getTheoPratHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getValuationTeacher().getRealTheoPratHours(getCourseValuation().getAssociatedExecutionCoursesLastYear());
        } else if(getTheoPratHoursType() == ValuationValueType.REAL_VALUE) {
        	return getValuationTeacher().getRealTheoPratHours(getCourseValuation().getAssociatedExecutionCourses());
        }
    	
    	return 0.0;
    }

    public Double getLaboratorialHours() {
    	if(getLaboratorialHoursType() == ValuationValueType.MANUAL_VALUE) {
    		return super.getLaboratorialHoursManual();
    	} else if(getLaboratorialHoursType() == ValuationValueType.LAST_YEAR_REAL_VALUE) {
    		return getValuationTeacher().getRealLaboratorialHours(getCourseValuation().getAssociatedExecutionCoursesLastYear());
        } else if(getLaboratorialHoursType() == ValuationValueType.REAL_VALUE) {
        	return getValuationTeacher().getRealLaboratorialHours(getCourseValuation().getAssociatedExecutionCourses());
        }
    	
    	return 0.0;
    }

	public void delete(){
		removeCourseValuation();
		removeValuationTeacher();
		removeRootDomainObject();
		super.deleteDomainObject();
	}
	
	public ExecutionPeriod getExecutionPeriod() {
		return getCourseValuation().getExecutionPeriod();
	}

	public static ProfessorshipValuation createAndCopy(CourseValuation courseValuation, ValuationTeacher valuationTeacher, ProfessorshipValuation oldProfessorshipValuation) {
		ProfessorshipValuation newProfessorshipValuation = new ProfessorshipValuation(courseValuation, valuationTeacher);

		newProfessorshipValuation.setTheoreticalHoursManual(oldProfessorshipValuation.getTheoreticalHours());
		newProfessorshipValuation.setTheoreticalHoursType(oldProfessorshipValuation.getTheoreticalHoursType());
		newProfessorshipValuation.setPraticalHoursManual(oldProfessorshipValuation.getPraticalHours());
		newProfessorshipValuation.setPraticalHoursType(oldProfessorshipValuation.getPraticalHoursType());
		newProfessorshipValuation.setTheoPratHoursManual(oldProfessorshipValuation.getTheoPratHours());
		newProfessorshipValuation.setTheoPratHoursType(oldProfessorshipValuation.getTheoPratHoursType());
		newProfessorshipValuation.setLaboratorialHoursManual(oldProfessorshipValuation.getLaboratorialHours());
		newProfessorshipValuation.setLaboratorialHoursType(oldProfessorshipValuation.getLaboratorialHoursType());		
		
		return newProfessorshipValuation;
	}
	
	public Boolean getIsActive() {
		return getCourseValuation().getIsActive();
	}
	
	public Boolean getHavePermissionToValuate(Person person) {
		return getCourseValuation().getHavePermissionToValuate(person) && getValuationTeacher().getHavePermissionToValuate(person);
	}
	
}
