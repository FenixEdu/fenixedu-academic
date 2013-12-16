/**
 * ReadCoordinatedDegrees class, implements the service that given a teacher returns 
 * a list containing the degree curricular plans for that teacher.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ReadCoordinatedDegrees {

    @Atomic
    public static List<InfoDegreeCurricularPlan> run(User userView) throws FenixServiceException {
        check(RolePredicates.COORDINATOR_PREDICATE);
        final Person person = userView.getPerson();
        if (person == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Set<DegreeCurricularPlan> activeDegreeCurricularPlans =
                new TreeSet<DegreeCurricularPlan>(
                        DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);
        for (final Coordinator coordinator : person.getCoordinatorsSet()) {
            final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
                activeDegreeCurricularPlans.add(degreeCurricularPlan);
            }
        }

        for (ScientificCommission commission : person.getScientificCommissions()) {
            ExecutionDegree executionDegree = commission.getExecutionDegree();
            DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

            if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
                activeDegreeCurricularPlans.add(degreeCurricularPlan);
            }
        }

        final List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>();
        for (final DegreeCurricularPlan degreeCurricularPlan : activeDegreeCurricularPlans) {
            result.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
        }

        return result;
    }

}