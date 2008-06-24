package net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class AssiduousnessStructureSearch implements Serializable {

    private String responsibleName;

    private DomainReference<Person> responsible;

    private Boolean searchPerson;

    private DomainReference<Employee> employee;

    private String employeeName;

    private DomainReference<UnitName> unit;

    private String unitName;

    private DomainListReference<PersonFunction> personFunctionList;

    public AssiduousnessStructureSearch(AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory) {
	setResponsible(assiduousnessPersonFunctionFactory.getResponsible());
	if (assiduousnessPersonFunctionFactory.getParty() instanceof Person) {
	    searchPerson = true;
	    setEmployee(((Person) assiduousnessPersonFunctionFactory.getParty()).getEmployee());
	} else {
	    searchPerson = false;
	    setUnit(((Unit) assiduousnessPersonFunctionFactory.getParty()).getUnitName());
	}
	setSearch();
    }

    public AssiduousnessStructureSearch(PersonFunction personFuntion) {
	setResponsible(personFuntion.getPerson());
	setSearch();
    }

    public AssiduousnessStructureSearch() {
    }

    public Employee getEmployee() {
	return employee != null ? employee.getObject() : null;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee != null ? new DomainReference<Employee>(employee) : null;
    }

    public Person getResponsible() {
	return responsible != null ? responsible.getObject() : null;
    }

    public void setResponsible(Person responsible) {
	this.responsible = responsible != null ? new DomainReference<Person>(responsible) : null;
    }

    public Boolean getSearchPerson() {
	return searchPerson;
    }

    public void setSearchPerson(Boolean searchPerson) {
	this.searchPerson = searchPerson;
    }

    public UnitName getUnit() {
	return unit != null ? unit.getObject() : null;
    }

    public void setUnit(UnitName unit) {
	this.unit = unit != null ? new DomainReference<UnitName>(unit) : null;
    }

    public String getResponsibleName() {
	return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
	this.responsibleName = responsibleName;
    }

    public String getEmployeeName() {
	return employeeName;
    }

    public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public List<PersonFunction> getPersonFunctionList() {
	if (this.personFunctionList == null) {
	    this.personFunctionList = new DomainListReference<PersonFunction>();
	}
	return personFunctionList;
    }

    public void setPersonFunctionList(DomainListReference<PersonFunction> personFunctionList) {
	this.personFunctionList = new DomainListReference<PersonFunction>(personFunctionList);
    }

    public boolean getHasEmployeeInSearch() {
	return ((getSearchPerson() != null && getSearchPerson()) && getEmployee() != null) ? true : false;
    }

    public boolean getHasUnitInSearch() {
	return ((getSearchPerson() != null && !getSearchPerson()) && getUnit() != null) ? true : false;
    }

    public boolean getAnyActivePersonFunction() {
	for (PersonFunction personFunction : getPersonFunctionList()) {
	    if (!personFunction.isFinished()) {
		return true;
	    }
	}
	return false;
    }

    public void setSearch() {
	getPersonFunctionList().clear();
	List<PersonFunction> personFunctionList = new ArrayList<PersonFunction>();

	if (getResponsible() != null) {
	    List<PersonFunction> allPersonFunctionList = new ArrayList<PersonFunction>(getResponsible().getPersonFunctions(
		    AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE));
	    if ((getSearchPerson() != null && getSearchPerson()) && getEmployee() != null) {
		personFunctionList.addAll(getResponsible().getPersonFunctions(getEmployee().getPerson(), true, true, false,
			AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE));
		if (getEmployee().getCurrentWorkingPlace() != null) {
		    personFunctionList.addAll(getPersonFunctions(allPersonFunctionList, getEmployee().getCurrentWorkingPlace()));
		}
	    } else if (((getSearchPerson() != null && !getSearchPerson()) || getSearchPerson() == null) && getUnit() != null) {
		personFunctionList.addAll(getPersonFunctions(allPersonFunctionList, getUnit().getUnit()));
	    } else {
		personFunctionList.addAll(allPersonFunctionList);
	    }
	} else {
	    if ((getSearchPerson() != null && getSearchPerson()) && getEmployee() != null) {
		personFunctionList.addAll(getPersonFunctions(getAssiduousnessPersonFunctions(), getEmployee().getPerson()));
		if (getEmployee().getCurrentWorkingPlace() != null) {
		    personFunctionList.addAll(getPersonFunctions(getAssiduousnessPersonFunctions(), getEmployee()
			    .getCurrentWorkingPlace()));
		}
	    } else if (((getSearchPerson() != null && !getSearchPerson()) || getSearchPerson() == null) && getUnit() != null) {
		personFunctionList.addAll(getPersonFunctions(getAssiduousnessPersonFunctions(), getUnit().getUnit()));
	    }
	}

	for (PersonFunction personFunction : personFunctionList) {
	    // YearMonthDay begin = new YearMonthDay();
	    if (// (!personFunction.getEndDate().isBefore(begin)) &&
	    personFunction.getFunction().getFunctionType() == FunctionType.ASSIDUOUSNESS_RESPONSIBLE) {
		getPersonFunctionList().add(personFunction);
	    }
	}

    }

    public List<PersonFunction> getPersonFunctions(List<PersonFunction> allPersonFunctionList, Party party) {
	List<PersonFunction> result = new ArrayList<PersonFunction>();

	Collection<Unit> allParentUnits = Collections.emptyList();
	if (party instanceof Unit) {
	    allParentUnits = ((Unit) party).getAllParentUnits();
	}
	// YearMonthDay today = new YearMonthDay();

	for (PersonFunction personFunction : allPersonFunctionList) {
	    // if (!personFunction.getEndDate().isBefore(today)) {
	    // continue;
	    // }

	    Party functionParty = personFunction.getParentParty();
	    if (party == null || functionParty.equals(party) || (allParentUnits.contains(functionParty))) {
		result.add(personFunction);
	    }
	}

	return result;
    }

    public List<PersonFunction> getAssiduousnessPersonFunctions() {
	List<PersonFunction> result = new ArrayList<PersonFunction>();
	for (Accountability accountability : RootDomainObject.getInstance().getAccountabilitys()) {
	    if (accountability instanceof PersonFunction) {
		PersonFunction personFunction = (PersonFunction) accountability;
		if ((!personFunction.getAccountabilityType().getType().equals(AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE))
		// || (!personFunction.isActive(today))
			|| (personFunction.getFunction().isVirtual())) {
		    continue;
		}
		result.add(personFunction);
	    }
	}

	return result;
    }

    public Party getParty() {
	return getHasEmployeeInSearch() ? getEmployee().getPerson() : getUnit().getUnit();
    }
}