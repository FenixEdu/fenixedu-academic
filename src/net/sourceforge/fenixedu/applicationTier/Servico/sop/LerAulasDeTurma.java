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
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAulasDeTurma implements IService {

    public List run(InfoClass infoClass) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        final List<IShift> shiftList = sp.getITurmaTurnoPersistente().readByClass(infoClass.getIdInternal());

        List<InfoLesson> infoLessonList = new ArrayList<InfoLesson>();
        for (IShift shift : shiftList) {
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
            final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                    .newInfoFromDomain(executionCourse);
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

            final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            final InfoExecutionPeriod infoExecutionPeriod2 = InfoExecutionPeriod
                    .newInfoFromDomain(executionPeriod);
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod2);

            final List<ILesson> lessons = shift.getAssociatedLessons();

            final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(lessons.size());
            infoShift.setInfoLessons(infoLessons);
            for (ILesson lesson : lessons) {
                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoLesson.setInfoShift(infoShift);
                infoLesson.setInfoShiftList(new ArrayList(1));
                infoLesson.getInfoShiftList().add(infoShift);

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
                infoLessonList.add(infoLesson);
            }
        }

        return infoLessonList;
    }

}