package ServidorAplicacao.Servico.gesdis.teacher;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
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
	public boolean run(
		InfoExecutionCourse infoExecutionCourse,
		Integer teacherNumber)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentProfessorship persistentProfessorship =
				sp.getIPersistentProfessorship();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			ITeacher teacher = null;
			IDisciplinaExecucao executionCourse = null;
			teacher = persistentTeacher.readTeacherByNumber(teacherNumber);
			executionCourse =
				persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
						infoExecutionCourse.getInfoExecutionPeriod()));

			if (teacher == null) {
				throw new InvalidArgumentsServiceException();
			} else {
				persistentProfessorship.lockWrite(
					new Professorship(teacher, executionCourse));

				return true;
			}
		} 
		catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		}
		catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}
	}
}
