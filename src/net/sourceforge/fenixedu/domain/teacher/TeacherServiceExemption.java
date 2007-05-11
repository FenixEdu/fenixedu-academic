/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class TeacherServiceExemption extends TeacherServiceExemption_Base {

    public static final Comparator<TeacherServiceExemption> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("startYearMonthDay"));
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    public TeacherServiceExemption(Teacher teacher, YearMonthDay beginDate, YearMonthDay endDate,
	    ServiceExemptionType type, String institution) {
	super();	
	super.init(beginDate, endDate, type, institution);
	setTeacher(teacher);	
    }

    @Override
    public void setTeacher(Teacher teacher) {
	if (teacher == null) {
	    throw new DomainException("error.serviceExemption.no.teacher");
	}
	super.setTeacher(teacher);
    }
    
    @Override
    public void delete() {
	super.setTeacher(null);
	super.delete();
    }
      
    public boolean isLongDuration() {
	Integer daysBetween = null;
	if (getEndYearMonthDay() != null) {
	    daysBetween = new Interval(getStartYearMonthDay().toDateMidnight(),
		    getEndYearMonthDay().toDateMidnight()).toPeriod(PeriodType.days()).getDays();
	}
	return (daysBetween == null || daysBetween > 90);
    }

    public boolean isMedicalSituation() {
	return (this.getType().equals(ServiceExemptionType.MEDICAL_SITUATION)
		|| this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE_WITH_SALARY_80PERCENT)
		|| this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE)
		|| this.getType().equals(ServiceExemptionType.DANGER_MATERNAL_LICENSE) 
		|| this.getType().equals(ServiceExemptionType.CHILDBIRTH_LICENSE));
    }
    
    public boolean isForCountInCreditsBecauseIsSabbaticalOrEquivalent() {
	return getType().equals(ServiceExemptionType.SABBATICAL)
		|| getType().equals(ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_E_C_D_U)
		|| getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL);
    }

    public boolean isForCountInCreditsButDontIsSabbatical(Teacher teacher, ExecutionPeriod executionPeriod) {	
	if (isLongDuration() && !isForCountInCreditsBecauseIsSabbaticalOrEquivalent()) {
	    
	    if (getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY)
		    || getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS)
		    || getType().equals(ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_DL24_84_ART51_N6_EST_DISC)) {
		return true;
	    }

	    if (getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY)) {
		Category teacherCategory = teacher.getCategoryForCreditsByPeriod(executionPeriod);
		Category pax_category = Category.readCategoryByCodeAndNameInPT("PAX", "Professor Auxiliar");
		return (teacherCategory != null && pax_category != null && !teacherCategory.equals(pax_category) && !teacherCategory.isMostImportantThan(pax_category));		    
	    }
	}

	return false;
    }

    public boolean isForNotCountInCredits() {
	return (getType().equals(ServiceExemptionType.CONTRACT_SUSPEND_ART_73_ECDU)
		|| getType().equals(ServiceExemptionType.CONTRACT_SUSPEND)
		|| getType().equals(ServiceExemptionType.GOVERNMENT_MEMBER)
		|| getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT)
		|| getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE)
		|| getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_LONG)
		|| getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS)
		|| getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_YEAR)
		|| getType().equals(ServiceExemptionType.MILITAR_SITUATION)
		|| getType().equals(ServiceExemptionType.REQUESTED_FOR)
		|| getType().equals(ServiceExemptionType.SERVICE_COMMISSION)
		|| getType().equals(ServiceExemptionType.SERVICE_COMMISSION_IST_OUT)
		|| getType().equals(ServiceExemptionType.SPECIAL_LICENSE)
		|| getType().equals(ServiceExemptionType.DETACHED_TO)
		|| getType().equals(ServiceExemptionType.INCAPACITY_FOR_TOGETHER_DOCTOR_OF_THE_CGA)
		|| getType().equals(ServiceExemptionType.FUNCTIONS_MANAGEMENT_SERVICE_EXEMPTION) 
		|| getType().equals(ServiceExemptionType.PUBLIC_MANAGER));
    }    
}
