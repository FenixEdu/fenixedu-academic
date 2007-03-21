package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class UnitSearchBean implements Serializable {

    private DomainReference<Unit> unit;

    private DomainReference<UnitName> unitName;

    private String name;

    public UnitSearchBean() {
    }

    public UnitSearchBean(Unit unit) {
        setUnit(unit);
        setName(unit.getName());
    }

    public UnitSearchBean(UnitName unitName) {
        setUnitName(unitName);
        setName(unitName.getName());
    }

    public Unit getUnit() {
        return unit != null ? unit.getObject() : null;
    }

    public void setUnit(Unit unit) {
        this.unit = (unit != null) ? new DomainReference<Unit>(unit) : null;
    }

    public UnitName getUnitName() {
        return unitName != null ? unitName.getObject() : null;
    }

    public void setUnitName(UnitName unitName) {
        this.unit = (unitName != null) ? new DomainReference<Unit>(unitName.getUnit()) : null;
        this.unitName = (unitName != null) ? new DomainReference<UnitName>(unitName) : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
