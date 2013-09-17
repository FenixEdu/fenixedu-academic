package net.sourceforge.fenixedu.domain;

public class CandidateEnrolment extends CandidateEnrolment_Base {

    public CandidateEnrolment() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public CandidateEnrolment(MasterDegreeCandidate masterDegreeCandidate, CurricularCourse curricularCourse) {
        this();
        setMasterDegreeCandidate(masterDegreeCandidate);
        setCurricularCourse(curricularCourse);
    }

    public void delete() {
        setCurricularCourse(null);
        setMasterDegreeCandidate(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMasterDegreeCandidate() {
        return getMasterDegreeCandidate() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
