package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DomainObjectActionLogEntry extends DomainObjectActionLogEntry_Base {
    
    public DomainObjectActionLogEntry(String name, String value, DomainObjectActionLog actionLog) {	
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setName(name);
        setValue(value);
        setDomainObjectActionLog(actionLog);
    }
        
    @Override
    public void setDomainObjectActionLog(DomainObjectActionLog domainObjectActionLog) {
	if(domainObjectActionLog == null) {
	    throw new DomainException("error.domainObjectActionLogEntry.empty.domainObjectActionLog");
	}
	super.setDomainObjectActionLog(domainObjectActionLog);
    }   
    
    public String getPresentationNameValue() {
	return getName() + "=" + getValue();
    }
}
