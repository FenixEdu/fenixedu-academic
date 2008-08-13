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

/**
 * @author lmac1
 */
public class DeleteCurricularCourseScope extends Service {

	public void run(Integer scopeId) throws FenixServiceException{
		CurricularCourseScope scope = rootDomainObject.readCurricularCourseScopeByOID(scopeId);
		if (scope != null) {

			try {
				scope.delete();
			} catch (DomainException e) {
				throw new CantDeleteServiceException();
			}
		}
	}
}