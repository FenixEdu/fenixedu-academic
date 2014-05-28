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
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import org.fenixedu.bennu.portal.StrutsApplication;

public class TeacherApplication {

    private static final String ACCESS_GROUP = "role(TEACHER) | professors";

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.phds", accessGroup = ACCESS_GROUP,
            hint = "Teacher")
    public static class TeacherPhdApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "final-work", titleKey = "link.manage.finalWork",
            accessGroup = ACCESS_GROUP, hint = "Teacher")
    public static class TeacherFinalWorkApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "tutor", titleKey = "link.teacher.tutor.operations",
            accessGroup = ACCESS_GROUP, hint = "Teacher")
    public static class TeacherTutorApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "teaching", titleKey = "title.teaching",
            accessGroup = ACCESS_GROUP, hint = "Teacher")
    public static class TeacherTeachingApp {
    }

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "mobility", titleKey = "label.application.mobility",
            accessGroup = "role(TEACHER)", hint = "Teacher")
    public static class TeacherMobilityApp {
    }

}
