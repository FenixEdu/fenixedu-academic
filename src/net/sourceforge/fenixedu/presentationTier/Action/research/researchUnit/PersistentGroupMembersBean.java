package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class PersistentGroupMembersBean implements Serializable {

	private DomainReference<Unit> unit;
	private List<DomainReference<Person>> people;
	private DomainReference<PersistentGroupMembers> group;
	
	private String name;
	private PersistentGroupMembersType type; 
	
	public PersistentGroupMembersType getType() {
		return type;
	}

	public void setType(PersistentGroupMembersType type) {
		this.type = type;
	}

	public PersistentGroupMembersBean(PersistentGroupMembers group) {
		this.unit = new DomainReference<Unit>(group.getUnit());
		this.group = new DomainReference<PersistentGroupMembers>(group);
		this.people = new ArrayList<DomainReference<Person>>();
		this.name = group.getName();
		setPeople(group.getPersons());
	}
	
	public PersistentGroupMembersBean(Unit unit) {
		this.unit = new DomainReference<Unit>(unit);
		this.people = new ArrayList<DomainReference<Person>>();
	}

	public PersistentGroupMembers getGroup() {
		return group.getObject();
	}
	
	public Unit getUnit() {
		return this.unit.getObject();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Person> getPeople() {
		List<Person> people = new ArrayList<Person>();
		for(DomainReference<Person> person : this.people) {
			people.add(person.getObject());
		}
		return people; 
	}

	public void setPeople(List<Person> people) {
		this.people.clear();
		for(Person person : people) {
			this.people.add(new DomainReference<Person>(person));
		}
	}
	
	
	
}
