package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SecondCyclePublicIndividualCandidacyDegreesProvider implements DataProvider {
    
    private static final String DEGREE_TO_REMOVE_COMPLEX_TRANSPORT_INFRASTRUCTURE_SYSTEMS_ACRONYM = "MSCIT";
    private static final String DEGREE_TO_REMOVE_TERRITORY_ACRONYM = "MET";
    
    public Object provide(Object source, Object currentValue) {
	List<Degree> degrees = new ArrayList<Degree>(Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
	
	Degree degreeToRemove = Degree.readBySigla(DEGREE_TO_REMOVE_COMPLEX_TRANSPORT_INFRASTRUCTURE_SYSTEMS_ACRONYM);
	Degree degreeToRemoveTerritory = Degree.readBySigla(DEGREE_TO_REMOVE_TERRITORY_ACRONYM);
	degrees.remove(degreeToRemove);
	degrees.remove(degreeToRemoveTerritory);
	
	return degrees;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
