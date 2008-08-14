package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;

public class NotInDegreeCurriculumCurriculumEntry extends CurriculumEntry {

    private final DomainReference<Enrolment> enrolmentDomainReference;

    public NotInDegreeCurriculumCurriculumEntry(final Enrolment enrolment) {
	super();
	this.enrolmentDomainReference = new DomainReference<Enrolment>(enrolment);
    }

    @Override
    public boolean isNotInDegreeCurriculumEnrolmentEntry() {
	return true;
    }

    public Enrolment getEnrolment() {
	return enrolmentDomainReference.getObject();
    }

    public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(getEnrolment().getEctsCredits());
    }

    public BigDecimal getWeigthForCurriculum() {
	return BigDecimal.valueOf(getEnrolment().getWeigth());
    }

    @Override
    public Grade getGrade() {
	return getEnrolment().getGrade();
    }

    @Override
    final public ExecutionSemester getExecutionPeriod() {
	return getEnrolment().getExecutionPeriod();
    }

    public Integer getIdInternal() {
	return getEnrolment().getIdInternal();
    }

}
