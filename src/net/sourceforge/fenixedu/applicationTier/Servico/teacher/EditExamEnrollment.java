/*
 * EditExamEnrollment.java Created on 2003/05/28
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

/**
 * @author João Mota
 */

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditExamEnrollment implements IService {

    /**
     * The actor of this class.
     */
    public EditExamEnrollment() {
    }

    public Boolean run(Integer executionCourseCode, Integer examCode, Calendar beginDate,
            Calendar endDate, Calendar beginTime, Calendar endTime) throws FenixServiceException {

        Boolean result = new Boolean(false);

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExam persistentExam = sp.getIPersistentExam();

            IExam exam = (IExam) persistentExam.readByOID(Exam.class, examCode, true);
            if (exam == null) {
                throw new InvalidArgumentsServiceException();
            }

            if ((exam.getEnrollmentBeginDay() == null || exam.getEnrollmentBeginTime() == null)
                    || (!equalDates(beginDate, beginTime, exam.getEnrollmentBeginDay(), exam
                            .getEnrollmentBeginTime()))) {
                if (!verifyDates(Calendar.getInstance(), Calendar.getInstance(), beginDate, beginTime)) {
                    throw new InvalidTimeIntervalServiceException("error.beginDate.sooner.today");
                }
            }

            persistentExam.simpleLockWrite(exam);
            exam.setEnrollmentBeginDay(beginDate);
            exam.setEnrollmentEndDay(endDate);
            exam.setEnrollmentBeginTime(beginTime);
            exam.setEnrollmentEndTime(endTime);

            if (!verifyDates(exam.getEnrollmentBeginDay(), exam.getEnrollmentBeginTime(), exam
                    .getEnrollmentEndDay(), exam.getEnrollmentEndTime())) {
                throw new InvalidTimeIntervalServiceException("error.endDate.sooner.beginDate");
            }

            if (!verifyDates(exam.getEnrollmentEndDay(), exam.getEnrollmentEndTime(), exam.getDay(),
                    exam.getBeginning())) {
                throw new InvalidTimeIntervalServiceException("error.examDate.sooner.endDate");
            }

            result = new Boolean(true);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return result;
    }

    private boolean verifyDates(Calendar beginDay, Calendar beginTime, Calendar endDay, Calendar endTime) {
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.YEAR, beginDay.get(Calendar.YEAR));
        begin.set(Calendar.MONTH, beginDay.get(Calendar.MONTH));
        begin.set(Calendar.DAY_OF_MONTH, beginDay.get(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.HOUR_OF_DAY, beginTime.get(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, beginTime.get(Calendar.MINUTE));
        begin.set(Calendar.SECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, endDay.get(Calendar.YEAR));
        end.set(Calendar.MONTH, endDay.get(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, endDay.get(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
        end.set(Calendar.SECOND, 0);

        return begin.getTimeInMillis() < end.getTimeInMillis();
    }

    private boolean equalDates(Calendar beginDay, Calendar beginTime, Calendar endDay, Calendar endTime) {
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.YEAR, beginDay.get(Calendar.YEAR));
        begin.set(Calendar.MONTH, beginDay.get(Calendar.MONTH));
        begin.set(Calendar.DAY_OF_MONTH, beginDay.get(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.HOUR_OF_DAY, beginTime.get(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, beginTime.get(Calendar.MINUTE));
        begin.set(Calendar.SECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, endDay.get(Calendar.YEAR));
        end.set(Calendar.MONTH, endDay.get(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, endDay.get(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
        end.set(Calendar.SECOND, 0);

        return begin.getTimeInMillis() == end.getTimeInMillis();
    }

}