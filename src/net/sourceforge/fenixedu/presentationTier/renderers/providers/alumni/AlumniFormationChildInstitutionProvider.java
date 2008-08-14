package net.sourceforge.fenixedu.presentationTier.renderers.providers.alumni;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AlumniFormationChildInstitutionProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	AlumniFormation formation = (AlumniFormation) source;
	return AcademicalInstitutionUnit.readOfficialChildUnits(formation.getParentInstitution());
    }

    public Converter getConverter() {
	return null;
    }

}
