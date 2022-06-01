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
package org.fenixedu.academic.ui.spring.controller.academicAdministration;

import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventTypes;
import org.joda.time.LocalDate;

public class OverduePaymentsParameters {
    private List<Event> eventList;
    private LocalDate startDate;
    private LocalDate endDate;
    private EventTypes eventTypes;
    private Person person;

    public OverduePaymentsParameters() {
    }

    public OverduePaymentsParameters(List<Event> allOverduePayments) {
        setEventList(allOverduePayments);
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EventTypes getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(EventTypes eventTypes) {
        this.eventTypes = eventTypes;
    }


//
//    private void findSimilarAccounts() {
//        String[] parts = target.getProfile().getFullName().split(" ");
//        String query = parts.length > 1 ? parts[0] + " " + parts[parts.length - 1] : target.getProfile().getFullName();
//        similars =
//                UserProfile.searchByName(query, Integer.MAX_VALUE).map(UserProfile::getUser).filter(Objects::nonNull)
//                        .filter(u -> !u.equals(target)).filter(u -> barelyCompatibleStudent(u, target))
//                        .collect(Collectors.toSet());
//    }
//
//    private static boolean barelyCompatibleStudent(User user, User target) {
//        return user.getPerson().getStudent() != null
//                && (!user.getPerson().getStudent().getRegistrationsSet().isEmpty() || !user.getPerson()
//                        .getPhdIndividualProgramProcessesSet().isEmpty())
//                && user.getPerson().getDateOfBirthYearMonthDay() != null
//                && user.getPerson().getDateOfBirthYearMonthDay().equals(target.getPerson().getDateOfBirthYearMonthDay());
//    }
}
