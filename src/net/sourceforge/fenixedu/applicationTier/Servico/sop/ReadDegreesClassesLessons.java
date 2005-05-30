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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewClassSchedule;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
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
                List degreeClasses = classDAO.readByExecutionDegree(executionDegree.getIdInternal());
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
                    final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

                    final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                    final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                    infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                    final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
                    final InfoExecutionPeriod infoExecutionPeriod2 = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
                    infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod2);

                    final Collection lessons = shift.getAssociatedLessons();
                    final List infoLessons = new ArrayList(lessons.size());
                    infoShift.setInfoLessons(infoLessons);
                    for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext(); ) {
                        final ILesson lesson = (ILesson) iterator2.next();
                        final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                        infoLesson.setInfoShift(infoShift);
                        infoLesson.setInfoShiftList(new ArrayList(1));
                        infoLesson.getInfoShiftList().add(infoShift);

                        final IRoomOccupation roomOccupation = lesson.getRoomOccupation();
                        final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
                        infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                        final IRoom room = roomOccupation.getRoom();
                        final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
                        infoRoomOccupation.setInfoRoom(infoRoom);
                        infoLesson.setInfoSala(infoRoom);

                        final IPeriod period = roomOccupation.getPeriod();
                        final InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
                        infoRoomOccupation.setInfoPeriod(infoPeriod);

                        infoLessons.add(infoLesson);
                        infoLessonList.add(infoLesson);
                    }

                    final Collection schoolClasses = shift.getAssociatedClasses();
                    final List infoSchoolClasses = new ArrayList(schoolClasses.size());
                    infoShift.setInfoClasses(infoSchoolClasses);
                    for (final Iterator iterator2 = schoolClasses.iterator(); iterator2.hasNext(); ) {
                        final ISchoolClass schoolClass = (ISchoolClass) iterator2.next();
                        final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
                        infoSchoolClasses.add(infoClass);
                    }

                 }

                InfoClass infoClass = InfoClass.newInfoFromDomain(turma);
                final IExecutionDegree executionDegree = turma.getExecutionDegree();
                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                infoClass.setInfoExecutionDegree(infoExecutionDegree);

                final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                final IDegree degree = degreeCurricularPlan.getDegree();
                final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);

                infoClass.setInfoExecutionPeriod(infoExecutionPeriod);

                infoViewClassSchedule.setInfoClass(infoClass);
                infoViewClassSchedule.setClassLessons(infoLessonList);
                infoViewClassScheduleList.add(infoViewClassSchedule);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoViewClassScheduleList;
    }
}