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
package org.fenixedu.academic.report.phd.thesis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.report.FenixReport;
import org.joda.time.DateTime;

public class PhdThesisJuryElementsDocument extends FenixReport {

    static private final long serialVersionUID = 1L;

    private final PhdThesisProcess process;

    public PhdThesisJuryElementsDocument(PhdThesisProcess process) {
        this.process = process;

        fillReport();
    }

    @Override
    protected void fillReport() {
        AdministrativeOffice adminOffice = process.getIndividualProgramProcess().getPhdProgram().getAdministrativeOffice();

        getPayload().addProperty("ratificationEntityMessage", process.getPhdJuryElementsRatificationEntity()
                .getRatificationEntityMessage(process, getLocale()));
        getPayload().addProperty("presidentTitle", this.process.getPresidentTitle().getContent(getLanguage()));
        getPayload().add("juryElements", getJuryElementsInformation());
        getPayload().addProperty("administrativeOfficeCoordinator", adminOffice.getCoordinator().getProfile()
                .getDisplayName());
    }

    private JsonArray getJuryElementsInformation() {
        JsonArray result = new JsonArray();

        process.getOrderedThesisJuryElements().stream().map(el -> {
            JsonObject juryElement = new JsonObject();

            juryElement.addProperty("title", el.getTitle());
            juryElement.addProperty("name", el.getName());
            juryElement.addProperty("category", el.getCategory());
            juryElement.addProperty("workLocation", el.getWorkLocation());
            juryElement.addProperty("institution", el.getInstitution());
            juryElement.addProperty("isExpert", el.getExpert());
            juryElement.addProperty("isAssistantGuiding", el.isAssistantGuiding());
            juryElement.addProperty("isMainGuiding", el.isMainGuiding());
            juryElement.addProperty("isReporter", el.getReporter());

            return juryElement;
        }).forEach(result::add);

        return result;
    }

    @Override
    public String getReportFileName() {
        return "JuryElements-" + new DateTime().toString(YYYYMMDDHHMMSS);
    }

}
