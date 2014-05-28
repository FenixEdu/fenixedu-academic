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
package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

public class LibraryHigherCleranceGroupManagementBean implements Serializable {

    private Person operator;
    private Group higherClearenceGroup;
    private String searchUserId;

    public void setOperator(User operator) {
        this.operator = operator.getPerson();
    }

    public boolean getBelongsToGroup() {
        return higherClearenceGroup.isMember(operator.getUser());
    }

    public Person getOperator() {
        return operator;
    }

    public void setHigherClearenceGroup(Group higherClearenceGroup) {
        this.higherClearenceGroup = higherClearenceGroup;
    }

    public Group getHigherClearenceGroup() {
        return higherClearenceGroup;
    }

    public void setSearchUserId(String searchUserId) {
        this.searchUserId = searchUserId;
    }

    public String getSearchUserId() {
        return searchUserId;
    }

    public void search() {
        User res = User.findByUsername(getSearchUserId());

        if (res != null && res.getPerson().hasRole(RoleType.LIBRARY)) {
            setOperator(res);
        } else {
            setOperator(null);
        }
    }
}
