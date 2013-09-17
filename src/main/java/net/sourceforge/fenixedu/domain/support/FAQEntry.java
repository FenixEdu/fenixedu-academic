package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class FAQEntry extends FAQEntry_Base {

    public FAQEntry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasParentSection() {
        return getParentSection() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAnswer() {
        return getAnswer() != null;
    }

    @Deprecated
    public boolean hasQuestion() {
        return getQuestion() != null;
    }

}
