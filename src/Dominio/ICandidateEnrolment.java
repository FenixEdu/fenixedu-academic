package Dominio;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface ICandidateEnrolment extends IDomainObject {

    public IMasterDegreeCandidate getMasterDegreeCandidate();

    public ICurricularCourse getCurricularCourse();

    public void setMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidate);

    public void setCurricularCourse(ICurricularCourse curricularCourse);

}