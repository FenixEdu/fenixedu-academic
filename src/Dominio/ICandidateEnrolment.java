
package Dominio;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface ICandidateEnrolment extends IDomainObject {
 
  
  IMasterDegreeCandidate getMasterDegreeCandidate();
  ICurricularCourseScope getCurricularCourseScope();
  
  void setMasterDegreeCandidate (IMasterDegreeCandidate masterDegreeCandidate);
  void setCurricularCourseScope(ICurricularCourseScope curricularCourseScope);
  
}
