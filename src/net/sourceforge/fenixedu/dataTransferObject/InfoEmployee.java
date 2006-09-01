/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class InfoEmployee extends InfoObject {

    private final DomainReference<Employee> employeeDomainReference;
    
    public InfoEmployee(final Employee employee) {
	employeeDomainReference = new DomainReference<Employee>(employee);
    }
    
    public static InfoEmployee newInfoFromDomain(final Employee employee) {
	return employee == null ? null : new InfoEmployee(employee);
    }
    
    private Employee getEmployee() {
	return employeeDomainReference == null ? null : employeeDomainReference.getObject();
    }
    
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
        return InfoUnit.newInfoFromDomain(getEmployee().getCurrentContract().getWorkingUnit());
    }

    public InfoUnit getMailingUnit() {
	return InfoUnit.newInfoFromDomain(getEmployee().getCurrentContract().getMailingUnit());
    }

    public InfoPerson getPerson() {
	return InfoPerson.newInfoFromDomain(getEmployee().getPerson());
    }

    public String toString() {
        return getEmployee().toString();
    }

}
