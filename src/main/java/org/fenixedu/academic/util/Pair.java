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
package org.fenixedu.academic.util;

import java.io.Serializable;

/**
 * A pair is simple aggregation of two values. This class can be used to keep
 * two values together, like a key and value, without depending in any
 * particular data structure.
 * 
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 13:27:42,31/Mar/2006
 * @version $Id: Pair.java 35158 2008-04-07 15:23:35Z nmgo $
 */
@Deprecated
public class Pair<K, V> implements Serializable {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Pair(" + getKey() + ", " + getValue() + ")";
    }

    @Override
    public int hashCode() {
        int keyHashCode = getKey() != null ? getKey().hashCode() : 0;
        int valueHashCode = getValue() != null ? getValue().hashCode() : 0;
        return keyHashCode + valueHashCode;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        Pair<K, V> pair = (Pair<K, V>) object;
        boolean keyEquals = getKey() != null ? getKey().equals(pair.getKey()) : pair.getKey() == null;
        if (!keyEquals) {
            return false;
        }
        return getValue() != null ? getValue().equals(pair.getValue()) : pair.getValue() == null;
    }
}
