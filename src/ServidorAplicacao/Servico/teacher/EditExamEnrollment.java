/*
 * EditExamEnrollment.java Created on 2003/05/28
 */

package ServidorAplicacao.Servico.teacher;

/**
 * @author João Mota
 */

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Exam;
import Dominio.IExam;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditExamEnrollment implements IService
{

   

    /**
     * The actor of this class.
     */
    public EditExamEnrollment()
    {
    }

    

    public Boolean run(Integer executionCourseCode, Integer examCode, Calendar beginDate,
            Calendar endDate, Calendar beginTime, Calendar endTime) throws FenixServiceException
    {

        Boolean result = new Boolean(false);

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            IExam exam = new Exam();
            exam.setIdInternal(examCode);
            exam = (IExam) persistentExam.readByOId(exam, true);
            if (exam == null) { throw new InvalidArgumentsServiceException(); }
            persistentExam.simpleLockWrite(exam);
            exam.setEnrollmentBeginDay(beginDate);
            exam.setEnrollmentEndDay(endDate);
            exam.setEnrollmentBeginTime(beginTime);
            exam.setEnrollmentEndTime(endTime);
            if (exam.getEnrollmentBeginDay().getTimeInMillis() > exam.getEnrollmentEndDay()
                    .getTimeInMillis()) { throw new InvalidTimeIntervalServiceException(); }
            if (exam.getEnrollmentEndDay().getTimeInMillis() > exam.getDay().getTimeInMillis()) { throw new InvalidTimeIntervalServiceException(); }
            result = new Boolean(true);

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        return result;
    }

}