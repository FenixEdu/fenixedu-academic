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
package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.YearMonthDay;

public class InvitedPersonBean extends ExternalPersonBean {

    private Person invitedPersonReference;

    private Party responsibleReference;

    private Person responsiblePersonReference;

    private YearMonthDay begin;

    private YearMonthDay end;

    public InvitedPersonBean() {
        super();
    }

    public Person getInvitedPerson() {
        return invitedPersonReference;
    }

    public void setInvitedPerson(Person person) {
        this.invitedPersonReference = person;
    }

    public Party getResponsible() {
        return responsibleReference;
    }

    public void setResponsible(Party party) {
        this.responsibleReference = party;
    }

    public Person getResponsiblePerson() {
        return responsiblePersonReference;
    }

    public void setResponsiblePerson(Person person) {
        this.responsiblePersonReference = person;
        setResponsible(person);
    }

    public YearMonthDay getBegin() {
        return begin;
    }

    public void setBegin(YearMonthDay begin) {
        this.begin = begin;
    }

    public YearMonthDay getEnd() {
        return end;
    }

    public void setEnd(YearMonthDay end) {
        this.end = end;
    }
}
