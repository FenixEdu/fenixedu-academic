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
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class CreditNoteEntryDTO implements Serializable {

    private boolean selected;

    private Entry entry;

    private Money amountToPay;

    public CreditNoteEntryDTO(final Entry entry) {

        setEntry(entry);
        setAmountToPay(entry.getAmountWithAdjustment());
    }

    public Money getTotalAmount() {
        return getEntry().getAmountWithAdjustment();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Money getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Money amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Entry getEntry() {
        return this.entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public LabelFormatter getDescription() {
        return getEntry().getDescription();
    }

}
