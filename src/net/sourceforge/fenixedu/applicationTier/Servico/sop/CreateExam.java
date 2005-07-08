/*
 * CreateExam.java Created on 2003/03/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * 
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.Season;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateExam implements IService {

    /**
     * The actor of this class.
     */
    public CreateExam() {
    }

    public Boolean run(Calendar examDate, Calendar examTime, Season season,
            InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

        Boolean result = new Boolean(false);

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

//            IExecutionPeriod executionPeriod = Cloner
//                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse
//                            .getInfoExecutionPeriod());

            IExecutionCourse executionCourse = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecutionCourse.getSigla(),
                            infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());

            for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
                IExam exam = executionCourse.getAssociatedExams().get(i);
                if (exam.getSeason().equals(season)) {
                    throw new ExistingServiceException();
                }
            }
            try {
                IExam exam = new Exam(examDate, examTime, null, season);
                sp.getIPersistentExam().simpleLockWrite(exam);

                IExamExecutionCourse examExecutionCourse = new ExamExecutionCourse(exam, executionCourse);
                sp.getIPersistentExamExecutionCourse().simpleLockWrite(examExecutionCourse);
            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }

            result = new Boolean(true);
        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

}