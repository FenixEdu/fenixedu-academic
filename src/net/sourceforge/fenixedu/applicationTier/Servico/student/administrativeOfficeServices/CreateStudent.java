/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.OriginInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.candidacy.RegisteredCandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * 
 */
public class CreateStudent extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static Registration run(PersonBean personBean, ExecutionDegreeBean executionDegreeBean,
	    PrecedentDegreeInformationBean precedentDegreeInformationBean, IngressionInformationBean ingressionInformationBean,
	    OriginInformationBean originInformationBean) {

	// get or update person
	Person person = getPerson(personBean);

	// create candidacy
	StudentCandidacy studentCandidacy = StudentCandidacy.createStudentCandidacy(executionDegreeBean.getExecutionDegree(),
		person);
	studentCandidacy.fillOriginInformation(originInformationBean, personBean);
	new RegisteredCandidacySituation(studentCandidacy, ingressionInformationBean.getRegistrationAgreement(),
		executionDegreeBean.getCycleType(), ingressionInformationBean.getIngression(), ingressionInformationBean
			.getEntryPhase(), personBean.getStudentNumber());

	// edit precedent degree information
	studentCandidacy.getPrecedentDegreeInformation().edit(precedentDegreeInformationBean);

	// create registration
	Registration registration = studentCandidacy.getRegistration();
	if (registration == null) {
	    /*
	     * 26/08/2009 - Due to curriculum validation we must support
	     * creation of students, if necessary, with a custom student number
	     * (for students that are not in the system).
	     */
	    // registration = new Registration(person,
	    // executionDegreeBean.getDegreeCurricularPlan(), studentCandidacy,
	    // ingressionInformationBean.getRegistrationAgreement(),
	    // executionDegreeBean.getCycleType(), executionDegreeBean
	    // .getExecutionYear());
	    registration = Registration.createRegistrationWithCustomStudentNumber(person, executionDegreeBean
		    .getDegreeCurricularPlan(), studentCandidacy, ingressionInformationBean.getRegistrationAgreement(),
		    executionDegreeBean.getCycleType(), executionDegreeBean.getExecutionYear(), personBean.getStudentNumber());
	}
	registration.setHomologationDate(ingressionInformationBean.getHomologationDate());
	registration.setStudiesStartDate(ingressionInformationBean.getStudiesStartDate());

	// create qualification
	new Qualification(person, studentCandidacy.getPrecedentDegreeInformation());

	// add roles
//	person.addPersonRoleByRoleType(RoleType.STUDENT);
//	person.addPersonRoleByRoleType(RoleType.PERSON);

	return registration;
    }

    private static Person getPerson(PersonBean personBean) {
	Person person = null;
	if (personBean.getPerson() != null) {
	    person = personBean.getPerson();
	    person.edit(personBean);
	} else {
	    // create person
	    person = new Person(personBean);
	}
	return person;
    }

}