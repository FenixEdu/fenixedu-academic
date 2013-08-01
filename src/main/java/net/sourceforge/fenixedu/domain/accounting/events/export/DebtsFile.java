package net.sourceforge.fenixedu.domain.accounting.events.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.bennu.core.util.ConfigurationManager;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;

public abstract class DebtsFile extends DebtsFile_Base {

    protected static final String ENTITY_CODE;

    protected static final String SOURCE_INSTITUTION_ID;

    protected static final String DESTINATION_INSTITUTION_ID;

    static {
        ENTITY_CODE = ConfigurationManager.getProperty("sibs.entityCode");
        SOURCE_INSTITUTION_ID = ConfigurationManager.getProperty("sibs.sourceInstitutionId");
        DESTINATION_INSTITUTION_ID = ConfigurationManager.getProperty("sibs.destinationInstitutionId");
    }

    public DebtsFile() {
        super();
    }

    protected Map<Person, List<Event>> getNotPayedEventsGroupedByPerson(final ExecutionYear executionYear,
            StringBuilder errorsBuilder) {
        final Map<Person, List<Event>> result = new HashMap<Person, List<Event>>();

        for (final AnnualEvent event : executionYear.getAnnualEventsSet()) {
            try {
                if (getAcceptedEventClasses().contains(event.getClass()) && event.isOpen()) {
                    getEventsForPerson(result, event.getPerson()).add(event);
                }
            } catch (Throwable e) {
                appendToErrors(errorsBuilder, event.getExternalId(), e);
            }
        }

        if (ResidenceYear.hasCurrentYear()) {
            for (final ResidenceMonth month : ResidenceYear.getCurrentYear().getMonths()) {
                for (final ResidenceEvent residenceEvent : month.getEventsSet()) {
                    if (residenceEvent.isOpen() && residenceEvent.getPaymentLimitDate().isAfterNow()) {
                        getEventsForPerson(result, residenceEvent.getPerson()).add(residenceEvent);
                    }
                }
            }
        }

        for (InstitutionAffiliationEvent event : Bennu.getInstance().getInstitutionUnit().getOpenAffiliationEventSet()) {
            Person person = event.getPerson();
            if (!result.containsKey(person)) {
                result.put(person, new ArrayList<Event>());
            }
            result.get(person).add(event);
        }

        return result;
    }

    protected abstract List<Class> getAcceptedEventClasses();

    protected List<Event> getEventsForPerson(final Map<Person, List<Event>> result, final Person person) {
        List<Event> eventsForPerson = result.get(person);
        if (eventsForPerson == null) {
            eventsForPerson = new ArrayList<Event>();
            result.put(person, eventsForPerson);
        }

        return eventsForPerson;
    }

    protected void appendToErrors(StringBuilder errorsBuilder, String domainObjectId, Throwable e) {
        String message = e.getMessage();
        String className = e.getStackTrace()[0].getClassName();
        int codeLine = e.getStackTrace()[0].getLineNumber();

        errorsBuilder.append(message).append("[ ").append("domain object externalId - ").append(domainObjectId).append(" : ")
                .append(className).append(" : ").append(codeLine).append("]");
    }

    @Deprecated
    public boolean hasErrors() {
        return getErrors() != null;
    }

}
