/*
 * Created on 6/Fev/2004
 *  
 */
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DataBeans.InfoRole;
import Dominio.ICoordinator;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.ITutor;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class EnrollmentLEECAuthorizationFilter extends AuthorizationByManyRolesFilter
{
	private static String DEGREE_LEEC_CODE = new String("LEEC");

	protected Collection getNeededRoles()
	{
		List roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.COORDINATOR);
		roles.add(infoRole);

		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TEACHER);
		roles.add(infoRole);

		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);
		roles.add(infoRole);

		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
		roles.add(infoRole);

		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
		roles.add(infoRole);
		return roles;
	}

	protected String hasProvilege(IUserView id, Object[] arguments)
	{
		try
		{
			List roles = getRoleList((List) id.getRoles());

			ISuportePersistente sp = null;
			sp = SuportePersistenteOJB.getInstance();

			//verify if the student making the enrollment is a LEEC degree student
			if (roles.contains(RoleType.STUDENT))
			{
				IStudent student = readStudent(id, sp);
				if (student == null)
				{
					return "noAuthorization";
				}

				if (!verifyStudentLEEC(arguments, sp))
				{
					return new String("error.student.degreeCurricularPlan.LEEC");
				}
				if (!curriculumOwner(student, id))
				{
					return "noAuthorization";

				}
				ITutor tutor = verifyStudentWithTutor(student, sp);
				if (tutor != null)
				{
					return new String(
						"error.enrollment.student.withTutor+"
							+ tutor.getTeacher().getTeacherNumber().toString()
							+ "+"
							+ tutor.getTeacher().getPerson().getNome());
				}
			}
			else
			{
				//verify if the student to enroll is a LEEC degree student
				if (!verifyStudentLEEC(arguments, sp))
				{
					return new String("error.student.degreeCurricularPlan.LEEC");
				}

				//verify if the coodinator is of the LEEC degree
				if (roles.contains(RoleType.COORDINATOR) && arguments[0] != null)
				{
					ITeacher teacher = readTeacher(id, sp);
					if (teacher == null)
					{
						return "noAuthorization";
					}

					if (!verifyCoordinatorLEEC(teacher, arguments, sp))
					{
						return "noAuthorization";
					}
				}
				else if (roles.contains(RoleType.TEACHER))
				{
					ITeacher teacher = readTeacher(id, sp);
					if (teacher == null)
					{
						return "noAuthorization";
					}

					IStudent student = readStudent(arguments, sp);
					if (student == null)
					{
						return "noAuthorization";
					}

					if (!verifyStudentTutor(teacher, student, sp))
					{
						return new String(
							"error.enrollment.notStudentTutor+" + student.getNumber().toString());
					}

				}
				else if (
					roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
						|| roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))
				{
					IStudentCurricularPlan studentCurricularPlan =
						readStudentCurricularPlan(arguments, sp);

					if (studentCurricularPlan.getStudent() == null)
					{
						return "noAuthorization";
					}
					if (insideEnrollmentPeriod(studentCurricularPlan, sp))
					{
						ITutor tutor = verifyStudentWithTutor(studentCurricularPlan.getStudent(), sp);
						if (tutor != null)
						{
							return new String(
								"error.enrollment.student.withTutor+"
									+ tutor.getTeacher().getTeacherNumber().toString()
									+ "+"
									+ tutor.getTeacher().getPerson().getNome());
						}
					}
					return null;
				}
				else
				{
					return "noAuthorization";
				}
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return "noAuthorization";
		}
		return null;
	}

	/**
	 * @param arguments
	 * @param sp
	 * @return
	 */
	private boolean insideEnrollmentPeriod(
		IStudentCurricularPlan studentCurricularPlan,
		ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IPersistentEnrolmentPeriod persistentEnrolmentPeriod = sp.getIPersistentEnrolmentPeriod();
		IEnrolmentPeriod enrolmentPeriod =
			persistentEnrolmentPeriod.readActualEnrolmentPeriodForDegreeCurricularPlan(
				studentCurricularPlan.getDegreeCurricularPlan());

		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();

		return executionPeriod.equals(enrolmentPeriod.getExecutionPeriod());
	}

	/**
	 * @param arguments
	 * @return
	 */
	private boolean studentWithTutor(Object[] arguments, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments, sp);

		IPersistentTutor persistentTutor = sp.getIPersistentTutor();
		ITutor tutor = persistentTutor.readTeachersByStudent(studentCurricularPlan.getStudent());
		if (tutor == null)
		{
			return false;
		}

		return true;
	}

	private IStudentCurricularPlan readStudentCurricularPlan(Object[] arguments, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
			sp.getIStudentCurricularPlanPersistente();

		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		if (arguments[1] != null)
		{
			studentCurricularPlan.setIdInternal((Integer) arguments[1]);
			studentCurricularPlan =
				(IStudentCurricularPlan) persistentStudentCurricularPlan.readByOId(
					studentCurricularPlan,
					false);
		}
		else
		{
			studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(
					(Integer) arguments[2],
					TipoCurso.LICENCIATURA_OBJ);
		}
		return studentCurricularPlan;
	}

	private IStudent readStudent(IUserView id, ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		IPersistentStudent persistentStudent = sp.getIPersistentStudent();

		return persistentStudent.readByUsername(id.getUtilizador());
	}

	private IStudent readStudent(Object[] arguments, ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments, sp);
		if (studentCurricularPlan == null)
		{
			return null;
		}

		return studentCurricularPlan.getStudent();
	}

	private boolean verifyStudentLEEC(Object[] arguments, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments, sp);
		if (studentCurricularPlan == null)
		{
			return false;
		}

		String degreeCode = null;
		if (studentCurricularPlan.getDegreeCurricularPlan() != null
			&& studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null)
		{
			degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla();
		}

		return DEGREE_LEEC_CODE.equals(degreeCode);
	}

	private boolean curriculumOwner(IStudent student, IUserView id)
	{
		if (!student.getPerson().getUsername().equals(id.getUtilizador()))
		{
			return false;
		}
		return true;
	}

	/**
	 * @param integer
	 * @param sp
	 * @return
	 */
	private ITutor verifyStudentWithTutor(IStudent student, ISuportePersistente sp)
	throws ExcepcaoPersistencia
	{
		IPersistentTutor persistentTutor = sp.getIPersistentTutor();

		ITutor tutor = persistentTutor.readTeachersByStudent(student);

		return tutor;
	}

	private ITeacher readTeacher(IUserView id, ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

		return persistentTeacher.readTeacherByUsername(id.getUtilizador());
	}

	private boolean verifyCoordinatorLEEC(ITeacher teacher, Object[] arguments, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{

		IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
		ICoordinator coordinator =
			persistentCoordinator.readCoordinatorByTeacherAndExecutionDegreeId(
				teacher,
				(Integer) arguments[0]);
		if (coordinator == null)
		{
			return false;
		}

		String degreeCode = null;
		if (coordinator.getExecutionDegree() != null
			&& coordinator.getExecutionDegree().getCurricularPlan() != null
			&& coordinator.getExecutionDegree().getCurricularPlan().getDegree() != null)
		{
			degreeCode = coordinator.getExecutionDegree().getCurricularPlan().getDegree().getSigla();
		}

		return DEGREE_LEEC_CODE.equals(degreeCode);
	}

	/**
	 * @param teacher
	 * @param arguments
	 * @param sp
	 * @return
	 */
	private boolean verifyStudentTutor(ITeacher teacher, IStudent student, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		IPersistentTutor persistentTutor = sp.getIPersistentTutor();

		ITutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher, student);

		return (tutor != null);
	}
}