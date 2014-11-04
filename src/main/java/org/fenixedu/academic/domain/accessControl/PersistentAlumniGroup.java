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

import java.util.Optional;

import org.fenixedu.academic.domain.Degree;

public class PersistentAlumniGroup extends PersistentAlumniGroup_Base {
    protected PersistentAlumniGroup(Degree degree) {
        super();
        setDegree(degree);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public org.fenixedu.bennu.core.groups.Group toGroup() {
        return AlumniGroup.get(getDegree());
    }

    @Override
    protected void gc() {
        setDegree(null);
        super.gc();
    }

    public static PersistentAlumniGroup getInstance() {
        return getInstance(null);
    }

    public static PersistentAlumniGroup getInstance(Degree degree) {
        return singleton(() -> degree == null ? find(PersistentAlumniGroup.class) : Optional.ofNullable(degree.getAlumniGroup()),
                () -> new PersistentAlumniGroup(degree));
    }
}
