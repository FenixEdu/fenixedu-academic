/*
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerTurnosDeDisciplinaExecucao {

    @Atomic
    public static List<InfoShift> run(InfoExecutionCourse infoExecutionCourse) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        List<InfoShift> infoShifts = new ArrayList<InfoShift>();

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourse.getExternalId());
        Iterator<Shift> itShiftList = executionCourse.getAssociatedShifts().iterator();

        while (itShiftList.hasNext()) {
            Shift shift = itShiftList.next();
            infoShifts.add(InfoShift.newInfoFromDomain(shift));
        }

        return infoShifts;
    }
}