/*
 * CreateExam.java Created on 2003/03/26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Service CriarAula.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.ArrayList;
import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.ExamExecutionCourse;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Season;

public class CreateExam implements IService
{

    

    /**
     * The actor of this class.
     */
    public CreateExam()
    {
    }

    

    public Boolean run(Calendar examDate, Calendar examTime, Season season,
            InfoExecutionCourse infoExecutionCourse) throws FenixServiceException
    {

        Boolean result = new Boolean(false);

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse
                            .getInfoExecutionPeriod());

            IExecutionCourse executionCourse = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(),
                            executionPeriod);

            for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++)
            {
                IExam exam = (IExam) executionCourse.getAssociatedExams().get(i);
                if (exam.getSeason().equals(season)) { throw new ExistingServiceException(); }
            }
            try
            {
                IExam exam = new Exam(examDate, examTime, null, season);
                sp.getIPersistentExam().simpleLockWrite(exam);
                exam.setAssociatedRooms(new ArrayList());
                IExamExecutionCourse examExecutionCourse = new ExamExecutionCourse(exam, executionCourse);
                sp.getIPersistentExamExecutionCourse().simpleLockWrite(examExecutionCourse);
            }
            catch (ExistingPersistentException ex)
            {
                throw new ExistingServiceException(ex);
            }

            result = new Boolean(true);
        }
        catch (ExcepcaoPersistencia ex)
        {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

}