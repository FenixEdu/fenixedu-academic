/*
 * Created on 16/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurriculum;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class EditCurriculum implements IServico {

	private static EditCurriculum service = new EditCurriculum();

	/**
	 * The singleton access method of this class.
	 */
	public static EditCurriculum getService() {
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private EditCurriculum() {
	}

	/**
	 * Service name
	 */
	public final String getNome() {
		return "EditCurriculum";
	}

	/**
	 * Executes the service.
	 */

	public void run(InfoCurriculum infoCurriculum, String language) throws FenixServiceException {

		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(infoCurriculum.getInfoCurricularCourse().getIdInternal()), false);
				if(curricularCourse == null)
					throw new NonExistingServiceException();
				
				IPersistentCurriculum persistentCurriculum = persistentSuport.getIPersistentCurriculum();
				ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
				if(curriculum == null)
					curriculum = new Curriculum();
				
				if(language == null) {
					curriculum.setGeneralObjectives(infoCurriculum.getGeneralObjectives());
					curriculum.setOperacionalObjectives(infoCurriculum.getOperacionalObjectives());
					curriculum.setProgram(infoCurriculum.getProgram());
				}
				else {
					curriculum.setGeneralObjectivesEn(infoCurriculum.getGeneralObjectivesEn());
					curriculum.setOperacionalObjectivesEn(infoCurriculum.getOperacionalObjectivesEn());
					curriculum.setProgramEn(infoCurriculum.getProgramEn());
				}
				curriculum.setCurricularCourse(curricularCourse);
				
				persistentCurriculum.lockWrite(curriculum);
						
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}