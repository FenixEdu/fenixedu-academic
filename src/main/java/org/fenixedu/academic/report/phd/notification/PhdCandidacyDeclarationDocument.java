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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.report.FenixReport;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Locale;

public class PhdCandidacyDeclarationDocument extends FenixReport {

    private static final String DATE_FORMAT_PT = "dd/MM/yyyy";

    private static final String DATE_FORMAT_EN = "yyyy/MM/dd";

    private PhdProgramCandidacyProcess candidacyProcess;

    private Locale language;

    public PhdCandidacyDeclarationDocument(PhdProgramCandidacyProcess candidacyProcess, Locale language) {
        setCandidacyProcess(candidacyProcess);
        setLanguage(language);

        fillReport();
    }

    @Override
    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public PhdProgramCandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(PhdProgramCandidacyProcess arg) {
        this.candidacyProcess = arg;
    }

    @Override
    protected void fillReport() {
        final ExecutionYear executionYear = getCandidacyProcess().getIndividualProgramProcess().getExecutionYear();

        getPayload().addProperty("name", getCandidacyProcess().getPerson().getName());
        getPayload().addProperty("programName", getCandidacyProcess().getIndividualProgramProcess()
                .getPhdProgram().getName(executionYear).getContent(getLanguage()));
        getPayload().addProperty("candidacyDate", getCandidacyProcess().getCandidacyDate().toString(getDateFormat()));
        getPayload().addProperty("documentIdNumber", getCandidacyProcess().getPerson().getDocumentIdNumber());
        getPayload().addProperty("candidacyNumber", getCandidacyProcess().getProcessNumber());
        getPayload().addProperty("administrativeOfficeCoordinator", getCandidacyProcess().getIndividualProgramProcess()
                .getPhdProgram().getAdministrativeOffice().getCoordinator().getProfile().getDisplayName());
        getPayload().addProperty("currentDate", new LocalDate().toString(getDateFormat()));
    }

    private String getDateFormat() {
        return getLanguage() == org.fenixedu.academic.util.LocaleUtils.PT ? DATE_FORMAT_PT : DATE_FORMAT_EN;
    }

    @Override
    public String getReportFileName() {
        return "CandidacyDeclaration-" + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
        return getClass().getName() + "." + getLanguage().getLanguage();
    }

}
