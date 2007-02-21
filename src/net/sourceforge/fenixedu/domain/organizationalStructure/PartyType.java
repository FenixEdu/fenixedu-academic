package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PartyType extends PartyType_Base {
    
    public PartyType(PartyTypeEnum partyTypeEnum) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setType(partyTypeEnum);
    }
    
    @Override
    public void setType(PartyTypeEnum type) {
	if(type == null) {
	    throw new DomainException("error.PartyType.empty.type");
	}
	super.setType(type);
    }   
    
    public static PartyType readPartyTypeByType(PartyTypeEnum partyTypeEnum) {
	for (PartyType partyType : RootDomainObject.getInstance().getPartyTypes()) {
	    if(partyType.getType().equals(partyTypeEnum)) {
		return partyType;
	    }
	}
	return null;
    }
}
