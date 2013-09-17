package net.sourceforge.fenixedu.domain;

public abstract class EquivalencePlan extends EquivalencePlan_Base {

    protected EquivalencePlan() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setRootDomainObject(null);
        for (; hasAnyEntries(); getEntries().iterator().next().delete()) {
            ;
        }
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EquivalencePlanEntry> getEntries() {
        return getEntriesSet();
    }

    @Deprecated
    public boolean hasAnyEntries() {
        return !getEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

}
