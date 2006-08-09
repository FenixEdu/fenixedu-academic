/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created on May 18, 2006, 12:07:00
 *         PM
 * 
 */
public class ForwardEmailToExecutionCourses extends CmsService {

    public static class ForwardMailsReport {
        private Collection<String> mailsToSend = new ArrayList<String>();

        private Collection<String> mailsSent = new ArrayList<String>();

        private boolean mailsSentIsComputed = false;

        private Collection<String> invalidAddresses = new ArrayList<String>();

        private Collection<String> unknownAddresses = new ArrayList<String>();

        private Collection<String> disabledDynamicDelivery = new ArrayList<String>();

        public ForwardMailsReport() {
        }

        public Collection<String> getMailsToSend() {
            return this.mailsToSend;
        }

        public void setMailsToSend(Collection<String> mailsToSend) {
            this.mailsToSend = mailsToSend;
        }

        public Collection<String> getInvalidAddresses() {
            return this.invalidAddresses;
        }

        public Collection<String> getSentMails() {
            if (!this.mailsSentIsComputed) {
                this.mailsSentIsComputed = true;
                for (String mail : this.mailsToSend) {
                    if (!this.disabledDynamicDelivery.contains(mail)
                            && !this.invalidAddresses.contains(mail)
                            && !this.unknownAddresses.contains(mail)) {
                        this.mailsSent.add(mail);
                    }
                }
            }
            return this.mailsSent;
        }

        /**
         * @return the unknownAddresses
         */
        public Collection<String> getUnknownAddresses() {
            return this.unknownAddresses;
        }

        /**
         * @param unknownAddresses
         *            the unknownAddresses to set
         */
        public void setUnknownAddresses(Collection<String> unknownAddresses) {
            this.unknownAddresses = unknownAddresses;
        }

        /**
         * @return the disabledDynamicDelivery
         */
        public Collection<String> getDisabledDynamicDelivery() {
            return this.disabledDynamicDelivery;
        }

        /**
         * @param disabledDynamicDelivery
         *            the disabledDynamicDelivery to set
         */
        public void setDisabledDynamicDelivery(Collection<String> disabledDynamicDelivery) {
            this.disabledDynamicDelivery = disabledDynamicDelivery;
        }
    }

    public ForwardMailsReport run(MimeMessage message, String prefix, String host)
            throws FenixServiceException {
        ForwardMailsReport report = new ForwardMailsReport();

        try {
            Address[] xOriginalTo = this.extractXOriginalTo(message, prefix, host, report);
            // Address[] to = message.getRecipients(RecipientType.TO);
            // Address[] cc = message.getRecipients(RecipientType.CC);
            // Address[] bcc = message.getRecipients(RecipientType.BCC);
            // Collection<Integer> executionCourseIds = this.extractExecutionCourseIds(to, prefix,
            // report);
            // executionCourseIds.addAll(this.extractExecutionCourseIds(cc, prefix, report));
            // executionCourseIds.addAll(this.extractExecutionCourseIds(bcc, prefix, report));
            Collection<Integer> executionCourseIds = this.extractExecutionCourseIds(xOriginalTo, prefix,
                    report);

            this.send(message, executionCourseIds, report, prefix, host);
            return report;

        } catch (MessagingException e) {
            throw new FenixServiceException(e);
        }
    }

    private Address[] extractXOriginalTo(MimeMessage message, String prefix, String host,
            ForwardMailsReport report) throws MessagingException {

        String[] xOriginalTo = message.getHeader("X-Original-To");
        try {
            if (xOriginalTo.length == 1) {
                Address a = new InternetAddress(xOriginalTo[0]);
                return new Address[] { a };
            } else {
                return new Address[0];
            }
        } catch (AddressException e) {
            report.getInvalidAddresses().add(xOriginalTo[0]);
            return new Address[0];
        }
    }

    private void send(MimeMessage message, Collection<Integer> executionCourseIds,
            ForwardMailsReport report, String prefix, String host) {
        Collection<ExecutionCourse> courses = new ArrayList<ExecutionCourse>(1);
        for (Integer id : executionCourseIds) {
            ExecutionCourse course = RootDomainObject.getInstance().readExecutionCourseByOID(id);
            if (course != null) {
                courses.add(course);
            } else {
                report.getUnknownAddresses().add(prefix + id.toString() + "@" + host);
            }
        }
        List<String> addresses = new ArrayList<String>();
        for (ExecutionCourse course : courses) {
            if (course.getSite() != null) {
                if (course.getSite().getDynamicMailDistribution()) {
                    for (Professorship professorship : course.getProfessorships()) {
                        addresses.add(professorship.getTeacher().getPerson().getEmail());
                    }
                } else {
                    report.getDisabledDynamicDelivery().add(
                            prefix + course.getIdInternal().toString() + "@" + host);
                }
            }
        }
        EmailSender.forward(message, addresses);
    }

    private Collection<Integer> extractExecutionCourseIds(Address[] addressArray,
            String emailAddressPrefix, ForwardMailsReport report) {
        Collection<Integer> executionCourseIds = new ArrayList<Integer>();
        if (addressArray != null) {
            for (int i = 0; i < addressArray.length; i++) {
                Address address = addressArray[i];
                if (address instanceof InternetAddress) {
                    String stringAddress = ((InternetAddress) address).getAddress().toString()
                            .toLowerCase();
                    report.getMailsToSend().add(stringAddress);
                    String[] splittedAddress = stringAddress.split("@");
                    if (splittedAddress.length > 0) {
                        if (splittedAddress[0].startsWith(emailAddressPrefix)) {
                            String stringId = splittedAddress[0].substring(emailAddressPrefix.length());
                            try {
                                Integer id = Integer.valueOf(stringId);
                                executionCourseIds.add(id);
                            } catch (NumberFormatException e) {
                                report.getInvalidAddresses().add(stringAddress);
                            }
                        } else {
                            report.getUnknownAddresses().add(stringAddress);
                        }
                    }
                }
            }
        }
        return executionCourseIds;
    }
}
