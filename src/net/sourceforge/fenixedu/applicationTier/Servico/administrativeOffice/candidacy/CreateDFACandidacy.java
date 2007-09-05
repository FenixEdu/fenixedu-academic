package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.YearMonthDay;

public class CreateDFACandidacy extends Service {
    
    public DFACandidacy run(ExecutionDegree executionDegree, String name,
	    String identificationDocumentNumber, IDDocumentType identificationDocumentType,
	    String contributorNumber, YearMonthDay startDate) {
	Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber,
		identificationDocumentType);
	if (person == null) {
	    person = new Person(name, identificationDocumentNumber, identificationDocumentType,
		    Gender.MALE);
	}

	person.setSocialSecurityNumber(contributorNumber);
	person.addPersonRoleByRoleType(RoleType.CANDIDATE);
	person.addPersonRoleByRoleType(RoleType.PERSON);

	return new DFACandidacy(person, executionDegree, startDate);

    }
    
}
