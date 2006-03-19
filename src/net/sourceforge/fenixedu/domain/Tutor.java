package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Tutor extends Tutor_Base {

    public Tutor() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	private Boolean getCanBeDeleted() {
        return Boolean.TRUE;
    }
    
    public void delete() {
        if (getCanBeDeleted()) {
            deleteDomainObject();
        } else {
            throw new DomainException("tutor.cant.delete");
        }
    }

}
