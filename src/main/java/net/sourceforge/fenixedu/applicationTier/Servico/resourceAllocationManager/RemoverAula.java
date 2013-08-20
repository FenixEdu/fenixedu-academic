package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Lesson;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RemoverAula {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Object run(final InfoLesson infoLesson, final InfoShift infoShift) {
        AbstractDomainObject.<Lesson> fromExternalId(infoLesson.getExternalId()).delete();
        return Boolean.TRUE;
    }

}