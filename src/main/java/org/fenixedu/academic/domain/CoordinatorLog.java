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

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CoordinatorLog extends CoordinatorLog_Base {

    public CoordinatorLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CoordinatorLog(DateTime time, OperationType operation, Person person, ExecutionDegree executionDegree, Person personWho) {
        super();
        setRootDomainObject(Bennu.getInstance());
        super.setDate(time);
        super.setOperation(operation);
        super.setPerson(person);
        super.setPersonWho(personWho);
        super.setExecutionDegree(executionDegree);
    }

    @Atomic
    public static CoordinatorLog createCoordinatorLog(DateTime time, OperationType operation, Person personActing,
            Coordinator coordinator) {

        return new CoordinatorLog(time, operation, coordinator.getPerson(), coordinator.getExecutionDegree(), personActing);
    }

}
