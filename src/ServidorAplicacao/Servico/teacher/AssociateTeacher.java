package ServidorAplicacao.Servico.teacher;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
/**
 * @author Fernanda Quitério
 *
 */
public class AssociateTeacher implements IServico {
	private static AssociateTeacher service = new AssociateTeacher();
	/**
	 * The singleton access method of this class.
	 **/
	public static AssociateTeacher getService() {
		return service;
	}
	/**
	 * The Actor of this class.
	 **/
	private AssociateTeacher() {
	}
	/**
	 * returns the service name
	 **/
	public final String getNome() {
		return "AssociateTeacher";
	}
	/**
	 * Executes the service.
	 *
	 **/
	public boolean run(Integer infoExecutionCourseCode, Integer teacherNumber) throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			
			ITeacher iTeacher = persistentTeacher.readTeacherByNumber(teacherNumber);
			if (iTeacher == null) {
				throw new InvalidArgumentsServiceException();
			}
			ExecutionCourse executionCourse = new ExecutionCourse(infoExecutionCourseCode);
			IExecutionCourse iExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
			persistentProfessorship.lockWrite(new Professorship(iTeacher, iExecutionCourse));
		} catch (ExistingPersistentException ex){
			throw new ExistingServiceException(ex); 
		}catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}
		return true;
	}
}