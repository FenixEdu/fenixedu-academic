package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SecondCyclePublicIndividualCandidacyDegreesProvider implements DataProvider {

    private static final String DEGREE_TO_REMOVE_COMPLEX_TRANSPORT_INFRASTRUCTURE_SYSTEMS_ACRONYM = "MSCIT";
    private static final String DEGREE_TO_REMOVE_TERRITORY_ACRONYM = "MET";
    private static final String DEGREE_TO_REMOVE_PHARMACEUTICAL_ACRONYM = "MEFarm";
    private static final String DEGREE_TO_REMOVE_MPOT_ACRONYM = "MPOT";
    private static final String DEGREE_TO_REMOVE_ENV_ACRONYM = "MEAmb";
    private static final String DEGREE_EUSYSBIO = "EuSysBio";
    private static final String DEGREE_MASTER_STRUCTURE = "MMM";

    public static final DateTime CONSTRUCTION_ONLY_START_DATE = new DateTime(2011, 8, 16, 0, 0, 0, 0);

    public Object provide(Object source, Object currentValue) {

	if (CONSTRUCTION_ONLY_START_DATE.isBefore(new DateTime())) {
	    List<Degree> degrees = new ArrayList<Degree>();
	    degrees.add(Degree.readBySigla("MCR"));

	    return degrees;
	}

	List<Degree> degrees = new ArrayList<Degree>(Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE,
		DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));

	Degree degreeToRemoveComplexInfrastructure = Degree
		.readBySigla(DEGREE_TO_REMOVE_COMPLEX_TRANSPORT_INFRASTRUCTURE_SYSTEMS_ACRONYM);
	Degree degreeToRemoveTerritory = Degree.readBySigla(DEGREE_TO_REMOVE_TERRITORY_ACRONYM);
	Degree degreeToPharmaceutical = Degree.readBySigla(DEGREE_TO_REMOVE_PHARMACEUTICAL_ACRONYM);
	Degree degreeToRemoveMpot = Degree.readBySigla(DEGREE_TO_REMOVE_MPOT_ACRONYM);
	Degree degreeToRemoveEuSysBio = Degree.readBySigla(DEGREE_EUSYSBIO);

	degrees.remove(degreeToRemoveComplexInfrastructure);
	degrees.remove(degreeToRemoveTerritory);
	// degrees.remove(degreeToPharmaceutical);
	// degrees.remove(degreeToRemoveMpot);
	degrees.remove(Degree.readBySigla(DEGREE_TO_REMOVE_ENV_ACRONYM));
	degrees.remove(degreeToRemoveEuSysBio);
	degrees.remove(Degree.readBySigla(DEGREE_MASTER_STRUCTURE));

	return degrees;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
