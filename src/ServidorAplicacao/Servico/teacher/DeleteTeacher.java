package ServidorAplicacao.Servico.teacher;
import Dominio.DisciplinaExecucao;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
/**
 *@author Fernanda Quitério
 *
 */
public class DeleteTeacher implements IServico {
	private static DeleteTeacher service = new DeleteTeacher();
	/**
	 * The singleton access method of this class.
	 **/
	public static DeleteTeacher getService() {
		return service;
	}
	/**
	 * The Actor of this class.
	 **/
	private DeleteTeacher() {
	}
	/**
	 * Returns service name
	 **/
	public final String getNome() {
		return "DeleteTeacher";
	}
	/**
	 * Executes the service.	
	 **/
	public Boolean run(Integer infoExecutionCourseCode, Integer teacherCode) throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
			IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();

			Teacher teacher = new Teacher(teacherCode);
			ITeacher iTeacher = (ITeacher) persistentTeacher.readByOId(teacher, false);
			if (teacher == null) {
				throw new InvalidArgumentsServiceException();
			}
			
			DisciplinaExecucao executionCourse = new DisciplinaExecucao(infoExecutionCourseCode);
			IExecutionCourse iExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
			
			//note: removed the possibility for a responsible teacher to remove from himself the professorship 
			//(it was a feature that didnt make sense)
			IResponsibleFor responsibleFor = persistentResponsibleFor.readByTeacherAndExecutionCourse(iTeacher, iExecutionCourse);
			if (responsibleFor != null) {
				throw new notAuthorizedServiceDeleteException();
			}
			IProfessorship professorshipToDelete = persistentProfessorship.readByTeacherAndExecutionCourse(iTeacher, iExecutionCourse);
			persistentProfessorship.delete(professorshipToDelete);
			return Boolean.TRUE;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}