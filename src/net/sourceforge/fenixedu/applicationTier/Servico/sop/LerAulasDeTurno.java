/*
 * LerAulasDeTurno.java
 *
 * Created on 28 de Outubro de 2002, 22:23
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeTurno
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeTurno implements IService {

    public List run(ShiftKey shiftKey) {
        ArrayList infoAulas = null;

        //   try {
        //   ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IShift shift = Cloner.copyInfoShift2Shift(new InfoShift(shiftKey.getShiftName(), null, null,
                shiftKey.getInfoExecutionCourse()));

        //List aulas = sp.getITurnoAulaPersistente().readByShift(shift);
        List aulas = shift.getAssociatedLessons();

        Iterator iterator = aulas.iterator();
        infoAulas = new ArrayList();

        while (iterator.hasNext()) {
            ILesson elem = (ILesson) iterator.next();

            InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);

            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoLesson.setInfoShift(infoShift);

            infoAulas.add(infoLesson);
        }

        //    } catch (ExcepcaoPersistencia ex) {
        //      ex.printStackTrace();
        //    }

        return infoAulas;
    }

}