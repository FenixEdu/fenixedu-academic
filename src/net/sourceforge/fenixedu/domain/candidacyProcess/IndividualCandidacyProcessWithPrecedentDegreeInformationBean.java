package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

abstract public class IndividualCandidacyProcessWithPrecedentDegreeInformationBean extends IndividualCandidacyProcessBean {

    private PrecedentDegreeType precedentDegreeType;

    private CandidacyPrecedentDegreeInformationBean precedentDegreeInformation;

    private DomainReference<StudentCurricularPlan> precedentStudentCurricularPlan;

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

    public List<StudentCurricularPlan> getPrecedentStudentCurricularPlans() {
	final Student student = getStudent();
	if (student == null) {
	    return Collections.emptyList();
	}

	final List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
	for (final Registration registration : student.getRegistrations()) {
	    if (registration.isBolonha()) {
		final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

		for (final CycleType cycleType : getValidPrecedentCycleTypes()) {

		    if (studentCurricularPlan.hasCycleCurriculumGroup(cycleType)) {
			final CycleCurriculumGroup cycle = studentCurricularPlan.getCycle(cycleType);

			// not concluded cycles count if respect minimum ects
			if (cycle.isConclusionProcessed() || cycle.isConcluded()
				|| cycle.getCreditsConcluded().doubleValue() >= getMinimumEcts(cycleType)) {
			    studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
			    break;
			}
		    }
		}

	    } else if (isPreBolonhaPrecedentDegreeAllowed()) {
		if (registration.isRegistrationConclusionProcessed()) {
		    studentCurricularPlans.add(registration.getLastStudentCurricularPlan());
		}
	    }
	}

	return studentCurricularPlans;
    }

    /**
     * If cycle is not concluded, this represents the minimum number of ects
     * that student must have to candidate in order to conclude the degree with
     * current semester
     */
    protected double getMinimumEcts(final CycleType cycleType) {
	return cycleType.getDefaultEcts();
    }

    abstract protected List<CycleType> getValidPrecedentCycleTypes();

    abstract protected boolean isPreBolonhaPrecedentDegreeAllowed();

    protected Student getStudent() {
	return getPersonBean().hasStudent() ? getPersonBean().getPerson().getStudent() : null;
    }

    public boolean isValidPrecedentDegreeInformation() {
	return hasPrecedentDegreeType() && (isExternalPrecedentDegreeType() || hasPrecedentStudentCurricularPlan());
    }

    public StudentCurricularPlan getPrecedentStudentCurricularPlan() {
	return (this.precedentStudentCurricularPlan != null) ? this.precedentStudentCurricularPlan.getObject() : null;
    }

    public boolean hasPrecedentStudentCurricularPlan() {
	return getPrecedentStudentCurricularPlan() != null;
    }

    public void setPrecedentStudentCurricularPlan(StudentCurricularPlan precedentStudentCurricularPlan) {
	this.precedentStudentCurricularPlan = (precedentStudentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(
		precedentStudentCurricularPlan)
		: null;
    }

    // static information

    static public enum PrecedentDegreeType {
	INSTITUTION_DEGREE, EXTERNAL_DEGREE;

	public String getName() {
	    return name();
	}

	public static PrecedentDegreeType valueOf(final CandidacyPrecedentDegreeInformation precedentDegreeInformation) {
	    return precedentDegreeInformation.isExternal() ? EXTERNAL_DEGREE : INSTITUTION_DEGREE;
	}
    }
}
