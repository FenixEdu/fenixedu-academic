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
package net.sourceforge.fenixedu.domain.messaging;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class BoardSearchBean implements Serializable, HasExecutionSemester {

    private Boolean searchExecutionCourseBoards;

    private Unit unit;

    private ExecutionSemester executionSemester;

    private ExecutionDegree executionDegree;

    public BoardSearchBean() {
        super();
    }

    public Boolean getSearchExecutionCourseBoards() {
        return searchExecutionCourseBoards;
    }

    public void setSearchExecutionCourseBoards(Boolean searchExecutionCourseBoards) {
        this.searchExecutionCourseBoards = searchExecutionCourseBoards;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    @Override
    public ExecutionSemester getExecutionPeriod() {
        if (executionSemester == null) {
            final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
            setExecutionPeriod(executionSemester);
        }
        return executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public Set getSearchResult() {
        if (getSearchExecutionCourseBoards() == null) {
            return null;
        }
        final Set<AnnouncementBoard> boards = new TreeSet<AnnouncementBoard>(AnnouncementBoard.BY_NAME);
        if (getSearchExecutionCourseBoards().booleanValue()) {
            final ExecutionDegree executionDegree = getExecutionDegree();
            final ExecutionSemester executionSemester = getExecutionPeriod();
            if (executionDegree != null && executionSemester != null) {
                for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                    if (isExecutionCourseForExecutionDegree(executionCourse, executionDegree)) {
                        boards.add(executionCourse.getBoard());
                    }
                }
            }
        } else {
            final Unit unit = getUnit();
            if (unit != null) {
                boards.addAll(unit.getBoards());
            }
        }
        return boards;
    }

    private boolean isExecutionCourseForExecutionDegree(final ExecutionCourse executionCourse,
            final ExecutionDegree executionDegree) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            for (final ExecutionDegree otherExecutionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                if (executionDegree == otherExecutionDegree) {
                    return true;
                }
            }
        }
        return false;
    }
}
