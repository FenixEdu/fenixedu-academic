/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package ServidorAplicacao.Servico.student;

/**
 * Service ReadShiftSignup
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurno;

public class ReadShiftLessons implements IService {

    public Object run(InfoShift infoShift) {
        List infoLessons = new ArrayList();

        //		try {
        //			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ITurno shift = Cloner.copyInfoShift2Shift(infoShift);

        List lessons = shift.getAssociatedLessons();
        //				sp.getITurnoAulaPersistente().readByShift(
        //					shift);

        for (int i = 0; i < lessons.size(); i++) {
            IAula lesson = (IAula) lessons.get(i);

            InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
            infoLesson.setInfoShift(infoShift);

            infoLessons.add(infoLesson);
        }
        //		} catch (ExcepcaoPersistencia ex) {
        //			ex.printStackTrace();
        //		}

        return infoLessons;
    }

}