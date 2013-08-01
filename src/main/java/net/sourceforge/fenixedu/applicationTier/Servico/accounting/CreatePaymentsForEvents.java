package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreatePaymentsForEvents {

    @Atomic
    public static Receipt run(final User responsibleUser, final Collection<EntryDTO> entryDTOs, final PaymentMode paymentMode,
            final boolean differedPayment, final DateTime whenRegistered, final Person person, final Party contributorParty,
            final String contributorName) {

        final DateTime dateToSet = differedPayment ? whenRegistered : new DateTime();

        final List<Entry> createdEntries = createEntries(responsibleUser, entryDTOs, paymentMode, dateToSet);

        return Receipt.createWithContributorPartyOrContributorName(responsibleUser.getPerson(), person, contributorParty,
                contributorName, dateToSet.getYear(), createdEntries);

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