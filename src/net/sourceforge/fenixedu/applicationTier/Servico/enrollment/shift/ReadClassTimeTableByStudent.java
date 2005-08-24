/**
* Aug 7, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadClassTimeTableByStudent implements IService {
    
    public List run(final String username, final Integer classID, final Integer executionCourseID) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();        
        ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();
        
        IStudent student = persistentStudent.readByUsername(username);
        ISchoolClass schoolClass = (ISchoolClass) persistentSchoolClass.readByOID(SchoolClass.class, classID);
        
        List studentAttends = student.getAssociatedAttends();
        
        List classShifts = new ArrayList();
        for (Iterator iter = schoolClass.getAssociatedShifts().iterator(); iter.hasNext();) {
            final IShift shift = (IShift) iter.next();
            if(CollectionUtils.exists(studentAttends,new Predicate(){
                public boolean evaluate(Object arg0) {
                    IAttends attends = (IAttends) arg0;
                    boolean result = shift.getDisciplinaExecucao().equals(attends.getDisciplinaExecucao());
                    if(executionCourseID != null){
                        result = result & shift.getDisciplinaExecucao().getIdInternal().equals(executionCourseID);
                    }
                    return result;
                }})){
                classShifts.add(shift);
            }
        }

        List lessons = new ArrayList();
        Iterator shiftIter = classShifts.iterator();
        while (shiftIter.hasNext()) {
            IShift shift = (IShift) shiftIter.next();
            lessons.addAll(shift.getAssociatedLessons());
        }

        Iterator iter = lessons.iterator();
        List infoLessons = new ArrayList();
        while (iter.hasNext()) {
            ILesson lesson = (ILesson) iter.next();
            InfoLesson infolesson = copyILesson2InfoLesson(lesson);
            IShift shift = lesson.getShift();
            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(shift
                    .getDisciplinaExecucao());
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


