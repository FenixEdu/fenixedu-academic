package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID implements IService {

	public InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException, ExcepcaoPersistencia {

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionDegree persistentExecutionCourse = sp.getIPersistentExecutionDegree();
		if (executionCourseOID == null) {
			throw new FenixServiceException("nullId");
		}

		ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
				ExecutionCourse.class, executionCourseOID);

		List curricularCourses = executionCourse.getAssociatedCurricularCourses();

		List infoCurricularCourses = new ArrayList();

		CollectionUtils.collect(curricularCourses, new Transformer() {
			public Object transform(Object input) {
				CurricularCourse curricularCourse = (CurricularCourse) input;

				return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
			}
		}, infoCurricularCourses);

		infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
		infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
		return infoExecutionCourse;
	}
}