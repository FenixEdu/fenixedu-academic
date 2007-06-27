package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;

public class RemoverAula extends Service {

    public Object run(final InfoLesson infoLesson, final InfoShift infoShift) {
        rootDomainObject.readLessonByOID(infoLesson.getIdInternal()).delete();
        return Boolean.TRUE;
    }

}
