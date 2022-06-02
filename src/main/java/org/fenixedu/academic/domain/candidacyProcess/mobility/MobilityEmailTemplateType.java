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
package org.fenixedu.academic.domain.candidacyProcess.mobility;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.core.domain.Message;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;

public enum MobilityEmailTemplateType {

    PREREGISTRATION {

        private static final String APPLICATION_SUBMISSION_LINK =
                "MobilityIndividualApplicationProcess.const.public.application.submission.link";

        @Override
        public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {
            String subject = mobilityEmailTemplate.getSubject();
            String body = mobilityEmailTemplate.getBody();
            String link = BundleUtil.getString(Bundle.CANDIDATE, APPLICATION_SUBMISSION_LINK);

            link = String.format(link, hashCode.getValue(), I18N.getLocale());

            if (body.contains("[submission_link]")) {
                body = body.replace("[submission_link]", link);
            }

            this.sendEmail(subject, body, hashCode.getEmail());
        }

        @Override
        public void sendMultiEmailFor(MobilityEmailTemplate mobilityEmailTemplate,
                Collection<MobilityIndividualApplicationProcess> processes) {
            throw new DomainException("template.meant.for.single.recipient.only");
        }

    },

    APPLICATION_SUBMISSION {

        private static final String APPLICATION_ACCESS_LINK =
                "MobilityIndividualApplicationProcess.const.public.application.access.link";

        @Override
        public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {
            IndividualCandidacyProcess individualCandidacyProcess = hashCode.getIndividualCandidacyProcess();

            String subject = mobilityEmailTemplate.getSubject();
            String body = mobilityEmailTemplate.getBody();
            String link =
                    String.format(BundleUtil.getString(Bundle.CANDIDATE, APPLICATION_ACCESS_LINK), hashCode.getValue(), I18N
                            .getLocale().getLanguage());

            String processCode = individualCandidacyProcess.getProcessCode();
            String endDate = individualCandidacyProcess.getCandidacyProcess().getCandidacyEnd().toString("dd/MM/yyyy");

            if (body.contains("[process_number]")) {
                body = body.replace("[process_number]", processCode);
            }

            if (body.contains("[process_access_link]")) {
                body = body.replace("[process_access_link]", link);
            }

            if (body.contains("[application_submission_end_date]")) {
                body = body.replace("[application_submission_end_date]", endDate);
            }

            sendEmail(subject, body, hashCode.getEmail());
        }

        @Override
        public void sendMultiEmailFor(MobilityEmailTemplate mobilityEmailTemplate,
                Collection<MobilityIndividualApplicationProcess> processes) {
            throw new DomainException("template.meant.for.single.recipient.only");
        }

    },

    MISSING_DOCUMENTS {

        @Override
        public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {

            MobilityIndividualApplicationProcess miap =
                    ((MobilityIndividualApplicationProcess) hashCode.getIndividualCandidacyProcess());

            StringBuilder missingDocs = new StringBuilder();
            for (IndividualCandidacyDocumentFileType missingDocumentType : miap.getMissingRequiredDocumentFiles()) {
                missingDocs.append("- ").append(missingDocumentType.localizedName(Locale.ENGLISH)).append("\n");
            }

            String subject =
                    StringUtils.isEmpty(mobilityEmailTemplate.getSubject()) ? MessageFormat.format(
                            BundleUtil.getString(Bundle.CANDIDATE, "message.erasmus.missing.required.documents.email.subject"),
                            Unit.getInstitutionAcronym()) : mobilityEmailTemplate.getSubject();
            String body =
                    StringUtils.isEmpty(mobilityEmailTemplate.getBody()) ? BundleUtil.getString(Bundle.CANDIDATE,
                            "message.erasmus.missing.required.documents.email.body") : mobilityEmailTemplate.getBody();

            if (body.contains("[missing_documents]")) {
                body = body.replace("[missing_documents]", missingDocs.toString());
            }

            if (body.contains("[application_submission_end_date]")) {
                body = body.replace("[application_submission_end_date]", miap.getCandidacyEnd().toString("dd/MM/yyyy"));
            }

            sendEmail(subject, body, hashCode.getEmail());
        }

        @Override
        public void sendMultiEmailFor(MobilityEmailTemplate mobilityEmailTemplate,
                Collection<MobilityIndividualApplicationProcess> processes) {
            for (MobilityIndividualApplicationProcess process : processes) {
                sendEmailFor(mobilityEmailTemplate, process.getCandidacyHashCode());
            }

        }

    },

