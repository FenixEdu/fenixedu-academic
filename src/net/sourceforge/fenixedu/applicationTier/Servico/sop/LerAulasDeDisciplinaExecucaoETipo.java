/*
 * LerAulasDeDisciplinaExecucaoETipo.java
 *
 * Created on 28 de Outubro de 2002, 18:03
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucaoETipo.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseKeyAndLessonType;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeDisciplinaExecucaoETipo implements IService {

    public Object run(ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao,
            InfoExecutionCourse infoExecutionCourse) {

        ArrayList infoAulas = null;

        try {

            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            //			List aulas =
            //				sp.getIAulaPersistente().readByExecutionCourseAndLessonType(
            //					executionCourse,
            //					tipoAulaAndKeyDisciplinaExecucao.getTipoAula());
            List aulas = new ArrayList();

            List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
            for (int i = 0; i < shifts.size(); i++) {
                IShift shift = (IShift) shifts.get(i);
                List aulasTemp = sp.getIAulaPersistente().readLessonsByShiftAndLessonType(shift,
                        tipoAulaAndKeyDisciplinaExecucao.getTipoAula());

                aulas.addAll(aulasTemp);
            }

            Iterator iterator = aulas.iterator();
            infoAulas = new ArrayList();
            while (iterator.hasNext()) {
                ILesson lesson = (ILesson) iterator.next();
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
                IShift shift = lesson.getShift();
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