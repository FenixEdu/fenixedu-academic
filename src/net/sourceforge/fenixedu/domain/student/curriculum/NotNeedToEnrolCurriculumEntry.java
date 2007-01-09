package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

public class NotNeedToEnrolCurriculumEntry extends SimpleCurriculumEntry {

    private final DomainReference<NotNeedToEnrollInCurricularCourse> notNeedToEnrolDomainReference;

    public NotNeedToEnrolCurriculumEntry(final CurricularCourse curricularCourse,
	    final NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse) {
	super(curricularCourse);
	this.notNeedToEnrolDomainReference = new DomainReference<NotNeedToEnrollInCurricularCourse>(
		notNeedToEnrollInCurricularCourse);
    }

    @Override
    public boolean isNotNeedToEnrolEntry() {
	return true;
    }

    public NotNeedToEnrollInCurricularCourse getNotNeedToEnrol() {
	return notNeedToEnrolDomainReference.getObject();
    }

    @Override
    public double getEctsCredits() {
	return getNotNeedToEnrol().getCurricularCourse().getEctsCredits().doubleValue();
    }

    @Override
    public double getWeigth() {
	throw new RuntimeException();
    }

    @Override
    public Double getWeigthTimesClassification() {
	return null;
    }

}
