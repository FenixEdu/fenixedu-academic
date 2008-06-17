package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;

public class CreatePaymentsForEvents extends Service {

    public CreatePaymentsForEvents() {
	super();
    }

    public Receipt run(final User responsibleUser, final List<EntryDTO> entryDTOs, final PaymentMode paymentMode,
	    final boolean differedPayment, final DateTime whenRegistered, final Person person, final Party contributorParty,
	    final String contributorName, final Unit creatorUnit, final Unit ownerUnit) throws FenixServiceException {

	final DateTime dateToSet = differedPayment ? whenRegistered : new DateTime();

	final List<Entry> createdEntries = createEntries(responsibleUser, entryDTOs, paymentMode, dateToSet);

	return Receipt.createWithContributorPartyOrContributorName(responsibleUser.getPerson().getEmployee(), person,
		contributorParty, contributorName, dateToSet.getYear(), creatorUnit, ownerUnit, createdEntries);

    }

    private List<Entry> createEntries(final User responsibleUser, final List<EntryDTO> entryDTOs, final PaymentMode paymentMode,
	    final DateTime whenRegistered) {
	final Map<Event, List<EntryDTO>> entryDTOsByEvent = splitEntryDTOsByEvent(entryDTOs);
	final List<Entry> resultingEntries = new ArrayList<Entry>();

	for (final Map.Entry<Event, List<EntryDTO>> entry : entryDTOsByEvent.entrySet()) {
	    resultingEntries.addAll(entry.getKey().process(responsibleUser, entry.getValue(),
		    new AccountingTransactionDetailDTO(whenRegistered, paymentMode)));

	}

	return resultingEntries;
    }

    private Map<Event, List<EntryDTO>> splitEntryDTOsByEvent(List<EntryDTO> entryDTOs) {
	final Map<Event, List<EntryDTO>> result = new HashMap<Event, List<EntryDTO>>();

	for (final EntryDTO entryDTO : entryDTOs) {
	    List<EntryDTO> entryDTOsByEvent = result.get(entryDTO.getEvent());
	    if (entryDTOsByEvent == null) {
		result.put(entryDTO.getEvent(), entryDTOsByEvent = new ArrayList<EntryDTO>());
	    }
	    entryDTOsByEvent.add(entryDTO);
	}

	return result;
    }

}