/*
 * LerAulasDeSalaEmSemestre.java Created on 29 de Outubro de 2002, 15:44
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeSalaEmSemestre.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeSalaEmSemestre implements IService {

    public LerAulasDeSalaEmSemestre() {
    }

    public List run(InfoExecutionPeriod infoExecutionPeriod, InfoRoom infoRoom, Integer executionPeriodId) {
        List infoAulas = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            IExecutionPeriod executionPeriod = null;
            if (executionPeriodId != null) {
                executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                        ExecutionPeriod.class, executionPeriodId, false);
            } else {
                executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
            }

            IRoom room = Cloner.copyInfoRoom2Room(infoRoom);

            List lessonList = lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);

            Iterator iterator = lessonList.iterator();
            infoAulas = new ArrayList();
            while (iterator.hasNext()) {
                ILesson elem = (ILesson) iterator.next();
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
                IShift shift = elem.getShift();
                if (shift == null) {
                    continue;
                }
                InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                infoLesson.setInfoShift(infoShift);

                infoAulas.add(infoLesson);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoAulas;
    }

}