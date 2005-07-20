/*
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço LerTurnosDeDisciplinaExecucao.
 * 
 * @author tfc130
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerTurnosDeDisciplinaExecucao implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {

        List infoShiftList = new ArrayList();
        List infoShiftAndLessons = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        infoShiftList = sp.getITurnoPersistente().readByExecutionCourse(infoExecutionCourse.getIdInternal());
        Iterator itShiftList = infoShiftList.iterator();

        while (itShiftList.hasNext()) {
            IShift shift = (IShift) itShiftList.next();

            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoShiftAndLessons.add(infoShift);

        }

        return infoShiftAndLessons;
    }
}