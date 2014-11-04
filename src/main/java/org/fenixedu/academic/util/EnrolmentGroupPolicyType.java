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
 * Created on 23/Jul/2003
 *
 */
package net.sourceforge.fenixedu.util;

/**
 * @author asnr and scpo
 * 
 */
public class EnrolmentGroupPolicyType extends FenixUtil {

    public static final int ATOMIC = 1;

    public static final int INDIVIDUAL = 2;

    private final Integer type;

    public EnrolmentGroupPolicyType(int type) {
        this.type = new Integer(type);
    }

    public EnrolmentGroupPolicyType(Integer type) {
        this.type = type;
    }

    public java.lang.Integer getType() {
        return type;
    }

    public String getTypeFullName() {

        int value = this.type.intValue();
        String stringValue = null;

        switch (value) {
        case ATOMIC:
            stringValue = "ATOMICA";
            break;
        case INDIVIDUAL:
            stringValue = "INDIVIDUAL";
            break;
        default:
            break;
        }

        return stringValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EnrolmentGroupPolicyType) {
            EnrolmentGroupPolicyType aux = (EnrolmentGroupPolicyType) o;
            return this.type.equals(aux.getType());
        }
        return false;

    }

    @Override
    public String toString() {

        int value = this.type.intValue();
        String stringValue = null;

        switch (value) {
        case ATOMIC:
            stringValue = "ATOMIC";
            break;
        case INDIVIDUAL:
            stringValue = "INDIVIDUAL";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + stringValue + "]";
    }
}