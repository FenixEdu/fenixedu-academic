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
/*
 * Created on Oct 24, 2005
 *  by jdnf
 */
package org.fenixedu.academic.service.filter.coordinator;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.filter.framework.DomainObjectAuthorizationFilter;
import org.fenixedu.bennu.core.domain.User;

public class DegreeCurricularPlanAuthorizationFilter extends DomainObjectAuthorizationFilter {

    public static final DegreeCurricularPlanAuthorizationFilter instance = new DegreeCurricularPlanAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    protected boolean verifyCondition(User id, String degreeCurricularPlanID) {
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        for (final Coordinator coordinator : person.getCoordinatorsSet()) {
            if (coordinator.getExecutionDegree().getDegreeCurricularPlan().getExternalId().equals(degreeCurricularPlanID)) {
                return true;
            }
        }
        return false;
    }

}
