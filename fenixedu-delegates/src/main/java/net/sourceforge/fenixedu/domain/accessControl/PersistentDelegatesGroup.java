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

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentDelegatesGroup extends PersistentDelegatesGroup_Base {
    public PersistentDelegatesGroup(Degree degree, FunctionType function) {
        super();
        setDegree(degree);
        setFunction(function);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public Group toGroup() {
        return DelegatesGroup.get(getDegree(), getFunction());
    }

    @Override
    protected void gc() {
        setDegree(null);
        super.gc();
    }

    public static PersistentDelegatesGroup getInstance(Degree degree, FunctionType function) {
        return singleton(() -> select(degree, function), () -> new PersistentDelegatesGroup(degree, function));
    }

    private static Optional<PersistentDelegatesGroup> select(Degree degree, final FunctionType function) {
        Stream<PersistentDelegatesGroup> options =
                degree != null ? degree.getDelegatesGroupSet().stream() : filter(PersistentDelegatesGroup.class);
        return options.filter(group -> Objects.equals(group.getFunction(), function)).findAny();
    }
}
