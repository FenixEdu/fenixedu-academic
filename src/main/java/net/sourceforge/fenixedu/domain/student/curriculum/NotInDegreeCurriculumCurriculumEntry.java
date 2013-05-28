package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class NotInDegreeCurriculumCurriculumEntry extends CurriculumEntry {

    private final Enrolment enrolmentDomainReference;

    public NotInDegreeCurriculumCurriculumEntry(final Enrolment enrolment) {
        super();
        this.enrolmentDomainReference = enrolment;
    }

    @Override
    public boolean isNotInDegreeCurriculumEnrolmentEntry() {
        return true;
    }

    public Enrolment getEnrolment() {
        return enrolmentDomainReference;
    }

    @Override
    public BigDecimal getEctsCreditsForCurriculum() {
        return BigDecimal.valueOf(getEnrolment().getEctsCredits());
    }

    @Override
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

    @Override
    public Integer getExternalId() {
        return getEnrolment().getExternalId();
    }

    @Override
    public DateTime getCreationDateDateTime() {
        return getEnrolment().getCreationDateDateTime();
    }

    @Override
    public YearMonthDay getApprovementDate() {
        return getEnrolment().getApprovementDate();
    }

    @Override
    public MultiLanguageString getName() {
        return enrolmentDomainReference.getName();
    }

}
