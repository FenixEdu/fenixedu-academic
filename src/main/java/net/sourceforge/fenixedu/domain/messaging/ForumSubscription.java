package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;

public class ForumSubscription extends ForumSubscription_Base {

    public ForumSubscription() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setReceivePostsByEmail(false);
        setFavorite(false);

    }

    public ForumSubscription(Person person, Forum forum) {
        this();
        setPerson(person);
        setForum(forum);
    }

    public void delete() {
        setPerson(null);
        setForum(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void removePerson() {
        super.setPerson(null);
    }

    public void removeForum() {
        super.setForum(null);
    }

    @Deprecated
    public boolean hasFavorite() {
        return getFavorite() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasForum() {
        return getForum() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasReceivePostsByEmail() {
        return getReceivePostsByEmail() != null;
    }

}
