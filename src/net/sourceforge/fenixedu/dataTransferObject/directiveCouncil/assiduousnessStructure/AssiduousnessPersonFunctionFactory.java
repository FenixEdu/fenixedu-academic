package net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.apache.struts.action.ActionMessage;
import org.joda.time.YearMonthDay;

public class AssiduousnessPersonFunctionFactory implements Serializable, FactoryExecutor {

    private DomainReference<Person> responsible;

    private DomainReference<Party> party;

    private boolean byPersons = false;

    private DomainListReference<Employee> employeesList;

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    public AssiduousnessPersonFunctionFactory(AssiduousnessStructureSearch assiduousnessStructureSearch, boolean byPersons) {
	setResponsible(assiduousnessStructureSearch.getResponsible());
	setParty(assiduousnessStructureSearch.getParty());
	setByPersons(byPersons);
	YearMonthDay now = new YearMonthDay();
	YearMonthDay lastDay = new YearMonthDay(now.getYear(), 12, 31);
	setBeginDate(now);
	setEndDate(lastDay);
    }

    public boolean isByPersons() {
	return byPersons;
    }

    public void setByPersons(boolean byPersons) {
	this.byPersons = byPersons;
    }

    public Party getParty() {
	return party != null ? party.getObject() : null;
    }

    public boolean getIsPartyAnUnit() {
	return getParty() instanceof Unit;
    }

    public void setParty(Party party) {
	this.party = party != null ? new DomainReference<Party>(party) : null;
    }

    public Person getResponsible() {
	return responsible != null ? responsible.getObject() : null;
    }

    public void setResponsible(Person responsible) {
	this.responsible = responsible != null ? new DomainReference<Person>(responsible) : null;
    }

    public List<Employee> getEmployeesList() {
	if (this.employeesList == null) {
	    this.employeesList = new DomainListReference<Employee>();
	}
	return employeesList;
    }

    public void setEmployeesList(List<Employee> employeesList) {
	this.employeesList = new DomainListReference<Employee>(employeesList);
    }

    public YearMonthDay getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
	this.beginDate = beginDate;
    }

    public YearMonthDay getEndDate() {
	return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
	this.endDate = endDate;
    }

    public Object execute() {
	if (getBeginDate() == null) {
	    return new ActionMessage("error.requiredBeginDate");
	}
	if (getEndDate() == null) {
	    return new ActionMessage("error.requiredEndDate");
	}
	if (getBeginDate().isAfter(getEndDate())) {
	    return new ActionMessage("error.beginDateAfterEndDate");
	}
	if (getParty() instanceof Unit) {
	    Function function = getFunction((Unit) getParty(), getBeginDate());
	    if (getEmployeesList() != null && getEmployeesList().size() != 0) {
		for (Employee employee : getEmployeesList()) {
		    new PersonFunction(employee.getPerson(), getResponsible(), function, getBeginDate(), getEndDate());
		}
	    } else {
		new PersonFunction(getParty(), getResponsible(), function, getBeginDate(), getEndDate());
	    }

	} else if (getParty() instanceof Person) {
	    Function function = getFunction(((Person) getParty()).getEmployee().getCurrentWorkingPlace(), getBeginDate());
	    new PersonFunction((Person) getParty(), getResponsible(), function, getBeginDate(), getEndDate());
	}
	return null;
    }

    private Function getFunction(Unit unit, YearMonthDay begin) {
	for (AccountabilityType accountabilityType : RootDomainObject.getInstance().getAccountabilityTypes()) {
	    if (accountabilityType instanceof Function) {
		Function function = ((Function) accountabilityType);
		if (function.getUnit().equals(unit) && function.getFunctionType() != null
			&& function.getFunctionType().equals(FunctionType.ASSIDUOUSNESS_RESPONSIBLE)) {
		    return function;
		}
	    }
	}
	return new Function(new MultiLanguageString(Language.pt, "Responsável pela Assiduidade"), begin, null,
		FunctionType.ASSIDUOUSNESS_RESPONSIBLE, unit, AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE);
    }

}