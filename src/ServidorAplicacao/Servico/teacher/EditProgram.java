package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoSiteProgram;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *
 * 
 */
public class EditProgram implements IServico {

	private static EditProgram service = new EditProgram();
	public static EditProgram getService() {
		return service;
	}

	private EditProgram() {
	}
	public final String getNome() {
		return "EditProgram";
	}

	public boolean run(
		Integer infoExecutionCourseCode,
		Integer infoCurricularCourseCode,
		InfoSiteProgram infoSiteProgramNew)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurriculum persistentCurriculum =
				sp.getIPersistentCurriculum();
			IPersistentCurricularCourse persistentCurricularCourse =
				sp.getIPersistentCurricularCourse();
			ICurricularCourse curricularCourse =
				(ICurricularCourse) persistentCurricularCourse.readByOId(
					new CurricularCourse(infoCurricularCourseCode),
					false);

			ICurriculum curriculum = null;
			curriculum =
				persistentCurriculum.readCurriculumByCurricularCourse(
					curricularCourse);

			if (curriculum != null) {
				persistentCurriculum.lockWrite(curriculum);
				curriculum.setCurricularCourse(curricularCourse);
				curriculum.setProgram(infoSiteProgramNew.getProgram());
				curriculum.setProgramEn(infoSiteProgramNew.getProgramEn());
			} else {
				curriculum =
					new Curriculum(
						curricularCourse,
						infoSiteProgramNew.getProgram(),
						infoSiteProgramNew.getProgramEn());
				persistentCurriculum.lockWrite(curriculum);
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}