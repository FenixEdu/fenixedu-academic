package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CoordinationTeamLog;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveCoordinators {

    @Atomic
    public static void run(String executionDegreeID, List<String> coordinatorsToRemoveIDs) {

        for (final String coordinatorToRemoveID : coordinatorsToRemoveIDs) {
            final Coordinator coordinator = FenixFramework.getDomainObject(coordinatorToRemoveID);
            if (coordinator != null) {
                final Person person = coordinator.getPerson();

                CoordinationTeamLog.createLog(coordinator.getExecutionDegree().getDegree(), coordinator.getExecutionDegree()
                        .getExecutionYear(), "resources.MessagingResources", "log.degree.coordinationteam.removemember",
                        coordinator.getPerson().getPresentationName(), coordinator.getExecutionDegree().getPresentationName());

                coordinator.delete();
                if (!person.hasAnyCoordinators()) {
                    person.removeRoleByType(RoleType.COORDINATOR);
                }
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final RemoveCoordinators serviceInstance = new RemoveCoordinators();

    @Atomic
    public static void runRemoveCoordinators(String executionDegreeID, List<String> coordinatorsToRemoveIDs)
            throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeID);
        serviceInstance.run(executionDegreeID, coordinatorsToRemoveIDs);
    }

}