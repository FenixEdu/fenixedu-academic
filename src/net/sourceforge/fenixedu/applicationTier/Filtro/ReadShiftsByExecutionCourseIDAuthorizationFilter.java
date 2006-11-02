package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

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
        if ((((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, argumentos))
                || (id == null) || (id.getRoleTypes() == null)))
                && (!lecturesExecutionCourse(id, argumentos))) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.TIME_TABLE_MANAGER);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {
        if (id.hasRoleType(RoleType.TIME_TABLE_MANAGER)) {
            return true;
        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            // Read The ExecutionDegree
            Integer executionCourseID = (Integer) arguments[0];

            final Person person = id.getPerson();

            ExecutionCourse executionCourse = rootDomainObject
                    .readExecutionCourseByOID(executionCourseID);

            // For all Associated Curricular Courses
            Iterator curricularCourseIterator = executionCourse.getAssociatedCurricularCourses()
                    .iterator();
            while (curricularCourseIterator.hasNext()) {
                CurricularCourse curricularCourse = (CurricularCourse) curricularCourseIterator.next();

                // Read All Execution Degrees for this Degree Curricular
                // Plan

                List executionDegrees = curricularCourse.getDegreeCurricularPlan().getExecutionDegrees();

                // Check if the Coordinator is the logged one
                Iterator executionDegreesIterator = executionDegrees.iterator();
                while (executionDegreesIterator.hasNext()) {
                    ExecutionDegree executionDegree = (ExecutionDegree) executionDegreesIterator.next();

                    // modified by Tânia Pousão
                    Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

                    if (coordinator != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {

        Integer executionCourseID = null;

        if (argumentos == null) {
            return false;
        }
        try {
            if (argumentos[0] instanceof InfoExecutionCourse) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourseID = infoExecutionCourse.getIdInternal();
            } else {
                executionCourseID = (Integer) argumentos[0];
            }

            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
            Professorship professorship = null;
            if (teacher != null) {
                ExecutionCourse executionCourse = rootDomainObject
                        .readExecutionCourseByOID(executionCourseID);
                teacher.getProfessorshipByExecutionCourse(executionCourse);
            }
            return professorship != null;

        } catch (Exception e) {
            return false;
        }
    }
}