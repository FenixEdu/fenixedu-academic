package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

public class DegreeChangeIndividualCandidacySeriesGrade extends DegreeChangeIndividualCandidacySeriesGrade_Base {

    public DegreeChangeIndividualCandidacySeriesGrade() {
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
        setRootDomainObject(null);
        deleteDomainObject();
    }
}
