package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.ManagementCouncilSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ManagementCouncilUnit extends ManagementCouncilUnit_Base {

	private ManagementCouncilUnit() {
		super();
		super.setType(PartyTypeEnum.MANAGEMENT_COUNCIL);
		new ManagementCouncilSite(this);
	}

	public static ManagementCouncilUnit createManagementCouncilUnit(MultiLanguageString name, String unitNameCard,
			Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
			AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
			Boolean canBeResponsibleOfSpaces, Campus campus) {

		if (!PartyType.getPartiesSet(PartyTypeEnum.MANAGEMENT_COUNCIL).isEmpty()) {
			throw new DomainException("error.can.only.exist.one.managementCouncilUnit");
		}
		ManagementCouncilUnit unit = new ManagementCouncilUnit();
		unit.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
				canBeResponsibleOfSpaces, campus);
		unit.addParentUnit(parentUnit, accountabilityType);

		return unit;
	}

	@Override
	public void setAcronym(String acronym) {
		if (StringUtils.isEmpty(acronym)) {
			throw new DomainException("acronym.cannot.be.null");
		}
		super.setAcronym(acronym);
	}

	@Override
	public void setType(PartyTypeEnum partyTypeEnum) {
		throw new DomainException("unit.impossible.set.type");
	}
}
