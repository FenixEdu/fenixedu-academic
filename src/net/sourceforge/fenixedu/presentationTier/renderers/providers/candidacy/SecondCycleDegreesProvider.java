package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SecondCycleDegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
    }

    public Converter getConverter() {
	return null;
    }

}
