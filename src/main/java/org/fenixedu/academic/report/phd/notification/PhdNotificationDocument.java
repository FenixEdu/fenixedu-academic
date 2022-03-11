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
package org.fenixedu.academic.report.phd.notification;

import com.google.gson.JsonArray;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.postingRules.FixedAmountPR;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.domain.phd.notification.PhdNotification;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Locale;
import java.util.Set;

public class PhdNotificationDocument extends FenixReport {

    private static final String DATE_FORMAT_PT = "dd/MM/yyyy";

    private static final String DATE_FORMAT_EN = "yyyy/MM/dd";

    private PhdNotification notification;

    private Locale language;

    public PhdNotificationDocument(PhdNotification notification, Locale language) {
        setNotification(notification);
        setLanguage(language);
        fillReport();
    }

    private PhdNotification getNotification() {
        return this.notification;
    }

    private void setNotification(PhdNotification notification) {
        this.notification = notification;
    }

    @Override
    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    @Override
    protected void fillReport() {
        final PhdProgramCandidacyProcess candidacyProcess = getNotification().getCandidacyProcess();
        final Person person = candidacyProcess.getPerson();
        final PhdIndividualProgramProcess individualProgramProcess = candidacyProcess.getIndividualProgramProcess();
        final AdministrativeOffice administrativeOffice = individualProgramProcess.getPhdProgram().getAdministrativeOffice();
        final ExecutionYear executionYear = individualProgramProcess.getExecutionYear();
        final LocalDate whenRatified = candidacyProcess.getWhenRatified();

        getPayload().addProperty("administrativeOfficeName", administrativeOffice.getName().getContent());
        getPayload().addProperty("administrativeOfficeCoordinator", administrativeOffice.getCoordinator().getProfile().getDisplayName());
        getPayload().addProperty("name", person.getName());
        getPayload().addProperty("processNumber", individualProgramProcess.getProcessNumber());
        getPayload().addProperty("address", person.getAddress());
        getPayload().addProperty("areaCode", person.getAreaCode());
        getPayload().addProperty("areaOfAreaCode", person.getAreaOfAreaCode());
        getPayload().addProperty("programName", individualProgramProcess.getPhdProgram().getName(executionYear).getContent(getLanguage()));
        getPayload().addProperty("ratificationDate", whenRatified != null ? whenRatified.toString(getDateFormat()) : "");
        getPayload().addProperty("insuranceFee", getInsuranceFee(individualProgramProcess));
        getPayload().addProperty("registrationFee", getRegistrationFee(individualProgramProcess, whenRatified));
        getPayload().addProperty("currentDate", new LocalDate().toString(getDateFormat()));
        getPayload().addProperty("notificationNumber", getNotification().getNotificationNumber());
        getPayload().add("mainAdvisors", getAdvisors(individualProgramProcess.getGuidingsSet()));
        getPayload().add("coAdvisors", getAdvisors(individualProgramProcess.getAssistantGuidingsSet()));
    }

    private JsonArray getAdvisors(Set<PhdParticipant> advisors) {
        JsonArray advisorsArray = new JsonArray();

        advisors.stream().map(PhdParticipant::getNameWithTitle).forEach(advisorsArray::add);

        return advisorsArray;
    }

    private String getDateFormat() {
        return getLanguage() == org.fenixedu.academic.util.LocaleUtils.PT ? DATE_FORMAT_PT : DATE_FORMAT_EN;
    }

    private String getRegistrationFee(final PhdIndividualProgramProcess individualProgramProcess, final LocalDate whenRatified) {
        return whenRatified != null ? ((FixedAmountPR) individualProgramProcess.getPhdProgram().getServiceAgreementTemplate()
                .findPostingRuleByEventTypeAndDate(EventType.PHD_REGISTRATION_FEE, whenRatified.toDateTimeAtMidnight()))
                .getFixedAmount().toPlainString() : "";
    }

    private String getInsuranceFee(final PhdIndividualProgramProcess individualProgramProcess) {
        return ((FixedAmountPR) Bennu
                .getInstance()
                .getInstitutionUnit()
                .getUnitServiceAgreementTemplate()
                .findPostingRuleBy(EventType.INSURANCE,
                        individualProgramProcess.getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight(),
                        individualProgramProcess.getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight()))
                .getFixedAmount().toPlainString();
    }

    @Override
    public String getReportFileName() {
        return "Notification-" + getNotification().getNotificationNumber().replace("/", "-") + "-"
                + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
        return getClass().getName() + "." + getNotification().getType().name() + "." + getLanguage();
    }

}
