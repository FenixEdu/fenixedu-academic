package ServidorAplicacao.Servico.gesdis.teacher;
/**
 * @author João Mota
 */
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
public class RemoveTeacher implements IServico {
	private static RemoveTeacher service = new RemoveTeacher();
	/**
	 * The singleton access method of this class.
	 **/
	public static RemoveTeacher getService() {
		return service;
	}
	/**
	 * The Actor of this class.
	 **/
	private RemoveTeacher() {
	}
	/**
	 * Returns service name
	 **/
	public final String getNome() {
		return "RemoveTeacher";
	}
	/**
	 * Executes the service.	
	 **/
	public Boolean run(
		InfoExecutionCourse infoExecutionCourse,
		Integer teacherNumber)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
			IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			ITeacher teacher =
				persistentTeacher.readTeacherByNumber(teacherNumber);
			
			if (teacher == null) {
				throw new InvalidArgumentsServiceException();
			}
			IDisciplinaExecucao executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
						infoExecutionCourse.getInfoExecutionPeriod()));
					
			//note: removed the possibility for a responsible teacher to remove from himself the professorship (it was a feature that didnt make sense)
			IResponsibleFor responsibleFor = persistentResponsibleFor.readByTeacherAndExecutionCourse(teacher,executionCourse);
					if(responsibleFor!=null) {
				throw new notAuthorizedServiceDeleteException();
			}
			IProfessorship professorshipToDelete = persistentProfessorship.readByTeacherAndExecutionCourse(teacher,executionCourse);
			persistentProfessorship.delete(professorshipToDelete);
			
			return Boolean.TRUE;
		} catch (ExcepcaoPersistencia e) {
			
			throw new FenixServiceException(e);
		}
	}
}
