/*
 * Created on 24/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourseScope;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.Servico.exceptions.CantDeleteServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentWrittenEvaluationCurricularCourseScope;
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
            IPersistentWrittenEvaluationCurricularCourseScope persistentWrittenEvaluationCurricularCourseScope = sp
                    .getIPersistentWrittenEvaluationCurricularCourseScope();

            ICurricularCourseScope scope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class, scopeId);
            if (scope != null) {
                // added by Fernanda Quitério
                List writtenEvaluations = persistentWrittenEvaluationCurricularCourseScope
                        .readByCurricularCourseScope(scope);
                if (writtenEvaluations == null || writtenEvaluations.size() == 0) {

                    persistentCurricularCourseScope.delete(scope);
                } else {
                    throw new CantDeleteServiceException();
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}