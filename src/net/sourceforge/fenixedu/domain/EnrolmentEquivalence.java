package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EnrolmentEquivalence extends EnrolmentEquivalence_Base {

    public EnrolmentEquivalence() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
        deleteDomainObject();
    }

}
