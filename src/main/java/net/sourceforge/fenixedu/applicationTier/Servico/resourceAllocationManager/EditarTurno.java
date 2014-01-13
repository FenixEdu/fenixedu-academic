/*
 * EditarTurno.java Created on 27 de Outubro de 2002, 21:00
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditarTurno {

    @Atomic
    public static Object run(InfoShift infoShiftOld, InfoShiftEditor infoShiftNew) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final Shift shiftToEdit = FenixFramework.getDomainObject(infoShiftOld.getExternalId());
        final ExecutionCourse newExecutionCourse =
                FenixFramework.getDomainObject(infoShiftNew.getInfoDisciplinaExecucao().getExternalId());
        shiftToEdit.edit(infoShiftNew.getTipos(), infoShiftNew.getLotacao(), newExecutionCourse, infoShiftNew.getNome(),
                infoShiftNew.getComment());
        return InfoShift.newInfoFromDomain(shiftToEdit);
    }
}