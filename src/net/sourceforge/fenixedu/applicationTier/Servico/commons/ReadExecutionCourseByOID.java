/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadExecutionCourseByOID implements IService {

	public InfoExecutionCourse run(Integer oid) throws FenixServiceException,
			ExcepcaoPersistencia {

		InfoExecutionCourse result = null;
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentObject persistentObject = sp.getIPersistentObject();
		IExecutionCourse executionCourse = (IExecutionCourse) persistentObject
				.readByOID(ExecutionCourse.class, oid);
		if (executionCourse != null) {
			result = (InfoExecutionCourse) Cloner.get(executionCourse);
		}

		return result;
	}
}