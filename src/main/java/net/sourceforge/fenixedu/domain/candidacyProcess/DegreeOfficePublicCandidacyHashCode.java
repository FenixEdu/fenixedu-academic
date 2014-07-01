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
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplate;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.collections.CollectionUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class DegreeOfficePublicCandidacyHashCode extends DegreeOfficePublicCandidacyHashCode_Base {

    public DegreeOfficePublicCandidacyHashCode() {
        super();
    }

    public boolean isAssociatedWithEmailAndCandidacyProcess(String email, Class<? extends IndividualCandidacyProcess> type,
            CandidacyProcess process, List<Degree> degreeList) {
        if (email.equals(this.getEmail()) && this.hasIndividualCandidacyProcess()
                && !getIndividualCandidacyProcess().isCandidacyCancelled()
                && this.getIndividualCandidacyProcess().getClass() == type
                && this.getIndividualCandidacyProcess().getCandidacyProcess() == process) {
            return CollectionUtils.disjunction(this.getIndividualCandidacyProcess().getCandidacy().getAllDegrees(), degreeList)
                    .isEmpty();

        } else {
            return false;
        }
    }

    @Override
    public boolean hasCandidacyProcess() {
        return hasIndividualCandidacyProcess();
    }

    @Override
    public boolean isFromDegreeOffice() {
        return true;
    }

    /**
     * Get an hash code not associated with an individual candidacy process for
     * the email. Also sends an email
     * 
     * @throws HashCodeForEmailAndProcessAlreadyBounded
     */
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
            Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
            String email) throws HashCodeForEmailAndProcessAlreadyBounded {

        DegreeOfficePublicCandidacyHashCode hashCode =
                getUnusedOrCreateNewHashCode(individualCandidadyProcessClass, parentProcess, email);

        if (parentProcess.isMobility()) {
            MobilityApplicationProcess mobilityApplicationProcess = (MobilityApplicationProcess) parentProcess;
            MobilityApplicationPeriod candidacyPeriod = mobilityApplicationProcess.getCandidacyPeriod();

            MobilityEmailTemplate emailTemplateFor =
                    candidacyPeriod.getEmailTemplateFor(MobilityEmailTemplateType.PREREGISTRATION);

            emailTemplateFor.sendEmailFor(hashCode);
        } else {
            hashCode.sendEmailForApplicationSubmissionCandidacyForm(individualCandidadyProcessClass);
        }

        return hashCode;
    }

    private static final String APPLICATION_SUBMISSION_LINK = ".const.public.application.submission.link";
    private static final String SEND_LINK_TO_ACCESS_SUBMISSION_FORM_SUBJECT =
            ".message.email.subject.send.link.to.submission.form";
    private static final String SEND_LINK_TO_ACCESS_SUBMISSION_FORM_BODY = ".message.email.body.send.link.to.submission.form";

    private void sendEmailForApplicationSubmissionCandidacyForm(
            Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass) {
        String subject =
                BundleUtil.getString(Bundle.CANDIDATE, individualCandidadyProcessClass.getSimpleName() + SEND_LINK_TO_ACCESS_SUBMISSION_FORM_SUBJECT);
        String body =
                BundleUtil.getString(Bundle.CANDIDATE, individualCandidadyProcessClass.getSimpleName() + SEND_LINK_TO_ACCESS_SUBMISSION_FORM_BODY);
        String link = BundleUtil.getString(Bundle.CANDIDATE, individualCandidadyProcessClass.getSimpleName() + APPLICATION_SUBMISSION_LINK);
        link = String.format(link, this.getValue(), I18N.getLocale());
        body = String.format(body, link);
        this.sendEmail(subject, body);
    }

    private static final String APPLICATION_ACCESS_LINK = ".const.public.application.access.link";
    private static final String INFORM_APPLICATION_SUCCESS_SUBJECT = ".message.email.subject.application.submited";
    private static final String INFORM_APPLICATION_SUCCESS_BODY = ".message.email.body.application.submited";

    public void sendEmailForApplicationSuccessfullySubmited() {
        CandidacyProcess parentProcess = getIndividualCandidacyProcess().getCandidacyProcess();

        if (parentProcess.isMobility()) {
            MobilityApplicationProcess mobilityApplicationProcess = (MobilityApplicationProcess) parentProcess;
            MobilityApplicationPeriod candidacyPeriod = mobilityApplicationProcess.getCandidacyPeriod();

            MobilityEmailTemplate emailTemplateFor =
                    candidacyPeriod.getEmailTemplateFor(MobilityEmailTemplateType.APPLICATION_SUBMISSION);

            emailTemplateFor.sendEmailFor(this);

            return;
        }

        String subject =
                MessageFormat.format(BundleUtil.getString(Bundle.CANDIDATE, this.getIndividualCandidacyProcess().getClass().getSimpleName()
                        + INFORM_APPLICATION_SUCCESS_SUBJECT), Unit.getInstitutionAcronym(), Unit.getInstitutionName()
                        .getContent());
        String body =
                MessageFormat.format(BundleUtil.getString(Bundle.CANDIDATE, this.getIndividualCandidacyProcess().getClass().getSimpleName()
                        + INFORM_APPLICATION_SUCCESS_BODY), Unit.getInstitutionAcronym(), Unit.getInstitutionName().getContent());
        String link = getDefaultPublicLink();

        body =
                String.format(
                        body,
                        new String[] {
                                this.getIndividualCandidacyProcess().getProcessCode(),
                                link,
                                this.getIndividualCandidacyProcess().getCandidacyProcess().getCandidacyEnd()
                                        .toString(DateFormatUtil.DEFAULT_DATE_FORMAT) });

        sendEmail(subject, body);
    }

    private static final String RECOVERY_APPLICATION_SUBJECT = ".message.email.subject.recovery.access";
    private static final String RECOVERY_APPLICATION_BODY = ".message.email.body.recovery.access";

    public void sendEmailFoAccessLinkRecovery() {
        String subject =
                BundleUtil.getString(Bundle.CANDIDATE, this.getIndividualCandidacyProcess().getClass().getSimpleName() + RECOVERY_APPLICATION_SUBJECT);
        String body =
                BundleUtil.getString(Bundle.CANDIDATE, this.getIndividualCandidacyProcess().getClass().getSimpleName() + RECOVERY_APPLICATION_BODY);
        String link = getDefaultPublicLink();

        body = String.format(body, new String[] { link, this.getIndividualCandidacyProcess().getProcessCode() });

        sendEmail(subject, body);
    }

    public String getDefaultPublicLink() {
        return String.format(
                BundleUtil.getString(Bundle.CANDIDATE, this.getIndividualCandidacyProcess().getClass().getSimpleName() + APPLICATION_ACCESS_LINK),
                this.getValue(), I18N.getLocale().getLanguage());
    }

    /**
     * Get an hash code not associated with an individual candidacy process for
     * the email. If the hash
     * 
     * @throws HashCodeForEmailAndProcessAlreadyBounded
     */
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCode(
            Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
            String email) throws HashCodeForEmailAndProcessAlreadyBounded {

        DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode =
                getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(email, individualCandidadyProcessClass,
                        parentProcess);

        if (publicCandidacyHashCode == null) {
            return createNewHashCode(email);
        } else if (!publicCandidacyHashCode.hasCandidacyProcess()) {
            return publicCandidacyHashCode;
        } else {
            throw new HashCodeForEmailAndProcessAlreadyBounded("error.hash.code.for.email.and.process.already.bounded");
        }
    }

    private static DegreeOfficePublicCandidacyHashCode createNewHashCode(String email) {
        DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode = new DegreeOfficePublicCandidacyHashCode();
        publicCandidacyHashCode.setEmail(email);
        publicCandidacyHashCode.setValue(UUID.randomUUID().toString());
        return publicCandidacyHashCode;
    }

    public static DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(
            final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {
        return getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email, processType, process, new ArrayList<Degree>());
    }

    public static DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(
            final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process,
            List<Degree> degreeList) {

        for (final PublicCandidacyHashCode hashCode : getHashCodesAssociatedWithEmail(email)) {
            if (!hashCode.isFromDegreeOffice()) {
                continue;
            }

            final DegreeOfficePublicCandidacyHashCode hash = (DegreeOfficePublicCandidacyHashCode) hashCode;
            if (hash.isAssociatedWithEmailAndCandidacyProcess(email, processType, process, degreeList)) {
                return hash;
            }
        }

        return null;
    }

    static public DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
            final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {

        DegreeOfficePublicCandidacyHashCode associatedHashCode =
                getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email, processType, process);

        if (associatedHashCode != null) {
            return associatedHashCode;
        }

        for (final PublicCandidacyHashCode hashCode : getHashCodesAssociatedWithEmail(email)) {
            if (hashCode.isFromDegreeOffice() && !hashCode.hasCandidacyProcess()) {
                return (DegreeOfficePublicCandidacyHashCode) hashCode;
            }
        }

        return null;
    }

    @Deprecated
    public boolean hasIndividualCandidacyProcess() {
        return getIndividualCandidacyProcess() != null;
    }

}
