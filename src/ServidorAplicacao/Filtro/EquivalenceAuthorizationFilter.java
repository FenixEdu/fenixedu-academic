package ServidorAplicacao.Filtro;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoStudent;
import DataBeans.equivalence.InfoEquivalenceContext;
import Dominio.ICursoExecucao;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.Student;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author David Santos
 */

public class EquivalenceAuthorizationFilter extends Filtro
{

    public EquivalenceAuthorizationFilter()
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
        IUserView requester = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        Collection roles = requester.getRoles();
        boolean authorizedRequester = false;
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        // ATTENTION: ifs order matters...
        if (AuthorizationUtils.containsRole(roles, RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                        || AuthorizationUtils.containsRole(
                                        roles, RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)
                        || 
                        //			NOTE [DAVID]: Posso dar acesso a este RoleType?
                        AuthorizationUtils.containsRole(
                                        roles, RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE))
        {
            authorizedRequester = true;
        }
        else if (AuthorizationUtils.containsRole(roles, RoleType.COORDINATOR))
        {

            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                            .getIStudentCurricularPlanPersistente();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ICursoExecucaoPersistente persitentExecutionDegree = sp.getICursoExecucaoPersistente();

            if ((arguments[0] instanceof InfoStudent) || (arguments[0] instanceof Integer))
            {
                IStudent student = this.readStudent(arguments[0], sp);
                IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                                .readActiveStudentCurricularPlan(student.getNumber(), student
                                                .getDegreeType());
                ITeacher teacher = persistentTeacher.readTeacherByUsername(requester.getUtilizador());
                List executionDegreeList = persitentExecutionDegree.readByTeacher(teacher);

                Iterator iterator = executionDegreeList.iterator();
                while (iterator.hasNext())
                {
                    ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
                    if (executionDegree.getCurricularPlan().equals(
                                    studentCurricularPlan.getDegreeCurricularPlan()))
                    {
                        authorizedRequester = true;
                        break;
                    }
                }
            }
            else if (arguments[0] instanceof InfoEquivalenceContext)
            {
                authorizedRequester = true;
            }
        }

        if (!authorizedRequester)
        {
            throw new NotAuthorizedFilterException(" -----------> User = " + requester.getUtilizador()
                            + "ACCESS NOT GRANTED!");
        }

    }

    private IStudent readStudent(Object object, ISuportePersistente sp)
    {

        Integer studentOID = null;

        if (object instanceof InfoStudent)
        {
            studentOID = ((InfoStudent) object).getIdInternal();
        }
        else if (object instanceof Integer)
        {
            studentOID = (Integer) object;
        }

        return (IStudent) sp.getIPersistentStudent().readByOId(new Student(studentOID), false);
    }
}