package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.ICoordinator;
import Dominio.ICurricularCourse;
import Dominio.IExecutionDegree;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

//modified by gedl AT rnl dot IST dot uTl dot pT , September the 16th, 2003
//added the auth to a lecturing teacher

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *  
 */
public class ReadShiftsByExecutionCourseIDAuthorizationFilter extends Filtro {

    public ReadShiftsByExecutionCourseIDAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        if ((((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                || (id != null && id.getRoles() != null && !hasPrivilege(id, argumentos))
                || (id == null) || (id.getRoles() == null)))
                && (!lecturesExecutionCourse(id, argumentos))) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TIME_TABLE_MANAGER);
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
    private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {

        List roles = getRoleList((List) id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.TIME_TABLE_MANAGER);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            return true;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {

            ITeacher teacher = null;
            // Read The ExecutionDegree
            try {

                Integer executionCourseID = (Integer) arguments[0];

                teacher = sp.getIPersistentTeacher().readTeacherByUsername(id.getUtilizador());

                IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                        .readByOID(ExecutionCourse.class, executionCourseID);

                // For all Associated Curricular Courses
                Iterator curricularCourseIterator = executionCourse.getAssociatedCurricularCourses()
                        .iterator();
                while (curricularCourseIterator.hasNext()) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourseIterator
                            .next();

                    // Read All Execution Degrees for this Degree Curricular
                    // Plan

                    List executionDegrees = sp.getIPersistentExecutionDegree()
                            .readByDegreeCurricularPlan(curricularCourse.getDegreeCurricularPlan());

                    // Check if the Coordinator is the logged one
                    Iterator executionDegreesIterator = executionDegrees.iterator();
                    while (executionDegreesIterator.hasNext()) {
                        IExecutionDegree executionDegree = (IExecutionDegree) executionDegreesIterator
                                .next();

                        //modified by Tânia Pousão
                        ICoordinator coordinator = sp.getIPersistentCoordinator()
                                .readCoordinatorByTeacherAndExecutionDegree(teacher, executionDegree);
                        if (coordinator != null) {
                            return true;
                        }

                        //						if ((executionDegree.getCoordinator() != null)
                        //							&&
                        // (executionDegree.getCoordinator().equals(teacher)))
                        //						{
                        //							return true;
                        //						}
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private List getRoleList(List roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        IExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        IProfessorship professorship = null;
        if (argumentos == null) {
            return false;
        }
        try {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            } else {
                executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                        ExecutionCourse.class, (Integer) argumentos[0]);
            }

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            if (teacher != null && executionCourse != null) {
                IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
                professorship = persistentProfessorship.readByTeacherAndExecutionCoursePB(teacher,
                        executionCourse);
            }
        } catch (Exception e) {
            return false;
        }
        return professorship != null;
    }

}