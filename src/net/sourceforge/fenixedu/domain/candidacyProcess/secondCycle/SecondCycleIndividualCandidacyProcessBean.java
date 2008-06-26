package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.joda.time.LocalDate;

public class SecondCycleIndividualCandidacyProcessBean extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private DomainReference<Degree> selectedDegree;

    private String professionalStatus;

    private String otherEducation;

    public SecondCycleIndividualCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
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
	return (SecondCycleCandidacyProcess) super.getCandidacyProcess();
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

    public String getOtherEducation() {
	return otherEducation;
    }

    public void setOtherEducation(String otherEducation) {
	this.otherEducation = otherEducation;
    }

    public boolean hasChoosenPerson() {
	return getChoosePersonBean().hasPerson();
    }

    public void removeChoosePersonBean() {
	setChoosePersonBean(null);
    }

    @Override
    protected double getMinimumEcts(final CycleType cycleType) {
	if (!cycleType.equals(CycleType.FIRST_CYCLE)) {
	    throw new IllegalArgumentException();
	}
	return 150d;
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
	return Collections.singletonList(CycleType.FIRST_CYCLE);
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
	return true;
    }
}
