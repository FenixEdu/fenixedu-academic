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
package org.fenixedu.academic.domain.accessControl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.dml.runtime.Relation;

public class PersistentStudentGroup extends PersistentStudentGroup_Base {
    protected PersistentStudentGroup(DegreeType degreeType, Degree degree, CycleType cycle, Space campus,
            ExecutionCourse executionCourse, CurricularYear curricularYear, ExecutionYear executionYear, Boolean withEnrolments,
            Boolean firstTime) {
        super();
        setDegreeType(degreeType);
        setDegree(degree);
        setCycle(cycle);
        setCampus(campus);
        setExecutionCourse(executionCourse);
        setCurricularYear(curricularYear);
        setExecutionYear(executionYear);
        setWithEnrolments(withEnrolments);
        setFirstTimeInDegree(firstTime);

        if (degree != null || executionCourse != null || campus != null || curricularYear != null && executionYear != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public Group toGroup() {
        return StudentGroup.get(getDegreeType(), getDegree(), getCycle(), getCampus(), getExecutionCourse(), getCurricularYear(),
                getExecutionYear(), getWithEnrolments(), getFirstTimeInDegree());
    }

    @Override
    protected Collection<Relation<?, ?>> getContextRelations() {
        Set<Relation<?, ?>> set = new HashSet<>();
        set.add(getRelationPersistentStudentGroupCampus());
        set.add(getRelationPersistentStudentGroupExecutionCourse());
        set.add(getRelationPersistentStudentGroupExecutionYear());
        set.add(getRelationPersistentStudentGroupCurricularYear());
        set.add(getRelationPersistentStudentGroupDegreeType());
        set.add(getRelationPersistentStudentGroupDegree());
        set.addAll(super.getContextRelations());
        return set;
    }

    public static PersistentStudentGroup getInstance() {
        return getInstance(() -> filter(PersistentStudentGroup.class), null, null, null, null, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(DegreeType degreeType) {
        return getInstance(() -> filter(PersistentStudentGroup.class), degreeType, null, null, null, null, null, null, null,
                null);
    }

    public static PersistentStudentGroup getInstance(CycleType cycle) {
        return getInstance(() -> filter(PersistentStudentGroup.class), null, null, cycle, null, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Degree degree, CycleType cycle) {
        return getInstance(() -> degree.getStudentGroupSet().stream(), null, degree, cycle, null, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Space campus) {
        return getInstance(() -> campus.getStudentGroupSet().stream(), null, null, null, campus, null, null, null, null, null);
    }

    public static PersistentStudentGroup getInstance(Degree degree, CurricularYear curricularYear, ExecutionYear executionYear) {
        return getInstance(() -> curricularYear.getStudentGroupSet().stream(), null, degree, null, null, null, curricularYear,
                executionYear, null, null);
    }

    public static PersistentStudentGroup getInstance(ExecutionCourse executionCourse) {
        return getInstance(() -> executionCourse.getStudentGroupSet().stream(), null, null, null, null, executionCourse, null,
                null, null, null);
    }

    public static PersistentStudentGroup getInstance(DegreeType degreeType, Degree degree, CycleType cycle, Space campus,
            ExecutionCourse executionCourse, CurricularYear curricularYear, ExecutionYear executionYear, Boolean withEnrolments,
            Boolean firstTime) {
        if (curricularYear != null) {
            return getInstance(() -> curricularYear.getStudentGroupSet().stream(), degreeType, degree, cycle, campus,
                    executionCourse, curricularYear, executionYear, withEnrolments, firstTime);
        }
        if (executionCourse != null) {
            return getInstance(() -> executionCourse.getStudentGroupSet().stream(), degreeType, degree, cycle, campus,
                    executionCourse, curricularYear, executionYear, withEnrolments, firstTime);
        }
        if (campus != null) {
            return getInstance(() -> campus.getStudentGroupSet().stream(), degreeType, degree, cycle, campus, executionCourse,
                    curricularYear, executionYear, withEnrolments, firstTime);
        }
        if (degree != null) {
            return getInstance(() -> degree.getStudentGroupSet().stream(), degreeType, degree, cycle, campus, executionCourse,
                    curricularYear, executionYear, withEnrolments, firstTime);
        }
        return getInstance(() -> filter(PersistentStudentGroup.class), degreeType, degree, cycle, campus, executionCourse,
                curricularYear, executionYear, withEnrolments, firstTime);
    }

    private static PersistentStudentGroup getInstance(Supplier<Stream<PersistentStudentGroup>> options, DegreeType degreeType,
            Degree degree, CycleType cycle, Space campus, ExecutionCourse executionCourse, CurricularYear curricularYear,
            ExecutionYear executionYear, Boolean withEnrolments, Boolean firstTime) {
        return singleton(
                () -> select(options, degreeType, degree, cycle, campus, executionCourse, curricularYear, executionYear,
                        withEnrolments, firstTime),
                () -> new PersistentStudentGroup(degreeType, degree, cycle, campus, executionCourse, curricularYear,
                        executionYear, withEnrolments, firstTime));
    }

    private static Optional<PersistentStudentGroup> select(Supplier<Stream<PersistentStudentGroup>> options,
            final DegreeType degreeType, final Degree degree, final CycleType cycle, final Space campus,
            final ExecutionCourse executionCourse, final CurricularYear curricularYear, final ExecutionYear executionYear,
            Boolean withEnrolments, Boolean firstTime) {
        return options.get()
                .filter(group -> Objects.equals(group.getDegreeType(), degreeType) && Objects.equals(group.getDegree(), degree)
                        && Objects.equals(group.getCycle(), cycle) && Objects.equals(group.getCampus(), campus)
                        && Objects.equals(group.getExecutionCourse(), executionCourse)
                        && Objects.equals(group.getCurricularYear(), curricularYear)
                        && Objects.equals(group.getExecutionYear(), executionYear)
                        && Objects.equals(group.getWithEnrolments(), withEnrolments)
                        && Objects.equals(group.getFirstTimeInDegree(), firstTime))
                .findAny();
    }
}
