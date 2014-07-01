/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.AlumniRequestType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class AlumniNotificationService {

    private static void sendEmail(final Collection<Recipient> recipients, final String subject, final String body,
            final String bccs) {
        SystemSender systemSender = Bennu.getInstance().getSystemSender();
        new Message(systemSender, systemSender.getConcreteReplyTos(), recipients, subject, body, bccs);
    }

    private static List<Recipient> getAlumniRecipients(Alumni alumni) {
        return Collections.singletonList(Recipient.newInstance(UserGroup.of(alumni.getStudent().getPerson().getUser())));
    }

    protected static void sendPublicAccessMail(final Alumni alumni, final String alumniEmail) {

        final String subject =
                BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.mail.subject", Unit.getInstitutionAcronym());
        final Person person = alumni.getStudent().getPerson();
        final String body =
                BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.url", person.getFirstAndLastName(),
                        getRegisterConclusionURL(alumni));

        sendEmail(Collections.EMPTY_LIST, subject, body, alumniEmail);
    }

    public static String getRegisterConclusionURL(final Alumni alumni) {
        final String fenixURL = BundleUtil.getString(Bundle.GLOBAL, "fenix.url");
        return MessageFormat.format(BundleUtil.getString(Bundle.ALUMNI, "alumni.public.registration.conclusion.url"), fenixURL,
                alumni.getExternalId().toString(), alumni.getUrlRequestToken());
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
                                request.getAlumni().getLoginUsername(), request.getExternalId(), request.getRequestToken()
                                        .toString());
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

        sendEmail(Collections.EMPTY_LIST, subject, body, request.getContactEmail());
    }

    protected static void sendRegistrationSuccessMail(final Alumni alumni) {

        final String subject =
                MessageFormat.format(BundleUtil.getString(Bundle.ALUMNI, "alumni.public.success.mail.subject"),
                        Unit.getInstitutionAcronym());
        final String body =
                MessageFormat.format(BundleUtil.getString(Bundle.ALUMNI, "alumni.public.username.login.url"), alumni.getStudent()
                        .getPerson().getFirstAndLastName(), alumni.getLoginUsername());

        sendEmail(getAlumniRecipients(alumni), subject, body, null);
    }

}