package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;

public class EnrolmentCurriculumEntry extends SimpleCurriculumEntry {

    private final DomainReference<Enrolment> enrolmentDomainReference;

    public EnrolmentCurriculumEntry(final CurricularCourse curricularCourse, final Enrolment enrolment) {
        super(curricularCourse);
        this.enrolmentDomainReference = new DomainReference<Enrolment>(enrolment);
    }

    @Override
    public boolean isEnrolmentEntry() {
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

    public Integer getIdInternal() {
	return getEnrolment().getIdInternal();
    }

}
