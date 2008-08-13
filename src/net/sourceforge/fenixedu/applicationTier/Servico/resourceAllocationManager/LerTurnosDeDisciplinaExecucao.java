/*
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Servi�o LerTurnosDeDisciplinaExecucao.
 * 
 * @author tfc130
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerTurnosDeDisciplinaExecucao extends Service {

    public List<InfoShift> run(InfoExecutionCourse infoExecutionCourse) {

        List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());        
        Iterator<Shift> itShiftList = executionCourse.getAssociatedShifts().iterator();

        while (itShiftList.hasNext()) {
            Shift shift = itShiftList.next();
            infoShifts.add(InfoShift.newInfoFromDomain(shift));
        }

        return infoShifts;
    }
}