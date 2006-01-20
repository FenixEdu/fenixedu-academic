package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms.SmsNotSentServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.SmsUtil;
import net.sourceforge.fenixedu.applicationTier.utils.exceptions.FenixUtilException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks extends Service {

    public Object run(Integer executionCourseCode, Integer evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        // Site
        IPersistentSite siteDAO = persistentSupport.getIPersistentSite();
        Site site = siteDAO.readByExecutionCourse(executionCourseCode);
        // find what type of evaluation we are dealing with
        Evaluation evaluation = (Evaluation) persistentSupport.getIPersistentObject().readByOID(Evaluation.class,
                evaluationCode);

        if (publishmentMessage == null || publishmentMessage.length() == 0) {
            evaluation.setPublishmentMessage(" ");
        } else {
            evaluation.setPublishmentMessage(publishmentMessage);
            site.createAnnouncement(announcementTitle, publishmentMessage);                       
        }

        // publish marks
        IPersistentMark persistentMark = persistentSupport.getIPersistentMark();
        List marksList = persistentMark.readBy(evaluation);
        ListIterator iterMarks = marksList.listIterator();
        while (iterMarks.hasNext()) {

            Mark mark = (Mark) iterMarks.next();

            if (!mark.getMark().equals(mark.getPublishedMark())) {
                // update published mark
                mark.setPublishedMark(mark.getMark());
                if (sendSMS != null && sendSMS.booleanValue()) {
                    if (mark.getAttend().getAluno().getPerson().getTelemovel() != null
                            || mark.getAttend().getAluno().getPerson().getTelemovel().length() == 9) {
                        String StringDestinationNumber = mark.getAttend().getAluno().getPerson()
                                .getTelemovel();

                        if (StringDestinationNumber.startsWith("96")
                                || StringDestinationNumber.startsWith("91")
                                || StringDestinationNumber.startsWith("93")) {

                            try {
                                SmsUtil.getInstance().sendSmsWithoutDeliveryReports(
                                        Integer.valueOf(StringDestinationNumber),
                                        evaluation.getPublishmentMessage() + " "
                                                + mark.getAttend().getDisciplinaExecucao().getSigla()
                                                + " - " + mark.getMark());
                            } catch (FenixUtilException e1) {

                                throw new SmsNotSentServiceException("error.person.sendSms");
                            }

                        }
                    }
                }
            }
        }

        return Boolean.TRUE;
    }
}