package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import DataBeans.InfoCurricularCourse;
import DataBeans.util.Cloner;
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

	public InfoCurricularCourse run(Integer curricularCourseCode) throws FenixServiceException {

		InfoCurricularCourse infoCurricularCourse = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

			ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, curricularCourseCode, false);

			infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		return infoCurricularCourse;
	}
}
