package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

public class CandidateEnrolment extends CandidateEnrolment_Base {

    public CandidateEnrolment() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
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
