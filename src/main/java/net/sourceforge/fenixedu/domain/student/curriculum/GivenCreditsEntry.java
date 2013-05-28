package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

public class GivenCreditsEntry extends CreditsCurriculumEntry {

    private Double givenCredits;

    public GivenCreditsEntry(final Double givenCredits) {
        this.givenCredits = givenCredits;
    }

    @Override
    public BigDecimal getEctsCreditsForCurriculum() {
        return BigDecimal.valueOf(givenCredits);
    }

    @Override
    public Integer getExternalId() {
        return givenCredits.intValue();
    }

    @Override
    public YearMonthDay getApprovementDate() {
        throw new Error("not.implemented");
    }

}
