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
 * @author David Santos in Mar 1, 2004
 */

public class MasterDegreeEnrollmentWithoutRulesAuthorizationFilter extends AuthorizationByManyRolesFilter
{
	private static TipoCurso DEGREE_TYPE = TipoCurso.MESTRADO_OBJ;

	protected Collection getNeededRoles()
	{
		List roles = new ArrayList();

		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		roles.add(infoRole);

		return roles;
	}

	protected String hasPrevilege(IUserView id, Object[] arguments)
	{
		try
		{
			ISuportePersistente sp = null;
			sp = SuportePersistenteOJB.getInstance();

			if (!verifyDegreeTypeIsMasterDegree(arguments))
			{
				return new String("error.degree.type");
			}

			if (!verifyStudentIsFromMasterDegree(arguments, sp))
			{
				return new String("error.student.degree.nonMaster");
			}

			return null;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return "noAuthorization";
		}
	}

	private boolean verifyDegreeTypeIsMasterDegree(Object[] arguments)
	{
		boolean isNonMaster = false;

		if (arguments != null && arguments[1] != null)
		{
			isNonMaster = DEGREE_TYPE.equals(arguments[1]);
		}

		return isNonMaster;
	}

	private boolean verifyStudentIsFromMasterDegree(Object[] arguments, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		boolean isFromMasterDegree = false;

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
					isFromMasterDegree = true;
				}
			}
		}

		return isFromMasterDegree;
	}
}