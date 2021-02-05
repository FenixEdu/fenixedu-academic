/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public class Qualification extends Qualification_Base {

    public Qualification() {
        super();
        setRootDomainObject(Bennu.getInstance());
        Person personLogin = AccessControl.getPerson();
        if (personLogin != null) {
            setCreator(personLogin);
            setModifiedBy(personLogin);
        }
    }

    public Qualification(Person person, PrecedentDegreeInformation precedentDegreeInformation) {
        this();
        setPerson(person);
        setMark(precedentDegreeInformation.getConclusionGrade() == null ? null : precedentDegreeInformation.getConclusionGrade());
        setSchool(precedentDegreeInformation.getInstitution() == null ? null : precedentDegreeInformation.getInstitution()
                .getName());
        setDegree(precedentDegreeInformation.getDegreeDesignation() == null ? null : precedentDegreeInformation
                .getDegreeDesignation());
        setDateYearMonthDay(precedentDegreeInformation.getConclusionYear() == null ? null : new YearMonthDay(
                precedentDegreeInformation.getConclusionYear(), 1, 1));
        setCountry(precedentDegreeInformation.getCountry() == null ? null : precedentDegreeInformation.getCountry());
    }

    public void delete() {
        super.setPerson(null);
        super.setCreator(null);
        setModifiedBy(null);
        setCountry(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
