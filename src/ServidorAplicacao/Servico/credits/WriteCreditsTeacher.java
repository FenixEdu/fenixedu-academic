package ServidorAplicacao.Servico.credits;

import DataBeans.teacher.credits.InfoCredits;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
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
		return "WriteCreditsTeacher";
	}

	public Boolean run(Integer teacherOID, InfoCredits infoCredits)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			ITeacher teacherParam = new Teacher(teacherOID);
			ITeacher teacher =
				(ITeacher) teacherDAO.readByOId(teacherParam, false);

			IPersistentExecutionPeriod executionPeriodDAO =
				sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod =
				executionPeriodDAO.readActualExecutionPeriod();

			ICredits creditsTeacher = new Credits();
			creditsTeacher.setTeacher(teacher);
			creditsTeacher.setExecutionPeriod(executionPeriod);

			// mark teacher's credits for write
//			IPersistentCreditsTeacher creditsTeacherDAO =
//				sp.getIPersistentCreditsTeacher();
//			Integer tfcStudentNumber = infoCredits.getTfcStudentsNumber();
			
//			if (tfcStudentNumber == null || tfcStudentNumber.intValue() == 0) {
//				//delete credits because is zero
////				creditsTeacher =
////					(ICredits) creditsTeacherDAO.readByUnique(
////						creditsTeacher,
////						false);
////				if (creditsTeacher != null) {
////					creditsTeacherDAO.delete(creditsTeacher);
////				}
//			} else {
//////				ICredits creditsTeacherReaded =
//////					(ICredits) creditsTeacherDAO.readByUnique(
//////						creditsTeacher,
//////						true);
////				if (creditsTeacherReaded == null) {
////					creditsTeacherDAO.simpleLockWrite(creditsTeacher);
////				} else
////					creditsTeacher = creditsTeacherReaded;
////
//			}
//			creditsTeacher.setTfcStudentsNumber(tfcStudentNumber);
//			creditsTeacher.setAdditionalCredits(infoCredits.getAdditionalCredits());
//			creditsTeacher.setAdditionalCreditsJustification(infoCredits.getAdditionalCreditsJustification());
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException();
		}
		return Boolean.TRUE;
	}
}
