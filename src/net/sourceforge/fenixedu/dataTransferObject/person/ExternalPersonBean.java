package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class ExternalPersonBean extends PersonBean {

    private DomainReference<Unit> unitDomainReference;

    private DomainReference<UnitName> unitNameDomainReference;

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
        if (unit != null) {
            unitDomainReference = new DomainReference<Unit>(unit);
        }
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
}
