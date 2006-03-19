package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PartyType extends PartyType_Base {
    
    public PartyType(PartyTypeEnum partyTypeEnum) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setType(partyTypeEnum);
    }
    
}
