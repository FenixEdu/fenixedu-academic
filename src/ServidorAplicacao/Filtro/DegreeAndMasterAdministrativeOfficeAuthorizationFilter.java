package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author David Santos
 *  
 */

public class DegreeAndMasterAdministrativeOfficeAuthorizationFilter extends AuthorizationByRoleFilter
{

    public final static DegreeAndMasterAdministrativeOfficeAuthorizationFilter instance =
        new DegreeAndMasterAdministrativeOfficeAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the
	 *         authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    protected RoleType getRoleType()
    {
        return RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
    }

    public void execute(ServiceRequest arg0, ServiceResponse arg1) throws Exception
    {
        IUserView id = (IUserView) arg0.getRequester();
        // TODO : Verify this works, i.e., that it doesn't result in a class
		// cast exception
        IServico servico = (IServico) arg0.getService();
        Object[] argumentos = arg0.getArguments();

        if (AuthorizationUtils
            .containsRole(id.getRoles(), RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE))
        {
            if (!this
                .okToExecute(
                    RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE,
                    TipoCurso.MESTRADO,
                    servico,
                    argumentos))
            {
                throw new NotAuthorizedFilterException();
            }
        }
        else if (
            AuthorizationUtils.containsRole(
                id.getRoles(),
                RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER))
        {
            if (!this
                .okToExecute(
                    RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER,
                    TipoCurso.LICENCIATURA,
                    servico,
                    argumentos))
            {
                throw new NotAuthorizedFilterException();
            }
        }
        else
        {
            throw new NotAuthorizedFilterException();
        }

    }

    private boolean okToExecute(
        RoleType userViewRoleType,
        int degreeType,
        IServico servico,
        Object[] argumentos)
    {
        if (servico.getNome().equals("GetStudentByNumberAndDegreeType"))
        {
            if (((Integer) argumentos[0]).intValue() != degreeType)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else if (servico.getNome().equals("ReadExecutionDegreesByExecutionYearAndDegreeType"))
        {
            if (userViewRoleType.equals(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE))
            {
                return true;
            }
            else if (((TipoCurso) argumentos[1]).getTipoCurso().intValue() != degreeType)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else if (servico.getNome().equals("PrepareEnrolmentContext"))
        {
            if (((InfoStudent) argumentos[0]).getDegreeType().getTipoCurso().intValue() != degreeType)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else if (servico.getNome().equals("ValidateActualEnrolmentWithoutRules"))
        {
            if (((InfoEnrolmentContext) argumentos[0])
                .getInfoStudentActiveCurricularPlan()
                .getInfoStudent()
                .getDegreeType()
                .getTipoCurso()
                .intValue()
                != degreeType)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else if (servico.getNome().equals("ConfirmActualEnrolmentWithoutRules"))
        {
            if (((InfoEnrolmentContext) argumentos[0])
                .getInfoStudentActiveCurricularPlan()
                .getInfoStudent()
                .getDegreeType()
                .getTipoCurso()
                .intValue()
                != degreeType)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else if (servico.getNome().equals("ReadAllExecutionPeriodsForEnrollment"))
        {
            return true;
        }
        else if (servico.getNome().equals("ReadExecutionPeriodByOIDForEnrollment"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}