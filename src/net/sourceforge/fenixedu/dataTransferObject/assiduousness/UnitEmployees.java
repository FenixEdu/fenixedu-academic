package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitEmployees implements Serializable {
    private DomainReference<Unit> unit;

    private DomainListReference<Employee> employeeList;

    private String unitCode;

    public UnitEmployees(Unit unit, PersonFunction personFunction) {
	setUnit(unit);
	setEmployeeList(new ArrayList<Employee>(unit.getAllWorkingEmployees(personFunction.getFunction()
		.getBeginDateYearMonthDay(), personFunction.getFunction().getEndDateYearMonthDay())));
    }

    public UnitEmployees(Unit unit, List<Employee> employeeList) {
	setUnit(unit);
	setEmployeeList(employeeList);
    }

    public UnitEmployees(Unit unit) {
	setUnit(unit);
    }

    public List<Employee> getEmployeeList() {
	return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
	this.employeeList = new DomainListReference<Employee>(employeeList);
    }

    public Unit getUnit() {
	return unit != null ? unit.getObject() : null;
    }

    public void setUnit(Unit unit) {
	this.unit = unit != null ? new DomainReference<Unit>(unit) : null;
	if (unit.getCostCenterCode() != null) {
	    setUnitCode(unit.getCostCenterCode().toString());
	} else {
	    setUnitCode("");
	}
    }

    public String getUnitCode() {
	return unitCode;
    }

    public void setUnitCode(String unitCode) {
	this.unitCode = unitCode;
    }
}
