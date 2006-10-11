package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilantBoundBean implements Serializable{

	DomainReference<Person> person;
	DomainReference<VigilantGroup> group;
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
		this.person = new DomainReference<Person>(person);
	}
	
	public void setVigilantGroup(VigilantGroup group) {
		this.group = new DomainReference<VigilantGroup>(group);
	}
	
	public void setBounded(boolean value) {
		this.bounded = value;
	}
	
	public Person getPerson() {
		return this.person.getObject();
	}
	
	public VigilantGroup getVigilantGroup() {
		return this.group.getObject();
	}
	
	public boolean isBounded() {
		return bounded;
	}
	
	
}
