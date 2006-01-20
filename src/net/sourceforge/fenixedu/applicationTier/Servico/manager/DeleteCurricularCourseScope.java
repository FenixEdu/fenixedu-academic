/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;

/**
 * @author lmac1
 */
public class DeleteCurricularCourseScope extends Service {

	public void run(Integer scopeId) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport
				.getIPersistentCurricularCourseScope();

		CurricularCourseScope scope = (CurricularCourseScope) persistentCurricularCourseScope
				.readByOID(CurricularCourseScope.class, scopeId);
		if (scope != null) {

			try {
				scope.delete();
			} catch (DomainException e) {
				throw new CantDeleteServiceException();
			}
		}
	}
}