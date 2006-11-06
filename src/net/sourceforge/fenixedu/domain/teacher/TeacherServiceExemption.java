/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class TeacherServiceExemption extends TeacherServiceExemption_Base {

    public TeacherServiceExemption(Teacher teacher, YearMonthDay beginDate, YearMonthDay endDate,
	    ServiceExemptionType type, String institution) {

	super();
	setRootDomainObject(RootDomainObject.getInstance());	
	setTeacher(teacher);
	setType(type);
	setInstitution(institution);
	setOccupationInterval(beginDate, endDate);
    }

    @Override
    public void setStartYearMonthDay(YearMonthDay beginDate) {
	checkTeacherServiceExemptionsDatesIntersection(getTeacher(), beginDate, getEndYearMonthDay(),
		getType());
	super.setStartYearMonthDay(beginDate);
    }

    @Override
    public void setEndYearMonthDay(YearMonthDay endDate) {
	checkTeacherServiceExemptionsDatesIntersection(getTeacher(), getStartYearMonthDay(), endDate,
		getType());
	super.setEndYearMonthDay(endDate);
    }

    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkTeacherServiceExemptionsDatesIntersection(getTeacher(), beginDate, endDate, getType());
	super.setStartYearMonthDay(beginDate);
	super.setEndYearMonthDay(endDate);
    }

    @Override
    public void setTeacher(Teacher teacher) {
	if (teacher == null) {
	    throw new DomainException("error.serviceExemption.no.teacher");
	}
	super.setTeacher(teacher);
    }

    @Override
    public void setType(ServiceExemptionType type) {
	if (type == null) {
	    throw new DomainException("error.serviceExemption.no.type");
	}
	super.setType(type);
    }

    public void delete() {
	super.setTeacher(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	return ((endDate == null || !this.getStartYearMonthDay().isAfter(endDate)) && (this
		.getEndYearMonthDay() == null || !this.getEndYearMonthDay().isBefore(beginDate)));
    }

    public boolean isMedicalSituation() {
	return (this.getType().equals(ServiceExemptionType.MEDICAL_SITUATION)
		|| this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE_WITH_SALARY_80PERCENT)
		|| this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE)
		|| this.getType().equals(ServiceExemptionType.DANGER_MATERNAL_LICENSE) || this.getType()
		.equals(ServiceExemptionType.CHILDBIRTH_LICENSE));
    }

    public boolean isServiceExemptionToCountZeroInCredits() {
	return (this.getType().equals(ServiceExemptionType.CONTRACT_SUSPEND_ART_73_ECDU)
		|| this.getType().equals(ServiceExemptionType.CONTRACT_SUSPEND)
		|| this.getType().equals(ServiceExemptionType.GOVERNMENT_MEMBER)
		|| this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT)
		|| this.getType().equals(
			ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE)
		|| this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_LONG)
		|| this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS)
		|| this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_YEAR)
		|| this.getType().equals(ServiceExemptionType.MILITAR_SITUATION)
		|| this.getType().equals(ServiceExemptionType.REQUESTED_FOR)
		|| this.getType().equals(ServiceExemptionType.SERVICE_COMMISSION)
		|| this.getType().equals(ServiceExemptionType.SERVICE_COMMISSION_IST_OUT)
		|| this.getType().equals(ServiceExemptionType.SPECIAL_LICENSE)
		|| this.getType().equals(ServiceExemptionType.DETACHED_TO)
		|| this.getType().equals(ServiceExemptionType.INCAPACITY_FOR_TOGETHER_DOCTOR_OF_THE_CGA)
		|| this.getType().equals(ServiceExemptionType.FUNCTIONS_MANAGEMENT_SERVICE_EXEMPTION) || this
		.getType().equals(ServiceExemptionType.PUBLIC_MANAGER));
    }

    public boolean isServiceExemptionToCountInCredits() {
	return (this.getType().equals(ServiceExemptionType.SABBATICAL)
		|| this.getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY)
		|| this.getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY)
		|| this.getType().equals(
			ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL)
		|| this.getType().equals(
			ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS)
		|| this.getType().equals(ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_E_C_D_U) || this
		.getType().equals(
			ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_DL24_84_ART51_N6_EST_DISC));
    }

    private void checkTeacherServiceExemptionsDatesIntersection(Teacher teacher, YearMonthDay begin,
	    YearMonthDay end, ServiceExemptionType type) {

	checkBeginDateAndEndDate(begin, end);
	// for (TeacherServiceExemption serviceExemption :
        // teacher.getServiceExemptionSituations()) {
	// if (serviceExemption.getType().equals(type)
	// && serviceExemption.checkDatesIntersections(begin, end)) {
	// System.out.println("Teacher Number: " + teacher.getTeacherNumber());
	// throw new
        // DomainException("error.teacherLegalRegimen.dates.intersection");
	// }
	// }
    }

    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.serviceExemption.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.serviceExemption.endDateBeforeBeginDate");
	}
    }

    private boolean checkDatesIntersections(YearMonthDay begin, YearMonthDay end) {
	return ((end == null || this.getStartYearMonthDay().isBefore(end)) && (this.getEndYearMonthDay() == null || this
		.getEndYearMonthDay().isAfter(begin)));
    }
}
