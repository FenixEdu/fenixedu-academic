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
import java.util.Map;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreatePaymentsForEvents {

    @Atomic
    public static void run(final User responsibleUser, final Collection<EntryDTO> entryDTOs, final PaymentMethod paymentMethod,
            final String paymentReference, final DateTime whenRegistered) {

        final Map<Event, Collection<EntryDTO>> entryDTOsByEvent = splitEntryDTOsByEvent(entryDTOs);

        entryDTOsByEvent.forEach((event, entries) -> {
            event.process(responsibleUser, entries, new AccountingTransactionDetailDTO(whenRegistered, paymentMethod,
                    paymentReference, null));
        });
    }

    private static Map<Event, Collection<EntryDTO>> splitEntryDTOsByEvent(Collection<EntryDTO> entryDTOs) {
        // Split entries by event
        final Map<Event, Collection<EntryDTO>> entriesByEvent = new HashMap<>();
        for (final EntryDTO entryDTO : entryDTOs) {
            entriesByEvent.computeIfAbsent(entryDTO.getEvent(), k -> new ArrayList<>()).add(entryDTO);
        }

        // Reduce entries for the same event into a single one
        final Map<Event, Collection<EntryDTO>> result = new HashMap<>();
        entriesByEvent.forEach((event, entries) -> {
            EntryDTO first = entries.iterator().next();
            Money totalAmount = entries.stream().map(EntryDTO::getAmountToPay).reduce(Money.ZERO, Money::add);

            EntryDTO totalEntry = new EntryDTO(first.getEntryType(), first.getEvent(), totalAmount,
                    totalAmount, totalAmount, first.getDescription(), totalAmount);

            result.computeIfAbsent(event, k -> new ArrayList<>()).add(totalEntry);
        });

        return result;
    }

}