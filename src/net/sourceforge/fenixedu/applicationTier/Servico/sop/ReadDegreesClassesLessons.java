/*
 * ReadDegreesClassesLessons.java
 * 
 * Created on 2003/07/17
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewClassSchedule;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;
/**
 * TODO Remove cloner deste serviço...
 */
public class ReadDegreesClassesLessons implements IService {

    public List run(List infoExecutionDegrees, InfoExecutionPeriod infoExecutionPeriod) {

        List infoViewClassScheduleList = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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

                infoViewClassSchedule.setInfoClass(InfoClass.newInfoFromDomain(turma));
                infoViewClassSchedule.setClassLessons(infoLessonList);
                infoViewClassScheduleList.add(infoViewClassSchedule);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoViewClassScheduleList;
    }
}