package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreatePaymentsForEvents extends Service {

    public CreatePaymentsForEvents() {
        super();
    }

    public void run(final Person person, final User responsibleUser, final List<EntryDTO> entryDTOs)
            throws FenixServiceException {

        // TODO: remove account creation code

        final Map<Event, List<EntryDTO>> entryDTOsByEvent = splitEntryDTOsByEvent(entryDTOs);

        for (final Map.Entry<Event, List<EntryDTO>> entry : entryDTOsByEvent.entrySet()) {
            final Unit unit = ((DFACandidacyEvent) entry.getKey()).getCandidacy().getExecutionDegree()
                    .getDegreeCurricularPlan().getDegree().getUnit();
            final Account unitAccount = unit.getAccountBy(AccountType.INTERNAL);
            if (unitAccount == null) {
                unit.createAccount(AccountType.INTERNAL);
            }

            final Account personAccount = person.getAccountBy(AccountType.EXTERNAL);
            if (personAccount == null) {
                person.createAccount(AccountType.EXTERNAL);
            }

            entry.getKey().process(responsibleUser, entry.getValue());
        }

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