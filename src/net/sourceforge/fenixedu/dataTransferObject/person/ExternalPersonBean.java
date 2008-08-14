package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class ExternalPersonBean extends PersonBean {

    private DomainReference<Unit> unitDomainReference;

    private DomainReference<UnitName> unitNameDomainReference;

    private DomainReference<PersonName> personName;

    private String unitName;

    public ExternalPersonBean() {
	super();
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public Unit getUnit() {
	return unitDomainReference == null ? null : unitDomainReference.getObject();
    }

    public void setUnit(Unit unit) {
	this.unitDomainReference = unit != null ? new DomainReference<Unit>(unit) : null;
    }

    public UnitName getUnitNameDomainReference() {
	return unitNameDomainReference == null ? null : unitNameDomainReference.getObject();
    }

    public void setUnitNameDomainReference(UnitName unitName) {
	if (unitName != null) {
	    this.unitNameDomainReference = new DomainReference<UnitName>(unitName);
	    setUnit(unitName.getUnit());
	}
    }

    public PersonName getPersonName() {
	return personName != null ? personName.getObject() : null;
    }

    public void setPersonName(PersonName personName) {
	if (personName != null) {
	    this.personName = new DomainReference<PersonName>(personName);
	    setPerson(personName.getPerson());
	}
    }
}
