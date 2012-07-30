package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import pt.ist.fenixWebFramework.services.Service;

public class SecondCycleIndividualCandidacySeriesGrade extends SecondCycleIndividualCandidacySeriesGrade_Base {

    public SecondCycleIndividualCandidacySeriesGrade() {
	super();
    }

    public boolean isClean() {
	if (!(this.getAffinity() == null && this.getCandidacyGrade() == null && this.getDegreeNature() == null
		&& (this.getInterviewGrade() == null || this.getInterviewGrade().equals(""))
		&& this.getProfessionalExperience() == null && this.getSeriesCandidacyGrade() == null && this.getNotes() == null)) {
	    return false;
	} else {
	    return true;
	}
    }

    public void delete() {
	setIndividualCandidacy(null);
	setDegree(null);
	removeRootDomainObject();
	deleteDomainObject();
    }
}
