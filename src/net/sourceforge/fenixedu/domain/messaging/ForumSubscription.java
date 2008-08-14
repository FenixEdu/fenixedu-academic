package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ForumSubscription extends ForumSubscription_Base {

    public ForumSubscription() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setReceivePostsByEmail(false);
	setFavorite(false);

    }

    public ForumSubscription(Person person, Forum forum) {
	this();
	setPerson(person);
	setForum(forum);
    }

    public void delete() {
	removePerson();
	removeForum();
	removeRootDomainObject();
	deleteDomainObject();
    }

    @Override
    public void removePerson() {
	super.setPerson(null);
    }

    @Override
    public void removeForum() {
	super.setForum(null);
    }

}
