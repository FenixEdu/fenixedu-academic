package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoSiteObjectives;
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
		Integer infoCurricularCourseCode,
		InfoSiteObjectives infoSiteObjectivesNew)
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
					curriculum.setGeneralObjectives(
						infoSiteObjectivesNew.getGeneralObjectives());
					curriculum.setOperacionalObjectives(
						infoSiteObjectivesNew.getOperacionalObjectives());
					curriculum.setGeneralObjectivesEn(
						infoSiteObjectivesNew.getGeneralObjectivesEn());
					curriculum.setOperacionalObjectivesEn(
						infoSiteObjectivesNew.getOperacionalObjectivesEn());
				} else {
					System.out.println("novo curriculo");
					curriculum =
						new Curriculum(
							curricularCourse,
							infoSiteObjectivesNew.getGeneralObjectives(),
							infoSiteObjectivesNew.getOperacionalObjectives(),
							infoSiteObjectivesNew.getGeneralObjectivesEn(),
							infoSiteObjectivesNew.getOperacionalObjectivesEn());
					persistentCurriculum.lockWrite(curriculum);
				}

			

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}