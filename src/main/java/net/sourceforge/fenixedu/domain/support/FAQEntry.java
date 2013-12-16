package net.sourceforge.fenixedu.domain.support;

import pt.ist.bennu.core.domain.Bennu;

public class FAQEntry extends FAQEntry_Base {

    public FAQEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
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
