/*
 * Created on 2004/04/21
 */
package ServidorAplicacao.Servico.student;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudent;
import Dominio.finalDegreeWork.IScheduleing;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class CheckCandidacyConditionsForFinalDegreeWork implements IService
{

    public CheckCandidacyConditionsForFinalDegreeWork()
    {
        super();
    }

    public boolean run(IUserView userView, Integer executionDegreeOID)
    	throws ExcepcaoPersistencia, FenixServiceException
    {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        //IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport
        		//.getIStudentCurricularPlanPersistente();
        IPersistentEnrolment persistentEnrolment = persistentSupport
        	.getIPersistentEnrolment();

        IScheduleing scheduleing = persistentFinalDegreeWork
                .readFinalDegreeWorkScheduleing(executionDegreeOID);

        if (scheduleing == null
                || scheduleing.getStartOfCandidacyPeriod() == null
                || scheduleing.getEndOfCandidacyPeriod() == null)
        {
            throw new CandidacyPeriodNotDefinedException();
        }

        Calendar now = Calendar.getInstance();
        if (scheduleing.getStartOfCandidacyPeriod().after(now.getTime())
                || scheduleing.getEndOfCandidacyPeriod().before(now.getTime()))
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String start = simpleDateFormat.format(new Date(scheduleing.getStartOfCandidacyPeriod().getTime()));
            String end = simpleDateFormat.format(new Date(scheduleing.getEndOfCandidacyPeriod().getTime()));
            throw new OutOfCandidacyPeriodException(start + " - " + end);
        }

        IStudent student = persistentStudent.readByUsername(userView.getUtilizador());

        int numberOfCompletedCourses = persistentEnrolment
        	.countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(student);

        Integer numberOfNecessaryCompletedCourses = scheduleing.getMinimumNumberOfCompletedCourses();
        if (numberOfCompletedCourses < numberOfNecessaryCompletedCourses.intValue())
        {
            throw new InsufficientCompletedCoursesException(numberOfNecessaryCompletedCourses.toString());
        }

        return true;
    }

    public class CandidacyPeriodNotDefinedException extends FenixServiceException
    {

        public CandidacyPeriodNotDefinedException()
        {
            super();
        }

        public CandidacyPeriodNotDefinedException(int errorType)
        {
            super(errorType);
        }

        public CandidacyPeriodNotDefinedException(String s)
        {
            super(s);
        }

        public CandidacyPeriodNotDefinedException(Throwable cause)
        {
            super(cause);
        }

        public CandidacyPeriodNotDefinedException(String message, Throwable cause)
        {
            super(message, cause);
        }

    }

    public class OutOfCandidacyPeriodException extends FenixServiceException
    {

        public OutOfCandidacyPeriodException()
        {
            super();
        }

        public OutOfCandidacyPeriodException(int errorType)
        {
            super(errorType);
        }

        public OutOfCandidacyPeriodException(String s)
        {
            super(s);
        }

        public OutOfCandidacyPeriodException(Throwable cause)
        {
            super(cause);
        }

        public OutOfCandidacyPeriodException(String message, Throwable cause)
        {
            super(message, cause);
        }

    }

    public class InsufficientCompletedCoursesException extends FenixServiceException
    {

        public InsufficientCompletedCoursesException()
        {
            super();
        }

        public InsufficientCompletedCoursesException(int errorType)
        {
            super(errorType);
        }

        public InsufficientCompletedCoursesException(String s)
        {
            super(s);
        }

        public InsufficientCompletedCoursesException(Throwable cause)
        {
            super(cause);
        }

        public InsufficientCompletedCoursesException(String message, Throwable cause)
        {
            super(message, cause);
        }

    }

}