package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.DomainReference;

public class CreditsInScientificAreaCurriculumEntry extends CreditsCurriculumEntry {

    private final DomainReference<CreditsInScientificArea> creditsInScientificArea;
    
    public CreditsInScientificAreaCurriculumEntry(final CreditsInScientificArea creditsInScientificArea) {
	this.creditsInScientificArea = new DomainReference<CreditsInScientificArea>(creditsInScientificArea);
    }
    
    @Override
    public double getEctsCredits() {
	return creditsInScientificArea.getObject().getEctsCredits();
    }

}
