package Dominio;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolment extends DomainObject implements ICandidateEnrolment
{

    private IMasterDegreeCandidate masterDegreeCandidate;

    private ICurricularCourse curricularCourse;

    private Integer masterDegreeCandidateKey;
    private Integer curricularCourseKey;

    public CandidateEnrolment()
    {
    }

    //	public CandidateEnrolment(IMasterDegreeCandidate masterDegreeCandidate,
	// ICurricularCourseScope curricularCourseScope) {
    //		setMasterDegreeCandidate(masterDegreeCandidate);
    //		setCurricularCourseScope(curricularCourseScope);
    //	}

    public CandidateEnrolment(
        IMasterDegreeCandidate masterDegreeCandidate,
        ICurricularCourse curricularCourse)
    {
        setMasterDegreeCandidate(masterDegreeCandidate);
        setCurricularCourse(curricularCourse);
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof ICandidateEnrolment)
        {
            ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) obj;
            result =
                getMasterDegreeCandidate().equals(candidateEnrolment.getMasterDegreeCandidate())
                    && getCurricularCourse().equals(candidateEnrolment.getCurricularCourse());
        }
        return result;
    }

    public String toString()
    {
        String result = "[CANDIDATE_ENROLMENT";
        result += ", codInt=" + getIdInternal();
        result += ", masterDegreeCandidate=" + masterDegreeCandidate;
        result += ", curricularCourse=" + curricularCourse;
        result += "]";
        return result;
    }

    /**
	 * @return
	 */
    public IMasterDegreeCandidate getMasterDegreeCandidate()
    {
        return masterDegreeCandidate;
    }

    /**
	 * @return
	 */
    public Integer getMasterDegreeCandidateKey()
    {
        return masterDegreeCandidateKey;
    }

   

    /**
	 * @param candidate
	 */
    public void setMasterDegreeCandidate(IMasterDegreeCandidate candidate)
    {
        masterDegreeCandidate = candidate;
    }

    /**
	 * @param integer
	 */
    public void setMasterDegreeCandidateKey(Integer integer)
    {
        masterDegreeCandidateKey = integer;
    }

    /**
	 * @return
	 */
    public ICurricularCourse getCurricularCourse()
    {
        return curricularCourse;
    }

    /**
	 * @param curricularCourse
	 */
    public void setCurricularCourse(ICurricularCourse curricularCourse)
    {
        this.curricularCourse = curricularCourse;
    }

    /**
	 * @return
	 */
    public Integer getCurricularCourseKey()
    {
        return curricularCourseKey;
    }

    /**
	 * @param curricularCourseKey
	 */
    public void setCurricularCourseKey(Integer curricularCourseKey)
    {
        this.curricularCourseKey = curricularCourseKey;
    }
}
