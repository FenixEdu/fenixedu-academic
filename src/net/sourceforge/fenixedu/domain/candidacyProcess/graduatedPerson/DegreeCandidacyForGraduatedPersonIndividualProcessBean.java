package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.joda.time.LocalDate;

public class DegreeCandidacyForGraduatedPersonIndividualProcessBean extends
	IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private DomainReference<Degree> selectedDegree;

    public DegreeCandidacyForGraduatedPersonIndividualProcessBean() {
	setCandidacyDate(new LocalDate());
    }

    public DegreeCandidacyForGraduatedPersonIndividualProcessBean(final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
	setCandidacyDate(process.getCandidacyDate());
	setSelectedDegree(process.getCandidacySelectedDegree());
	setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean(process
		.getCandidacyPrecedentDegreeInformation()));
	setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getCandidacyPrecedentDegreeInformation()));
    }

    @Override
    public DegreeCandidacyForGraduatedPersonProcess getCandidacyProcess() {
	return (DegreeCandidacyForGraduatedPersonProcess) super.getCandidacyProcess();
    }

    @Override
    protected double getMinimumEcts(final CycleType cycleType) {
	if (cycleType.equals(CycleType.FIRST_CYCLE)) {
	    return 150d;
	} else if (cycleType.equals(CycleType.SECOND_CYCLE)) {
	    return 90d;
	} else {
	    throw new IllegalArgumentException();
	}
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
	return Arrays.asList(CycleType.FIRST_CYCLE, CycleType.SECOND_CYCLE);
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
	return true;
    }

    public Degree getSelectedDegree() {
	return (this.selectedDegree != null) ? this.selectedDegree.getObject() : null;
    }

    public void setSelectedDegree(Degree selectedDegree) {
	this.selectedDegree = (selectedDegree != null) ? new DomainReference<Degree>(selectedDegree) : null;
    }

}
