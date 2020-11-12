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
package org.fenixedu.academic.domain;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;

public class Coordinator extends Coordinator_Base {

    private Coordinator() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static Coordinator createCoordinator(ExecutionDegree executionDegree, Person person, Boolean responsible) {

        if (executionDegree.getCoordinatorsListSet().stream().anyMatch(c -> c.getPerson() == person)) {
            throw new DomainException("error.person.already.is.coordinator.for.same.execution.degree");
        }

        CoordinationTeamLog.createLog(executionDegree.getDegree(), executionDegree.getExecutionYear(), Bundle.MESSAGING,
                "log.degree.coordinationteam.addmember", person.getPresentationName(), executionDegree.getPresentationName());

        final Coordinator coordinator = new Coordinator();
        coordinator.setExecutionDegree(executionDegree);
        coordinator.setPerson(person);
        coordinator.setResponsible(responsible);
        return coordinator;
    }

    public void delete() throws DomainException {
        setExecutionDegree(null);
        setPerson(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isResponsible() {
        return getResponsible().booleanValue();
    }

    public void setResponsible(boolean responsible) {
        super.setResponsible(responsible);
    }

    public static Stream<Coordinator> findLastCoordinators(final Degree degree, final boolean responsiblesOnly) {
        final ExecutionYear current = ExecutionYear.findCurrent(degree.getCalendar());

        final List<ExecutionYear> years =
                degree.getDegreeCurricularPlansExecutionYears().stream().filter(year -> year.isBeforeOrEquals(current))
                        .sorted(Comparator.reverseOrder()).collect(Collectors.toUnmodifiableList());

        for (final ExecutionYear year : years) {
            final Collection<Coordinator> coordinators =
                    findCoordinators(degree, year, responsiblesOnly).collect(Collectors.toUnmodifiableSet());
            if (!coordinators.isEmpty()) {
                return coordinators.stream();
            }
        }

        return Stream.empty();
    }

    public static Stream<Coordinator> findCoordinators(final Degree degree, final ExecutionYear executionYear,
            final boolean responsiblesOnly) {
        return degree.getDegreeCurricularPlansSet().stream().map(dcp -> dcp.getExecutionDegreeByYear(executionYear))
                .filter(Objects::nonNull)
                .flatMap(ed -> responsiblesOnly ? ed.getResponsibleCoordinators().stream() : ed.getCoordinatorsListSet().stream())
                .distinct();
    }

}
