package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms.SmsNotSentServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.SmsUtil;
import net.sourceforge.fenixedu.applicationTier.utils.exceptions.FenixUtilException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks extends Service {

    private final static int MOBILE_NUMBER_LENGHT = 9;

    private final static String VODAFONE_NETWORK_PREFIX = "91";

    private final static String TMN_NETWORK_PREFIX = "96";

    private final static String OPTIMUS_NETWORK_PREFIX = "93";

    public Object run(Integer executionCourseCode, Integer evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseCode);
        final Site site = executionCourse.getSite();
        final Evaluation evaluation = rootDomainObject.readEvaluationByOID(evaluationCode);

        if (publishmentMessage == null || publishmentMessage.length() == 0) {
            evaluation.setPublishmentMessage(" ");
        } else {
            evaluation.setPublishmentMessage(publishmentMessage);
            Announcement announcement = new Announcement();
            announcement.setSubject(new MultiLanguageString(announcementTitle));
            announcement.setBody(new MultiLanguageString(publishmentMessage));
            announcement.setVisible(true);
            site.getExecutionCourse().getBoard().addAnnouncements(announcement);
        }

        for (Mark mark : evaluation.getMarks()) {
            if (!mark.getMark().equals(mark.getPublishedMark())) {
                // update published mark
                mark.setPublishedMark(mark.getMark());
                if (sendSMS != null && sendSMS) {
                    if (mark.getAttend().getAluno().getPerson().getMobile() != null
                            || mark.getAttend().getAluno().getPerson().getMobile().length() == MOBILE_NUMBER_LENGHT) {
                        String StringDestinationNumber = mark.getAttend().getAluno().getPerson()
                                .getMobile();

                        if (StringDestinationNumber.startsWith(TMN_NETWORK_PREFIX)
                                || StringDestinationNumber.startsWith(VODAFONE_NETWORK_PREFIX)
                                || StringDestinationNumber.startsWith(OPTIMUS_NETWORK_PREFIX)) {

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
