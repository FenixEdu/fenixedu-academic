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
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerTurnosDeDisciplinaExecucao extends Service {

    public List run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {

        List infoShiftList = new ArrayList();
        List infoShiftAndLessons = new ArrayList();

        infoShiftList = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal()).getAssociatedShifts();
        Iterator itShiftList = infoShiftList.iterator();

        while (itShiftList.hasNext()) {
            Shift shift = (Shift) itShiftList.next();

            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoShiftAndLessons.add(infoShift);

        }

        return infoShiftAndLessons;
    }
}