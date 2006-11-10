package net.sourceforge.fenixedu.domain;

import org.apache.commons.lang.StringUtils;

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
    public void setName(String name) {
	if (name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.domainObjectActionLogEntry.empty.name");
	}	
	super.setName(name);
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
