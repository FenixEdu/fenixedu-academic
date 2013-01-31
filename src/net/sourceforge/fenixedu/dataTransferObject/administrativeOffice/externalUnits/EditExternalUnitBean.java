package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class EditExternalUnitBean extends CreateExternalUnitBean {

	private Unit externalUnit;

	public EditExternalUnitBean(final Unit unit) {
		super();
		setParentUnit(unit.isEarth() ? null : unit.getParentUnits().iterator().next());
		setUnitType(unit.getPartyType().getType());
		setUnitName(unit.getName());
		setUnitCode(unit.getAcronym());
		setExternalUnit(unit);
	}

	public Unit getExternalUnit() {
		return this.externalUnit;
	}

	public void setExternalUnit(Unit externalUnit) {
		this.externalUnit = externalUnit;
	}
}
