package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class StudentPersonalDataAuthorization extends StudentPersonalDataAuthorization_Base {
    
    public StudentPersonalDataAuthorization() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
