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
package net.sourceforge.fenixedu.presentationTier.docs.accounting;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.IMessageResourceProvider;

public class GuideDocument extends FenixReport {

    private final PaymentsManagementDTO paymentsManagementDTO;

    private final IMessageResourceProvider messageResourceProvider;

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

    public GuideDocument(final PaymentsManagementDTO paymentsManagementDTO, final IMessageResourceProvider messageResourceProvider) {
        this.paymentsManagementDTO = paymentsManagementDTO;
        this.messageResourceProvider = messageResourceProvider;
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
        addParameter("studentNumber", this.paymentsManagementDTO.getPerson().hasStudent() ? this.paymentsManagementDTO
                .getPerson().getStudent().getNumber().toString() : null);

        addDataSourceElements(buildEntries());
    }

    private List<GuideDocumentEntry> buildEntries() {

        final List<GuideDocumentEntry> result = new ArrayList<GuideDocumentEntry>();

        for (final EntryDTO entryDTO : this.paymentsManagementDTO.getSelectedEntries()) {
            result.add(new GuideDocumentEntry(entryDTO.getDescription().toString(this.messageResourceProvider), entryDTO
                    .getAmountToPay().toPlainString()));
        }

        return result;

    }

    @Override
    public String getReportFileName() {
        return "Guide-" + new DateTime().toString(YYYYMMDDHHMMSS, getLocale());
    }

}
