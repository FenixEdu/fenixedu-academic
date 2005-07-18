package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms.SmsNotSentServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.SmsUtil;
import net.sourceforge.fenixedu.applicationTier.utils.exceptions.FenixUtilException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks implements IService {

    public Object run(Integer executionCourseCode, Integer evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        // Site
        IPersistentSite siteDAO = sp.getIPersistentSite();
        ISite site = siteDAO.readByExecutionCourse(executionCourseCode);
        // find what type of evaluation we are dealing with
        IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
        IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class,
                evaluationCode);
        persistentEvaluation.simpleLockWrite(evaluation);

        if (publishmentMessage == null || publishmentMessage.length() == 0) {
            evaluation.setPublishmentMessage(" ");
        } else {
            evaluation.setPublishmentMessage(publishmentMessage);
            site.createAnnouncement(announcementTitle, publishmentMessage);                       
        }

        // publish marks
        IPersistentMark persistentMark = sp.getIPersistentMark();
        List marksList = persistentMark.readBy(evaluation);
        ListIterator iterMarks = marksList.listIterator();
        while (iterMarks.hasNext()) {

            IMark mark = (IMark) iterMarks.next();

            if (!mark.getMark().equals(mark.getPublishedMark())) {
                // update published mark
                persistentMark.simpleLockWrite(mark);
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