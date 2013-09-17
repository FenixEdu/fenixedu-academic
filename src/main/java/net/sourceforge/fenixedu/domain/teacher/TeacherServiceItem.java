package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public abstract class TeacherServiceItem extends TeacherServiceItem_Base {

    public TeacherServiceItem() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setTeacherService(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }
    @Deprecated
    public boolean hasTeacherService() {
        return getTeacherService() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

}
