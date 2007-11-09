package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Grade;
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
    
    public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(getNotNeedToEnrol().getEctsCredits());
    }

    public BigDecimal getWeigthForCurriculum() {
	throw new RuntimeException();
    }

    @Override
    public BigDecimal getWeigthTimesGrade() {
	return null;
    }

    @Override
    public Grade getGrade() {
	return null;
    }

}
