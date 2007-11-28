package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class CardGenerationProblem extends CardGenerationProblem_Base {
    
    public CardGenerationProblem(final CardGenerationBatch cardGenerationBatch, final String descriptionKey, final String arg) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCardGenerationBatch(cardGenerationBatch);
        setDescriptionKey(descriptionKey);
        setArg(arg);
    }
    
}
