/*
 * Created on 19/Fev/2004
 *  
 */
package ServidorAplicacao.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DataBeans.InfoRole;
import DataBeans.InfoStudent;
import Dominio.IStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByManyRolesFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class EnrollmentWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter
{
	
	private static TipoCurso DEGREE_TYPE = TipoCurso.LICENCIATURA_OBJ;

	protected Collection getNeededRoles()
	{
		List roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
		roles.add(infoRole);

		infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
		roles.add(infoRole);
		return roles;
	}

	protected String hasPrevilege(IUserView id, Object[] arguments)
	{
		try
		{
			ISuportePersistente sp = null;
			sp = SuportePersistenteOJB.getInstance();

			//verify if the degree type is LICENCIATURA_OBJ
			if (!verifyDegreeTypeIsNonMaster(arguments))
			{
				return new String("error.degree.type");
			}

			//verify if the student to enroll is a non master degree student
			if (!verifyStudentNonMasterDegree(arguments, sp))
			{
				return new String("error.student.degree.nonMaster");
			}

			

			return null;
		}
		catch (Exception exception)
		{
			return "noAuthorization";
		}
	}

	private boolean verifyDegreeTypeIsNonMaster(Object[] arguments)
	{
		boolean isNonMaster = false;

		if (arguments != null && arguments[1] != null)
		{
			isNonMaster = DEGREE_TYPE.equals(arguments[1]);
		}

		return isNonMaster;
	}

	private boolean verifyStudentNonMasterDegree(Object[] arguments, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		boolean isNonMaster = false;

		if (arguments != null && arguments[0] != null)
		{
			Integer studentNumber = ((InfoStudent) arguments[0]).getNumber();
			if (studentNumber != null)
			{
				IPersistentStudent persistentStudent = sp.getIPersistentStudent();
				IStudent student =
					persistentStudent.readStudentByNumberAndDegreeType(studentNumber, DEGREE_TYPE);
				if (student != null)
				{
					isNonMaster = true; //non master student
				}
			}
		}

		return isNonMaster;
	}

	
}