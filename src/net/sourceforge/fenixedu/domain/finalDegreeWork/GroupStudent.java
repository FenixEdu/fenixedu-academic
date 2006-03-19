package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GroupStudent extends GroupStudent_Base {

    public GroupStudent() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

}