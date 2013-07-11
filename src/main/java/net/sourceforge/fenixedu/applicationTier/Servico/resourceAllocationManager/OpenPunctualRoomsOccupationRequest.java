package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class OpenPunctualRoomsOccupationRequest {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(PunctualRoomsOccupationRequest request, Person person) {
        if (request != null) {
            request.openRequestAndAssociateOwnerOnlyForEmployess(new DateTime(), person);
        }
    }
}