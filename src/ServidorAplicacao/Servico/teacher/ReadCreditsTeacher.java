package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
public class ReadCreditsTeacher implements IServico {
	private static ReadCreditsTeacher service = new ReadCreditsTeacher();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadCreditsTeacher getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadCredits";
	}

	public Object run(InfoTeacher infoTeacher) throws FenixServiceException {
		InfoCredits infoCreditsTeacher = null;
	
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//teacher
			ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoTeacher);

			//read actual execution period
			IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

			ICredits creditsTeacherExample = new Credits();
			creditsTeacherExample.setTeacher(teacher);
			creditsTeacherExample.setExecutionPeriod(executionPeriod);

			//read teacher's credits
			IPersistentCreditsTeacher creditsTeacherDAO = sp.getIPersistentCreditsTeacher();
			ICredits creditsTeacher = creditsTeacherDAO.readByUnique(creditsTeacherExample);

			infoCreditsTeacher = Cloner.copyICreditsTeacher2InfoCreditsTeacher(creditsTeacher);						
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException();
		} finally {
			return infoCreditsTeacher;
		}
	}
}
