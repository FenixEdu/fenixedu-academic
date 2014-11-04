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
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.academic.ui.struts.action.commons.FacesEntryPoint;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsApplication(bundle = "ResourceAllocationManagerResources", path = "resource-allocation-manager",
        titleKey = "title.resourceAllocationManager.management", hint = "Resource Allocation Manager",
        accessGroup = "role(RESOURCE_ALLOCATION_MANAGER)")
@Mapping(path = "/index", module = "resourceAllocationManager", parameter = "/resourceAllocationManager/mainPage.jsp")
public class RAMApplication extends ForwardAction {

    private static final String BUNDLE = "ResourceAllocationManagerResources";
    private static final String HINT = "Resource Allocation Manager";
    private static final String ACCESS_GROUP = "role(RESOURCE_ALLOCATION_MANAGER)";

    @StrutsApplication(bundle = BUNDLE, path = "periods", titleKey = "link.periods", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class RAMPeriodsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "execution-courses", titleKey = "link.courses.management", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMExecutionCoursesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "curriculum-historic", titleKey = "label.curriculumHistoric", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMCurriculumHistoricApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "first-year-shifts", titleKey = "label.firstYearShifts", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMFirstYearShiftsApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "schedules", titleKey = "link.schedules.management", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMSchedulesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "evaluations", titleKey = "link.writtenEvaluationManagement", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class RAMEvaluationsApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = RAMEvaluationsApp.class, path = "written-evaluations-by-room",
            titleKey = "link.writtenEvaluation.by.room")
    @Mapping(path = "/writtenEvaluations/writtenEvaluationsByRoom", module = "resourceAllocationManager")
    public static class WrittenEvaluationsByRoom extends FacesEntryPoint {
    }

}
