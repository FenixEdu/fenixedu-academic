/*
 * CreateExam.java
 *
 * Created on 2003/03/28
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingRoomsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Season;

public class EditExam implements IServico {

    private static EditExam _servico = new EditExam();

    /**
     * The singleton access method of this class.
     */
    public static EditExam getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private EditExam() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "EditExam";
    }

    public Boolean run(Calendar examDate, Calendar examTime, Season season,
            InfoViewExamByDayAndShift infoViewOldExam) throws FenixServiceException {

        Boolean result = new Boolean(false);

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(((InfoExecutionCourse) infoViewOldExam
                            .getInfoExecutionCourses().get(0)).getInfoExecutionPeriod());

            IExecutionCourse executionCourse = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriod(
                            ((InfoExecutionCourse) infoViewOldExam.getInfoExecutionCourses().get(0))
                                    .getSigla(), executionPeriod);

            IExam examFromDBToBeEdited = null;
            boolean newSeasonAlreadyScheduled = false;
            for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
                IExam exam = (IExam) executionCourse.getAssociatedExams().get(i);
                if (exam.getSeason().equals(infoViewOldExam.getInfoExam().getSeason())) {
                    examFromDBToBeEdited = exam;
                } else if (exam.getSeason().equals(season)) {
                    newSeasonAlreadyScheduled = true;
                }
            }

            if (newSeasonAlreadyScheduled) {
                throw new ExistingServiceException();
            }

            if (hasValidRooms(examFromDBToBeEdited, examDate, examTime)) {
                // TODO: Temporary solution to lock object for write. In the
                // future we'll use readByUnique()
                examFromDBToBeEdited = (IExam) sp.getIPersistentExam().readByOID(Exam.class,
                        examFromDBToBeEdited.getIdInternal(), true);
                examFromDBToBeEdited.setBeginning(examTime);
                examFromDBToBeEdited.setDay(examDate);
                examFromDBToBeEdited.setEnd(null);
                examFromDBToBeEdited.setSeason(season);

                result = new Boolean(true);
            } else {
                throw new InterceptingRoomsServiceException();
            }

        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

    private boolean hasValidRooms(IExam exam, Calendar examDate, Calendar examTime)
            throws FenixServiceException {

        ISuportePersistente sp;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ISalaPersistente persistentRoom = sp.getISalaPersistente();
            IExam examQuery = new Exam(examDate, examTime, null, null);
            examQuery.setIdInternal(exam.getIdInternal());
            List availableRooms = persistentRoom.readAvailableRooms(examQuery);

            if (availableRooms.containsAll(exam.getAssociatedRooms())) {
                return true;
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
        return false;
    }

}