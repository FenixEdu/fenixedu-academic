package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

public class DegreeCandidacyForGraduatedPersonSeriesGade extends DegreeCandidacyForGraduatedPersonSeriesGade_Base {

    public DegreeCandidacyForGraduatedPersonSeriesGade() {
        super();
    }

    public boolean isClean() {
        if (!(this.getAffinity() == null && this.getDegreeNature() == null && this.getCandidacyGrade() == null)) {
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
    public boolean hasCandidacyGrade() {
        return getCandidacyGrade() != null;
    }

}
