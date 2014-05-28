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
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author Luis Cruz
 * 
 */
public class PublishedExamsMapAuthorizationFilter {

    public static void execute(Object returnedObject) {
        User userView = Authenticate.getUser();

        if (returnedObject instanceof ExecutionCourseSiteView) {

            ExecutionCourseSiteView executionCourseSiteView = (ExecutionCourseSiteView) returnedObject;
            if (executionCourseSiteView.getComponent() instanceof InfoSiteEvaluation) {

                InfoSiteEvaluation infoSiteEvaluation = (InfoSiteEvaluation) executionCourseSiteView.getComponent();
                filterUnpublishedInformation(infoSiteEvaluation);

            }

        } else if (((userView != null && userView.getPerson().getPersonRolesSet() != null && !userView.getPerson().hasRole(
                getRoleType())))
                || (userView == null) || (userView.getPerson().getPersonRolesSet() == null)) {

            if (returnedObject instanceof InfoExamsMap) {

                InfoExamsMap infoExamsMap = (InfoExamsMap) returnedObject;
                filterUnpublishedInformation(infoExamsMap);

            }
        }
    }

    private static void filterUnpublishedInformation(InfoSiteEvaluation infoSiteEvaluation) {
        // This code is usefull for when the exams are to be filtered from the
        // public execution course page.
        CollectionUtils.filter(infoSiteEvaluation.getInfoEvaluations(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return !(arg0 instanceof InfoExam);
            }
        });
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