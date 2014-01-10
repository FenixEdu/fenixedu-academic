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

    @Deprecated
    public boolean hasApprovedEctsRate() {
        return getApprovedEctsRate() != null;
    }

    @Deprecated
    public boolean hasSeriesCandidacyGrade() {
        return getSeriesCandidacyGrade() != null;
    }

    @Deprecated
    public boolean hasGradeRate() {
        return getGradeRate() != null;
    }

}
