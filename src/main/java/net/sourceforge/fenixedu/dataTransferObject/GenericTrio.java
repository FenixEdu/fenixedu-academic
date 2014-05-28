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
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class GenericTrio<T, U, V> {

    private T first;

    private U second;

    private V third;

    public GenericTrio(T first, U second, V third) {
        super();
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public V getThird() {
        return third;
    }

    public void setThird(V third) {
        this.third = third;
    }

    @Override
    public boolean equals(Object obj) {
        GenericTrio genericTrio = null;
        if (obj instanceof GenericTrio) {
            genericTrio = (GenericTrio) obj;
        } else {
            return false;
        }
        return (this.getFirst().equals(genericTrio.getFirst()) && this.getSecond().equals(genericTrio.getSecond()) && this
                .getThird().equals(genericTrio.getThird()));
    }

    @Override
    public int hashCode() {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(getFirst().hashCode()));
        builder.append('@');
        builder.append(String.valueOf(getSecond().hashCode()));
        builder.append('@');
        builder.append(String.valueOf(getThird().hashCode()));
        return builder.toString().hashCode();
    }
}
