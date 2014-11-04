/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.ArrayList;
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
        this.people.addAll(group.getPersonsSet());
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

    public void setPeople(List<Person> people) {
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
