package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Proposal extends Proposal_Base {

    public Proposal() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }

    @Override
    public void setTitle(String title) {
	if (title != null && title.length() > 255) {
	    throw new DomainException("error.title.is.to.long.255.max");
	}
	super.setTitle(title);
    }

}
