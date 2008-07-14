package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.joda.time.LocalDate;

public class DegreeChangeIndividualCandidacyProcessBean extends IndividualCandidacyProcessWithPrecedentDegreeInformationBean {

    private DomainReference<Degree> selectDegree;
    
    public DegreeChangeIndividualCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
    }

    public DegreeChangeIndividualCandidacyProcessBean(final DegreeChangeIndividualCandidacyProcess process) {
	setCandidacyDate(process.getCandidacyDate());
	//TODO: not implemented
	//setPrecedentDegreeType(PrecedentDegreeType.valueOf(process.getCandidacyPrecedentDegreeInformation()));
    }

    @Override
    protected List<CycleType> getValidPrecedentCycleTypes() {
	return Collections.singletonList(CycleType.FIRST_CYCLE);
    }

    @Override
    protected double getMinimumEcts(CycleType cycleType) {
	if (cycleType.equals(CycleType.FIRST_CYCLE)) {
	    return 150d;
	}
	throw new IllegalArgumentException();
    }

    @Override
    protected boolean isPreBolonhaPrecedentDegreeAllowed() {
	return false;
    }
    
    @Override
    public DegreeChangeCandidacyProcess getCandidacyProcess() {
        return (DegreeChangeCandidacyProcess) super.getCandidacyProcess();
    }

    public Degree getSelectedDegree() {
	return (this.selectDegree != null) ? this.selectDegree.getObject() : null;
    }

    public void setSelectedDegree(final Degree selectDegree) {
	this.selectDegree = (selectDegree != null) ? new DomainReference<Degree>(selectDegree) : null;
    }
}
