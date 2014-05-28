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
import java.util.Optional;
import java.util.stream.Stream;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;

public class PersistentStudentGroup extends PersistentStudentGroup_Base {
    protected PersistentStudentGroup(DegreeType degreeType, Degree degree, CycleType cycle, Space campus,
            ExecutionCourse executionCourse, CurricularYear curricularYear, ExecutionYear executionYear) {
        super();
        setDegreeType(degreeType);
        setDegree(degree);
        setCycle(cycle);
        setCampus(campus);
        setExecutionCourse(executionCourse);
        setCurricularYear(curricularYear);
        setExecutionYear(executionYear);
        if (degree != null || executionCourse != null || campus != null || curricularYear != null && executionYear != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public Group toGroup() {
        return StudentGroup.get(getDegreeType(), getDegree(), getCycle(), getCampus(), getExecutionCourse(), getCurricularYear(),
                getExecutionYear());
    }

    @Override
    protected void gc() {
        setCampus(null);
        setDegree(null);
        setExecutionCourse(null);
        setCurricularYear(null);
        setExecutionYear(null);
        super.gc();
    }

    public static PersistentStudentGroup getInstance() {
        return getInstance(filter(PersistentStudentGroup.class), null, null, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(DegreeType degreeType) {
        return getInstance(filter(PersistentStudentGroup.class), degreeType, null, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(CycleType cycle) {
        return getInstance(filter(PersistentStudentGroup.class), null, null, cycle, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Degree degree, CycleType cycle) {
        return getInstance(degree.getStudentGroupSet().stream(), null, degree, cycle, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Space campus) {
        return getInstance(campus.getStudentGroupSet().stream(), null, null, null, campus, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Degree degree, CurricularYear curricularYear, ExecutionYear executionYear) {
        return getInstance(curricularYear.getStudentGroupSet().stream(), null, degree, null, null, null, curricularYear,
                executionYear);
    }

    public static PersistentStudentGroup getInstance(ExecutionCourse executionCourse) {
        return getInstance(executionCourse.getStudentGroupSet().stream(), null, null, null, null, executionCourse, null, null);
    }

    public static PersistentStudentGroup getInstance(DegreeType degreeType, Degree degree, CycleType cycle, Space campus,
            ExecutionCourse executionCourse, CurricularYear curricularYear, ExecutionYear executionYear) {
        if (curricularYear != null) {
            return getInstance(curricularYear.getStudentGroupSet().stream(), degreeType, degree, cycle, campus, executionCourse,
                    curricularYear, executionYear);
        }
        if (executionCourse != null) {
            return getInstance(executionCourse.getStudentGroupSet().stream(), degreeType, degree, cycle, campus, executionCourse,
                    curricularYear, executionYear);
        }
        if (campus != null) {
            return getInstance(campus.getStudentGroupSet().stream(), degreeType, degree, cycle, campus, executionCourse,
                    curricularYear, executionYear);
        }
        if (degree != null) {
            return getInstance(degree.getStudentGroupSet().stream(), degreeType, degree, cycle, campus, executionCourse,
                    curricularYear, executionYear);
        }
        return getInstance(filter(PersistentStudentGroup.class), degreeType, degree, cycle, campus, executionCourse,
                curricularYear, executionYear);
    }

    private static PersistentStudentGroup getInstance(Stream<PersistentStudentGroup> options, DegreeType degreeType,
            Degree degree, CycleType cycle, Space campus, ExecutionCourse executionCourse, CurricularYear curricularYear,
            ExecutionYear executionYear) {
        return singleton(
                () -> select(options, degreeType, degree, cycle, campus, executionCourse, curricularYear, executionYear),
                () -> new PersistentStudentGroup(degreeType, degree, cycle, campus, executionCourse, curricularYear,
                        executionYear));
    }

    private static Optional<PersistentStudentGroup> select(Stream<PersistentStudentGroup> options, final DegreeType degreeType,
            final Degree degree, final CycleType cycle, final Space campus, final ExecutionCourse executionCourse,
            final CurricularYear curricularYear, final ExecutionYear executionYear) {
        return options.filter(
                group -> Objects.equals(group.getDegreeType(), degreeType) && Objects.equals(group.getDegree(), degree)
                && Objects.equals(group.getCycle(), cycle) && Objects.equals(group.getCampus(), campus)
                && Objects.equals(group.getExecutionCourse(), executionCourse)
                && Objects.equals(group.getCurricularYear(), curricularYear)
                && Objects.equals(group.getExecutionYear(), executionYear)).findAny();
    }
}
