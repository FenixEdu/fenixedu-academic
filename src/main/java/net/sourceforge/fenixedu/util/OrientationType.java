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
public class OrientationType extends FenixValuedEnum {

    public static final int DEGREE_TYPE = 1;

    public static final int MASTER_TYPE = 2;

    public static final int PHD_TYPE = 3;

    public static OrientationType DEGREE = new OrientationType("DegreeFinalProject", DEGREE_TYPE);

    public static OrientationType MASTER = new OrientationType("MasterThesis", MASTER_TYPE);

    public static OrientationType PHD = new OrientationType("PHDThesis", PHD_TYPE);

    public OrientationType(String name, int value) {
        super(name, value);
    }

    public static OrientationType getEnum(String orientationType) {
        return (OrientationType) getEnum(OrientationType.class, orientationType);
    }

    public static OrientationType getEnum(int orientationType) {
        return (OrientationType) getEnum(OrientationType.class, orientationType);
    }

    public static Map getEnumMap() {
        return getEnumMap(OrientationType.class);
    }

    public static List getEnumList() {
        return getEnumList(OrientationType.class);
    }

    public static Iterator iterator() {
        return iterator(OrientationType.class);
    }

    @Override
    public String toString() {
        String result = "Orientation Type :\n";
        result += "\n  - Orientation Type : " + this.getName();

        return result;
    }
}