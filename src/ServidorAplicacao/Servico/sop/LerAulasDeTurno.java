/*
 * LerAulasDeTurno.java
 *
 * Created on 28 de Outubro de 2002, 22:23
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeTurno
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurno;

public class LerAulasDeTurno implements IService {

    public List run(ShiftKey shiftKey) {
        ArrayList infoAulas = null;

        //   try {
        //   ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ITurno shift = Cloner.copyInfoShift2Shift(new InfoShift(shiftKey.getShiftName(), null, null,
                shiftKey.getInfoExecutionCourse()));

        //List aulas = sp.getITurnoAulaPersistente().readByShift(shift);
        List aulas = shift.getAssociatedLessons();

        Iterator iterator = aulas.iterator();
        infoAulas = new ArrayList();

        while (iterator.hasNext()) {
            IAula elem = (IAula) iterator.next();

            InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);

            InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
            infoLesson.setInfoShift(infoShift);

            infoAulas.add(infoLesson);
        }

        //    } catch (ExcepcaoPersistencia ex) {
        //      ex.printStackTrace();
        //    }

        return infoAulas;
    }

}