package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class PersistentGroupMembersBean implements Serializable {

    /**
     * Default serial id.
     */
    private static final long serialVersionUID = 1L;

    private Unit unit;
    private List<Person> people;
    private PersistentGroupMembers group;

    private String name;
    private PersistentGroupMembersType type;
    private Person person;

    private void init(PersistentGroupMembers group, Unit unit) {
        this.unit = unit;
        this.group = group;
        this.people = new ArrayList<Person>();
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
        return group;
    }

    public Unit getUnit() {
        return this.unit;
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

    public void setPeople(Collection<Person> people) {
        this.people.clear();
        this.people.addAll(people);
    }

    public void addPeople(List<Person> people) {
        this.people.addAll(people);
    }

    public void setIstId(Person istId) {
        this.person = istId;
    }

    public Person getIstId() {
        return person;
    }

}
