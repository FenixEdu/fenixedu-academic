package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateExternalUnitBean implements Serializable {
    
    private DomainReference<Unit> parentUnit;
    private PartyTypeEnum unitType;
    private String unitName;
    private String unitCode;
    
    public CreateExternalUnitBean(final Unit parentUnit, PartyTypeEnum unitType) {
	setParentUnit(parentUnit);
	setUnitType(unitType);
    }
    
    public Unit getParentUnit() {
	return (this.parentUnit != null) ? this.parentUnit.getObject() : null;
    }

    public void setParentUnit(Unit parentUnit) {
	this.parentUnit = (parentUnit != null) ? new DomainReference<Unit>(parentUnit) : null;
    }
    
    public PartyTypeEnum getUnitType() {
        return unitType;
    }

    public void setUnitType(PartyTypeEnum unitType) {
        this.unitType = unitType;
    }

    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
