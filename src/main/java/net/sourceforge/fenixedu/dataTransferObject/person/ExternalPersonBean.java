package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class ExternalPersonBean extends PersonBean {

    private Unit unitDomainReference;

    private UnitName unitNameDomainReference;

    private PersonName personName;

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
        return unitDomainReference;
    }

    public void setUnit(Unit unit) {
        this.unitDomainReference = unit;
    }

    public UnitName getUnitNameDomainReference() {
        return unitNameDomainReference;
    }

    public void setUnitNameDomainReference(UnitName unitName) {
        if (unitName != null) {
            this.unitNameDomainReference = unitName;
            setUnit(unitName.getUnit());
        }
    }

    public PersonName getPersonName() {
        return personName;
    }

    public void setPersonName(PersonName personName) {
        if (personName != null) {
            this.personName = personName;
            setPerson(personName.getPerson());
        }
    }
}
