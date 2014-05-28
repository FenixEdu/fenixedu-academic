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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilantWrapperBean implements Serializable {

    public Person person;
    public List<VigilantGroup> convokableForGroups;
    public List<VigilantGroup> notConvokableForGroups;

    public VigilantWrapperBean(Person person) {
        setPerson(person);
        setConvokableForGroups(Collections.EMPTY_LIST);
        setNotConvokableForGroups(Collections.EMPTY_LIST);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<VigilantGroup> getConvokableForGroups() {
        List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
        for (VigilantGroup group : convokableForGroups) {
            groups.add(group);
        }
        return groups;
    }

    public void setConvokableForGroups(List<VigilantGroup> convokableForGroups) {
        this.convokableForGroups = new ArrayList<VigilantGroup>();
        for (VigilantGroup group : convokableForGroups) {
            this.convokableForGroups.add(group);
        }

    }

    public List<VigilantGroup> getNotConvokableForGroups() {
        List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
        for (VigilantGroup group : notConvokableForGroups) {
            groups.add(group);
        }
        return groups;
    }

    public void setNotConvokableForGroups(List<VigilantGroup> notConvokableForGroups) {
        this.notConvokableForGroups = new ArrayList<VigilantGroup>();
        for (VigilantGroup group : notConvokableForGroups) {
            this.notConvokableForGroups.add(group);
        }

    }

}
