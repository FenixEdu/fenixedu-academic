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
package net.sourceforge.fenixedu.presentationTier.docs.phd.notification;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdCandidacyDeclarationDocument extends FenixReport {
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
        addParameter("name", getCandidacyProcess().getPerson().getName());
        addParameter("programName",
                getCandidacyProcess().getIndividualProgramProcess().getPhdProgram().getName().getContent(getLanguage()));
        addParameter("candidacyDate", getCandidacyProcess().getCandidacyDate());
        addParameter("currentDate", new LocalDate());
        addParameter("documentIdNumber", getCandidacyProcess().getPerson().getDocumentIdNumber());
        addParameter("candidacyNumber", getCandidacyProcess().getProcessNumber());

        addParameter("administrativeOfficeCoordinator", getCandidacyProcess().getIndividualProgramProcess().getPhdProgram()
                .getAdministrativeOffice().getUnit().getActiveUnitCoordinator().getFirstAndLastName());

        addParameter("institutionName", Bennu.getInstance().getInstitutionUnit().getPartyName().getContent());
        addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getPartyName().getContent());
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
