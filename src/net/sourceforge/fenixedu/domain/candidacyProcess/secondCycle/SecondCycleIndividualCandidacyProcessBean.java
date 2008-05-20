package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class SecondCycleIndividualCandidacyProcessBean implements Serializable {

    private static final double MINIMUM_ECTS = 150d;

    private DomainReference<SecondCycleCandidacyProcess> candidacyProcess;

    private ChoosePersonBean choosePersonBean;

    private PersonBean personBean;

    private YearMonthDay candidacyDate;

    private DomainReference<Degree> selectedDegree;

    private String professionalStatus;

    private PrecedentDegreeType precedentDegreeType;

    private CandidacyPrecedentDegreeInformationBean precedentDegreeInformation;

    private String otherEducation;

    public SecondCycleIndividualCandidacyProcessBean() {
	setCandidacyDate(new YearMonthDay());
    }

    public SecondCycleIndividualCandidacyProcessBean(final SecondCycleIndividualCandidacyProcess process) {
	setSelectedDegree(process.getCandidacySelectedDegree());
	setProfessionalStatus(process.getCandidacyProfessionalStatus());
	setOtherEducation(process.getCandidacyOtherEducation());
	setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean(process
		.getCandidacyPrecedentDegreeInformation()));
	setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getCandidacyPrecedentDegreeInformation()));
	setCandidacyDate(process.getCandidacyDate());
    }

    public SecondCycleCandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(SecondCycleCandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<SecondCycleCandidacyProcess>(candidacyProcess)
		: null;
    }

    public YearMonthDay getCandidacyDate() {
	return candidacyDate;
    }

    public void setCandidacyDate(YearMonthDay candidacyDate) {
	this.candidacyDate = candidacyDate;
    }

    public ChoosePersonBean getChoosePersonBean() {
	return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
	this.choosePersonBean = choosePersonBean;
    }

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public Degree getSelectedDegree() {
	return (this.selectedDegree != null) ? this.selectedDegree.getObject() : null;
    }

    public void setSelectedDegree(Degree selectedDegree) {
	this.selectedDegree = (selectedDegree != null) ? new DomainReference<Degree>(selectedDegree) : null;
    }

    public String getProfessionalStatus() {
	return professionalStatus;
    }

    public void setProfessionalStatus(String professionalStatus) {
	this.professionalStatus = professionalStatus;
    }

    public PrecedentDegreeType getPrecedentDegreeType() {
	return precedentDegreeType;
    }

    public void setPrecedentDegreeType(PrecedentDegreeType precedentDegreeType) {
	this.precedentDegreeType = precedentDegreeType;
    }

    public boolean hasPrecedentDegreeType() {
	return precedentDegreeType != null;
    }

    public boolean isExternalPrecedentDegreeType() {
	return precedentDegreeType == PrecedentDegreeType.EXTERNAL_DEGREE;
    }

    public CandidacyPrecedentDegreeInformationBean getPrecedentDegreeInformation() {
	return precedentDegreeInformation;
    }

    public void setPrecedentDegreeInformation(CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {
	this.precedentDegreeInformation = precedentDegreeInformation;
    }

    public String getOtherEducation() {
	return otherEducation;
    }

    public void setOtherEducation(String otherEducation) {
	this.otherEducation = otherEducation;
    }

    static public enum PrecedentDegreeType {
	INSTITUTION_DEGREE, EXTERNAL_DEGREE;

	public String getName() {
	    return name();
	}

	public static PrecedentDegreeType valueOf(final CandidacyPrecedentDegreeInformation precedentDegreeInformation) {
	    return precedentDegreeInformation.isExternal() ? EXTERNAL_DEGREE : INSTITUTION_DEGREE;
	}
    }

    private CycleType getPrecedentCycleType() {
	return CycleType.FIRST_CYCLE;
    }

    public StudentCurricularPlan getPrecedentStudentCurricularPlan() {
	final Student student = getStudent();
	if (student == null) {
	    return null;
	}

	final Set<StudentCurricularPlan> studentCurricularPlans = new HashSet<StudentCurricularPlan>();
	for (final Registration registration : student.getRegistrations()) {
	    if (registration.isBolonha()) {
		final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
		if (studentCurricularPlan.hasCycleCurriculumGroup(getPrecedentCycleType())) {
		    final CycleCurriculumGroup cycle = studentCurricularPlan.getCycle(getPrecedentCycleType());
		    if (cycle.isConclusionProcessed() || cycle.isConcluded()
			    || cycle.getCreditsConcluded().doubleValue() >= MINIMUM_ECTS) {
			studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
		    }
		}

		/*
                 * if
                 * (registration.getDegreeType().getCycleTypes().contains(getPrecedentCycleType())) {
                 * if
                 * (registration.isRegistrationConclusionProcessed(getPrecedentCycleType()) ||
                 * registration.hasConcludedCycle(getPrecedentCycleType())) {
                 * studentCurricularPlans.add(registration.getLastStudentCurricularPlan()); } }
                 */
	    } else {
		if (registration.isRegistrationConclusionProcessed()) {
		    studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
		}
	    }
	}

	return (studentCurricularPlans.size() == 1) ? studentCurricularPlans.iterator().next() : null;
    }

    private Student getStudent() {
	return personBean.hasPerson() && personBean.getPerson().hasStudent() ? personBean.getPerson().getStudent() : null;
    }

    public boolean isValidPrecedentDegreeInformation() {
	return hasPrecedentDegreeType() && (isExternalPrecedentDegreeType() || getPrecedentStudentCurricularPlan() != null);
    }

    public boolean hasChoosenPerson() {
	return getChoosePersonBean().hasPerson();
    }

    public void removeChoosePersonBean() {
	setChoosePersonBean(null);
    }
}
