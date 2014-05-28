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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentStudentsConcludedInExecutionYearGroup extends PersistentStudentsConcludedInExecutionYearGroup_Base {
    protected PersistentStudentsConcludedInExecutionYearGroup(Degree degree, ExecutionYear conclusionYear) {
        super();
        setDegree(degree);
        setConclusionYear(conclusionYear);
    }

    @Override
    public Group toGroup() {
        return StudentsConcludedInExecutionYearGroup.get(getDegree(), getConclusionYear());
    }

    @Override
    protected void gc() {
        setDegree(null);
        setConclusionYear(null);
        super.gc();
    }

    public static PersistentStudentsConcludedInExecutionYearGroup getInstance(Degree degree, ExecutionYear conclusionYear) {
        return singleton(
                () -> conclusionYear.getStudentsConcludedInExecutionYearGroupSet().stream()
                .filter(group -> Objects.equals(group.getDegree(), degree)).findAny(),
                () -> new PersistentStudentsConcludedInExecutionYearGroup(degree, conclusionYear));
    }
}
