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
import ServidorPersistente.exceptions.ExistingPersistentException;

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
		return "WriteCreditsTeacher";
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

			// mark teacher's credits for write
			IPersistentCreditsTeacher creditsTeacherDAO = sp.getIPersistentCreditsTeacher();

			try {
				if (tfcStudentNumber.intValue() == 0) {
					//delete credits because is zero
					creditsTeacher = creditsTeacherDAO.readByUnique(creditsTeacher);
					if (creditsTeacher != null) {
						creditsTeacherDAO.delete(creditsTeacher);
					}
				} else {
					creditsTeacherDAO.lockWrite(creditsTeacher);
				}
			} catch (ExistingPersistentException e) {
				creditsTeacher = creditsTeacherDAO.readByUnique(creditsTeacher);
			}
			creditsTeacher.setTfcStudentsNumber(tfcStudentNumber);
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException();
		} finally {
			return Boolean.TRUE;
		}
	}
}
