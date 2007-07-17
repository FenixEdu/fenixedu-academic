package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public abstract class TeacherServiceItem extends TeacherServiceItem_Base {

    public TeacherServiceItem() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {      
        removeRootDomainObject();
        removeTeacherService();
        deleteDomainObject();
    }   
}
