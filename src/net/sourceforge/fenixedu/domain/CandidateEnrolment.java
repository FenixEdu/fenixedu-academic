package net.sourceforge.fenixedu.domain;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolment extends CandidateEnrolment_Base {

	public CandidateEnrolment() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public CandidateEnrolment(MasterDegreeCandidate masterDegreeCandidate,
			CurricularCourse curricularCourse) {
		this();
		setMasterDegreeCandidate(masterDegreeCandidate);
		setCurricularCourse(curricularCourse);
	}

}