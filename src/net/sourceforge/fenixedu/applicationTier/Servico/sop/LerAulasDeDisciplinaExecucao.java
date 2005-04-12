/*
 * LerAulasDeDisciplinaExecucao.java
 *
 * Created on 27 de Outubro de 2002, 23:09
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucao.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeDisciplinaExecucao implements IService {

    public Object run(InfoExecutionCourse infoExecutionCourse) {

        ArrayList infoLessonList = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                infoLesson.setInfoShift(infoShift);

                infoLessonList.add(infoLesson);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return infoLessonList;
    }

}