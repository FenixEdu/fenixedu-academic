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

import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;

public abstract class FenixPredicateGroup extends FenixPredicateGroup_Base {
    public FenixPredicateGroup() {
        super();
        setRootForFenixPredicate(getRoot());
    }

    @Override
    protected void gc() {
        setRootForFenixPredicate(null);
        super.gc();
    }

    protected static <T extends FenixPredicateGroup> Stream<T> filter(Class<T> type) {
        return (Stream<T>) Bennu.getInstance().getFenixPredicateGroupSet().stream().filter(p -> p.getClass() == type);
    }

    protected static <T extends FenixPredicateGroup> Optional<T> find(Class<T> type) {
        return filter(type).findAny();
    }
}
