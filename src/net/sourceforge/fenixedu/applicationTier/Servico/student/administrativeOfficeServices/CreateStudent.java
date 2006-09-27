/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.domain.candidacy.RegisteredCandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class CreateStudent extends Service {

    public Registration run(PersonBean personBean, ExecutionDegreeBean executionDegreeBean,
	    PrecedentDegreeInformationBean precedentDegreeInformationBean,
	    IngressionInformationBean ingressionInformationBean) {

	// get or update person 
	Person person = getPerson(personBean);
	
	// create candidacy
	StudentCandidacy studentCandidacy = StudentCandidacy.createStudentCandidacy(executionDegreeBean
		.getExecutionDegree(), person);
	new RegisteredCandidacySituation(studentCandidacy);

	//set ingression information
	studentCandidacy.setIngression(ingressionInformationBean.getIngression().getName());
	studentCandidacy.setEntryPhase(ingressionInformationBean.getEntryPhase());
	
	// edit precedent degree information
	studentCandidacy.getPrecedentDegreeInformation().edit(precedentDegreeInformationBean);

	// create registration
	Registration registration = new Registration(person, StudentKind.readByStudentType(StudentType.NORMAL), executionDegreeBean
		.getDegreeCurricularPlan(), studentCandidacy);

	// create qualification
	new Qualification(person, studentCandidacy.getPrecedentDegreeInformation());

	// add roles
	person.addPersonRoleByRoleType(RoleType.STUDENT);
	person.addPersonRoleByRoleType(RoleType.PERSON);

	return registration;
    }

    private Person getPerson(PersonBean personBean) {
	Person person = null;
	if(personBean.getPerson() != null){
	    person = personBean.getPerson();
	    person.setProperties(personBean);
	}else{
	    // create person
	    person = new Person(personBean);	    
	}
	return person;
    }

}
