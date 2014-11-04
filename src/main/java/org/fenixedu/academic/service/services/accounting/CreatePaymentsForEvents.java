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
package org.fenixedu.academic.service.services.accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentMode;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreatePaymentsForEvents {

    @Atomic
    public static Receipt run(final User responsibleUser, final Collection<EntryDTO> entryDTOs, final PaymentMode paymentMode,
            final boolean differedPayment, final DateTime whenRegistered, final Person person, final String contributorName,
            final String contributorNumber, final String contributorAddress) {

        final DateTime dateToSet = differedPayment ? whenRegistered : new DateTime();

        final List<Entry> createdEntries = createEntries(responsibleUser, entryDTOs, paymentMode, dateToSet);

        return Receipt.create(responsibleUser.getPerson(), person, contributorName, contributorNumber, contributorAddress,
                dateToSet.getYear(), createdEntries);

    }

    private static List<Entry> createEntries(final User responsibleUser, final Collection<EntryDTO> entryDTOs,
            final PaymentMode paymentMode, final DateTime whenRegistered) {
        final Map<Event, Collection<EntryDTO>> entryDTOsByEvent = splitEntryDTOsByEvent(entryDTOs);
        final List<Entry> resultingEntries = new ArrayList<Entry>();

        for (final Map.Entry<Event, Collection<EntryDTO>> entry : entryDTOsByEvent.entrySet()) {
            resultingEntries.addAll(entry.getKey().process(responsibleUser, entry.getValue(),
                    new AccountingTransactionDetailDTO(whenRegistered, paymentMode)));

        }

        return resultingEntries;
    }

    private static Map<Event, Collection<EntryDTO>> splitEntryDTOsByEvent(Collection<EntryDTO> entryDTOs) {
        final Map<Event, Collection<EntryDTO>> result = new HashMap<Event, Collection<EntryDTO>>();

        for (final EntryDTO entryDTO : entryDTOs) {
            Collection<EntryDTO> entryDTOsByEvent = result.get(entryDTO.getEvent());
            if (entryDTOsByEvent == null) {
                result.put(entryDTO.getEvent(), entryDTOsByEvent = new ArrayList<EntryDTO>());
            }
            entryDTOsByEvent.add(entryDTO);
        }

        return result;
    }

}