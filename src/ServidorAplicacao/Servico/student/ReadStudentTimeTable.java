package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoRoomOccupation;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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

    /**
     * The actor of this class.
     */
    public ReadStudentTimeTable() {
    }

    public List run(String username) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readByUsername(username);
            ITurnoAlunoPersistente persistentShiftStudent = sp
                    .getITurnoAlunoPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod
                    .readActualExecutionPeriod();
            List studentShifts = persistentShiftStudent
                    .readByStudentAndExecutionPeriod(student, executionPeriod);

            List lessons = new ArrayList();
            Iterator shiftIter = studentShifts.iterator();
            while (shiftIter.hasNext()) {
                ITurnoAluno shiftStudent = (ITurnoAluno) shiftIter.next();
                ITurno shift = shiftStudent.getShift();
                lessons.addAll(shift.getAssociatedLessons());
            }

            Iterator iter = lessons.iterator();
            List infoLessons = new ArrayList();
            while (iter.hasNext()) {
                IAula lesson = (IAula) iter.next();
                InfoLesson infolesson = copyILesson2InfoLesson(lesson);
				ITurno shift = lesson.getShift();
				InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
				infolesson.setInfoShift(infoShift);				
                infoLessons.add(infolesson);
            }

            return infoLessons;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

    private InfoLesson copyILesson2InfoLesson(IAula lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.setIdInternal(lesson.getIdInternal());
            infoLesson.setDiaSemana(lesson.getDiaSemana());
            infoLesson.setFim(lesson.getFim());
            infoLesson.setInicio(lesson.getInicio());
            infoLesson.setTipo(lesson.getTipo());
            infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));

            InfoRoomOccupation infoRoomOccupation = Cloner.copyIRoomOccupation2InfoRoomOccupation(lesson.getRoomOccupation());
            infoLesson.setInfoRoomOccupation(infoRoomOccupation);
        }
        return infoLesson;
    }

    /**
     * @param executionCourse
     * @return
     */
    private InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(
            IExecutionCourse executionCourse) {
        InfoExecutionCourse infoExecutionCourse = null;
        if (executionCourse != null) {
            infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.setIdInternal(executionCourse.getIdInternal());
            infoExecutionCourse.setNome(executionCourse.getNome());
            infoExecutionCourse.setSigla(executionCourse.getSigla());

        }
        return infoExecutionCourse;
    }

    /**
     * @param sala
     * @return
     */
    private InfoRoom copyISala2InfoRoom(ISala sala) {
        InfoRoom infoRoom = null;
        if (sala != null) {
            infoRoom = new InfoRoom();
            infoRoom.setIdInternal(sala.getIdInternal());
            infoRoom.setNome(sala.getNome());
        }
        return infoRoom;
    }
}