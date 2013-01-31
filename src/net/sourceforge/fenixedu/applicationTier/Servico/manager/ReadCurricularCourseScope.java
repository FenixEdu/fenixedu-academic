/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScope extends FenixService {

	/**
	 * Executes the service. Returns the current InfoCurricularCourseScope.
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	@Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
	@Service
	public static InfoCurricularCourseScope run(Integer idInternal) throws FenixServiceException {
		CurricularCourseScope curricularCourseScope;

		curricularCourseScope = rootDomainObject.readCurricularCourseScopeByOID(idInternal);

		if (curricularCourseScope == null) {
			throw new NonExistingServiceException();
		}

		return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
	}
}