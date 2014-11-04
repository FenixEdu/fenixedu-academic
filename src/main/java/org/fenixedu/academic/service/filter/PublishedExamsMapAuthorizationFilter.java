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
package org.fenixedu.academic.service.filter;

import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.dto.InfoExamsMap;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author Luis Cruz
 * 
 */
public class PublishedExamsMapAuthorizationFilter {

    public static void execute(Object returnedObject) {
        User userView = Authenticate.getUser();

        if (((userView != null && !userView.getPerson().hasRole(getRoleType()))) || (userView == null)) {

            if (returnedObject instanceof InfoExamsMap) {

                InfoExamsMap infoExamsMap = (InfoExamsMap) returnedObject;
                filterUnpublishedInformation(infoExamsMap);
            }
        }
    }

    private static void filterUnpublishedInformation(InfoExamsMap infoExamsMap) {
        if (infoExamsMap != null
                && infoExamsMap.getInfoExecutionDegree() != null
                && infoExamsMap.getInfoExecutionDegree().isPublishedExam(
                        infoExamsMap.getInfoExecutionPeriod().getExecutionPeriod())) {
            for (Object element : infoExamsMap.getExecutionCourses()) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) element;
                infoExecutionCourse.getAssociatedInfoExams().clear();
            }
        }
    }

    protected static RoleType getRoleType() {
        return RoleType.RESOURCE_ALLOCATION_MANAGER;
    }

}
