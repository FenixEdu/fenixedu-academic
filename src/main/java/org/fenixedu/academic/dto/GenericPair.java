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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class GenericPair<T, V> implements Serializable {

    private T left;

    private V right;

    public GenericPair(T left, V right) {
        super();
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public V getRight() {
        return right;
    }

    public void setRight(V right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GenericPair) {
            T oLeft = ((GenericPair<T, V>) o).getLeft();
            V oRight = ((GenericPair<T, V>) o).getRight();

            return ((getLeft() == null && oLeft == null) || getLeft().equals(oLeft))
                    && ((getRight() == null && oRight == null) || getRight().equals(oRight));
        }

        return false;
    }

    @Override
    public int hashCode() {
        final String hashBase = String.valueOf(getLeft()) + String.valueOf(getRight());
        return hashBase.hashCode();
    }

}
