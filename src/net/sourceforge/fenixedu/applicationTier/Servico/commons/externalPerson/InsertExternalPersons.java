package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPersonEditor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class InsertExternalPersons extends Service {

    public List<ExternalContract> run(List<InfoExternalPersonEditor> infoExternalPersons)
	    throws FenixServiceException {

	final List<ExternalContract> externalPersons = new ArrayList<ExternalContract>();

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
	    Person externalPerson = Person.createExternalPerson(infoExternalPerson.getName(),
		    Gender.MALE, null, null, null, null, null, null, null, null, null, null, null,
		    String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);
	    
	    ExternalContract contract = new ExternalContract(externalPerson, currentInstitution, new YearMonthDay(), null);
	    externalPersons.add(contract);
	}

	return externalPersons;
    }

}
