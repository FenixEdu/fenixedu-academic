/*
 * Created on 2003/07/30
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

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
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadShiftByOID implements IService {

    public InfoShift run(final Integer oid) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
        final IShift shift = (IShift) shiftDAO.readByOID(Shift.class, oid);

        if (shift != null) {
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
            final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

            final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            final InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

            final Collection lessons = shift.getAssociatedLessons();
            final List infoLessons = new ArrayList(lessons.size());
            infoShift.setInfoLessons(infoLessons);
            for (final Iterator iterator = lessons.iterator(); iterator.hasNext(); ) {
                final ILesson lesson = (ILesson) iterator.next();
                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);

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
            }

            final Collection schoolClasses = shift.getAssociatedClasses();
            final List infoSchoolClasses = new ArrayList(schoolClasses.size());
            infoShift.setInfoClasses(infoSchoolClasses);
            for (final Iterator iterator = schoolClasses.iterator(); iterator.hasNext(); ) {
                final ISchoolClass schoolClass = (ISchoolClass) iterator.next();
                final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
                infoSchoolClasses.add(infoClass);
            }

            return infoShift;
        }
        return null;
    }

}