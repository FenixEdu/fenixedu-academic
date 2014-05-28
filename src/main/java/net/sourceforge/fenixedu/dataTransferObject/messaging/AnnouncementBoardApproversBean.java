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
package net.sourceforge.fenixedu.dataTransferObject.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;

public class AnnouncementBoardApproversBean implements Serializable {

    private Person person;
    private boolean approver;

    public AnnouncementBoardApproversBean(final Person person, final boolean approver) {
        setPerson(person);
        setApprover(approver);
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isApprover() {
        return approver;
    }

    public void setApprover(boolean approver) {
        this.approver = approver;
    }

}
