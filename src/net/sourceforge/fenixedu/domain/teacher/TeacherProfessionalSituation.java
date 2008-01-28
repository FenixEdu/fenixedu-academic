package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.ProfessionalSituationType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.RegimeType;

import org.joda.time.YearMonthDay;

public class TeacherProfessionalSituation extends TeacherProfessionalSituation_Base {

    public TeacherProfessionalSituation(Teacher teacher, Category category, YearMonthDay beginDate, YearMonthDay endDate,
	    Integer lessonHoursNumber, ProfessionalSituationType legalRegimenType, RegimeType regimenType, Integer percentage) {

	super();
	super.init(beginDate, endDate, legalRegimenType, regimenType, teacher.getPerson().getEmployee(), category);
	setWeeklyLessonHours(lessonHoursNumber);
	setPercentage(percentage);
    }

    @Override
    public void setCategory(Category category) {
	if (category == null && !isEndSituation() && !isFunctionAccumulation()) {
	    throw new DomainException("error.TeacherProfessionalSituation.no.category");
	}
	super.setCategory(category);
    }

    @Override
    public void setRegimeType(RegimeType regimeType) {
	if (regimeType == null && !isEndSituation()) {
	    throw new DomainException("error.TeacherProfessionalSituation.no.regimeType");
	}
	super.setRegimeType(regimeType);
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
	return situationType.isEndSituation();
    }
    
    @Override
    public boolean isTeacherProfessionalSituation() {
	return true;
    }
}
