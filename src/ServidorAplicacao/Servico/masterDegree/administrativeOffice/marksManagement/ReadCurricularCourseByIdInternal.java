package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 01/07/2003
 * 
 */
public class ReadCurricularCourseByIdInternal implements IServico {

	private static ReadCurricularCourseByIdInternal servico = new ReadCurricularCourseByIdInternal();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCurricularCourseByIdInternal getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadCurricularCourseByIdInternal() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadCurricularCourseByIdInternal";
	}

	public ICurricularCourse run(Integer curricularCourseCode) throws FenixServiceException {

		ICurricularCourse curricularCourse = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

			curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		return curricularCourse;
	}
}
