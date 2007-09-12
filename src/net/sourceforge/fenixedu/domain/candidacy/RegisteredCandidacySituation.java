package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

public class RegisteredCandidacySituation extends RegisteredCandidacySituation_Base {

    public RegisteredCandidacySituation(Candidacy candidacy) {
	this(candidacy, (AccessControl.getUserView() != null) ? AccessControl.getPerson()
		: null);
    }

    public RegisteredCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);
	registerCandidacy(candidacy);
    }

//    private void registerCandidacy(Candidacy candidacy) {
//	Person person = candidacy.getPerson();
//	Student student = person.getStudent();
//	if (student == null) {
//	    student = new Student(person);
//	}
//
//	// create registration
//	
//	
//	Registration registration = createNewRegistration((DFACandidacy) candidacy);
//
//	
//
//	((DFACandidacy) getCandidacy()).setRegistration(registration);
//
//	final AdministrativeOffice administrativeOffice = AdministrativeOffice
//		.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
//
//	new DfaGratuityEvent(administrativeOffice, person, registration.getActiveStudentCurricularPlan(),
//		ExecutionYear.readCurrentExecutionYear());
//
//	new DfaRegistrationEvent(administrativeOffice, person, registration);
//
//    }

    private void registerCandidacy(Candidacy candidacy) {
	Person person = candidacy.getPerson();
	Registration registration = null;
	
	if(candidacy instanceof DFACandidacy) {
	    DFACandidacy dfaCandidacy = ((DFACandidacy)candidacy);
	    registration = new Registration(person, dfaCandidacy.getExecutionDegree().getDegreeCurricularPlan());
	    dfaCandidacy.setRegistration(registration);
	    createQualification();
	}
	else if(candidacy instanceof PHDProgramCandidacy) {
	    PHDProgramCandidacy programCandidacy = (PHDProgramCandidacy)candidacy;
	    registration = new Registration(person, (programCandidacy).getExecutionDegree().getDegreeCurricularPlan());
	    programCandidacy.setRegistration(registration);
	}
	
	if(!person.hasStudent()) {
	    new Student(person);
	}
	person.addPersonRoles(Role.getRoleByRoleType(RoleType.STUDENT));
    }

    
    private void createQualification() {
	DFACandidacy dfaCandidacy = (DFACandidacy) getCandidacy();
	if (dfaCandidacy.hasPrecedentDegreeInformation()) {
	    Qualification qualification = new Qualification();
	    qualification.setPerson(dfaCandidacy.getPerson());
	    qualification.setMark(dfaCandidacy.getPrecedentDegreeInformation().getConclusionGrade());
	    qualification.setSchool(dfaCandidacy.getPrecedentDegreeInformation().getInstitutionName());
	    qualification.setDegree(dfaCandidacy.getPrecedentDegreeInformation().getDegreeDesignation());
	    if (dfaCandidacy.getPrecedentDegreeInformation().getConclusionYear() != null) {
		qualification.setDateYearMonthDay(new YearMonthDay(dfaCandidacy
			.getPrecedentDegreeInformation().getConclusionYear(), 1, 1));
	    }
	    qualification.setCountry(dfaCandidacy.getPrecedentDegreeInformation().getCountry());
	}
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.REGISTERED;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }

}