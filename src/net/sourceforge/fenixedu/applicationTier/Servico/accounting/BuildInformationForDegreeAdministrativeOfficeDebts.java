package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;

public abstract class BuildInformationForDegreeAdministrativeOfficeDebts extends Service {

    protected static final String ENTITY_CODE;

    protected static final String SOURCE_INSTITUTION_ID;

    protected static final String DESTINATION_INSTITUTION_ID;

    static {
	ENTITY_CODE = PropertiesManager.getProperty("sibs.entityCode");
	SOURCE_INSTITUTION_ID = PropertiesManager.getProperty("sibs.sourceInstitutionId");
	DESTINATION_INSTITUTION_ID = PropertiesManager.getProperty("sibs.destinationInstitutionId");
    }

    private static final List<Class> acceptedEventClasses = Arrays.asList(new Class[] {
	    AdministrativeOfficeFeeAndInsuranceEvent.class, GratuityEventWithPaymentPlan.class });

    protected Map<Person, List<Event>> getNotPayedEventsGroupedByPerson(final ExecutionYear executionYear) {
	final Map<Person, List<Event>> result = new HashMap<Person, List<Event>>();
	for (final Event event : Event.readNotPayedBy(executionYear)) {
	    if (acceptedEventClasses.contains(event.getClass())) {
		getEventsForPerson(result, event).add(event);
	    }
	}

	return result;
    }

    protected List<Event> getEventsForPerson(final Map<Person, List<Event>> result, final Event event) {
	List<Event> eventsForPerson = result.get(event.getPerson());
	if (eventsForPerson == null) {
	    eventsForPerson = new ArrayList<Event>();
	    result.put(event.getPerson(), eventsForPerson);
	}

	return eventsForPerson;
    }

}
