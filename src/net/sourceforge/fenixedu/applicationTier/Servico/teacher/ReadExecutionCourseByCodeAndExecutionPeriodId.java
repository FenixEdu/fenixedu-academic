/*
 * Created on Feb 23, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadExecutionCourseByCodeAndExecutionPeriodId implements IService {

	public InfoExecutionCourse run(Integer executionPeriodId, String code) throws ExcepcaoInexistente,
			FenixServiceException, ExcepcaoPersistencia {

		IExecutionCourse iExecCourse = null;
		InfoExecutionCourse infoExecCourse = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
		iExecCourse = executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriodId(code,
				executionPeriodId);
		if (iExecCourse != null)
			infoExecCourse = InfoExecutionCourse.newInfoFromDomain(iExecCourse);

		return infoExecCourse;
	}

}
