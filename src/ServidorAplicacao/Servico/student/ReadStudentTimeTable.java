package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoPeriod;
import DataBeans.InfoRoom;
import DataBeans.InfoRoomOccupation;
import DataBeans.InfoShift;
import Dominio.ILesson;
import Dominio.IExecutionPeriod;
import Dominio.IPeriod;
import Dominio.IRoomOccupation;
import Dominio.IRoom;
import Dominio.IStudent;
import Dominio.IShift;
import Dominio.ITurnoAluno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 */

public class ReadStudentTimeTable implements IService {

    public List run(String username) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IStudent student = persistentStudent.readByUsername(username);
        ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        List studentShifts = persistentShiftStudent.readByStudentAndExecutionPeriod(student,
                executionPeriod);

        List lessons = new ArrayList();
        Iterator shiftIter = studentShifts.iterator();
        while (shiftIter.hasNext()) {
            ITurnoAluno shiftStudent = (ITurnoAluno) shiftIter.next();
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