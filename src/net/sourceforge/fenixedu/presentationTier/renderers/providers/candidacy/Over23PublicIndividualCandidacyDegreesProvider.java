package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class Over23PublicIndividualCandidacyDegreesProvider implements DataProvider {

    private static final String DEGREE_TO_REMOVE_FIRST_CYCLE_CHEMISTRY = "LQ";
    private static final String DEGREE_TO_REMOVE_TERRITORY = "LET";
    private static final String DEGREE_TO_REMOVE_FIRST_CYCLE_ENVIRONMENT = "LEAmb";

    public Object provide(Object source, Object currentValue) {
	List<Degree> degrees = new ArrayList<Degree>(Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE,
		DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));

	Degree degreeToRemoveChemistry = Degree.readBySigla(DEGREE_TO_REMOVE_FIRST_CYCLE_CHEMISTRY);
	Degree degreeToRemoveTerritory = Degree.readBySigla(DEGREE_TO_REMOVE_TERRITORY);
	degrees.remove(degreeToRemoveChemistry);
	degrees.remove(degreeToRemoveTerritory);
	degrees.remove(Degree.readBySigla(DEGREE_TO_REMOVE_FIRST_CYCLE_ENVIRONMENT));
	return degrees;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
