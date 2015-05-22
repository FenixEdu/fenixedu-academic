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
package org.fenixedu.academic.util.predicates;

import java.util.Collection;
import java.util.function.Predicate;

public class AndPredicate<T> extends ChainPredicate<T> {

    public AndPredicate() {
        super();
    }

    public AndPredicate(Collection<Predicate<T>> predicates) {
        super(predicates);
    }

    public AndPredicate(Predicate<T> pred1, Predicate<T> pred2) {
        super();
        add(pred1);
        add(pred2);
    }

    @Override
    public boolean test(T t) {
        for (Predicate<T> predicate : predicates) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

}
