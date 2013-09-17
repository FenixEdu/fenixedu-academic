/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CriarTurno {

    @Atomic
    public static InfoShift run(InfoShiftEditor infoTurno) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final ExecutionCourse executionCourse =
                FenixFramework.getDomainObject(infoTurno.getInfoDisciplinaExecucao().getExternalId());
        final Shift newShift = new Shift(executionCourse, infoTurno.getTipos(), infoTurno.getLotacao());
        return InfoShift.newInfoFromDomain(newShift);
    }
}