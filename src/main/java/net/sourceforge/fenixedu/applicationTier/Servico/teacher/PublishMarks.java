package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms.SmsNotSentServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.SmsUtil;
import net.sourceforge.fenixedu.applicationTier.utils.exceptions.FenixUtilException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks {

    private final static int MOBILE_NUMBER_LENGHT = 9;

    private final static String VODAFONE_NETWORK_PREFIX = "91";

    private final static String TMN_NETWORK_PREFIX = "96";

    private final static String OPTIMUS_NETWORK_PREFIX = "93";

    protected Object run(String executionCourseCode, String evaluationCode, String publishmentMessage, Boolean sendSMS,
            String announcementTitle) throws ExcepcaoInexistente, FenixServiceException {

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseCode);
        final ExecutionCourseSite site = executionCourse.getSite();
        final Evaluation evaluation = AbstractDomainObject.fromExternalId(evaluationCode);

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
                    if (mark.getAttend().getRegistration().getPerson().getDefaultMobilePhoneNumber() != null
                            || mark.getAttend().getRegistration().getPerson().getDefaultMobilePhoneNumber().length() == MOBILE_NUMBER_LENGHT) {
                        String StringDestinationNumber =
                                mark.getAttend().getRegistration().getPerson().getDefaultMobilePhoneNumber();

                        if (StringDestinationNumber.startsWith(TMN_NETWORK_PREFIX)
                                || StringDestinationNumber.startsWith(VODAFONE_NETWORK_PREFIX)
                                || StringDestinationNumber.startsWith(OPTIMUS_NETWORK_PREFIX)) {

                            try {
                                SmsUtil.getInstance().sendSmsWithoutDeliveryReports(
                                        Integer.valueOf(StringDestinationNumber),
                                        evaluation.getPublishmentMessage() + " "
                                                + mark.getAttend().getExecutionCourse().getSigla() + " - " + mark.getMark());
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

    // Service Invokers migrated from Berserk

    private static final PublishMarks serviceInstance = new PublishMarks();

    @Service
    public static Object runPublishMarks(String executionCourseCode, String evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente, FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, evaluationCode, publishmentMessage, sendSMS, announcementTitle);
    }

}