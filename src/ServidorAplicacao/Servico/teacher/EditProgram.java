package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoCurriculum;
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
 * @deprecated
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
		InfoCurriculum infoCurriculumNew)
		throws FenixServiceException {

		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse =
				sp.getIPersistentCurricularCourse();

			ICurricularCourse curricularCourse =
				(ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(infoCurricularCourseCode),false);
				
			IPersistentCurriculum persistentCurriculum =
				sp.getIPersistentCurriculum();

			ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
			
			if (curriculum != null) {
				curriculum.setCurricularCourse(curricularCourse);
				curriculum.setProgram(infoCurriculumNew.getProgram());
				curriculum.setProgramEn(infoCurriculumNew.getProgramEn());
				persistentCurriculum.lockWrite(curriculum);
				
			} else {
				curriculum = new Curriculum();
				curriculum.setCurricularCourse(curricularCourse);
				curriculum.setProgram(infoCurriculumNew.getProgram());
				curriculum.setProgramEn(infoCurriculumNew.getProgramEn());
				persistentCurriculum.lockWrite(curriculum);
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}