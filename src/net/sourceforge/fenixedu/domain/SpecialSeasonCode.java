package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SpecialSeasonCode extends SpecialSeasonCode_Base {
    
    public  SpecialSeasonCode() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete() {
    	if(canBeDeleted()) {
    		setRootDomainObject(null);
    		super.deleteDomainObject();
    	} else {
    		throw new DomainException("error.cannot.delete.specialSeasonCode");
    	}
    }
    
    private boolean canBeDeleted() {
    	return !hasAnyYearStudentSpecialSeasonCodes();
    }
    
}
