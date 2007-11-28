package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.CreditsInScientificArea;
import net.sourceforge.fenixedu.domain.DomainReference;

public class CreditsInScientificAreaCurriculumEntry extends CreditsCurriculumEntry {

    private final DomainReference<CreditsInScientificArea> creditsInScientificArea;
    
    public CreditsInScientificAreaCurriculumEntry(final CreditsInScientificArea creditsInScientificArea) {
	this.creditsInScientificArea = new DomainReference<CreditsInScientificArea>(creditsInScientificArea);
    }
    
    public CreditsInScientificArea getCreditsInScientificArea() {
	return creditsInScientificArea.getObject();
    }
    
    public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(creditsInScientificArea.getObject().getEctsCredits());
    }
    
    public Integer getIdInternal() {
	return getCreditsInScientificArea().getIdInternal();
    }

}
