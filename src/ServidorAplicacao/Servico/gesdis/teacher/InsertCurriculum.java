/*
 * Created on 24/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoCurriculum;
import DataBeans.util.Cloner;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InsertCurriculum implements IServico {

	/**
	 * 
	 */
	public InsertCurriculum() {

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {

		return "InsertCurriculum";
	}
	private static InsertCurriculum service = new InsertCurriculum();
	public static InsertCurriculum getService() {
		return service;
	}
	public Boolean run(InfoCurriculum infoCurriculum)
		throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente  persistentExecutionCourse= sp.getIDisciplinaExecucaoPersistente();
			ICurriculum curriculum =
				Cloner.copyInfoCurriculum2ICurriculum(infoCurriculum);
			IDisciplinaExecucao executionCourse = persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod(curriculum.getExecutionCourse().getSigla(),curriculum.getExecutionCourse().getExecutionPeriod());
			curriculum.setExecutionCourse(executionCourse);	
			IPersistentCurriculum persistentCurriculum =
				sp.getIPersistentCurriculum();
			ICurriculum existingCurriculum =persistentCurriculum.readCurriculumByExecutionCourse(executionCourse);
			if(existingCurriculum!= null) throw new ExistingServiceException();	
			persistentCurriculum.lockWrite(curriculum);

		} catch (ExistingPersistentException ex) {

			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return new Boolean(true);
	}
}
