package net.sourceforge.fenixedu.presentationTier.renderers.providers.internship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.SchoolUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.pstm.Transaction;

public class AcademicUnitsForInternshipsProvider implements DataProvider {
    public abstract class AcademicInstitution<UnitType extends AcademicalInstitutionUnit> {
	UnitType unit;

	public AcademicInstitution(UnitType unit) {
	    this.unit = unit;
	}

	public abstract String getPresentationName();

	public UnitType getUnit() {
	    return unit;
	}
    }

    public class SchoolInstitution extends AcademicInstitution<SchoolUnit> {
	public SchoolInstitution(SchoolUnit school) {
	    super(school);
	}

	@Override
	public String getPresentationName() {
	    SchoolUnit school = getUnit();
	    StringBuilder output = new StringBuilder();
	    output.append(school.getName().trim());
	    output.append(" da ");
	    List<Unit> parents = school.getParentUnitsPath();
	    output.append(parents.get(parents.size() - 1).getName());
	    return output.toString();
	}
    }

    public class UniversityInstitution extends AcademicInstitution<UniversityUnit> {
	public UniversityInstitution(UniversityUnit university) {
	    super(university);
	}

	@Override
	public String getPresentationName() {
	    return getUnit().getName();
	}
    }

    @Override
    public Converter getConverter() {
	return new BiDirectionalConverter() {
	    @Override
	    public Object convert(Class type, Object value) {
		if (value == null || value.equals("")) {
		    return null;
		}
		try {
		    final long oid = Long.parseLong((String) value);
		    return Transaction.getObjectForOID(oid);
		} catch (NumberFormatException e) {
		    throw new ConversionException("invalid oid in key: " + value, e);
		}
	    }

	    @Override
	    public String deserialize(Object object) {
		return object == null ? "" : Long.toString(((AcademicInstitution) object).getUnit().getOID());
	    }
	};
    }

    @Override
    public synchronized Object provide(Object source, Object currentValue) {
	RootDomainObject root = RootDomainObject.getInstance();
	List<AcademicInstitution> units = new ArrayList<AcademicInstitution>();
	// FCT UC
	units.add(new SchoolInstitution((SchoolUnit) root.getEarthUnit().readUnitByAcronymAndType("FCT", PartyTypeEnum.SCHOOL)));
	// FCT UNL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(422210)));
	// FC UL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(422182)));
	// FE UP
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(182553)));
	// ISCTE
	units.add(new UniversityInstitution((UniversityUnit) root.readPartyByOID(180556)));
	// UV
	units.add(new UniversityInstitution((UniversityUnit) root.readPartyByOID(196758)));
	// UBI
	units.add(new UniversityInstitution((UniversityUnit) root.readPartyByOID(113122)));
	// FA UTL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(422216)));
	// FMV UTL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(422217)));
	// FMH UTL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(422218)));
	// ISA UTL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(183559)));
	// ISCSP UTL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(422219)));
	// IST UTL
	units.add(new SchoolInstitution((SchoolUnit) root.readPartyByOID(422221)));
	return units;
    }
}
