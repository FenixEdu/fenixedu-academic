package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

public class GivenCreditsEntry extends CreditsCurriculumEntry {

    private Double givenCredits;

    public GivenCreditsEntry(final Double givenCredits) {
	this.givenCredits = givenCredits;
    }

    public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(givenCredits);
    }

    public Integer getIdInternal() {
	return givenCredits.intValue();
    }

}
