package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 */

public class ReadStudentTimeTable implements IService {

    public List run(String username) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IStudent student = persistentStudent.readByUsername(username);
        ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        List studentShifts = persistentShiftStudent.readByStudentAndExecutionPeriod(student.getIdInternal(),
                executionPeriod.getIdInternal());

        List lessons = new ArrayList();
        Iterator shiftIter = studentShifts.iterator();
        while (shiftIter.hasNext()) {
            IShiftStudent shiftStudent = (IShiftStudent) shiftIter.next();
            IShift shift = shiftStudent.getShift();
            lessons.addAll(shift.getAssociatedLessons());
        }

        Iterator iter = lessons.iterator();
        List infoLessons = new ArrayList();
        while (iter.hasNext()) {
            ILesson lesson = (ILesson) iter.next();
            InfoLesson infolesson = copyILesson2InfoLesson(lesson);
            IShift shift = lesson.getShift();
            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(shift.getDisciplinaExecucao());
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
            infolesson.setInfoShift(infoShift);
            infoLessons.add(infolesson);
        }

        return infoLessons;
    }

    private InfoLesson copyILesson2InfoLesson(ILesson lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.setIdInternal(lesson.getIdInternal());
            infoLesson.setDiaSemana(lesson.getDiaSemana());
            infoLesson.setFim(lesson.getFim());
            infoLesson.setInicio(lesson.getInicio());
            infoLesson.setTipo(lesson.getTipo());
            infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));

            IRoomOccupation roomOccupation = lesson.getRoomOccupation();
            InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
            infoLesson.setInfoRoomOccupation(infoRoomOccupation);

            IRoom room = roomOccupation.getRoom();
            InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            infoRoomOccupation.setInfoRoom(infoRoom);

            IPeriod period = roomOccupation.getPeriod();
            InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
            infoRoomOccupation.setInfoPeriod(infoPeriod);
        }
        return infoLesson;
    }

    /**
     * @param sala
     * @return
     */
    private InfoRoom copyISala2InfoRoom(IRoom sala) {
        InfoRoom infoRoom = null;
        if (sala != null) {
            infoRoom = new InfoRoom();
            infoRoom.setIdInternal(sala.getIdInternal());
            infoRoom.setNome(sala.getNome());
        }
        return infoRoom;
    }
}