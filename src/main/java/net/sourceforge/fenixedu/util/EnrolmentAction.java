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
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentAction extends FenixValuedEnum {

    public static final int ENROL_TYPE = 1;

    public static final int UNENROL_TYPE = 2;

    public static final EnrolmentAction ENROL = new EnrolmentAction("enrol", EnrolmentAction.ENROL_TYPE);

    public static final EnrolmentAction UNENROL = new EnrolmentAction("unenrol", EnrolmentAction.UNENROL_TYPE);

    private EnrolmentAction(String name, int value) {
        super(name, value);
    }

    public static EnrolmentAction getEnum(String actionType) {
        return (EnrolmentAction) getEnum(EnrolmentAction.class, actionType);
    }

    public static EnrolmentAction getEnum(int actionType) {
        return (EnrolmentAction) getEnum(EnrolmentAction.class, actionType);
    }

    public static Map getEnumMap() {
        return getEnumMap(EnrolmentAction.class);
    }

    public static List getEnumList() {
        return getEnumList(EnrolmentAction.class);
    }

    public static Iterator iterator() {
        return iterator(EnrolmentAction.class);
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
