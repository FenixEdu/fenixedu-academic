package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;

public class RemoverAula extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Object run(final InfoLesson infoLesson, final InfoShift infoShift) {
	rootDomainObject.readLessonByOID(infoLesson.getIdInternal()).delete();
	return Boolean.TRUE;
    }

}