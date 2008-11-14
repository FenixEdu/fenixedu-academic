package net.sourceforge.fenixedu.presentationTier.renderers.providers.internship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.SchoolUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcademicUnitsForInternshipsProvider implements DataProvider {
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	RootDomainObject root = RootDomainObject.getInstance();
	List<AcademicalInstitutionUnit> units = new ArrayList<AcademicalInstitutionUnit>();
	// FCT UC
	units.add((SchoolUnit) root.getEarthUnit().readUnitByAcronymAndType("FCT", PartyTypeEnum.SCHOOL));
	// FCT UNL
	units.add((SchoolUnit) root.readPartyByOID(422210));
	// FC UL
	units.add((SchoolUnit) root.readPartyByOID(422182));
	// FE UP
	units.add((SchoolUnit) root.readPartyByOID(182553));
	// ISCTE
	units.add((UniversityUnit) root.readPartyByOID(180556));
	// UV
	units.add((UniversityUnit) root.readPartyByOID(196758));
	// UBI
	units.add((UniversityUnit) root.readPartyByOID(113122));
	// FA UTL
	units.add((SchoolUnit) root.readPartyByOID(422216));
	// FMV UTL
	units.add((SchoolUnit) root.readPartyByOID(422217));
	// FMH UTL
	units.add((SchoolUnit) root.readPartyByOID(422218));
	// ISA UTL
	units.add((SchoolUnit) root.readPartyByOID(183559));
	// ISCSP UTL
	units.add((SchoolUnit) root.readPartyByOID(422219));
	// IST UTL
	units.add((SchoolUnit) root.readPartyByOID(422221));
	return units;
    }
}
