package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ErasmusIndividualCandidacyDegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<Degree> degrees = Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE,
		DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);

	degrees.remove(Degree.readBySigla("MSCIT"));

	return degrees;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
