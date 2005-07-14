/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

    public void run(Integer scopeId) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();

            ICurricularCourseScope scope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class, scopeId);
            if (scope != null) {
				
				try {
					scope.deleteCurricularCourseScope();
					persistentCurricularCourseScope.deleteByOID(CurricularCourseScope.class, scope.getIdInternal());
				}
				catch (DomainException e) {
					throw new CantDeleteServiceException();
				}            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}