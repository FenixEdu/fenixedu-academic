package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID extends Service {

	public InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException, ExcepcaoPersistencia {

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

		if (executionCourseOID == null) {
			throw new FenixServiceException("nullId");
		}

		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseOID);

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