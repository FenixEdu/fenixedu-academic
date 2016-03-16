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

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.bennu.core.groups.Group;

public class PersistentCoordinatorGroup extends PersistentCoordinatorGroup_Base {

    protected PersistentCoordinatorGroup(DegreeType degreeType, Degree degree) {
        this(degreeType, degree, null);
    }

    protected PersistentCoordinatorGroup(DegreeType degreeType, Degree degree, Boolean responsible) {
        super();
        setDegreeType(degreeType);
        setResponsible(responsible);
        setDegree(degree);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public Group toGroup() {
        return CoordinatorGroup.get(getDegreeType(), getDegree(), getResponsible());
    }

    @Override
    protected void gc() {
        setDegree(null);
        super.gc();
    }

    public static PersistentCoordinatorGroup getInstance() {
        return getInstance(null, null, null);
    }

    public static PersistentCoordinatorGroup getInstance(DegreeType degreeType, Degree degree) {
        return getInstance(degreeType, degree, null);
    }

    public static PersistentCoordinatorGroup getInstance(DegreeType degreeType, Degree degree, Boolean responsible) {
        return singleton(() -> select(degreeType, degree, responsible),
                () -> new PersistentCoordinatorGroup(degreeType, degree, responsible));
    }

    private static Optional<PersistentCoordinatorGroup> select(final DegreeType degreeType, final Degree degree,
            Boolean responsible) {
        Stream<PersistentCoordinatorGroup> stream =
                degree != null ? degree.getCoordinatorGroupSet().stream() : filter(PersistentCoordinatorGroup.class);
        return stream.filter(group -> Objects.equals(group.getDegreeType(), degreeType)
                && Objects.equals(group.getDegree(), degree) && Objects.equals(group.getResponsible(), responsible)).findAny();
    }
}
