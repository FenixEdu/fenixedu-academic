/*
 * ReadDegreesClassesLessons.java
 * 
 * Created on 2003/07/17
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoViewClassSchedule;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.IAula;
import Dominio.ICursoExecucao;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadDegreesClassesLessons implements IService {

    public List run(List infoExecutionDegrees, InfoExecutionPeriod infoExecutionPeriod) {

        List infoViewClassScheduleList = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurmaPersistente classDAO = sp.getITurmaPersistente();
            //ITurnoAulaPersistente shiftLessonDAO =
            // sp.getITurnoAulaPersistente();

            // Read executionDegrees classes
            List classes = new ArrayList();
            for (int i = 0; i < infoExecutionDegrees.size(); i++) {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees
                        .get(i);
                ICursoExecucao executionDegree = (ICursoExecucao) classDAO.readByOID(
                        CursoExecucao.class, infoExecutionDegree.getIdInternal());
                List degreeClasses = classDAO.readByExecutionDegree(executionDegree);
                Iterator iterator = degreeClasses.iterator();
                CollectionUtils.addAll(classes, iterator);
            }

            for (int i = 0; i < classes.size(); i++) {
                InfoViewClassSchedule infoViewClassSchedule = new InfoViewClassSchedule();
                ITurma turma = (ITurma) classes.get(i);

                // read class lessons
                List shiftList = sp.getITurmaTurnoPersistente().readByClass(turma);
                Iterator iterator = shiftList.iterator();
                List infoLessonList = new ArrayList();
                while (iterator.hasNext()) {
                    ITurno shift = (ITurno) iterator.next();
                    InfoShift infoShift = (InfoShift) Cloner.get(shift);
                    List lessonList = shift.getAssociatedLessons();
                    //List lessonList = shiftLessonDAO.readByShift(shift);
                    Iterator lessonIterator = lessonList.iterator();
                    while (lessonIterator.hasNext()) {
                        IAula elem = (IAula) lessonIterator.next();
                        InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
                        if (infoLesson != null) {
                            infoLesson.setInfoShift(infoShift);

                            infoLesson.getInfoShiftList().add(infoShift);
                            infoLessonList.add(infoLesson);
                        }
                    }
                }

                infoViewClassSchedule.setInfoClass(Cloner.copyClass2InfoClass(turma));
                infoViewClassSchedule.setClassLessons(infoLessonList);
                infoViewClassScheduleList.add(infoViewClassSchedule);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoViewClassScheduleList;
    }
}