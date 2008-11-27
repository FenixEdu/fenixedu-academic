/*
 * EditarTurno.java Created on 27 de Outubro de 2002, 21:00
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * Servi�o EditarTurno.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class EditarTurno extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Object run(InfoShift infoShiftOld, InfoShiftEditor infoShiftNew) {
	final Shift shiftToEdit = rootDomainObject.readShiftByOID(infoShiftOld.getIdInternal());
	final ExecutionCourse newExecutionCourse = rootDomainObject.readExecutionCourseByOID(infoShiftNew
		.getInfoDisciplinaExecucao().getIdInternal());
	shiftToEdit.edit(infoShiftNew.getTipos(), infoShiftNew.getLotacao(), newExecutionCourse, infoShiftNew.getNome());
	return InfoShift.newInfoFromDomain(shiftToEdit);
    }
}