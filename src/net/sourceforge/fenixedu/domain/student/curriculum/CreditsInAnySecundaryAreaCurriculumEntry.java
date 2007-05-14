package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.CreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.domain.DomainReference;

public class CreditsInAnySecundaryAreaCurriculumEntry extends CreditsCurriculumEntry {

    private final DomainReference<CreditsInAnySecundaryArea> creditsInAnySecundaryArea;
    
    public CreditsInAnySecundaryAreaCurriculumEntry(final CreditsInAnySecundaryArea creditsInAnySecundaryArea) {
	this.creditsInAnySecundaryArea = new DomainReference<CreditsInAnySecundaryArea>(creditsInAnySecundaryArea);
    }
    
    @Override
    public double getEctsCredits() {
	return creditsInAnySecundaryArea.getObject().getEctsCredits();
    }

}
