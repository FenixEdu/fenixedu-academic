package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

public class ExternalTeacherAuthorization extends ExternalTeacherAuthorization_Base {
    
    public  ExternalTeacherAuthorization() {
        super();
    }
    
    @Service
    public void revoke(){
	this.setActive(false);
	this.setRevoker(AccessControl.getPerson());
	this.setUnactiveTime(new DateTime());
    }
}
