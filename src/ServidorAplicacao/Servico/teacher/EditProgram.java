package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSiteProgram;
import Dominio.Curriculum;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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
		InfoSiteProgram infoSiteProgramNew)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentCurriculum persistentCurriculum =
				sp.getIPersistentCurriculum();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					new DisciplinaExecucao(infoExecutionCourseCode),
					false);

			List curricularCourses =
				executionCourse.getAssociatedCurricularCourses();
			Iterator iter = curricularCourses.iterator();
			while (iter.hasNext()) {
				ICurricularCourse curricularCourse =
					(ICurricularCourse) iter.next();
				ICurriculum curriculum = null;
				curriculum =
					persistentCurriculum.readCurriculumByCurricularCourse(
						curricularCourse);

				persistentCurriculum.lockWrite(curriculum);

				if (curriculum != null) {
					curriculum.setCurricularCourse(curricularCourse);
					curriculum.setProgram(infoSiteProgramNew.getProgram());
					curriculum.setProgramEn(infoSiteProgramNew.getProgramEn());
				} else {
					curriculum =
						new Curriculum(
							curricularCourse,
							infoSiteProgramNew.getProgram(),
							infoSiteProgramNew.getProgramEn());
				}

			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}