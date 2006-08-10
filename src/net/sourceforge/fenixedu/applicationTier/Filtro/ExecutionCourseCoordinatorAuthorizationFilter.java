/*
 * Created on 19/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 */
public class ExecutionCourseCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExecutionCourseCoordinatorAuthorizationFilter() {

    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try {
            if ((id == null) || (id.getRoleTypes() == null)
                    || !id.hasRoleType(getRoleType())
                    || !hasExecutionCourseInCurricularCourseList(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasExecutionCourseInCurricularCourseList(IUserView id, Object[] argumentos) {
        boolean result = false;
        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;

        if (argumentos == null) {
            return result;
        }
        try {
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
            } else {
                executionCourse = rootDomainObject.readExecutionCourseByOID((Integer) argumentos[0]);
            }

            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());

            if (teacher != null && executionCourse != null) {
                List<ExecutionDegree> executionDegrees = teacher.getCoordinatedExecutionDegrees();
                if (executionDegrees != null && !executionDegrees.isEmpty()) {
                    Iterator iter = executionDegrees.iterator();
                    while (iter.hasNext() && !result) {
                        ExecutionDegree executionDegree = (ExecutionDegree) iter.next();
                        if (executionDegree.getExecutionYear().equals(
                                executionCourse.getExecutionPeriod().getExecutionYear())) {
                            if (CollectionUtils.containsAny(executionDegree.getDegreeCurricularPlan()
                                    .getCurricularCourses(), executionCourse
                                    .getAssociatedCurricularCourses())) {
                                result = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return result;
    }

}
