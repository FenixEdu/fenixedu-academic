package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoSiteObjectives;
import Dominio.Curriculum;
import Dominio.DisciplinaExecucao;
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

	public boolean run(Integer infoExecutionCourseCode, InfoSiteObjectives infoSiteObjectivesNew) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(infoExecutionCourseCode), false);

			ICurriculum curriculum = null;
			curriculum = persistentCurriculum.readCurriculumByExecutionCourse(executionCourse);

			persistentCurriculum.lockWrite(curriculum);

			if (curriculum != null) {
				curriculum.setExecutionCourse(executionCourse);
				curriculum.setGeneralObjectives(infoSiteObjectivesNew.getGeneralObjectives());
				curriculum.setOperacionalObjectives(infoSiteObjectivesNew.getOperacionalObjectives());
				curriculum.setGeneralObjectivesEn(infoSiteObjectivesNew.getGeneralObjectivesEn());
				curriculum.setOperacionalObjectivesEn(infoSiteObjectivesNew.getOperacionalObjectivesEn());
			} else {
				curriculum =
					new Curriculum(
						executionCourse,
						infoSiteObjectivesNew.getGeneralObjectives(),
						infoSiteObjectivesNew.getOperacionalObjectives(),
						infoSiteObjectivesNew.getGeneralObjectivesEn(),
						infoSiteObjectivesNew.getOperacionalObjectivesEn());
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}