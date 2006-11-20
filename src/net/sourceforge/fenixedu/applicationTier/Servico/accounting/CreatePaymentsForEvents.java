package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransactionDetail;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

import org.joda.time.DateTime;

public class CreatePaymentsForEvents extends Service {

    public CreatePaymentsForEvents() {
	super();
    }

    public Collection<Entry> run(final User responsibleUser, final List<EntryDTO> entryDTOs,
	    final PaymentMode paymentMode, final boolean differedPayment, final DateTime whenRegistered)
	    throws FenixServiceException {

	final Map<Event, List<EntryDTO>> entryDTOsByEvent = splitEntryDTOsByEvent(entryDTOs);
	final Collection<Entry> resultingEntries = new HashSet<Entry>();

	for (final Map.Entry<Event, List<EntryDTO>> entry : entryDTOsByEvent.entrySet()) {
	    if (differedPayment) {
		resultingEntries.addAll(entry.getKey().process(responsibleUser, entry.getValue(),
			new AccountingTransactionDetailDTO(whenRegistered, paymentMode)));
	    } else {
		resultingEntries.addAll(entry.getKey().process(responsibleUser, entry.getValue(),
			new AccountingTransactionDetailDTO(new DateTime(), paymentMode)));
	    }
	}

	return resultingEntries;

    }

    private Map<Event, List<EntryDTO>> splitEntryDTOsByEvent(List<EntryDTO> entryDTOs) {
	final Map<Event, List<EntryDTO>> result = new HashMap<Event, List<EntryDTO>>();

	for (final EntryDTO entryDTO : entryDTOs) {
	    List<EntryDTO> entryDTOsByEvent = result.get(entryDTO.getEvent());
	    if (entryDTOsByEvent == null) {
		entryDTOsByEvent = new ArrayList<EntryDTO>();
	    }

	    entryDTOsByEvent.add(entryDTO);

	    result.put(entryDTO.getEvent(), entryDTOsByEvent);
	}

	return result;
    }

}