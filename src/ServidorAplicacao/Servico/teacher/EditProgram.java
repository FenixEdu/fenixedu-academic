package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoSiteProgram;
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

	public boolean run(Integer infoExecutionCourseCode, InfoSiteProgram infoSiteProgramNew) throws FenixServiceException {

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
				curriculum.setProgram(infoSiteProgramNew.getProgram());
				curriculum.setProgramEn(infoSiteProgramNew.getProgramEn());
			} else {
				curriculum = new Curriculum(executionCourse, infoSiteProgramNew.getProgram(), infoSiteProgramNew.getProgramEn());
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}