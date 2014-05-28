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
package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.List;

public class RoleTypeHelper {

    private RoleTypeHelper() {
        super();
    }

    public static String enumRoleTypeNamesToCSV() {
        StringBuilder retval = new StringBuilder();
        for (RoleType r : RoleType.values()) {
            if (retval.length() > 0) {
                retval.append(",");
            }
            retval.append(r.name());
        }
        return retval.toString();
    }

    public static String enumRoleTypeNamesToArrayFormat() {
        StringBuilder retval = new StringBuilder("{\"");
        for (RoleType r : RoleType.values()) {
            if (retval.length() > 0) {
                retval.append("\",\"");
            }
            retval.append(r.name());
        }
        retval.append("\"}");
        return retval.toString();
    }

    public static String enumRoleTypeLabelsToArrayFormat() {
        StringBuilder retval = new StringBuilder("{\"");
        for (RoleType r : RoleType.values()) {
            if (retval.length() > 0) {
                retval.append("\",\"");
            }
            retval.append(r.getDefaultLabel());
        }
        retval.append("\"}");
        return retval.toString();
    }

    public static List<String> enumRoleTypeNames() {
        ArrayList<String> retVal = new ArrayList<String>(RoleType.values().length);
        for (RoleType r : RoleType.values()) {
            retVal.add(r.name());
        }
        return retVal;
    }

    public static List<String> enumRoleTypeLabels() {
        ArrayList<String> retVal = new ArrayList<String>(RoleType.values().length);
        for (RoleType r : RoleType.values()) {
            retVal.add(r.getDefaultLabel());
        }
        return retVal;
    }
}
