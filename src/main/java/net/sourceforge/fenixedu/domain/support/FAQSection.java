package net.sourceforge.fenixedu.domain.support;

import org.fenixedu.bennu.core.domain.Bennu;

public class FAQSection extends FAQSection_Base {

    public FAQSection() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        for (; !getChildEntries().isEmpty(); getChildEntries().iterator().next().delete()) {
            ;
        }
        for (; !getChildSections().isEmpty(); getChildSections().iterator().next().delete()) {
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSectionName() {
        return getSectionName() != null;
    }

}
