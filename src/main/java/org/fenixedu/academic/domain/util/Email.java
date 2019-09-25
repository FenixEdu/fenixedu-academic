/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Email extends Email_Base {

    private static final Logger LOG = LoggerFactory.getLogger(Email.class);

    private static Session SESSION = null;
    private static int MAX_MAIL_RECIPIENTS;

    private static DateTimeFormatter rfc5322Fmt =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z").withLocale(Locale.ENGLISH);

    private static synchronized Session init() {
        final Properties properties = new Properties();
        properties.put("mail.smtp.host", FenixEduAcademicConfiguration.getConfiguration().getMailSmtpHost());
        properties.put("mail.smtp.name", FenixEduAcademicConfiguration.getConfiguration().getMailSmtpName());
        properties.put("mailSender.max.recipients",
                FenixEduAcademicConfiguration.getConfiguration().getMailSenderMaxRecipients());
        properties.put("mail.debug", "false");
        final Session tempSession = Session.getDefaultInstance(properties, null);
        MAX_MAIL_RECIPIENTS = Integer.parseInt(properties.getProperty("mailSender.max.recipients"));
        SESSION = tempSession;
        LOG.debug("Initialize mail properties");
        for (Entry<Object, Object> entry : properties.entrySet()) {
            LOG.debug("\t{}={}", entry.getKey(), entry.getValue());
        }
        return SESSION;
    }

    private Email() {
        super();
        setRootDomainObjectFromEmailQueue(Bennu.getInstance());
    }

    public Email(final String[] replyTos, final Collection<String> toAddresses, final Collection<String> ccAddresses,
            final Collection<String> bccAddresses, final Message message) {
        this();
        setReplyTos(new EmailAddressList(replyTos == null ? null : Arrays.asList(replyTos)));
        setToAddresses(new EmailAddressList(toAddresses));
        setCcAddresses(new EmailAddressList(ccAddresses));
        setBccAddresses(new EmailAddressList(bccAddresses));
        setMessage(message);
    }

    public String getBody() {
        return getMessage().getBody();
    }

    public String getSubject() {
        return getMessage().getSubject();
    }

    public String getHtmlBody() {
        return getMessage().getHtmlBody();
    }

    public void delete() {
        setMessage(null);
        setRootDomainObjectFromEmailQueue(null);
        super.deleteDomainObject();
    }

    public String[] replyTos() {
        return getReplyTos() == null ? null : getReplyTos().toArray();
    }

    public Collection<String> toAddresses() {
        return getToAddresses() == null ? null : getToAddresses().toCollection();
    }

    public Collection<String> ccAddresses() {
        return getCcAddresses() == null ? null : getCcAddresses().toCollection();
    }

    public Collection<String> bccAddresses() {
        return getBccAddresses() == null ? null : getBccAddresses().toCollection();
    }

    private void logProblem(final String description) {
        LOG.warn("Sending of email {} failed. Description: {}", this.getExternalId(), description);
    }

    private void logProblem(final MessagingException e) {
        logProblem(e.getMessage());
        final Exception nextException = e.getNextException();
        if (nextException != null) {
            if (nextException instanceof MessagingException) {
                logProblem((MessagingException) nextException);
            } else {
                logProblem(nextException.getMessage());
            }
        }
    }

    private void abort() {
        final Collection<String> failed = new HashSet<String>();
        final EmailAddressList failedAddresses = getFailedAddresses();
        if (failedAddresses != null && !failedAddresses.isEmpty()) {
            failed.addAll(failedAddresses.toCollection());
        }
        final EmailAddressList toAddresses = getToAddresses();
        if (toAddresses != null && !toAddresses.isEmpty()) {
            failed.addAll(toAddresses.toCollection());
        }
        final EmailAddressList ccAddresses = getCcAddresses();
        if (ccAddresses != null && !ccAddresses.isEmpty()) {
            failed.addAll(ccAddresses.toCollection());
        }
        final EmailAddressList bccAddresses = getBccAddresses();
        if (bccAddresses != null && !bccAddresses.isEmpty()) {
            failed.addAll(bccAddresses.toCollection());
        }
        final EmailAddressList emailAddressList = new EmailAddressList(failed);
        setFailedAddresses(emailAddressList);

        setToAddresses(null);
        setCcAddresses(null);
        setBccAddresses(null);
    }

    private void retry(final EmailAddressList toAddresses, final EmailAddressList ccAddresses,
            final EmailAddressList bccAddresses) {
        setToAddresses(toAddresses);
        setCcAddresses(ccAddresses);
        setBccAddresses(bccAddresses);
    }

    private static String encode(final String string) {
        try {
            return string == null ? "" : MimeUtility.encodeText(string);
        } catch (final UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            return string;
        }
    }

    protected static String constructFromString(final String fromName, String fromAddress) {
        return (fromName == null || fromName.length() == 0) ? fromAddress : fromName.replace(',', ' ').replace(':', ' ') + " <"
                + fromAddress + ">";
    }

    private class EmailMimeMessage extends MimeMessage {

        private String fenixMessageId = null;

        public EmailMimeMessage() {
            super(SESSION == null ? init() : SESSION);
        }

        @Override
        public String getMessageID() throws MessagingException {
            if (fenixMessageId == null) {
                final String externalId = getExternalId();
                final String instituitionEmailDomain = Installation.getInstance().getInstituitionEmailDomain();
                fenixMessageId = "<" + externalId + "." + new DateTime().getMillis() + "@" + instituitionEmailDomain + ">";
            }
            return fenixMessageId;
        }

        @Override
        protected void updateMessageID() throws MessagingException {
            setHeader("Message-ID", getMessageID());
            setHeader("Date", rfc5322Fmt.print(getMessage().getCreated()));
        }

        public void send(final Email email) throws MessagingException {
            if (email.getMessage().getSender().getFromName() == null) {
                logProblem("error.from.address.cannot.be.null");
                abort();
                return;
            }

            final String from =
                    constructFromString(encode(email.getMessage().getFromName()), email.getMessage().getFromAddress());

            final String[] replyTos = email.replyTos();
            final Address[] replyToAddresses = new Address[replyTos == null ? 0 : replyTos.length];
            if (replyTos != null) {
                for (int i = 0; i < replyTos.length; i++) {
                    try {
                        replyToAddresses[i] = new InternetAddress(encode(replyTos[i]));
                    } catch (final AddressException e) {
                        logProblem("invalid.reply.to.address: " + replyTos[i]);
                        abort();
                        return;
                    }
                }
            }

            setFrom(new InternetAddress(from));
            setSubject(encode(email.getSubject()));
            setReplyTo(replyToAddresses);

            final String body = email.getBody();
            final String htmlBody = getHtmlBody();

            final MimeMultipart mimeMultipart = createMimeMultipart(body, htmlBody);

            if (body != null && !body.trim().isEmpty()) {
                final BodyPart bodyPart = new MimeBodyPart();
                bodyPart.setText(body);
                mimeMultipart.addBodyPart(bodyPart);
            }

            if (htmlBody != null && !htmlBody.trim().isEmpty()) {
                final BodyPart bodyPart = new MimeBodyPart();
                bodyPart.setContent(htmlBody, "text/html; charset=utf-8");
                mimeMultipart.addBodyPart(bodyPart);
            }

            setContent(mimeMultipart);

            addRecipientsAux();

            LOG.info("Sending email {} with message id {}", email.getExternalId(), getMessageID());

            Transport.send(this);

            final Address[] allRecipients = getAllRecipients();
            setConfirmedAddresses(allRecipients);
        }

        private MimeMultipart createMimeMultipart(final String body, final String htmlBody) {
            return body != null && !body.trim().isEmpty() && htmlBody != null
                    && !htmlBody.trim().isEmpty() ? new MimeMultipart("alternative") : new MimeMultipart();
        }

        private void addRecipientsAux() {
            boolean hasAnyToOrCC = false;
            if (hasAnyRecipients(getToAddresses())) {
                final EmailAddressList tos = getToAddresses();
                final EmailAddressList remainder = addRecipientsAux(RecipientType.TO, tos);
                setToAddresses(remainder);
                hasAnyToOrCC = true;
            }
            if (hasAnyRecipients(getCcAddresses())) {
                final EmailAddressList ccs = getCcAddresses();
                final EmailAddressList remainder = addRecipientsAux(RecipientType.CC, ccs);
                setCcAddresses(remainder);
                hasAnyToOrCC = true;
            }
            if (!hasAnyToOrCC && hasAnyRecipients(getBccAddresses())) {
                final EmailAddressList bccs = getBccAddresses();
                final EmailAddressList remainder = addRecipientsAux(RecipientType.BCC, bccs);
                setBccAddresses(remainder);
            }
        }

        private EmailAddressList addRecipientsAux(final javax.mail.Message.RecipientType recipientType,
                final EmailAddressList emailAddressList) {
            final String[] emailAddresses = emailAddressList.toArray();
            for (int i = 0; i < emailAddresses.length; i++) {
                final String emailAddress = emailAddresses[i];
                try {
                    if (emailAddressFormatIsValid(emailAddress)) {
                        addRecipient(recipientType, new InternetAddress(encode(emailAddress)));
                    } else {
                        logProblem("invalid.email.address.format: " + emailAddress);
                    }
                } catch (final AddressException e) {
                    logProblem(e.getMessage() + " " + emailAddress);
                } catch (final MessagingException e) {
                    logProblem(e.getMessage() + " " + emailAddress);
                }
                if (i == MAX_MAIL_RECIPIENTS && i + 1 < emailAddresses.length) {
                    final String all = emailAddressList.toString();
                    final int next = all.indexOf(emailAddress) + emailAddress.length() + 2;
                    return new EmailAddressList(all.substring(next));
                }
            }
            return null;
        }

        public boolean emailAddressFormatIsValid(String emailAddress) {
            if ((emailAddress == null) || (emailAddress.length() == 0)) {
                return false;
            }

            if (emailAddress.indexOf(' ') > 0) {
                return false;
            }

            String[] atSplit = emailAddress.split("@");
            if (atSplit.length != 2) {
                return false;
            } else if ((atSplit[0].length() == 0) || (atSplit[1].length() == 0)) {
                return false;
            }

            String domain = new String(atSplit[1]);

            if (domain.lastIndexOf('.') == (domain.length() - 1)) {
                return false;
            }

            if (domain.indexOf('.') <= 0) {
                return false;
            }

            return true;
        }
    }

    private void setConfirmedAddresses(final Address[] recipients) {
        final Collection<String> addresses = new HashSet<String>();
        final EmailAddressList confirmedAddresses = getConfirmedAddresses();
        if (confirmedAddresses != null && !confirmedAddresses.isEmpty()) {
            addresses.addAll(confirmedAddresses.toCollection());
        }
        if (recipients != null) {
            for (final Address address : recipients) {
                addresses.add(address.toString());
            }
        }
        setConfirmedAddresses(new EmailAddressList(addresses));
    }

    private void setFailedAddresses(final Address[] recipients) {
        final Collection<String> addresses = new HashSet<String>();
        final EmailAddressList failedAddresses = getFailedAddresses();
        if (failedAddresses != null && !failedAddresses.isEmpty()) {
            addresses.addAll(failedAddresses.toCollection());
        }
        if (recipients != null) {
            for (final Address address : recipients) {
                addresses.add(address.toString());
            }
        }
        setFailedAddresses(new EmailAddressList(addresses));
    }

    private void resend(final Address[] recipients) {
        // final Collection<String> addresses = new HashSet<String>();
        // final EmailAddressList bccAddresses = getBccAddresses();
        // if (bccAddresses != null && !bccAddresses.isEmpty()) {
        // addresses.addAll(bccAddresses.toCollection());
        // }
        if (recipients != null && recipients.length > 0) {
            for (final Address address : recipients) {
                final String[] replyTos = getReplyTos() == null ? null : getReplyTos().toArray();
                new Email(replyTos, Collections.emptySet(), Collections.emptySet(), Collections.singleton(address.toString()),
                        getMessage());
                // addresses.add(address.toString());
            }
        }
        // setBccAddresses(new EmailAddressList(addresses));
    }

    @Atomic(mode = TxMode.WRITE)
    public void deliver() {
        if (!hasAnyRecipients() || (getMessage() != null && getMessage().getCreated().plusDays(5).isBeforeNow())) {
            setRootDomainObjectFromEmailQueue(null);
        } else {
            final EmailAddressList toAddresses = getToAddresses();
            final EmailAddressList ccAddresses = getCcAddresses();
            final EmailAddressList bccAddresses = getBccAddresses();

            final EmailMimeMessage emailMimeMessage = new EmailMimeMessage();
            try {
                emailMimeMessage.send(this);
            } catch (final SendFailedException e) {
                logProblem(e);

                final Address[] invalidAddresses = e.getInvalidAddresses();
                setFailedAddresses(invalidAddresses);
                final Address[] validSentAddresses = e.getValidSentAddresses();
                setConfirmedAddresses(validSentAddresses);
                final Address[] validUnsentAddresses = e.getValidUnsentAddresses();
                resend(validUnsentAddresses);
            } catch (final MessagingException e) {
                logProblem(e);
                // abort();
                retry(toAddresses, ccAddresses, bccAddresses);
            }

            if (!hasAnyRecipients()) {
                setRootDomainObjectFromEmailQueue(null);
            }
        }
    }

    private boolean hasAnyRecipients() {
        return hasAnyRecipients(getToAddresses()) || hasAnyRecipients(getCcAddresses()) || hasAnyRecipients(getBccAddresses());
    }

    private boolean hasAnyRecipients(final EmailAddressList emailAddressList) {
        return emailAddressList != null && !emailAddressList.isEmpty();
    }

}
