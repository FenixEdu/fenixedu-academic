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
package org.fenixedu.academic.domain.accounting.events.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.domain.residence.ResidenceYear;
import org.fenixedu.academic.FenixEduAcademicConfiguration;

public abstract class DebtsFile extends DebtsFile_Base {

    protected static final String ENTITY_CODE;

    protected static final String SOURCE_INSTITUTION_ID;

    protected static final String DESTINATION_INSTITUTION_ID;

    static {
        ENTITY_CODE = FenixConfigurationManager.getConfiguration().getSibsEntityCode();
        SOURCE_INSTITUTION_ID = FenixConfigurationManager.getConfiguration().getSibsSourceInstitutionId();
        DESTINATION_INSTITUTION_ID = FenixConfigurationManager.getConfiguration().getSibsDestinationInstitutionId();
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
            for (final ResidenceMonth month : ResidenceYear.getCurrentYear().getMonthsSet()) {
                for (final ResidenceEvent residenceEvent : month.getEventsSet()) {
                    if (residenceEvent.isOpen() && residenceEvent.getPaymentLimitDate().isAfterNow()) {
                        getEventsForPerson(result, residenceEvent.getPerson()).add(residenceEvent);
                    }
                }
            }
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
        final StackTraceElement[] trace = e.getStackTrace();
        String className = trace.length > 0 ? trace[0].getClassName() : null;
        int codeLine = trace.length > 0 ? trace[0].getLineNumber() : -1;

        errorsBuilder.append(message).append("[ ").append("domain object externalId - ").append(domainObjectId).append(" : ")
                .append(className).append(" : ").append(codeLine).append("]\n");
    }

}
