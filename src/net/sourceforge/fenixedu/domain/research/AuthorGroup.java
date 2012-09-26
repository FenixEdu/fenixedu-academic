package net.sourceforge.fenixedu.domain.research;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class AuthorGroup extends Group {

    @Override
    public boolean equals(final Object other) {
	boolean result = other != null;

	if (result) {
	    result = this.getClass().equals(other.getClass());
	}

	return result;
    }

    @Override
    public int hashCode() {
	return getClass().hashCode();
    }

    public static class AuthorGroupBuilder implements GroupBuilder {
	@Override
	public Group build(Object[] arguments) {
	    return new AuthorGroup();
	}

	@Override
	public int getMinArguments() {
	    return 0;
	}

	@Override
	public int getMaxArguments() {
	    return 0;
	}
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> authors = new HashSet<Person>();
	for (Party party : RootDomainObject.getInstance().getPartysSet()) {
	    if (party instanceof Person) {
		if (((Person) party).hasAnyResultParticipations()) {
		    authors.add((Person) party);
		}
	    }
	}
	return authors;
    }

    @Override
    public boolean isMember(Person person) {
	return person != null && person.hasAnyResultParticipations();
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[0];
    }

}
