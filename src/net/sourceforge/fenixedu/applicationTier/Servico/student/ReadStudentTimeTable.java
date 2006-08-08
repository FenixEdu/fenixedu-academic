package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;

public class ReadStudentTimeTable extends Service {

    public List<InfoLesson> run(Student student) throws FenixServiceException {

        if (student == null) {
        	throw new FenixServiceException("error.service.readStudentTimeTable.noStudent");
        }
        
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (final Shift shift : student.getShiftsForCurrentExecutionPeriod()) {
        	lessons.addAll(shift.getAssociatedLessonsSet());
        }
        
        final List<InfoLesson> result = new ArrayList<InfoLesson>(lessons.size());
        for (final Lesson lesson : lessons) {
        	InfoLesson infolesson = copyILesson2InfoLesson(lesson);
            Shift shift = lesson.getShift();
            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(shift.getDisciplinaExecucao());
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
            infolesson.setInfoShift(infoShift);
            result.add(infolesson);
        	
        }

        return result;
    }

    private InfoLesson copyILesson2InfoLesson(Lesson lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.setIdInternal(lesson.getIdInternal());
            infoLesson.setDiaSemana(lesson.getDiaSemana());
            infoLesson.setFim(lesson.getFim());
            infoLesson.setInicio(lesson.getInicio());
            infoLesson.setTipo(lesson.getTipo());
            infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));
			infoLesson.setFrequency(lesson.getFrequency());
			infoLesson.setWeekOfQuinzenalStart(lesson.getWeekOfQuinzenalStart());

            RoomOccupation roomOccupation = lesson.getRoomOccupation();
            InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
            infoLesson.setInfoRoomOccupation(infoRoomOccupation);

            OldRoom room = roomOccupation.getRoom();
            InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            infoRoomOccupation.setInfoRoom(infoRoom);

            OccupationPeriod period = roomOccupation.getPeriod();
            InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
            infoRoomOccupation.setInfoPeriod(infoPeriod);
        }
        return infoLesson;
    }

    private InfoRoom copyISala2InfoRoom(OldRoom sala) {
        InfoRoom infoRoom = null;
        if (sala != null) {
            infoRoom = new InfoRoom();
            infoRoom.setIdInternal(sala.getIdInternal());
            infoRoom.setNome(sala.getName());
        }
        return infoRoom;
    }
}