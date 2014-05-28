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
/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ProviderRegimeType extends FenixValuedEnum {

    public static final int EXCLUSIVE_TYPE = 1;

    public static final int CUMULATIVE_TYPE = 2;

    public static final int COMPLEMENT_TYPE = 3;

    public static final int EXCLUSIVE_CUMULATIVE_TYPE = 4;

    public static final ProviderRegimeType EXCLUSIVE = new ProviderRegimeType("regime.exclusive",
            ProviderRegimeType.EXCLUSIVE_TYPE);

    public static final ProviderRegimeType CUMULATIVE = new ProviderRegimeType("regime.cumulative",
            ProviderRegimeType.CUMULATIVE_TYPE);

    public static final ProviderRegimeType COMPLEMENT = new ProviderRegimeType("regime.complement",
            ProviderRegimeType.COMPLEMENT_TYPE);

    public static final ProviderRegimeType EXCLUSIVE_CUMULATIVE = new ProviderRegimeType("regime.exclusiveCumulative",
            ProviderRegimeType.EXCLUSIVE_CUMULATIVE_TYPE);

    public ProviderRegimeType(String name, int value) {
        super(name, value);
    }

    public static ProviderRegimeType getEnum(String providerRegimeType) {
        return (ProviderRegimeType) getEnum(ProviderRegimeType.class, providerRegimeType);
    }

    public static ProviderRegimeType getEnum(int providerRegimeType) {
        return (ProviderRegimeType) getEnum(ProviderRegimeType.class, providerRegimeType);
    }

    public static Map getEnumMap() {
        return getEnumMap(ProviderRegimeType.class);
    }

    public static List getEnumList() {
        return getEnumList(ProviderRegimeType.class);
    }

    public static Iterator iterator() {
        return iterator(ProviderRegimeType.class);
    }

    @Override
    public String toString() {
        switch (getValue()) {
        case EXCLUSIVE_TYPE:
            return "EXCLUSIVE_TYPE";
        case CUMULATIVE_TYPE:
            return "CUMULATIVE_TYPE";
        case COMPLEMENT_TYPE:
            return "COMPLEMENT_TYPE";
        case EXCLUSIVE_CUMULATIVE_TYPE:
            return "EXCLUSIVE_CUMULATIVE_TYPE";
        default:
            throw new Error("Unknown provider regime type value.");
        }
    }
}