package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPersonEditor;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.Gender;

public class InsertExternalPersons extends Service {

    public List<ExternalPerson> run(List<InfoExternalPersonEditor> infoExternalPersons)
	    throws FenixServiceException {

	final List<ExternalPerson> externalPersons = new ArrayList<ExternalPerson>();

	List<Unit> institutions = Unit.readAllUnits();

	for (final InfoExternalPersonEditor infoExternalPerson : infoExternalPersons) {

	    // retrieving existing work location
	    Unit currentInstitution = UnitUtils.readExternalInstitutionUnitByName(infoExternalPerson
		    .getInfoInstitution().getName());

	    // creating a new one if it doesn't already exist
	    if (currentInstitution == null) {
		currentInstitution = new InsertInstitution().run(infoExternalPerson.getInfoInstitution().getName());
		institutions.add(currentInstitution);
	    }

	    // creating a new ExternalPerson
	    ExternalPerson externalPerson = new ExternalPerson(infoExternalPerson.getName(),
		    Gender.MALE, null, null, null, null, null, null, null, null, null, null, null,
		    String.valueOf(System.currentTimeMillis()), currentInstitution);

	    externalPersons.add(externalPerson);
	}

	return externalPersons;
    }

}
