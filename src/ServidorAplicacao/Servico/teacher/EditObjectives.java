package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSiteObjectives;
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
public class EditObjectives implements IServico {

	private static EditObjectives service = new EditObjectives();
	public static EditObjectives getService() {
		return service;
	}

	private EditObjectives() {
	}
	public final String getNome() {
		return "EditObjectives";
	}

	public boolean run(
		Integer infoExecutionCourseCode,
		InfoSiteObjectives infoSiteObjectivesNew)
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
					curriculum.setGeneralObjectives(
						infoSiteObjectivesNew.getGeneralObjectives());
					curriculum.setOperacionalObjectives(
						infoSiteObjectivesNew.getOperacionalObjectives());
					curriculum.setGeneralObjectivesEn(
						infoSiteObjectivesNew.getGeneralObjectivesEn());
					curriculum.setOperacionalObjectivesEn(
						infoSiteObjectivesNew.getOperacionalObjectivesEn());
				} else {
					curriculum =
						new Curriculum(
							curricularCourse,
							infoSiteObjectivesNew.getGeneralObjectives(),
							infoSiteObjectivesNew.getOperacionalObjectives(),
							infoSiteObjectivesNew.getGeneralObjectivesEn(),
							infoSiteObjectivesNew.getOperacionalObjectivesEn());
				}

			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}