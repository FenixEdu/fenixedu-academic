/*
 * Created on Nov 12, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.degree.finalProject;

import java.util.List;

import Dominio.IDepartment;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectFilter extends AuthorizationByRoleFilter
{
	// the singleton of this class
	public final static TeacherDegreeFinalProjectFilter instance =
		new TeacherDegreeFinalProjectFilter();

	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance() {
		return instance;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	protected RoleType getRoleType()
	{
		// TODO Auto-generated method stub
		return RoleType.DEPARTMENT_CREDITS_MANAGER;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.Filtro#preFiltragem(ServidorAplicacao.IUserView,
	 *          ServidorAplicacao.IServico, java.lang.Object[])
	 */
	public void preFiltragem(IUserView id, IServico servico, Object[] argumentos) throws Exception
	{

		super.preFiltragem(id, servico, argumentos);
		verifyTeacherPermission(id, (Integer) argumentos[0]);
	}
	/**
	 * @param integer
	 */
	private void verifyTeacherPermission(IUserView requester, Integer teacherNumber) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

			ITeacher teacher = teacherDAO.readByNumber(teacherNumber);
			if (teacher == null)
			{
				throw new NonExistingServiceException("Teacher doesn't exists");
			}

			IPessoaPersistente personDAO = sp.getIPessoaPersistente();
			IPessoa requesterPerson = personDAO.lerPessoaPorUsername(requester.getUtilizador());
			if (requesterPerson == null)
			{
				throw new NotAuthorizedException("No person with that userView");
			}

			List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();
			IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();

			IDepartment department = departmentDAO.readByTeacher(teacher);

			if (department == null)
			{
				throw new NotAuthorizedException("Teacher number " + teacher.getTeacherNumber() + " doesn't have department!");
			}

			if (!departmentsWithAccessGranted.contains(department))
			{
				throw new NotAuthorizedException("Not authorized to run the service!");
			}

		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException("Problems with database!", e);

		}

	}

}
