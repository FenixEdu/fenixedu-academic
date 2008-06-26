package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.joda.time.LocalDate;

public class DegreeCandidacyForGraduatedPersonIndividualProcessBean extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private DomainReference<Degree> selectedDegree;
    
    public DegreeCandidacyForGraduatedPersonIndividualProcessBean() {
	setCandidacyDate(new LocalDate());
    }
    
    @Override
    public DegreeCandidacyForGraduatedPersonProcess getCandidacyProcess() {
        return (DegreeCandidacyForGraduatedPersonProcess) super.getCandidacyProcess();
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
	// TODO Auto-generated method stub
	return false;
    }

    public Degree getSelectedDegree() {
	return (this.selectedDegree != null) ? this.selectedDegree.getObject() : null;
    }

    public void setSelectedDegree(Degree selectedDegree) {
	this.selectedDegree = (selectedDegree != null) ? new DomainReference<Degree>(selectedDegree) : null;
    }
    
}
