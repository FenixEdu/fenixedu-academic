/*
 * Created on 24/Set/2003
 */
package ServidorAplicacao.Servico.manager;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourseScope;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
/**
 * @author lmac1
 */
public class DeleteCurricularCourseScope implements IService {
    
	public DeleteCurricularCourseScope() {
	}
	// delete a scope
	public void run(Integer scopeId) throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
					.getIPersistentCurricularCourseScope();
//			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
//					.getIStudentCurricularPlanPersistente();
			ICurricularCourseScope helpCurricularCourseScope = new CurricularCourseScope();
			helpCurricularCourseScope.setIdInternal(scopeId);
			ICurricularCourseScope scope = (ICurricularCourseScope) persistentCurricularCourseScope
					.readByOId(helpCurricularCourseScope, false);
			if (scope != null) {
				//if (scope.getCurricularCourse().getScopes().size() > 1) {
					persistentCurricularCourseScope.delete(scope);
//				} else {
//					throw new CantDeleteServiceException();
//				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}