/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScope extends Service {

	/**
	 * Executes the service. Returns the current InfoCurricularCourseScope.
	 * @throws ExcepcaoPersistencia 
	 */
	public InfoCurricularCourseScope run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
		CurricularCourseScope curricularCourseScope;

        curricularCourseScope = rootDomainObject.readCurricularCourseScopeByOID(idInternal);

		if (curricularCourseScope == null) {
			throw new NonExistingServiceException();
		}

		return InfoCurricularCourseScope
				.newInfoFromDomain(curricularCourseScope);
	}
}