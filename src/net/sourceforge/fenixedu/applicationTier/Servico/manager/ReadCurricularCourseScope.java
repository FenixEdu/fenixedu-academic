/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScope implements IService {

	/**
	 * Executes the service. Returns the current InfoCurricularCourseScope.
	 * @throws ExcepcaoPersistencia 
	 */
	public InfoCurricularCourseScope run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
		CurricularCourseScope curricularCourseScope;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		curricularCourseScope = (CurricularCourseScope) sp.getIPersistentCurricularCourseScope()
				.readByOID(CurricularCourseScope.class, idInternal);

		if (curricularCourseScope == null) {
			throw new NonExistingServiceException();
		}

		return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
				.newInfoFromDomain(curricularCourseScope);
	}
}