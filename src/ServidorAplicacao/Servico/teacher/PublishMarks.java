package ServidorAplicacao.Servico.teacher;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Announcement;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.IAnnouncement;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IMark;
import Dominio.ISite;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks implements IService
{

    public PublishMarks()
    {

    }

    public Object run(Integer executionCourseCode, Integer evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente, FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Execution Course
            IExecutionCourse executionCourse = new ExecutionCourse(executionCourseCode);
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            executionCourse = (IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);

            //Site
            IPersistentSite siteDAO = sp.getIPersistentSite();
            ISite site = siteDAO.readByExecutionCourse(executionCourse);

            //		find what type of evaluation we are dealing with
            IEvaluation evaluation = new Evaluation(evaluationCode);
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            evaluation = (IEvaluation) persistentEvaluation.readByOId(evaluation, true);
            persistentEvaluation.simpleLockWrite(evaluation);

            if (publishmentMessage == null || publishmentMessage.length() == 0)
            {
                evaluation.setPublishmentMessage(" ");
            }
            else
            {
                evaluation.setPublishmentMessage(publishmentMessage);

                // create announcement
                Calendar calendar = Calendar.getInstance();

                IAnnouncement announcement = new Announcement();
                IPersistentAnnouncement persistentAnnouncement = sp.getIPersistentAnnouncement();
                persistentAnnouncement.simpleLockWrite(announcement);
                announcement.setInformation(publishmentMessage);
                announcement.setCreationDate(new Timestamp(calendar.getTimeInMillis()));
                announcement.setLastModifiedDate(new Timestamp(calendar.getTimeInMillis()));
                announcement.setSite(site);
                announcement.setTitle(announcementTitle);

            }

            // publish marks
            IPersistentMark persistentMark = sp.getIPersistentMark();
            List marksList = persistentMark.readBy(evaluation);
            ListIterator iterMarks = marksList.listIterator();
            while (iterMarks.hasNext())
            {
                IMark mark = (IMark) iterMarks.next();

                if (!mark.getMark().equals(mark.getPublishedMark()))
                {
                    // update published mark
                    persistentMark.simpleLockWrite(mark);
                    mark.setPublishedMark(mark.getMark());
                    if (sendSMS != null && sendSMS.booleanValue())
                    {
                        // send SMS with student's mark and publishment message
                        // if exists
                        // TODO: send SMS
                    }
                }
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
            throw new FenixServiceException("error.impossiblePublishMarks");
        }

        return Boolean.TRUE;
    }
}