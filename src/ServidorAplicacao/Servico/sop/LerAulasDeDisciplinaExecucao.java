/*
 * LerAulasDeDisciplinaExecucao.java
 *
 * Created on 27 de Outubro de 2002, 23:09
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucao.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ILesson;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IShift;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeDisciplinaExecucao implements IService {

    public Object run(InfoExecutionCourse infoExecutionCourse) {

        ArrayList infoLessonList = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse
                            .getInfoExecutionPeriod());

            IExecutionCourse executionCourse = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(),
                            executionPeriod);

            //			List aulas =
            // sp.getIAulaPersistente().readByExecutionCourse(executionCourse);
            List aulas = new ArrayList();

            List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
            for (int i = 0; i < shifts.size(); i++) {
                IShift shift = (IShift) shifts.get(i);
                List aulasTemp = sp.getIAulaPersistente().readLessonsByShift(shift);

                aulas.addAll(aulasTemp);
            }

            Iterator iterator = aulas.iterator();
            infoLessonList = new ArrayList();
            while (iterator.hasNext()) {
                ILesson elem = (ILesson) iterator.next();
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
                IShift shift = elem.getShift();
                InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                infoLesson.setInfoShift(infoShift);

                infoLessonList.add(infoLesson);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return infoLessonList;
    }

}