package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;

import org.apache.commons.lang.StringUtils;

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

    @Override
    public double getEctsCredits() {
	return getEnrolment().getEctsCredits().doubleValue();
    }

    @Override
    public double getWeigth() {
	return getEnrolment().getWeigth().doubleValue();
    }

    @Override
    public Double getWeigthTimesClassification() {
	final String grade = getEnrolment().getLatestEnrolmentEvaluation().getGrade();
	return StringUtils.isNumeric(grade) ? Double.valueOf(getWeigth() * Integer.valueOf(grade)) : null;
    }

}
