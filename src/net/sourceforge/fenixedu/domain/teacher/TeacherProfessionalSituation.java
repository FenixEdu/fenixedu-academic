package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.ProfessionalSituationType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.RegimeType;

import org.joda.time.YearMonthDay;

public class TeacherProfessionalSituation extends TeacherProfessionalSituation_Base {
  
    public TeacherProfessionalSituation(Teacher teacher, Category category, YearMonthDay beginDate,
	    YearMonthDay endDate, Double totalHoursNumber, Integer lessonHoursNumber,
	    ProfessionalSituationType legalRegimenType, RegimeType regimenType, Integer percentage) {

	super();	
	super.init(beginDate, endDate, legalRegimenType, regimenType, teacher.getPerson().getEmployee());		
	setCategory(category);
	setWeeklyWorkingHours(totalHoursNumber);
	setWeeklyLessonHours(lessonHoursNumber);
	setPercentage(percentage);	
    }   

    @Override
    public void delete() {
	super.setCategory(null);	
	super.delete();
    }
      
    @Override
    public void setWeeklyLessonHours(Integer weeklyLessonHours) {
	if(weeklyLessonHours == null && !isEndSituation()) {
	    throw new DomainException("error.TeacherProfessionalSituation.no.lesson.hours");
	}
	super.setWeeklyLessonHours(weeklyLessonHours);
    }
    
    @Override
    public void setWeeklyWorkingHours(Double weeklyWorkingHours) {
	if(weeklyWorkingHours == null && !isEndSituation()) {
	    throw new DomainException("error.TeacherProfessionalSituation.no.total.hours");
	}
        super.setWeeklyWorkingHours(weeklyWorkingHours);
    }
    
    @Override
    public void setRegimeType(RegimeType regimeType) {
	if (regimeType == null && !isEndSituation()) {
	    throw new DomainException("error.TeacherProfessionalSituation.no.regimeType");
	}
	super.setRegimeType(regimeType);
    }     
    
    @Override
    public void setCategory(Category category) {
	if (category == null && !isEndSituation()) {
	    throw new DomainException("error.TeacherProfessionalSituation.no.category");
	}
	super.setCategory(category);
    }

    public Teacher getTeacher() {
	return getEmployee().getPerson().getTeacher();
    }
    
    public boolean isEndSituation() {
	return isEndProfessionalSituationType(getSituationType());
    }
    
    public boolean isFunctionAccumulation() {
	return isFunctionsAccumulation(getSituationType());
    }
    
    public boolean isFunctionsAccumulation(ProfessionalSituationType situationType) {
	return situationType.equals(ProfessionalSituationType.FUNCTIONS_ACCUMULATION_WITH_LEADING_POSITIONS);
    }
    
    public static boolean isEndProfessionalSituationType(ProfessionalSituationType situationType) {
	return (situationType.equals(ProfessionalSituationType.DEATH)
		|| situationType.equals(ProfessionalSituationType.TERM_WORK_CONTRACT_END)
		|| situationType.equals(ProfessionalSituationType.EMERITUS)
		|| situationType.equals(ProfessionalSituationType.RETIREMENT)
		|| situationType.equals(ProfessionalSituationType.RETIREMENT_IN_PROGRESS)		
		|| situationType.equals(ProfessionalSituationType.CERTAIN_FORWARD_CONTRACT_END)
		|| situationType.equals(ProfessionalSituationType.CERTAIN_FORWARD_CONTRACT_END_PROPER_PRESCRIPTIONS)
		|| situationType.equals(ProfessionalSituationType.CERTAIN_FORWARD_CONTRACT_RESCISSION)
		|| situationType.equals(ProfessionalSituationType.CERTAIN_FORWARD_CONTRACT_RESCISSION_PROPER_PRESCRIPTIONS)
		|| situationType.equals(ProfessionalSituationType.CONTRACT_END)
		|| situationType.equals(ProfessionalSituationType.DENUNCIATION)
		|| situationType.equals(ProfessionalSituationType.RESIGNATION)
		|| situationType.equals(ProfessionalSituationType.IST_OUT_NOMINATION)
		|| situationType.equals(ProfessionalSituationType.SERVICE_TURN_OFF)
		|| situationType.equals(ProfessionalSituationType.TEMPORARY_SUBSTITUTION_CONTRACT_END)
		|| situationType.equals(ProfessionalSituationType.EXONERATION)
		|| situationType.equals(ProfessionalSituationType.RESCISSION) 
		|| situationType.equals(ProfessionalSituationType.TRANSFERENCE)
		|| situationType.equals(ProfessionalSituationType.REFUSED_DEFINITIVE_NOMINATION));
    }   
    
    @Override
    public boolean isTeacherProfessionalSituation() {
        return true;
    }
}
