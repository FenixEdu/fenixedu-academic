package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PreferredPublication extends PreferredPublication_Base {
    public PreferredPublication(Person person, ResearchResultPublication preferredPublication,
	    PreferredPublicationPriority priority) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPersonThatPrefers(person);
	setPreferredPublication(preferredPublication);
	setPriority(priority);
    }

    public void delete() {
	removePersonThatPrefers();
	removePreferredPublication();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
