package org.fenixedu.academic.ui.spring.controller.academicAdministration;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.EventTypes;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;

@Service
public class OverduePaymentsService {
    public List<Event> getAllOpenedPayments() {
        return Bennu.getInstance().getAccountingEventsSet().stream().filter(e -> e.getEventState() == EventState.OPEN && e.getParty() instanceof Person)
                .sorted(Event.COMPARATOR_BY_DATE).collect(Collectors.toList());
    }

    public List<Event> getAllPaymentsByEventType(EventType type) {
        return Bennu.getInstance().getAccountingEventsSet().stream().filter(e -> e.getEventType() == type)
                .sorted(Event.COMPARATOR_BY_DATE).collect(Collectors.toList());
    }

    public JsonElement getEventTypes() {
        return new EventTypes().toJson();
    }

    public Spreadsheet reportList(LocalDate start, LocalDate end) {
        DateTime startDateTime = start.toDateTimeAtStartOfDay();
        DateTime endDateTime = end.toDateTimeAtStartOfDay();
        Spreadsheet payments = new Spreadsheet("Pagamentos em Incomprimentos");
        return payments;
    }
}
