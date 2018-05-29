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
package org.fenixedu.academic.service.services.commons.alumni;

import java.text.MessageFormat;

import org.fenixedu.academic.domain.Alumni;
import org.fenixedu.academic.domain.AlumniIdentityCheckRequest;
import org.fenixedu.academic.domain.AlumniRequestType;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.messaging.core.domain.Message;

public class AlumniNotificationService {

    private static void sendEmail(final Group recipients, final String subject, final String body) {
        Message.fromSystem()
                .replyToSender()
                .to(recipients)
                .subject(subject)
                .textBody(body)
                .send();
    }

    private static void sendEmail(final String subject, final String body,
            final String bccs) {
        Message.fromSystem()
                .replyToSender()
                .singleBcc(bccs)
                .subject(subject)
                .textBody(body)
                .send();
    }

    private static Group getAlumniRecipients(Alumni alumni) {
        return alumni.getStudent().getPerson().getPersonGroup();
    }

    protected static void sendPublicAccessMail(final Alumni alumni, final String alumniEmail) {

        final String subject =
                BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.mail.subject", Unit.getInstitutionAcronym());
        final Person person = alumni.getStudent().getPerson();
        final String body =
                BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.url", person.getFirstAndLastName(),
                        getRegisterConclusionURL(alumni));

        sendEmail(subject, body, alumniEmail);
    }

    public static String getRegisterConclusionURL(final Alumni alumni) {
        String fenixURL = CoreConfiguration.getConfiguration().applicationUrl();
        if (!fenixURL.endsWith("/")) {
            fenixURL += "/";
        }
        return MessageFormat.format(BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.conclusion.url"), fenixURL,
                alumni.getExternalId(), alumni.getUrlRequestToken());
    }

    protected static void sendIdentityCheckEmail(AlumniIdentityCheckRequest request, Boolean approval) {

        final String subject =
                MessageFormat.format(BundleUtil.getString(Bundle.MANAGER, "alumni.identity.request.mail.subject"),
                        Unit.getInstitutionAcronym());

        String body;
        if (approval) {
            body =
                    MessageFormat.format(BundleUtil.getString(Bundle.MANAGER, "alumni.identity.request.confirm.identity"),
                            request.getAlumni().getStudent().getPerson().getFirstAndLastName());

            switch (request.getRequestType()) {

            case IDENTITY_CHECK: // legacy behavior
                body += BundleUtil.getString(Bundle.MANAGER, "alumni.identity.request.curriculum.access");
                break;
            case PASSWORD_REQUEST:
                body +=
                        MessageFormat.format(BundleUtil.getString(Bundle.MANAGER, "alumni.identity.request.password.request"),
                                request.getAlumni().getLoginUsername(), "https://id.tecnico.ulisboa.pt", request.getExternalId(),
                                request.getRequestToken().toString());
                break;
            case STUDENT_NUMBER_RECOVERY:
                body +=
                        MessageFormat.format(BundleUtil.getString(Bundle.MANAGER, "alumni.identity.request.student.number.info"),
                                request.getAlumni().getStudent().getNumber().toString(), Unit.getInstitutionAcronym());
                break;

            default:
                return;
            }
        } else {
            body =
                    MessageFormat.format(BundleUtil.getString(Bundle.MANAGER, "alumni.identity.request.refuse.identity"), request
                            .getAlumni().getStudent().getPerson().getFirstAndLastName());
        }

        body = body + " " + request.getComment();
        if (!approval && request.getRequestType().equals(AlumniRequestType.PASSWORD_REQUEST)) {
            body +=
                    "\n"
                            + MessageFormat.format(
                                    BundleUtil.getString(Bundle.MANAGER, "alumni.identity.request.password.request.refuse"),
                                    getRegisterConclusionURL(request.getAlumni()));
        }

        sendEmail(subject, body, request.getContactEmail());
    }

    protected static void sendRegistrationSuccessMail(final Alumni alumni) {

        final String subject =
                MessageFormat.format(BundleUtil.getString(Bundle.ALUMNI, "alumni.public.success.mail.subject"),
                        Unit.getInstitutionAcronym());
        final String body =
                MessageFormat.format(BundleUtil.getString(Bundle.ALUMNI, "alumni.public.username.login.url"), alumni.getStudent()
                        .getPerson().getFirstAndLastName(), alumni.getLoginUsername());

        sendEmail(getAlumniRecipients(alumni), subject, body);
    }

}