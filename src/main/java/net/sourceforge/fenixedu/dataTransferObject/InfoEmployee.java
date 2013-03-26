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
    public Integer getIdInternal() {
        return getEmployee().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
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
