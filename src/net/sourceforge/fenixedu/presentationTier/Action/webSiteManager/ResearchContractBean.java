package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.PersonName;

import org.joda.time.YearMonthDay;

public class ResearchContractBean implements Serializable {

	private Boolean externalPerson;
	
	private DomainReference<PersonName> personName;

	private String personNameString;
	
	private YearMonthDay begin;

	private YearMonthDay end;

	private DomainReference<Unit> unit;

	private ResearchFunctionType functionType;

	public ResearchContractBean() {
		setPersonName(null);
		setUnit(null);
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

	public ResearchFunctionType getFunctionType() {
		return functionType;
	}

	public void setFunctionType(ResearchFunctionType functionType) {
		this.functionType = functionType;
	}

	public Person getPerson() {
		PersonName personName = getPersonName();
		return personName != null ? personName.getPerson() : null;	
	}

	public PersonName getPersonName() {
		return personName.getObject();
	}
	
	public void setPersonName(PersonName personName) {
		this.personName = new DomainReference<PersonName>(personName);
	}

	public Unit getUnit() {
		return unit.getObject();
	}

	public void setUnit(Unit unit) {
		this.unit = new DomainReference<Unit>(unit);
	}

	public String getPersonNameString() {
		return personNameString;
	}

	public void setPersonNameString(String personName) {
		this.personNameString = personName;
	}

	public Boolean getExternalPerson() {
		return externalPerson;
	}

	public void setExternalPerson(Boolean externalPerson) {
		this.externalPerson = externalPerson;
	}

}
