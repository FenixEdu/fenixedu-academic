/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ProfessionalSituationType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class TeacherServiceExemption extends TeacherServiceExemption_Base {

    public TeacherServiceExemption(Teacher teacher, YearMonthDay beginDate, YearMonthDay endDate,
	    ProfessionalSituationType type, String institution) {
	
	super();	
	super.init(beginDate, endDate, type, null, teacher.getPerson().getEmployee(), null);	
	setInstitution(institution);
    }
             
    public boolean isLongDuration() {
	Integer daysBetween = null;
	if (getEndDateYearMonthDay() != null) {
	    daysBetween = new Interval(getBeginDateYearMonthDay().toDateMidnight(),
		    getEndDateYearMonthDay().toDateMidnight()).toPeriod(PeriodType.days()).getDays();
	}
	return (daysBetween == null || daysBetween > 90);
    }

    public boolean isMedicalSituation() {
	return (getSituationType().equals(ProfessionalSituationType.MEDICAL_SITUATION)
		|| getSituationType().equals(ProfessionalSituationType.MATERNAL_LICENSE_WITH_SALARY_80PERCENT)
		|| getSituationType().equals(ProfessionalSituationType.MATERNAL_LICENSE)
		|| getSituationType().equals(ProfessionalSituationType.DANGER_MATERNAL_LICENSE) 
		|| getSituationType().equals(ProfessionalSituationType.CHILDBIRTH_LICENSE));
    }
    
    public boolean isForCountInCreditsBecauseIsSabbaticalOrEquivalent() {
	return getSituationType().equals(ProfessionalSituationType.SABBATICAL)
		|| getSituationType().equals(ProfessionalSituationType.TEACHER_SERVICE_EXEMPTION_E_C_D_U)
		|| getSituationType().equals(ProfessionalSituationType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL);
    }

    public boolean isForCountInCreditsButDontIsSabbatical(Teacher teacher, ExecutionPeriod executionPeriod) {	
	
	if (isLongDuration() && !isForCountInCreditsBecauseIsSabbaticalOrEquivalent()) {
	   
	    if (getSituationType().equals(ProfessionalSituationType.GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY)
		    || getSituationType().equals(ProfessionalSituationType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS)
		    || getSituationType().equals(ProfessionalSituationType.TEACHER_SERVICE_EXEMPTION_DL24_84_ART51_N6_EST_DISC)) {
		return true;
	    }

	    if (getSituationType().equals(ProfessionalSituationType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY)) {
		Category teacherCategory = teacher.getCategoryForCreditsByPeriod(executionPeriod);
		Category pax_category = Category.readCategoryByCodeAndNameInPT("PAX", "Professor Auxiliar");
		return (teacherCategory != null && pax_category != null	&& !teacherCategory.equals(pax_category) 
			&& !teacherCategory.isTeacherCategoryMostImportantThan(pax_category));		    
	    }
	}

	return false;
    }

    public boolean isForNotCountInCredits() {
	return (getSituationType().equals(ProfessionalSituationType.CONTRACT_SUSPEND_ART_73_ECDU)
		|| getSituationType().equals(ProfessionalSituationType.CONTRACT_SUSPEND)
		|| getSituationType().equals(ProfessionalSituationType.GOVERNMENT_MEMBER)
		|| getSituationType().equals(ProfessionalSituationType.LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT)
		|| getSituationType().equals(ProfessionalSituationType.LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE)
		|| getSituationType().equals(ProfessionalSituationType.LICENSE_WITHOUT_SALARY_LONG)
		|| getSituationType().equals(ProfessionalSituationType.LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS)
		|| getSituationType().equals(ProfessionalSituationType.LICENSE_WITHOUT_SALARY_YEAR)
		|| getSituationType().equals(ProfessionalSituationType.MILITAR_SITUATION)
		|| getSituationType().equals(ProfessionalSituationType.REQUESTED_FOR)
		|| getSituationType().equals(ProfessionalSituationType.SERVICE_COMMISSION)
		|| getSituationType().equals(ProfessionalSituationType.SERVICE_COMMISSION_IST_OUT)
		|| getSituationType().equals(ProfessionalSituationType.SPECIAL_LICENSE)
		|| getSituationType().equals(ProfessionalSituationType.DETACHED_TO)
		|| getSituationType().equals(ProfessionalSituationType.INCAPACITY_FOR_TOGETHER_DOCTOR_OF_THE_CGA)
		|| getSituationType().equals(ProfessionalSituationType.FUNCTIONS_MANAGEMENT_SERVICE_EXEMPTION) 
		|| getSituationType().equals(ProfessionalSituationType.PUBLIC_MANAGER));
    }    
    
    @Override
    public boolean isTeacherServiceExemption() {
        return true;
    }
    
    @Deprecated
    public Date getStart() {
	return getBeginDate();
    }
    
    @Deprecated
    public Date getEnd() {
	return getEndDate();
    }
    
    public Teacher getTeacher() {
	return getEmployee().getPerson().getTeacher();
    }
    
    public ProfessionalSituationType getType() {
	return getSituationType();
    }
    
    public YearMonthDay getStartYearMonthDay() {
	return getBeginDateYearMonthDay();
    }
    
    public YearMonthDay getEndYearMonthDay() {
	return getEndDateYearMonthDay();
    }
}
