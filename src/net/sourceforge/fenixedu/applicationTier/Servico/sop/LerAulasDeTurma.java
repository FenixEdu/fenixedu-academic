/*
 * LerAulasDeTurma.java
 * 
 * Created on 29 de Outubro de 2002, 22:18
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeTurma
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeTurma implements IService {

    public List run(InfoClass infoClass) throws ExcepcaoPersistencia {
        ArrayList infoLessonList = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ITurmaPersistente persistentDomainClass = sp.getITurmaPersistente();
        ISchoolClass group = null;
        if (infoClass.getIdInternal() != null) {

            group = (ISchoolClass) persistentDomainClass.readByOID(SchoolClass.class, infoClass.getIdInternal());
        } else {
            group = Cloner.copyInfoClass2Class(infoClass);
        }

        List shiftList = sp.getITurmaTurnoPersistente().readByClass(group);

        Iterator iterator = shiftList.iterator();

        infoLessonList = new ArrayList();

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
        }
//            List lessonList = shift.getAssociatedLessons();
//            Iterator lessonIterator = lessonList.iterator();
//            while (lessonIterator.hasNext()) {
//                ILesson elem = (ILesson) lessonIterator.next();
//
//                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(elem);
//                infoLesson.setInfoShift(infoShift);
//                infoLesson.setInfoShiftList(new ArrayList(1));
//                infoLesson.getInfoShiftList().add(infoShift);
//
//                final IRoomOccupation roomOccupation = lesson.getRoomOccupation();
//                final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
//                infoLesson.setInfoRoomOccupation(infoRoomOccupation);
//
//                final IRoom room = roomOccupation.getRoom();
//                final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
//                infoRoomOccupation.setInfoRoom(infoRoom);
//                infoLesson.setInfoSala(infoRoom);
//
//                final IPeriod period = roomOccupation.getPeriod();
//                final InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
//                infoRoomOccupation.setInfoPeriod(infoPeriod);
//
//                infoLessons.add(infoLesson);
//                infoLessonList.add(infoLesson);
//
//                // ILesson lesson = (ILesson)
//                // sp.getIAulaPersistente().readByOID(
//                // Lesson.class, elem.getIdInternal());
//                // InfoLesson infoLesson =
//                // Cloner.copyILesson2InfoLesson(elem);
//                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
//
//                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
//                infoLesson.setInfoShift(infoShift);
//
//                if (infoLesson != null) {
//                    infoLessonList.add(infoLesson);
//                }
//
//            }
//        }

        return infoLessonList;
    }

}