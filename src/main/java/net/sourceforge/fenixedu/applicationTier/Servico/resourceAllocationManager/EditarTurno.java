/*
 * EditarTurno.java Created on 27 de Outubro de 2002, 21:00
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditarTurno {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static Object run(InfoShift infoShiftOld, InfoShiftEditor infoShiftNew) {
        final Shift shiftToEdit = FenixFramework.getDomainObject(infoShiftOld.getExternalId());
        final ExecutionCourse newExecutionCourse =
                FenixFramework.getDomainObject(infoShiftNew.getInfoDisciplinaExecucao().getExternalId());
        shiftToEdit.edit(infoShiftNew.getTipos(), infoShiftNew.getLotacao(), newExecutionCourse, infoShiftNew.getNome(),
                infoShiftNew.getComment());
        return InfoShift.newInfoFromDomain(shiftToEdit);
    }
}