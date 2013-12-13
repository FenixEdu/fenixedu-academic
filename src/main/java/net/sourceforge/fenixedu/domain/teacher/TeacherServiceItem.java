package net.sourceforge.fenixedu.domain.teacher;

import pt.ist.bennu.core.domain.Bennu;

public abstract class TeacherServiceItem extends TeacherServiceItem_Base {

    public TeacherServiceItem() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
