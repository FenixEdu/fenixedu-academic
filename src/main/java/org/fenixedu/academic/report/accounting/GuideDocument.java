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
package org.fenixedu.academic.report.accounting;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.PaymentsManagementDTO;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class GuideDocument extends FenixReport {

    private final PaymentsManagementDTO paymentsManagementDTO;

    public static class GuideDocumentEntry {
        private String description;

        private String amountToPay;

        public GuideDocumentEntry(final String description, final String amountToPay) {
            this.description = description;
            this.amountToPay = amountToPay;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAmountToPay() {
            return amountToPay;
        }

        public void setAmountToPay(String amountToPay) {
            this.amountToPay = amountToPay;
        }

    }

    public GuideDocument(final PaymentsManagementDTO paymentsManagementDTO) {
        this.paymentsManagementDTO = paymentsManagementDTO;
        fillReport();
    }

    @Override
    protected void fillReport() {

        addParameter("total", this.paymentsManagementDTO.getTotalAmountToPay().toPlainString());
        addParameter("date", new LocalDate().toString(DD_MMMM_YYYY, getLocale()));
        addParameter("unitPhone", Bennu.getInstance().getInstitutionUnit().getDefaultPhoneNumber());
        addParameter("documentIdType", this.paymentsManagementDTO.getPerson().getIdDocumentType().getLocalizedName());
        addParameter("documentIdNumber", this.paymentsManagementDTO.getPerson().getDocumentIdNumber());
        addParameter("name", this.paymentsManagementDTO.getPerson().getName());
        addParameter("studentNumber", this.paymentsManagementDTO.getPerson().getStudent() != null ? this.paymentsManagementDTO
                .getPerson().getStudent().getNumber().toString() : null);

        addDataSourceElements(buildEntries());
    }

    private List<GuideDocumentEntry> buildEntries() {

        final List<GuideDocumentEntry> result = new ArrayList<GuideDocumentEntry>();

        for (final EntryDTO entryDTO : this.paymentsManagementDTO.getSelectedEntries()) {
            result.add(new GuideDocumentEntry(entryDTO.getDescription().toString(), entryDTO.getAmountToPay().toPlainString()));
        }

        return result;

    }

    @Override
    public String getReportFileName() {
        return "Guide-" + new DateTime().toString(YYYYMMDDHHMMSS, getLocale());
    }

}
