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
/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Employee;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class InfoEmployee extends InfoObject {

    private final Employee employeeDomainReference;

    public InfoEmployee(final Employee employee) {
        employeeDomainReference = employee;
    }

    public static InfoEmployee newInfoFromDomain(final Employee employee) {
        return employee == null ? null : new InfoEmployee(employee);
    }

    private Employee getEmployee() {
        return employeeDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoEmployee && getEmployee() == ((InfoEmployee) obj).getEmployee();
    }

    @Override
    public String getExternalId() {
        return getEmployee().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public Integer getEmployeeNumber() {
        return getEmployee().getEmployeeNumber();
    }

    public InfoUnit getWorkingUnit() {
        return InfoUnit.newInfoFromDomain((getEmployee().getCurrentWorkingPlace() != null) ? getEmployee()
                .getCurrentWorkingPlace() : null);
    }

    public InfoUnit getMailingUnit() {
        return InfoUnit.newInfoFromDomain((getEmployee().getCurrentMailingPlace() != null) ? getEmployee()
                .getCurrentMailingPlace() : null);
    }

    public InfoPerson getPerson() {
        return InfoPerson.newInfoFromDomain(getEmployee().getPerson());
    }

    @Override
    public String toString() {
        return getEmployee().toString();
    }

}
