/*
 * Created on 19/Mai/2003
 *
 * 
 */
package ServidorAplicacao.Filtro;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISection;
import Dominio.ITeacher;
import Dominio.Section;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author João Mota
 *
 */
public class ExecutionCourseAndSectionLecturingTeacherAuthorizationFilter
	extends AuthorizationByRoleFilter {

	public final static ExecutionCourseAndSectionLecturingTeacherAuthorizationFilter instance =
		new ExecutionCourseAndSectionLecturingTeacherAuthorizationFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType() {
		return RoleType.TEACHER;
	}

	public void preFiltragem(
		IUserView id,
		IServico servico,
		Object[] argumentos)
		throws Exception {
		if ((id == null)
			|| (id.getRoles() == null)
			|| !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
			|| !lecturesExecutionCourse(id, argumentos)
			|| !sectionBelongsExecutionCourse(id, argumentos)) {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean sectionBelongsExecutionCourse(
		IUserView id,
		Object[] argumentos) {
		InfoExecutionCourse infoExecutionCourse = null;
		IExecutionCourse executionCourse = null;
		ISuportePersistente sp;
		ISection section = null;
		InfoSection infoSection = null;

		if (argumentos == null) {
			return false;
		}
		try {

			sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			if (argumentos[0] instanceof InfoExecutionCourse) {
				infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse);
			} else {
				executionCourse =
					(IExecutionCourse) persistentExecutionCourse.readByOId(
						new ExecutionCourse((Integer) argumentos[0]),
						false);
			}
			IPersistentSection persistentSection = sp.getIPersistentSection();
			if (argumentos[1]==null) {
				return true;
			}
			if (argumentos[1] instanceof InfoSection) {
				infoSection = (InfoSection) argumentos[1];
				section = Cloner.copyInfoSection2ISection(infoSection);
				section =
					(ISection) persistentSection.readByOId(section, false);
			} else {
				section =
					(ISection) persistentSection.readByOId(
						new Section((Integer) argumentos[1]),
						false);

			}
		} catch (Exception e) {
			return false;
		}
		
		if(section == null)
			return false;
		else
			return section.getSite().getExecutionCourse().equals(executionCourse);
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	private boolean lecturesExecutionCourse(
		IUserView id,
		Object[] argumentos) {
		InfoExecutionCourse infoExecutionCourse = null;
		IExecutionCourse executionCourse = null;
		ISuportePersistente sp;
		IProfessorship professorship = null;
		if (argumentos == null) {
			return false;
		}
		try {

			sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			if (argumentos[0] instanceof InfoExecutionCourse) {
				infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						infoExecutionCourse);
			} else {
				executionCourse =
					(IExecutionCourse) persistentExecutionCourse.readByOId(
						new ExecutionCourse((Integer) argumentos[0]),
						false);
			}

			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			ITeacher teacher =
				persistentTeacher.readTeacherByUsernamePB(id.getUtilizador());
			if (teacher != null && executionCourse != null) {
				IPersistentProfessorship persistentProfessorship =
					sp.getIPersistentProfessorship();
				professorship =
					persistentProfessorship.readByTeacherAndExecutionCoursePB(
						teacher,
						executionCourse);
			}
		} catch (Exception e) {
			return false;
		}
		return professorship != null;
	}

}
