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
