package net.sourceforge.fenixedu.presentationTier.renderers.providers.alumni;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.QualificationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AlumniFormationDegreeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	AlumniFormation formation = (AlumniFormation) source;
	return QualificationType.getbyFormationType(formation.getFormationType());
    }

    public Converter getConverter() {
	return null;
    }

}
