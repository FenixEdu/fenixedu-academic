package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.DomainReference;

public class CreditsInAnySecundaryAreaCurriculumEntry extends CreditsCurriculumEntry {

    private final DomainReference<CreditsInAnySecundaryArea> creditsInAnySecundaryArea;
    
    public CreditsInAnySecundaryAreaCurriculumEntry(final CreditsInAnySecundaryArea creditsInAnySecundaryArea) {
	this.creditsInAnySecundaryArea = new DomainReference<CreditsInAnySecundaryArea>(creditsInAnySecundaryArea);
    }

    public CreditsInAnySecundaryArea getCreditsInAnySecundaryArea() {
	return creditsInAnySecundaryArea.getObject();
    }
    
    public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(getCreditsInAnySecundaryArea().getEctsCredits());
    }

    public Integer getIdInternal() {
	return getCreditsInAnySecundaryArea().getIdInternal();
    }

}
