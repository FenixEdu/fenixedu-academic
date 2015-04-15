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
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.report.FenixReport;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import com.google.common.base.Strings;

public class ReceiptDocument extends FenixReport {

    private final Receipt receipt;

    private final boolean original;

    public static class ReceiptDocumentEntry {
        private String description;

        private String amount;

        public ReceiptDocumentEntry(final String description, final String amount) {
            this.description = description;
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ReceiptDocument(final Receipt receipt, boolean original) {
        this.receipt = receipt;
        this.original = original;

        fillReport();

    }

    @Override
    protected void fillReport() {

        addParameter("ownerUnitPhone", Bennu.getInstance().getInstitutionUnit().getDefaultPhoneNumber());
        addParameter("documentIdType", this.receipt.getPerson().getIdDocumentType().getLocalizedName());
        addParameter("documentIdNumber", this.receipt.getPerson().getDocumentIdNumber());
        addParameter("name", this.receipt.getPerson().getName());

        addParameter("number", this.receipt.getNumberWithSeries());
        addParameter("year", this.receipt.getYear().toString());
        addParameter("secondPrintVersion", this.receipt.isSecondPrintVersion());
        addParameter("annulled", this.receipt.isAnnulled());
        addParameter("receiptDate", this.receipt.getReceiptDate().toString(DD_MMMM_YYYY, getLocale()));
        addParameter("total", this.receipt.getTotalAmount().toPlainString());

        addParameter("original", this.original);
        addParameter("contributorName", this.receipt.getContributorName());
        addParameter(
                "contributorSocialSecurityNumber",
                Strings.isNullOrEmpty(this.receipt.getContributorNumber()) ? Receipt.GENERIC_CONTRIBUTOR_PARTY_NUMBER : this.receipt
                        .getContributorNumber());
        addParameter("contributorAddress", this.receipt.getContributorAddress());

        addParameter("studentNumber", this.receipt.getPerson().getStudent() != null ? this.receipt.getPerson().getStudent()
                .getNumber().toString() : null);

        addDataSourceElements(buildEntries());

    }

    private List<ReceiptDocumentEntry> buildEntries() {

        final List<ReceiptDocumentEntry> result = new ArrayList<ReceiptDocumentEntry>();

        final SortedSet<Entry> sortedEntries = new TreeSet<Entry>(Entry.COMPARATOR_BY_MOST_RECENT_WHEN_REGISTERED);
        sortedEntries.addAll(receipt.getEntriesSet());

        for (final Entry entry : sortedEntries) {
            result.add(new ReceiptDocumentEntry(entry.getDescription().toString(), entry.getOriginalAmount().toPlainString()));
        }

        return result;

    }

    @Override
    public String getReportFileName() {
        return "Receipt-" + new DateTime().toString(YYYYMMDDHHMMSS, getLocale());
    }

}
