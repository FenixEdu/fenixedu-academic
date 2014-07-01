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
package net.sourceforge.fenixedu.presentationTier.docs.phd.thesis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
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

        addJuryElementsInformation();

        Unit adminOfficeUnit = process.getIndividualProgramProcess().getPhdProgram().getAdministrativeOffice().getUnit();

        addParameter("presidentTitle", this.process.getPresidentTitle().getContent(getLanguage()));
        addParameter("administrativeOfficeCoordinator", adminOfficeUnit.getActiveUnitCoordinator().getPartyName().getContent());
        addParameter("administrativeOfficeName", adminOfficeUnit.getPartyName().getContent());
        addParameter("ratificationEntityMessage",
                process.getPhdJuryElementsRatificationEntity().getRatificationEntityMessage(process, getLocale()));
    }

    private void addJuryElementsInformation() {
        final List<ThesisJuryElementInfo> elements = new ArrayList<ThesisJuryElementInfo>();

        for (final ThesisJuryElement element : process.getOrderedThesisJuryElements()) {
            elements.add(new ThesisJuryElementInfo(element));
        }

        addDataSourceElements(elements);
    }

    @Override
    public String getReportFileName() {
        return "JuryElements-" + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    static public class ThesisJuryElementInfo {

        private final String description;

        public ThesisJuryElementInfo(ThesisJuryElement element) {
            this.description = buildDescription(element);
        }

        private String buildDescription(ThesisJuryElement element) {
            final StringBuilder builder = new StringBuilder();

            builder.append(!StringUtils.isEmpty(element.getTitle()) ? element.getTitle() : "").append(" ");
            builder.append(element.getName()).append(", ");
            builder.append(element.getCategory());

            if (!StringUtils.isEmpty(element.getWorkLocation())) {
                builder.append(" ").append(BundleUtil.getString(Bundle.PHD, "label.phd.thesis.jury.elements.document.keyword.of")).append(" ")
                        .append(element.getWorkLocation());
            }

            if (!StringUtils.isEmpty(element.getInstitution())) {
                builder.append(" ").append(BundleUtil.getString(Bundle.PHD, "label.phd.thesis.jury.elements.document.keyword.of")).append(" ")
                        .append(element.getInstitution());
            }

            if (element.getExpert()) {
                builder.append(" ").append(BundleUtil.getString(Bundle.PHD, "label.phd.thesis.jury.elements.document.expert"));
            }

            builder.append(";");

            if (element.isAssistantGuiding()) {
                builder.append(" (").append(BundleUtil.getString(Bundle.PHD, "label.phd.thesis.jury.elements.document.assistantGuiding")).append(")");
            } else if (element.isMainGuiding()) {
                builder.append(" (").append(BundleUtil.getString(Bundle.PHD, "label.phd.thesis.jury.elements.document.guiding")).append(")");
            } else if (element.getReporter()) {
                builder.append(" - ").append(BundleUtil.getString(Bundle.PHD, "label.phd.thesis.jury.elements.document.reporter"));
            }

            return builder.toString();
        }

        public String getDescription() {
            return description;
        }

    }

}
