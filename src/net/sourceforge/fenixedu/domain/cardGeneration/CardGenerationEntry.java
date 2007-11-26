package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class CardGenerationEntry extends CardGenerationEntry_Base {
    
    public CardGenerationEntry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
