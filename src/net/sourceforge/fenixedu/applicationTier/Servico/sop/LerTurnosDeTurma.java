/*
 * LerTurnosDeTurma.java
 *
 * Created on 28 de Outubro de 2002, 20:26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerTurnosDeTurma
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
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
import net.sourceforge.fenixedu.persistenceTier.ITurmaTurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerTurnosDeTurma implements IService {

    public Object run(String className, InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {

        List infoShiftAndLessons = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ITurmaTurnoPersistente classShiftDAO = sp.getITurmaTurnoPersistente();

        IExecutionPeriod executionPeriod = Cloner
                .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
        IExecutionDegree executionDegree = Cloner
                .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

        ISchoolClass group = new SchoolClass();

        group.setExecutionDegree(executionDegree);
        group.setExecutionPeriod(executionPeriod);
        group.setName(className);

        List shiftList = classShiftDAO.readByClass(group);

        Iterator iterator = shiftList.iterator();
        // infoTurnos = new ArrayList();

        while (iterator.hasNext()) {
            IShift shift = (IShift) iterator.next();
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            final IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
            final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

            final Collection lessons = shift.getAssociatedLessons();
            final List infoLessons = new ArrayList(lessons.size());
            infoShift.setInfoLessons(infoLessons);
            for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext(); ) {
                final ILesson lesson = (ILesson) iterator2.next();
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
            infoShiftAndLessons.add(infoShift);

        }
        return infoShiftAndLessons;

    }

}