package ServidorAplicacao.Filtro.enrollment.shift;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoRole;
import DataBeans.InfoStudent;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionCourse;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Student;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AccessControlFilter;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/*
 * 
 * @author Fernanda Quitério 13/Fev/2004
 *  
 */
public class StudentShiftEnrollmentAuthorizationFilter extends AccessControlFilter
{

	private static String DEGREE_LEEC_CODE = new String("LEEC");

	public StudentShiftEnrollmentAuthorizationFilter()
	{
		super();
	}

	/**
	 * @param collection
	 * @return boolean
	 */
	private boolean containsRole(Collection roles)
	{
		CollectionUtils.intersection(roles, getNeededRoles());

		if (roles.size() != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
	 *      pt.utl.ist.berserk.ServiceResponse)
	 */
	public void execute(ServiceRequest request, ServiceResponse response) throws Exception
	{
		IUserView id = (IUserView) request.getRequester();
		String messageException = hasProvilege(id, request.getArguments());
		if ((id == null)
			|| (id.getRoles() == null)
			|| (!containsRole(id.getRoles()))
			|| (messageException != null))
		{
			throw new NotAuthorizedFilterException(messageException);
		}
	}

	/**
	 * @return The Needed Roles to Execute The Service
	 */
	private Collection getNeededRoles()
	{
		List roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.STUDENT);
		roles.add(infoRole);

		return roles;
	}

	private List getRoleList(List roles)
	{
		List result = new ArrayList();
		Iterator iterator = roles.iterator();
		while (iterator.hasNext())
		{
			result.add(((InfoRole) iterator.next()).getRoleType());
		}

		return result;
	}

	/**
	 * @param id
	 * @param argumentos
	 * @return null if authorized string with message if not authorized
	 */
	private String hasProvilege(IUserView id, Object[] arguments)
	{
		List roles = getRoleList((List) id.getRoles());
		CollectionUtils.intersection(roles, getNeededRoles());

		InfoStudent infoStudent = (InfoStudent) arguments[0];
		Integer executionCourseIdToAttend = (Integer) arguments[1];

		IStudentCurricularPlan studentCurricularPlan = null;

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			/* :AQUI: o que chega é o id do student */
		IStudent student =  (IStudent) sp.getIPersistentStudent().readByOId(new Student(infoStudent.getIdInternal()),false);
		
		Integer studentNumber = student.getNumber();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();

			studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(
					studentNumber,
					TipoCurso.LICENCIATURA_OBJ);

		}
		catch (Exception e)
		{
			return "noAuthorization";
		}

		if (studentCurricularPlan == null || studentCurricularPlan.getStudent() == null)
		{
			return "noAuthorization";
		}

		List roleTemp = new ArrayList();
		roleTemp.add(RoleType.STUDENT);
		if (CollectionUtils.containsAny(roles, roleTemp))
		{
			try
			{
				if (!id
					.getUtilizador()
					.equals(studentCurricularPlan.getStudent().getPerson().getUsername()))
				{
					return "noAuthorization";
				}

				if (verifyStudentLEEC(studentCurricularPlan))
				{
					if(!findEnrollmentForAttend(studentCurricularPlan, executionCourseIdToAttend)){
						return "noAuthorization";
					}
				}
			}
			catch (Exception e)
			{
				return "noAuthorization";
			}
			return null;
		}
		return "noAuthorization";
	}

	private boolean findEnrollmentForAttend(
		IStudentCurricularPlan studentCurricularPlan,
		Integer executionCourseIdToAttend)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
		IExecutionCourse executionCourse = findExecutionCourse(executionCourseIdToAttend, sp);
		
		if(executionCourse == null) {
			return false;
		}
		// checks if there is an enrollment for this attend
		Iterator iterCurricularCourses = executionCourse.getAssociatedCurricularCourses().iterator();
		while (iterCurricularCourses.hasNext())
		{
			ICurricularCourse curricularCourseElem = (ICurricularCourse) iterCurricularCourses.next();

			IEnrolment enrollment =
				persistentEnrolment.readByStudentCurricularPlanAndCurricularCourse(
					studentCurricularPlan,
					curricularCourseElem);
			if (enrollment != null)
			{
				return true;
			}
		}
		return false;
	}

	private IExecutionCourse findExecutionCourse(Integer executionCourseIdToAttend, ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

		IExecutionCourse executionCourse =
			(IExecutionCourse) persistentExecutionCourse.readByOID(
				ExecutionCourse.class,
				executionCourseIdToAttend);
		return executionCourse;
	}

	private boolean verifyStudentLEEC(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		String degreeCode = null;
		if (studentCurricularPlan.getDegreeCurricularPlan() != null
			&& studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null)
		{
			degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla();
		}

		return DEGREE_LEEC_CODE.equals(degreeCode);
	}
}
