package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * Base filter for all resources that can be accessed by an degree coordinator.
 * 
 * @author cfgi
 */
public abstract class CoordinatorAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        Person person = getRemoteUser(request).getPerson();
        
        if (! person.hasRole(RoleType.COORDINATOR)) {
            deny();
        }
        
        SortedSet<Coordinator> coordinators = new TreeSet<Coordinator>(new CoordinatorByExecutionDegreeComparator());
        coordinators.addAll(person.getCoordinators());
        
        if (coordinators.isEmpty()) {
            deny();
        }
        
        ExecutionYear executionYear = getSpecificExecutionYear(request, response);
        
        Coordinator coordinator = coordinators.first();
        ExecutionYear coordinatorExecutionYear = coordinator.getExecutionDegree().getExecutionYear();
        
        if (executionYear == null || coordinatorExecutionYear.compareTo(executionYear) < 0) {
            deny();
        }
    }

    /**
     * @return the execution year that represents the scope of the resource being accessed.
     */
    protected abstract ExecutionYear getSpecificExecutionYear(ServiceRequest request, ServiceResponse response);

    public void deny() throws NotAuthorizedFilterException {
        throw new NotAuthorizedFilterException();
    }
    
    /**
     * Compares coordinators by they respective execution year. The most recent
     * execution year first.
     * 
     * @author cfgi
     */
    public static class CoordinatorByExecutionDegreeComparator implements Comparator<Coordinator> {

        public int compare(Coordinator o1, Coordinator o2) {
            return ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR.compare(o2.getExecutionDegree(), o1.getExecutionDegree());
        }
        
    }
    
}
