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
/**
 * 
 */
package org.fenixedu.academic.predicate;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ContextPredicates {

    public static final AccessControlPredicate<Context> curricularPlanMemberWritePredicate =
            new AccessControlPredicate<Context>() {

                @Override
                public boolean evaluate(Context context) {
                    User user = Authenticate.getUser();
                    if (RoleType.SCIENTIFIC_COUNCIL.isMember(user)) {
                        return true;
                    }

                    final DegreeCurricularPlan parentDegreeCurricularPlan =
                            context.getParentCourseGroup().getParentDegreeCurricularPlan();
                    if (!parentDegreeCurricularPlan.isBolonhaDegree()) {
                        return true;
                    }

                    if (AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_DEGREE_CURRICULAR_PLANS).isMember(user)) {
                        return true;
                    }

                    return parentDegreeCurricularPlan.getCurricularPlanMembersGroup().isMember(user);
                }

            };

}
