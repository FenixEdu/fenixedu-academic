package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoTeacher;
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
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
public class WriteCreditsTeacher implements IServico {
	private static WriteCreditsTeacher service = new WriteCreditsTeacher();

	/**
	 * The singleton access method of this class.
	 */
	public static WriteCreditsTeacher getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "WriteCredits";
	}

	public Boolean run(InfoTeacher infoTeacher, Integer tfcStudentNumber) throws FenixServiceException {
		
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			ITeacher teacherParam = Cloner.copyInfoTeacher2Teacher(infoTeacher);
			ITeacher teacher = (ITeacher) teacherDAO.readByOId(teacherParam);
			
			IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
			
			ICredits creditsTeacher = new Credits();
			creditsTeacher.setTeacher(teacher);
			creditsTeacher.setExecutionPeriod(executionPeriod);
			creditsTeacher.setTfcStudentsNumber(tfcStudentNumber);
			creditsTeacher.setCredits(new Double(0));
			
			//escreve teacher's credits
			IPersistentCreditsTeacher creditsTeacherDAO = sp.getIPersistentCreditsTeacher();
			creditsTeacherDAO.lockWrite(creditsTeacher);								
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException();
		} finally {
			return Boolean.TRUE;
		}
	}
}
