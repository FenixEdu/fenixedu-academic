package net.sourceforge.fenixedu.presentationTier.renderers.providers.alumni;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AlumniFormationParentInstitutionProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	AlumniFormation formation = (AlumniFormation) source;
	List<AcademicalInstitutionUnit> unitsByType = AcademicalInstitutionUnit.readOfficialParentUnitsByType(formation
		.getInstitutionType());
	return unitsByType;
    }

    public Converter getConverter() {
	return null;
    }

}
