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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;

public class CreateCreditNoteBean implements Serializable {

    private List<CreditNoteEntryDTO> creditNoteEntryDTOs;

    private Receipt receipt;

    public CreateCreditNoteBean(final Receipt receipt) {
        setReceipt(receipt);
        setCreditNoteEntryDTOs(buildCreditNoteEntryDTOs(receipt));
    }

    public Receipt getReceipt() {
        return this.receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public List<CreditNoteEntryDTO> getCreditNoteEntryDTOs() {
        return creditNoteEntryDTOs;
    }

    public void setCreditNoteEntryDTOs(List<CreditNoteEntryDTO> creditNoteEntryDTOs) {
        this.creditNoteEntryDTOs = creditNoteEntryDTOs;
    }

    private List<CreditNoteEntryDTO> buildCreditNoteEntryDTOs(final Receipt receipt) {
        final List<CreditNoteEntryDTO> result = new ArrayList<CreditNoteEntryDTO>();
        for (final Entry entry : receipt.getReimbursableEntries()) {
            result.add(new CreditNoteEntryDTO(entry));
        }

        return result;

    }

    public List<CreditNoteEntryDTO> getSelectedEntries() {
        final List<CreditNoteEntryDTO> result = new ArrayList<CreditNoteEntryDTO>();

        for (final CreditNoteEntryDTO creditNoteEntryDTO : getCreditNoteEntryDTOs()) {
            if (creditNoteEntryDTO.isSelected()) {
                result.add(creditNoteEntryDTO);
            }
        }

        return result;
    }

}
