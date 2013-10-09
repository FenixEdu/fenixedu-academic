package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoverAula {

    @Atomic
    public static Object run(final InfoLesson infoLesson, final InfoShift infoShift) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        FenixFramework.<Lesson> getDomainObject(infoLesson.getExternalId()).delete();
        return Boolean.TRUE;
    }

}