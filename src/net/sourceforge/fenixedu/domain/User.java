package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.IdentificationCard;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author mrsp
 * @author shezad
 */

public class User extends User_Base {

    private User() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public User(Person person) {
	this();
	if (person == null) {
	    throw new DomainException("error.no.person");
	}
	super.setPerson(person);
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.impossible.to.edit.person");
    }
   
    public static User readUserByUserUId(final String userUId) {
	for (final User user : RootDomainObject.getInstance().getUsers()) {
	    if (user.getUserUId() != null && user.getUserUId().equalsIgnoreCase(userUId)) {
		return user;
	    }
	}
	return null;
    }

    public Login readUserLoginIdentification() {
	for (Identification identification : getIdentifications()) {
	    if (identification.isLogin()) {
		return (Login) identification;
	    }
	}
	return null;
    }

    public List<IdentificationCard> getIdentificationCards() {
	List<IdentificationCard> cards = new ArrayList<IdentificationCard>();
	for (Identification identification : this.getIdentifications()) {
	    if (identification instanceof IdentificationCard) {
		cards.add((IdentificationCard) identification);
	    }
	}
	return cards;
    }

    public void delete() {
	for (; !getIdentifications().isEmpty(); getIdentifications().get(0).delete()) ;
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public String getAliass() {
        final StringBuilder aliass = new StringBuilder();
        for (final LoginAlias loginAlias : readUserLoginIdentification().getLoginAliasOrderByImportance()) {
            if (aliass.length() > 0) {
                aliass.append(", ");
            }
            aliass.append(loginAlias.getAlias());
        }
        return aliass.toString();
    }

}
