package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
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
		return "ReadCreditsTeacher";
	}

	public Object run(Integer teacherOID) throws FenixServiceException {
		InfoCredits infoCreditsTeacher = null;
	
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//teacher
			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			ITeacher teacher = new Teacher();
			teacher.setIdInternal(teacherOID);
			teacher = (ITeacher) teacherDAO.readByOId(teacher, false);
			
			//read actual execution period
			IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

			ICredits creditsTeacherExample = new Credits();
			creditsTeacherExample.setTeacher(teacher);
			creditsTeacherExample.setExecutionPeriod(executionPeriod);

			//read teacher's credits
			IPersistentCreditsTeacher creditsTeacherDAO = sp.getIPersistentCreditsTeacher();
			ICredits creditsTeacher = creditsTeacherDAO.readByUnique(creditsTeacherExample);
			
			if (creditsTeacher != null) {
				infoCreditsTeacher = Cloner.copyICreditsTeacher2InfoCreditsTeacher(creditsTeacher);
			}else  {
				infoCreditsTeacher = Cloner.copyICreditsTeacher2InfoCreditsTeacher(creditsTeacherExample);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException();
		} 
		return infoCreditsTeacher;
		
	}
}
