/*
 * Created on 19/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author João Mota
 */
public class ExecutionCourseCoordinatorAuthorizationFilter extends CoordinatorAuthorizationFilter {

    public static final ExecutionCourseCoordinatorAuthorizationFilter instance =
            new ExecutionCourseCoordinatorAuthorizationFilter();

    protected ExecutionYear getSpecificExecutionYear(String executionCourseID) {
        ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);

        return (executionCourse == null) ? null : executionCourse.getExecutionYear();
    }

    public void execute(String executionCourseID) throws NotAuthorizedException {
        Person person = AccessControl.getUserView().getPerson();

        if (!person.hasRole(RoleType.COORDINATOR)) {
            deny();
        }

        SortedSet<Coordinator> coordinators = new TreeSet<Coordinator>(new CoordinatorByExecutionDegreeComparator());
        coordinators.addAll(person.getCoordinators());

        if (coordinators.isEmpty()) {
            deny();
        }

        ExecutionYear executionYear = getSpecificExecutionYear(executionCourseID);

        Coordinator coordinator = coordinators.first();
        ExecutionYear coordinatorExecutionYear = coordinator.getExecutionDegree().getExecutionYear();

        if (executionYear == null || coordinatorExecutionYear.compareTo(executionYear) < 0) {
            deny();
        }
    }

    public void deny() throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

}
