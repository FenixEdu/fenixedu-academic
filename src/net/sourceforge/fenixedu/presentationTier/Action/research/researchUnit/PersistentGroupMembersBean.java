package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class PersistentGroupMembersBean implements Serializable {

	/**
	 * Default serial id.
	 */
	private static final long serialVersionUID = 1L;
	
	private DomainReference<Unit> unit;
	private DomainListReference<Person> people;
	private DomainReference<PersistentGroupMembers> group;
	
	private String name;
	private PersistentGroupMembersType type; 

	private void init(PersistentGroupMembers group, Unit unit) {
		this.unit = new DomainReference<Unit>(unit);
		this.group = new DomainReference<PersistentGroupMembers>(group);
		this.people = new DomainListReference<Person>();
	}
	
	public PersistentGroupMembersBean(PersistentGroupMembers group) {
		init(group, group.getUnit());
		
		setName(group.getName());
		setPeople(group.getPersons());
	}
	
	public PersistentGroupMembersBean(Unit unit, PersistentGroupMembersType type) {
		init(null, unit);
		this.type = type;
	}
	
	public PersistentGroupMembersType getType() {
		return type;
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
		return this.people;
	}

	public void setPeople(List<Person> people) {
		this.people.clear();
		this.people.addAll(people);
	}
	
}
