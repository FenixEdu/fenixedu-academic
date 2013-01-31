package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilantBoundBean implements Serializable {

	Person person;
	VigilantGroup group;
	boolean bounded;

	VigilantBoundBean() {
		setPerson(null);
		setVigilantGroup(null);
		setBounded(false);
	}

	VigilantBoundBean(Person person, VigilantGroup group, boolean bounded) {
		setPerson(person);
		setVigilantGroup(group);
		setBounded(bounded);
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setVigilantGroup(VigilantGroup group) {
		this.group = group;
	}

	public void setBounded(boolean value) {
		this.bounded = value;
	}

	public Person getPerson() {
		return this.person;
	}

	public VigilantGroup getVigilantGroup() {
		return this.group;
	}

	public boolean isBounded() {
		return bounded;
	}

}
