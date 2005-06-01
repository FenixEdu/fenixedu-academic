/*
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço LerTurnosDeDisciplinaExecucao.
 * 
 * @author tfc130
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerTurnosDeDisciplinaExecucao implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {

        List infoShiftList = new ArrayList();
        List infoShiftAndLessons = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExecutionCourse executionCourse = Cloner
                .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

        final InfoExecutionCourse infoExecutionCourse2 = InfoExecutionCourse.newInfoFromDomain(executionCourse);
        final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        final InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
        infoExecutionCourse2.setInfoExecutionPeriod(infoExecutionPeriod);

        infoShiftList = sp.getITurnoPersistente().readByExecutionCourse(executionCourse.getIdInternal());
        Iterator itShiftList = infoShiftList.iterator();

        while (itShiftList.hasNext()) {
            IShift shift = (IShift) itShiftList.next();

            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
//            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse2);
//
//            final Collection lessons = shift.getAssociatedLessons();
//            final List infoLessons = new ArrayList(lessons.size());
//            infoShift.setInfoLessons(infoLessons);
//            for (final Iterator iterator = lessons.iterator(); iterator.hasNext(); ) {
//                final ILesson lesson = (ILesson) iterator.next();
//                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
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
//            }
//
//            final Collection schoolClasses = shift.getAssociatedClasses();
//            final List infoSchoolClasses = new ArrayList(schoolClasses.size());
//            infoShift.setInfoClasses(infoSchoolClasses);
//            for (final Iterator iterator = schoolClasses.iterator(); iterator.hasNext(); ) {
//                final ISchoolClass schoolClass = (ISchoolClass) iterator.next();
//                final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
//                infoSchoolClasses.add(infoClass);
//            }

            
//            final List studentShifts = shift.getStudentShifts();
//            final List infoStudentShifts = new ArrayList(studentShifts.size());
//            for (final Iterator iterator = studentShifts.iterator(); iterator.hasNext();) {
//                final IStudent student = (IStudent) iterator.next();
//                final InfoStudent infoStudent = InfoStudent.newInfoFromDomain(student);
//                infoStudentShifts.add(infoStudent);
//            }
            //infoShift.set
            
            
            
            
            
            
            
            
//
//            List lessons = shift.getAssociatedLessons();
//            Iterator itLessons = lessons.iterator();
//
//            List infoLessons = new ArrayList();
//            InfoLesson infoLesson;
//
//            while (itLessons.hasNext()) {
//                infoLesson = Cloner.copyILesson2InfoLesson((ILesson) itLessons.next());
//
//                infoLesson.setInfoShift(infoTurno);
//
//                infoLessons.add(infoLesson);
//            }
//
//            infoTurno.setInfoLessons(infoLessons);
            infoShiftAndLessons.add(infoShift);

        }

        return infoShiftAndLessons;
    }
}