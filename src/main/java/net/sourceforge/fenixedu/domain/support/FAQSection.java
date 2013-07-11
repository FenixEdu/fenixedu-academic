package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class FAQSection extends FAQSection_Base {

    public FAQSection() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        for (; !getChildEntries().isEmpty(); getChildEntries().get(0).delete()) {
            ;
        }
        for (; !getChildSections().isEmpty(); getChildSections().get(0).delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.support.FAQSection> getChildSections() {
        return getChildSectionsSet();
    }

    @Deprecated
    public boolean hasAnyChildSections() {
        return !getChildSectionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.support.FAQEntry> getChildEntries() {
        return getChildEntriesSet();
    }

    @Deprecated
    public boolean hasAnyChildEntries() {
        return !getChildEntriesSet().isEmpty();
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
    public boolean hasSectionName() {
        return getSectionName() != null;
    }

}
