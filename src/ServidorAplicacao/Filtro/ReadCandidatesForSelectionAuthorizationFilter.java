package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoRole;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelectionAuthorizationFilter extends Filtro
{

    public final static ReadCandidatesForSelectionAuthorizationFilter instance =
        new ReadCandidatesForSelectionAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    public void preFiltragem(IUserView id, Object[] argumentos) throws Exception
    {

        if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
            || (id != null && id.getRoles() != null && !hasPrivilege(id, argumentos))
            || (id == null)
            || (id.getRoles() == null))
        {
            throw new NotAuthorizedException();
        }
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
        } else
        {
            return false;
        }
    }

    /**
	 * @return The Needed Roles to Execute The Service
	 */
    private Collection getNeededRoles()
    {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        return roles;
    }

    /**
	 * @param id
	 * @param argumentos
	 * @return
	 */
    private boolean hasPrivilege(IUserView id, Object[] arguments)
    {

        List roles = getRoleList((List) id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        String executionYearString = (String) arguments[0];
        String degreeCode = (String) arguments[1];

        ICursoExecucao executionDegree = null;

        // Read The DegreeCurricularPlan
        try
        {
            IExecutionYear executionYear = new ExecutionYear();
            executionYear.setYear(executionYearString);
            executionDegree =
                SuportePersistenteOJB
                    .getInstance()
                    .getICursoExecucaoPersistente()
                    .readByDegreeCodeAndExecutionYear(
                    degreeCode,
                    executionYear);

        } catch (Exception e)
        {
            return false;
        }

        if (executionDegree == null)
        {
            return false;
        }

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp))
        {
            if (executionDegree
                .getCurricularPlan()
                .getDegree()
                .getTipoCurso()
                .equals(TipoCurso.MESTRADO_OBJ))
            {
                return true;
            } else
            {
                return false;
            }
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp))
        {

            ITeacher teacher = null;
            // Read The ExecutionDegree
            try
            {
                // IMPORTANT: It's assumed that the coordinator for a Degree is ALWAYS the same
                teacher = executionDegree.getCoordinator();

                if (teacher == null)
                {
                    return false;
                }

                if (id.getUtilizador().equals(teacher.getPerson().getUsername()))
                {
                    return true;
                } else
                {
                    return false;
                }
            } catch (Exception e)
            {
                return false;
            }
        }
        return false;
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

}
