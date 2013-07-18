package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import java.util.Comparator;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * Base filter for all resources that can be accessed by an degree coordinator.
 * 
 * @author cfgi
 */
public abstract class CoordinatorAuthorizationFilter extends Filtro {

    /**
     * Compares coordinators by they respective execution year. The most recent
     * execution year first.
     * 
     * @author cfgi
     */
    public static class CoordinatorByExecutionDegreeComparator implements Comparator<Coordinator> {

        @Override
        public int compare(Coordinator o1, Coordinator o2) {
            return ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR.compare(o2.getExecutionDegree(), o1.getExecutionDegree());
        }

    }

}
