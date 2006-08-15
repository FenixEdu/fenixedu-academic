package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadCurricularCourseByIdInternal extends Service {

	public InfoCurricularCourse run(Integer curricularCourseCode) throws FenixServiceException, ExcepcaoPersistencia {
		InfoCurricularCourse infoCurricularCourse = null;
		CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);

		infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
		return infoCurricularCourse;
	}
}