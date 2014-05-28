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
package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.groups.Group;

public class GroupsBean implements Serializable {
    List<Group> groups;
    List<Group> selected;

    public GroupsBean() {
        selected = new ArrayList<Group>();
    }

    public GroupsBean(List<Group> groups) {
        setGroups(groups);
        selected = new ArrayList<Group>();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getSelected() {
        return selected;
    }

    public void setSelected(List<Group> groups) {
        selected = groups;
    }
}
