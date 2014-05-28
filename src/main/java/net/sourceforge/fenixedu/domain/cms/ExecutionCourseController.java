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
package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Site;

public class ExecutionCourseController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        if (parts.length > 2) {
            ExecutionInterval executionInterval = getExecutionInterval(parts);
            ExecutionCourse executionCourse =
                    executionInterval == null ? ExecutionCourse.readLastBySigla(parts[0]) : ExecutionCourse
                            .readLastByExecutionIntervalAndSigla(parts[0], executionInterval);
            return executionCourse == null ? null : executionCourse.getSite();
        } else {
            return null;
        }
    }

    @Override
    public Class<ExecutionCourseSite> getControlledClass() {
        return ExecutionCourseSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 3;
    }

    private ExecutionInterval getExecutionInterval(String[] pathElements) {
        if (pathElements.length > 1 && pathElements[1].matches("[1-9][0-9]{3}-[1-9][0-9]{3}")) {
            ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(pathElements[1].replace('-', '/'));
            if (executionYear != null) {
                if (pathElements.length > 2 && pathElements[2].matches("[1-2]-semestre")) {
                    return executionYear.getExecutionSemesterFor(Integer.valueOf(String.valueOf(pathElements[2].charAt(0))));
                }
                return executionYear;
            }
        }
        return null;
    }
}
