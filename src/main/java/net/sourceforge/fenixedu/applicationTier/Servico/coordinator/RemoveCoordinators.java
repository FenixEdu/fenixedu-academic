package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CoordinationTeamLog;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RemoveCoordinators {

    @Service
    public static void run(Integer executionDegreeID, List<Integer> coordinatorsToRemoveIDs) {

        for (final Integer coordinatorToRemoveID : coordinatorsToRemoveIDs) {
            final Coordinator coordinator = AbstractDomainObject.fromExternalId(coordinatorToRemoveID);
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

    @Service
    public static void runRemoveCoordinators(Integer executionDegreeID, List<Integer> coordinatorsToRemoveIDs)
            throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeID);
        serviceInstance.run(executionDegreeID, coordinatorsToRemoveIDs);
    }

}