package net.sourceforge.fenixedu.domain;

public abstract class EquivalencePlan extends EquivalencePlan_Base {

    protected EquivalencePlan() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setRootDomainObject(null);
        for (; hasAnyEntries(); getEntries().get(0).delete()) {
            ;
        }
        super.deleteDomainObject();
    }

}
