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
import Dominio.ExecutionDegree;
import Dominio.ILesson;
import Dominio.IExecutionDegree;
import Dominio.ISchoolClass;
import Dominio.IShift;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
/**
 * TODO Remove cloner deste serviço...
 */
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
                IExecutionDegree executionDegree = (IExecutionDegree) classDAO.readByOID(
                        ExecutionDegree.class, infoExecutionDegree.getIdInternal());
                List degreeClasses = classDAO.readByExecutionDegree(executionDegree);
                for (Iterator iterator = degreeClasses.iterator(); iterator.hasNext(); ) {
                    ISchoolClass klass = (ISchoolClass) iterator.next();
                    if (klass.getExecutionPeriod().getIdInternal().equals(infoExecutionPeriod.getIdInternal())) {
                        classes.add(klass);
                    }
                }
                //CollectionUtils.addAll(classes, iterator);
            }

            for (int i = 0; i < classes.size(); i++) {
                InfoViewClassSchedule infoViewClassSchedule = new InfoViewClassSchedule();
                ISchoolClass turma = (ISchoolClass) classes.get(i);

                // read class lessons
                List shiftList = sp.getITurmaTurnoPersistente().readByClass(turma);
                Iterator iterator = shiftList.iterator();
                List infoLessonList = new ArrayList();
                while (iterator.hasNext()) {
                    IShift shift = (IShift) iterator.next();
                    InfoShift infoShift = (InfoShift) Cloner.get(shift);
                    List lessonList = shift.getAssociatedLessons();
                    //List lessonList = shiftLessonDAO.readByShift(shift);
                    Iterator lessonIterator = lessonList.iterator();
                    while (lessonIterator.hasNext()) {
                        ILesson elem = (ILesson) lessonIterator.next();
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