package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PartyType extends PartyType_Base {
    
    public PartyType(final PartyTypeEnum partyTypeEnum) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setType(partyTypeEnum);
    }
    
    @Override
    public void setType(final PartyTypeEnum type) {
	if(type == null) {
	    throw new DomainException("error.PartyType.empty.type");
	}
	super.setType(type);
    }   
    
    public static PartyType readPartyTypeByType(final PartyTypeEnum partyTypeEnum) {
	for (final PartyType partyType : RootDomainObject.getInstance().getPartyTypes()) {
	    if(partyType.getType() == partyTypeEnum) {
		return partyType;
	    }
	}
	return null;
    }

    public static Set<Party> getPartiesSet(final PartyTypeEnum partyTypeEnum) {
	final PartyType partyType = readPartyTypeByType(partyTypeEnum);
	return partyType == null ? Collections.EMPTY_SET : partyType.getPartiesSet();
    }
}
