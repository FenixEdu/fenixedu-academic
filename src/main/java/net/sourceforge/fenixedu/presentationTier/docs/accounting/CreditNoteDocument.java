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
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteEntry;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.IMessageResourceProvider;

public class CreditNoteDocument extends FenixReport {

    private final CreditNote creditNote;

    private final IMessageResourceProvider messageResourceProvider;

    private final boolean original;

    public static class CreditNoteDocumentEntry {
        private String description;

        private String amount;

        public CreditNoteDocumentEntry(final String description, final String amount) {
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

    public CreditNoteDocument(final CreditNote creditNote, final IMessageResourceProvider messageResourceProvider,
            boolean original) {
        this.creditNote = creditNote;
        this.messageResourceProvider = messageResourceProvider;
        this.original = original;

        fillReport();

    }

    @Override
    protected void fillReport() {
        addParameter("documentIdType", this.creditNote.getReceipt().getPerson().getIdDocumentType().getLocalizedName());
        addParameter("documentIdNumber", this.creditNote.getReceipt().getPerson().getDocumentIdNumber());
        addParameter("name", this.creditNote.getReceipt().getPerson().getName());

        addParameter("ownerUnitPhone", Bennu.getInstance().getInstitutionUnit().getDefaultPhoneNumber());

        addParameter("creditNoteNumber", this.creditNote.getNumber() + "/" + this.creditNote.getYear().toString());
        addParameter("receiptNumber", this.creditNote.getReceipt().getNumberWithSeries() + "/"
                + this.creditNote.getReceipt().getYear().toString());
        addParameter("annulled", this.creditNote.isAnnulled());
        addParameter("creditNoteDate", this.creditNote.getWhenCreated().toString(DD_MMMM_YYYY, getLocale()));
        addParameter("total", this.creditNote.getTotalAmount().toPlainString());

        addParameter("original", this.original);
        addParameter("studentNumber", this.creditNote.getReceipt().getPerson().hasStudent() ? this.creditNote.getReceipt()
                .getPerson().getStudent().getNumber().toString() : null);

        addDataSourceElements(buildEntries());

    }

    private List<CreditNoteDocumentEntry> buildEntries() {

        final SortedSet<CreditNoteEntry> sortedEntries =
                new TreeSet<CreditNoteEntry>(CreditNoteEntry.COMPARATOR_BY_WHEN_REGISTERED);
        sortedEntries.addAll(this.creditNote.getCreditNoteEntries());

        final List<CreditNoteDocumentEntry> result = new ArrayList<CreditNoteDocumentEntry>();
        for (final CreditNoteEntry each : sortedEntries) {
            result.add(new CreditNoteDocumentEntry(each.getAccountingEntry().getDescription()
                    .toString(this.messageResourceProvider), each.getAmount().toPlainString()));
        }

        return result;

    }

    @Override
    public String getReportFileName() {
        return "CreditNote-" + new DateTime().toString(YYYYMMDDHHMMSS, getLocale());
    }

}
