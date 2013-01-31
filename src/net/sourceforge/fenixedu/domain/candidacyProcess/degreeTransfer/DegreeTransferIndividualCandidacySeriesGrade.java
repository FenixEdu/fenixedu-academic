package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;


public class DegreeTransferIndividualCandidacySeriesGrade extends DegreeTransferIndividualCandidacySeriesGrade_Base {

	public DegreeTransferIndividualCandidacySeriesGrade() {
		super();
	}

	public boolean isClean() {
		if (!(this.getAffinity() == null && this.getDegreeNature() == null && this.getApprovedEctsRate() == null
				&& this.getGradeRate() == null && this.getSeriesCandidacyGrade() == null)) {
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
