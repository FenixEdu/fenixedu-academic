package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadCurricularCourseByIdInternal implements IService {

	public InfoCurricularCourse run(Integer curricularCourseCode) throws FenixServiceException, ExcepcaoPersistencia {
		InfoCurricularCourse infoCurricularCourse = null;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

		CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
				CurricularCourse.class, curricularCourseCode);

		infoCurricularCourse = InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse);
		return infoCurricularCourse;
	}
}