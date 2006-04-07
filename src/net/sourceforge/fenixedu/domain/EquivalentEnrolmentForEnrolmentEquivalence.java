package net.sourceforge.fenixedu.domain;

/**
 * @author jpvl
 */
public class EquivalentEnrolmentForEnrolmentEquivalence extends EquivalentEnrolmentForEnrolmentEquivalence_Base {

    public EquivalentEnrolmentForEnrolmentEquivalence() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
        removeRootDomainObject();
        deleteDomainObject();
    }

}
