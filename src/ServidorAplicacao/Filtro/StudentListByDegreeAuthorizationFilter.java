package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoRole;
import Dominio.DegreeCurricularPlan;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByDegreeAuthorizationFilter extends Filtro
{

    public StudentListByDegreeAuthorizationFilter()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                        || (id != null && id.getRoles() != null && !hasPrivilege(id, argumentos))
                        || (id == null) || (id.getRoles() == null))
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
        }
        else
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

        Integer degreeCurricularPlanID = (Integer) arguments[0];
        TipoCurso degreeType = (TipoCurso) arguments[1];

        IDegreeCurricularPlan degreeCurricularPlan = null;

        // Read The DegreeCurricularPlan
        try
        {
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = SuportePersistenteOJB
                            .getInstance().getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlanTemp = new DegreeCurricularPlan();
            degreeCurricularPlanTemp.setIdInternal(degreeCurricularPlanID);

            degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(
                            degreeCurricularPlanTemp, false);
        }
        catch (Exception e)
        {
            return false;
        }

        if ((degreeCurricularPlan == null)
                        || (!degreeCurricularPlan.getDegree().getTipoCurso().equals(degreeType)))
        {
            return false;
        }

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp))
        {
            if (degreeCurricularPlan.getDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp))
        {

            // Read The ExecutionDegree
            try
            {
                ICursoExecucaoPersistente persistentExecutionDegree = SuportePersistenteOJB
                                .getInstance().getICursoExecucaoPersistente();

                List executionDegrees = persistentExecutionDegree
                                .readByDegreeCurricularPlan(degreeCurricularPlan);
                if (executionDegrees == null)
                {
                    return false;
                }
                // IMPORTANT: It's assumed that the coordinator for a Degree is
                // ALWAYS the same
                //modified by Tânia Pousão
                List coodinatorsList = SuportePersistenteOJB.getInstance().getIPersistentCoordinator()
                                .readCoordinatorsByExecutionDegree(
                                                ((ICursoExecucao) executionDegrees.get(0)));
                if (coodinatorsList == null)
                {
                    return false;
                }
                ListIterator listIterator = coodinatorsList.listIterator();
                while (listIterator.hasNext())
                {
                    ICoordinator coordinator = (ICoordinator) listIterator.next();

                    if (id.getUtilizador().equals(coordinator.getTeacher().getPerson().getUsername()))
                    {
                        return true;
                    }
                }

                //                teacher = ((ICursoExecucao)
                // executionDegrees.get(0)).getCoordinator();
                //
                //                if (teacher == null)
                //                {
                //                    return false;
                //                }
                //
                //                if
                // (id.getUtilizador().equals(teacher.getPerson().getUsername()))
                //                {
                //                    return true;
                //                } else
                //                {
                //                    return false;
                //                }
            }
            catch (Exception e)
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
