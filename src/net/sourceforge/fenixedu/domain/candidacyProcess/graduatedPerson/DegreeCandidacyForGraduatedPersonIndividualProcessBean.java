package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.FormationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.PrecedentDegreeInformationBeanFactory;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.joda.time.LocalDate;

public class DegreeCandidacyForGraduatedPersonIndividualProcessBean extends
	IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private Degree selectedDegree;

    public DegreeCandidacyForGraduatedPersonIndividualProcessBean() {
	setCandidacyDate(new LocalDate());
	setFormationConcludedBeanList(new ArrayList<FormationBean>());
	initializeDocumentUploadBeans();
	setObservations("");
	setPrecedentDegreeType(PrecedentDegreeType.EXTERNAL_DEGREE);
    }

    public DegreeCandidacyForGraduatedPersonIndividualProcessBean(final DegreeCandidacyForGraduatedPersonIndividualProcess process) {
	setIndividualCandidacyProcess(process);
	setCandidacyDate(process.getCandidacyDate());
	setSelectedDegree(process.getCandidacySelectedDegree());
	setPrecedentDegreeInformation(PrecedentDegreeInformationBeanFactory.createBean(process.getCandidacy()));
	setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getPrecedentDegreeInformation()));
	initializeFormation(process.getCandidacy().getFormations());
	setObservations(process.getCandidacy().getObservations());
	setProcessChecked(process.getProcessChecked());
	setPaymentChecked(process.getPaymentChecked());
	setUtlStudent(process.getCandidacy().getUtlStudent());
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
	return this.selectedDegree;
    }

    public void setSelectedDegree(Degree selectedDegree) {
	this.selectedDegree = selectedDegree;
    }

    @Override
    public boolean isDegreeCandidacyForGraduatedPerson() {
	return true;
    }
}