    MISSING_SHIFTS {

        @Override
        public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {

            MobilityIndividualApplicationProcess miap =
                    ((MobilityIndividualApplicationProcess) hashCode.getIndividualCandidacyProcess());

            StringBuilder missingShifts = new StringBuilder();

            for (ExecutionCourse course : miap.getMissingShifts()) {
                missingShifts.append("- ").append(course.getNameI18N().getContent()).append("\n");
            }

            String subject =
                    StringUtils.isEmpty(mobilityEmailTemplate.getSubject()) ? MessageFormat.format(
                            BundleUtil.getString(Bundle.CANDIDATE, "message.erasmus.missing.shifts.email.subject"),
                            Unit.getInstitutionAcronym()) : mobilityEmailTemplate.getSubject();
            String body =
                    StringUtils.isEmpty(mobilityEmailTemplate.getBody()) ? BundleUtil.getString(Bundle.CANDIDATE,
                            "message.erasmus.missing.shifts.email.body") : mobilityEmailTemplate.getBody();

            if (body.contains("[missing_shifts]")) {
                body = body.replace("[missing_shifts]", missingShifts.toString());
            }

            sendEmail(subject, body, hashCode.getEmail());
        }

        @Override
        public void sendMultiEmailFor(MobilityEmailTemplate mobilityEmailTemplate,
                Collection<MobilityIndividualApplicationProcess> processes) {
            for (MobilityIndividualApplicationProcess process : processes) {
                sendEmailFor(mobilityEmailTemplate, process.getCandidacyHashCode());
            }

        }

    },

    CANDIDATE_ACCEPTED {

        private static final String APPLICATION_ACCESS_LINK =
                "MobilityIndividualApplicationProcess.const.public.application.access.link";

        @Override
        public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {
            String subject = mobilityEmailTemplate.getSubject();
            String body = mobilityEmailTemplate.getBody();
            String link = BundleUtil.getString(Bundle.CANDIDATE, APPLICATION_ACCESS_LINK);

            link = String.format(link, hashCode.getValue(), I18N.getLocale());

            if (body.contains("[access_link]")) {
                body = body.replace("[access_link]", link);
            }

            this.sendEmail(subject, body, hashCode.getEmail());
        }

        @Override
        public void sendMultiEmailFor(MobilityEmailTemplate mobilityEmailTemplate,
                Collection<MobilityIndividualApplicationProcess> processes) {
            throw new DomainException("template.meant.for.single.recipient.only");
        }

    },

    IST_RECEPTION {
        private static final String REGISTRATION_ACCESS_LINK =
                "MobilityIndividualApplicationProcess.const.public.registration.access.link";

        @Override
        public void sendEmailFor(MobilityEmailTemplate mobilityEmailTemplate, DegreeOfficePublicCandidacyHashCode hashCode) {
            String subject = mobilityEmailTemplate.getSubject();
            String body = mobilityEmailTemplate.getBody();

            String link = BundleUtil.getString(Bundle.CANDIDATE, REGISTRATION_ACCESS_LINK, hashCode.getValue());

            if (body.contains("[registration_link]")) {
                body = body.replace("[registration_link]", link);
            }
            final Registration registration = hashCode.getIndividualCandidacyProcess().getCandidacy().getRegistration();
            if (registration == null) {
                throw new NullPointerException("Students have not yet been registered.");
            }
            if (body.contains("[username]")) {
                body = body.replace("[username]", registration.getPerson().getUsername());
            }

            sendEmail(subject, body, hashCode.getEmail());
        }

        @Override
        public void sendMultiEmailFor(MobilityEmailTemplate mobilityEmailTemplate,
                Collection<MobilityIndividualApplicationProcess> processes) {
            for (MobilityIndividualApplicationProcess process : processes) {
                this.sendEmailFor(mobilityEmailTemplate, process.getCandidacyHashCode());
            }
        }

    };

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getQualifiedName());
    }

    public String getQualifiedName() {
        return MobilityEmailTemplateType.class.getSimpleName() + "." + name();
    }

    public String getFullQualifiedName() {
        return MobilityEmailTemplateType.class.getName() + "." + name();
    }

    protected void sendEmail(final String fromSubject, final String body, final String email) {
        Message.fromSystem().replyToSender().singleBcc(email)
                .subject(fromSubject).textBody(body)
                .send();
    }

    abstract public void sendEmailFor(final MobilityEmailTemplate mobilityEmailTemplate,
            final DegreeOfficePublicCandidacyHashCode hashCode);

    abstract public void sendMultiEmailFor(final MobilityEmailTemplate mobilityEmailTemplate,
            final Collection<MobilityIndividualApplicationProcess> processes);

}
