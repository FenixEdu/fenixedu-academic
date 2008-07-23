package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeTransferIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.LocalDate;

public class DegreeTransferIndividualCandidacy extends DegreeTransferIndividualCandidacy_Base {

    private DegreeTransferIndividualCandidacy() {
	super();
    }

    DegreeTransferIndividualCandidacy(final DegreeTransferIndividualCandidacyProcess process,
	    final DegreeTransferIndividualCandidacyProcessBean bean) {
	this();

	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	init(person, process, bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	createPrecedentDegreeInformation(bean);
	createDebt(person);
    }

    private void checkParameters(final Person person, final DegreeTransferIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (person.hasValidDegreeTransferIndividualCandidacy(process.getCandidacyExecutionInterval())) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.person.already.has.candidacy", process
		    .getCandidacyExecutionInterval().getName());
	}

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(person, selectedDegree)) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.existing.degree", selectedDegree.getName());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    @Override
    protected void createInstitutionPrecedentDegreeInformation(final StudentCurricularPlan studentCurricularPlan) {
	final Registration registration = studentCurricularPlan.getRegistration();
	if (registration.isConcluded() || registration.isRegistrationConclusionProcessed()) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.studentCurricularPlan.cannot.be.concluded");
	}
	super.createInstitutionPrecedentDegreeInformation(studentCurricularPlan);
    }

    @Override
    protected ExternalPrecedentDegreeInformation createExternalPrecedentDegreeInformation(
	    final CandidacyPrecedentDegreeInformationBean bean) {
	final ExternalPrecedentDegreeInformation information = super.createExternalPrecedentDegreeInformation(bean);
	information.init(bean.getNumberOfApprovedCurricularCourses(), bean.getGradeSum(), bean.getApprovedEcts(), bean
		.getEnroledEcts());
	return information;
    }

    private void createDebt(final Person person) {
	new DegreeTransferIndividualCandidacyEvent(this, person);
    }

    @Override
    public DegreeTransferIndividualCandidacyProcess getCandidacyProcess() {
	return (DegreeTransferIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    protected Registration createRegistration(Person person, DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType,
	    Ingression ingression) {
	final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
	registration.setRegistrationYear(getCandidacyExecutionInterval().hasNextExecutionYear() ? getCandidacyExecutionInterval()
		.getNextExecutionYear() : getCandidacyExecutionInterval());
	return registration;
    }
}
