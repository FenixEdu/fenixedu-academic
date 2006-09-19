package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentState;

import org.joda.time.YearMonthDay;

public class RegisteredCandidacySituation extends RegisteredCandidacySituation_Base {

    public RegisteredCandidacySituation(Candidacy candidacy) {
	this(candidacy, (AccessControl.getUserView() != null) ? AccessControl.getUserView().getPerson()
		: null);
    }

    public RegisteredCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);

	if (getCandidacy() instanceof DFACandidacy) {
	    registerDFACandidacy(candidacy);
	}
    }

    private void registerDFACandidacy(Candidacy candidacy) {
	Person person = candidacy.getPerson();
	Student student = person.getStudent();
	if (student == null) {
	    student = new Student(person);
	}

	// create registration
	Registration registration = createNewRegistration();

	// create scp
	StudentCurricularPlan.createBolonhaStudentCurricularPlan(registration,
		((DFACandidacy) candidacy).getExecutionDegree().getDegreeCurricularPlan(),
		StudentCurricularPlanState.ACTIVE, new YearMonthDay(), ExecutionPeriod
			.readActualExecutionPeriod());

	createQualification();

	((DFACandidacy) getCandidacy()).setRegistration(registration);

    }

    private Registration createNewRegistration() {

	StudentKind studentKind = StudentKind.readByStudentType(StudentType.NORMAL);
	StudentState state = new StudentState(StudentState.INSCRITO);
	Person person = getCandidacy().getPerson();
	Registration registration = new Registration(person, null, studentKind, state, false, false,
		EntryPhase.FIRST_PHASE_OBJ, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
	registration.setInterruptedStudies(false);

	person.getStudent().addRegistrations(registration);

	person.addPersonRoles(Role.getRoleByRoleType(RoleType.STUDENT));

	return registration;
    }

    private void createQualification() {
	DFACandidacy dfaCandidacy = (DFACandidacy) getCandidacy();
	Qualification qualification = new Qualification();
	qualification.setPerson(dfaCandidacy.getPerson());
	qualification.setMark(dfaCandidacy.getPrecedentDegreeInformation().getConclusionGrade());
	qualification.setSchool(dfaCandidacy.getPrecedentDegreeInformation().getInstitution().getName());
	qualification.setDegree(dfaCandidacy.getPrecedentDegreeInformation().getDegreeDesignation());
	qualification.setDateYearMonthDay(new YearMonthDay(dfaCandidacy.getPrecedentDegreeInformation()
		.getConclusionYear(), 1, 1));
	qualification.setCountry(dfaCandidacy.getPrecedentDegreeInformation().getCountry());
    }

    @Override
    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.REGISTERED;
    }

    @Override
    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    @Override
    public void nextState() {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }

}
