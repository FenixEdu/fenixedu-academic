package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class CreateDFACandidacy extends Service {
    public DFACandidacy run(ExecutionDegree executionDegree, String name,
	    String identificationDocumentNumber, IDDocumentType identificationDocumentType,
	    String contributorNumber) {
	Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber,
		identificationDocumentType);
	if (person == null) {
	    person = new Person(name, identificationDocumentNumber, identificationDocumentType,
		    Gender.MALE);
	}

	person.setSocialSecurityNumber(contributorNumber);
	person.addPersonRoleByRoleType(RoleType.CANDIDATE);
	person.addPersonRoleByRoleType(RoleType.PERSON);

	final DFACandidacy candidacy = new DFACandidacy(person, executionDegree);

	final AdministrativeOffice administrativeOffice = AdministrativeOffice
		.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
	new DFACandidacyEvent(administrativeOffice, person, candidacy);

	new DegreeCurricularPlanServiceAgreement(person, executionDegree.getDegreeCurricularPlan()
		.getServiceAgreementTemplate());

	return candidacy;
    }
}
