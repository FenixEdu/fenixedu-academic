package net.sourceforge.fenixedu.domain;

public abstract class EquivalencePlan extends EquivalencePlan_Base {

    protected EquivalencePlan() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
    }

}
