package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.ForwardEmailToExecutionCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.ForwardEmailToExecutionCourses.ForwardMailsReport;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/> <br/> Created on
 *         0:32:09,17/Out/2005
 * @version $Id$
 */
public class ForwardEmailAction extends FenixAction {

    public static String emailAddressPrefix = "course-";

    private static Properties properties;

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        String result = "450 Error: Email forwarding service did not run";

        if (LogLevel.INFO) {
            System.out.println("Got a request from " + request.getRemoteAddr());
        }

        if (HostAccessControl.isAllowed(this, request)) {

            MimeMessage message = null;
            final DynaActionForm containerForm = (DynaActionForm) actionForm;
            final FormFile formFile = (FormFile) containerForm.get("theFile");
            try {

                InputStream source = formFile.getInputStream();
                message = new MimeMessage(Session.getDefaultInstance(System.getProperties()), source);
                if (isValid(message)) {
                    MessageResources resources = this.getResources(request, "MESSAGING_RESOURCES");
                    String subjectPrefix = resources.getMessage(this.getLocale(request),
                            "messaging.mailSender.mailingLists.subjectPrefix");
                    message.setSubject(subjectPrefix + " " + message.getSubject());
                    if (LogLevel.INFO) {
                        System.out.println("He/she asked for me to deliver a message whose size is "
                                + message.getSize());
                    }

                    ForwardEmailToExecutionCourses.ForwardMailsReport report = (ForwardEmailToExecutionCourses.ForwardMailsReport) ServiceUtils
                            .executeService(null, "ForwardEmailToExecutionCourses", new Object[] {
                                    message, emailAddressPrefix, mailingListDomainConfiguration() });

                    if (this.noRecipients(report)) {
                        result = "550 No recipients. Please verify that you are sending me the X-Original-To header";
                    } else if (this.allMailsSent(report)) {
                        result = "250 OK";
                    } else if (this.allTargetsAreDisabled(report)) {
                        result = "550 Target course have dynamic mail distribution in the DISABLED state";
                    } else if (this.allTargetsAreUnknown(report)) {
                        result = "550 Unknown address";
                    } else if (this.allMailsFailed(report)) {
                        result = "550 Message not delivered";
                    } else {
                        result = "269 At least one message delivered";
                    }

                    if (LogLevel.INFO) {
                        for (String address : report.getSentMails()) {
                            System.out.println("Mail enviado para " + address);
                        }
                    }
                } else {
                    result = "554 Error: Invalid mail message";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "450 Error: Got an exception when trying to send email: "
                        + e.getClass().getName();
            }
        } else {
            result = "550 requester not allowed";
        }
        try {
            sendAnswer(response, result);
        } catch (IOException e) {
            throw new FenixActionException(e);
        }

        // System.out.println("Over and out. Result: ------->" + result + "<-------");
        return null;

    }

    private boolean noRecipients(ForwardMailsReport report) {
        return report.getMailsToSend().size() == 0;
    }

    private boolean isValid(MimeMessage message) {
        boolean result = true;
        try {
            if (message.getRecipients(RecipientType.TO) == null
                    && message.getRecipients(RecipientType.BCC) == null
                    && message.getRecipients(RecipientType.CC) == null && message.getFrom() == null) {
                result = false;
            }
        } catch (MessagingException e) {
            result = false;
        }
        return result;
    }

    private boolean allTargetsAreUnknown(ForwardMailsReport report) {
        return report.getUnknownAddresses().containsAll(report.getMailsToSend());
    }

    private boolean allMailsFailed(ForwardMailsReport report) {
        return !report.getSentMails().containsAll(report.getMailsToSend());
    }

    private boolean allTargetsAreDisabled(ForwardMailsReport report) {
        return report.getDisabledDynamicDelivery().containsAll(report.getMailsToSend());
    }

    private boolean allMailsSent(ForwardMailsReport report) {
        return report.getSentMails().containsAll(report.getMailsToSend());
    }

    public static String mailingListDomainConfiguration() {
        try {
            if (properties == null) {
                properties = PropertiesManager.loadProperties("/SMTPConfiguration.properties");
            }
            return properties.getProperty("mailingList.host.name");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendAnswer(HttpServletResponse response, String result) throws IOException {
        ServletOutputStream writer = response.getOutputStream();
        response.setContentType("text/plain");
        writer.print(result);
        writer.flush();
        response.flushBuffer();
    }

}
