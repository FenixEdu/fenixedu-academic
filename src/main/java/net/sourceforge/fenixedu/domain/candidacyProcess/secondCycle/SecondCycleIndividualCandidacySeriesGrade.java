package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

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
        setRootDomainObject(null);
        deleteDomainObject();
    }
    @Deprecated
    public boolean hasProfessionalExperience() {
        return getProfessionalExperience() != null;
    }

    @Deprecated
    public boolean hasCandidacyGrade() {
        return getCandidacyGrade() != null;
    }

    @Deprecated
    public boolean hasSeriesCandidacyGrade() {
        return getSeriesCandidacyGrade() != null;
    }

    @Deprecated
    public boolean hasInterviewGrade() {
        return getInterviewGrade() != null;
    }

}
