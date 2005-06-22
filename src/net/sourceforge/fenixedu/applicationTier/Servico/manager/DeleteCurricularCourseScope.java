/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class DeleteCurricularCourseScope implements IService {
    public DeleteCurricularCourseScope() {
    }

    // delete a scope
    public void run(Integer scopeId) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();

            ICurricularCourseScope scope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class, scopeId);
            if (scope != null) {
                // added by Fernanda Quitério
                List writtenEvaluations = scope.getAssociatedWrittenEvaluations();
				List writtenEvaluationCurricularCourseScopes = scope.getWrittenEvaluationCurricularCourseScopes();
                if ((writtenEvaluations == null || 
						writtenEvaluations.isEmpty()) &&
					(writtenEvaluationCurricularCourseScopes == null || 
						writtenEvaluationCurricularCourseScopes.isEmpty())){
				
					dereferenceCurricularCourseScope(scope);
					persistentCurricularCourseScope.deleteByOID(CurricularCourseScope.class, scope.getIdInternal());
                } else {
                    throw new CantDeleteServiceException();
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

	private void dereferenceCurricularCourseScope(ICurricularCourseScope scope) {
/*		IPersistentCurricularSemester persistentCurricularSemester = sp.getIPersistentCurricularSemester();
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
		IPersistentBranch persistentBranch = sp.getIPersistentBranch();
	*/	
		ICurricularSemester curricularSemester = scope.getCurricularSemester();
		ICurricularCourse curricularCourse = scope.getCurricularCourse();
		IBranch branch = scope.getBranch();
		
		curricularSemester.getScopes().remove(scope);
		curricularCourse.getScopes().remove(scope);
		branch.getScopes().remove(scope);
	}
}