package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class SectionUnit extends SectionUnit_Base {

    private SectionUnit() {
	super();
	super.setType(PartyTypeEnum.SECTION);
    }

    public static Unit createNewSectionUnit(MultiLanguageString name, Integer costCenterCode, String acronym,
	    YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType,
	    String webAddress, UnitClassification classification, Boolean canBeResponsibleOfSpaces, Campus campus) {

	SectionUnit sectionUnit = new SectionUnit();
	sectionUnit.init(name, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces,
		campus);
	sectionUnit.addParentUnit(parentUnit, accountabilityType);

	return sectionUnit;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
	throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public boolean isSectionUnit() {
	return true;
    }
}
