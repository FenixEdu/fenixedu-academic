/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CriarTurno {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static InfoShift run(InfoShiftEditor infoTurno) {
        final ExecutionCourse executionCourse =
                RootDomainObject.getInstance().readExecutionCourseByOID(infoTurno.getInfoDisciplinaExecucao().getIdInternal());
        final Shift newShift = new Shift(executionCourse, infoTurno.getTipos(), infoTurno.getLotacao());
        return InfoShift.newInfoFromDomain(newShift);
    }
}