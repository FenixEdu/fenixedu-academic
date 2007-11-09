package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreditsInScientificAreaCurriculumEntry extends CreditsCurriculumEntry {

    private final DomainReference<CreditsInScientificArea> creditsInScientificArea;
    
    public CreditsInScientificAreaCurriculumEntry(final CreditsInScientificArea creditsInScientificArea) {
	this.creditsInScientificArea = new DomainReference<CreditsInScientificArea>(creditsInScientificArea);
    }
    
    public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(creditsInScientificArea.getObject().getEctsCredits());
    }

}
