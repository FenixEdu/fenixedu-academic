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
 * Created on 22/Nov/2003
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
public class OldPublicationType extends FenixValuedEnum {
    public static final int DIDACTIC_TYPE = 1;

    public static final int CIENTIFIC_TYPE = 2;

    public static OldPublicationType DIDACTIC = new OldPublicationType("Didactic", DIDACTIC_TYPE);

    public static OldPublicationType CIENTIFIC = new OldPublicationType("Cientific", CIENTIFIC_TYPE);

    public OldPublicationType(String name, int value) {
        super(name, value);
    }

    public static OldPublicationType getEnum(String oldPublicationType) {
        return (OldPublicationType) getEnum(OldPublicationType.class, oldPublicationType);
    }

    public static OldPublicationType getEnum(int oldPublicationType) {
        return (OldPublicationType) getEnum(OldPublicationType.class, oldPublicationType);
    }

    public static Map getEnumMap() {
        return getEnumMap(OldPublicationType.class);
    }

    public static List getEnumList() {
        return getEnumList(OldPublicationType.class);
    }

    public static Iterator iterator() {
        return iterator(OldPublicationType.class);
    }

    @Override
    public String toString() {
        String result = "Old Publication Type Type :\n";
        result += "\n  - OldPublication Type : " + this.getName();

        return result;
    }

}