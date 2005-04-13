/*
 * 
 * Created on 2003/08/13
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
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAvailableShiftsForClass implements IService {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        List infoShifts = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                infoClass.getIdInternal());

        List shifts = sp.getITurnoPersistente().readAvailableShiftsForClass(schoolClass);

        infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                IShift shift = (IShift) arg0;
                final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

                final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                        .newInfoFromDomain(executionCourse);
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                final Collection lessons = shift.getAssociatedLessons();
                final List infoLessons = new ArrayList(lessons.size());
                infoShift.setInfoLessons(infoLessons);
                for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext();) {
                    final ILesson lesson = (ILesson) iterator2.next();
                    final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);

                    final IRoomOccupation roomOccupation = lesson.getRoomOccupation();
                    final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation
                            .newInfoFromDomain(roomOccupation);
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
                return infoShift;
            }
        });

        return infoShifts;
    }

}