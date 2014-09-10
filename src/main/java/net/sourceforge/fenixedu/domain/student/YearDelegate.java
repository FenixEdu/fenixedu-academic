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
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

public class YearDelegate extends YearDelegate_Base {

    public YearDelegate() {
        super();
    }

    public YearDelegate(Registration registration, PersonFunction delegateFunction) {
        this();
        setRegistration(registration);
        setDelegateFunction(delegateFunction);
    }

    public CurricularYear getCurricularYear() {
        return getDelegateFunction().getCurricularYear();
    }

    public Person getPerson() {
        return getRegistration().getPerson();
    }

    public boolean isAfter(YearDelegate yearDelegate) {
        return getDelegateFunction().getEndDate().isAfter(yearDelegate.getDelegateFunction().getEndDate());
    }

    @Override
    public Degree getDegree() {
        return super.getDegree();
    }

}
