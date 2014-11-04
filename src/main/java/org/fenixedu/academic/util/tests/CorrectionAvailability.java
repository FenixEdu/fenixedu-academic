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
 * Created on 19/Ago/2003
 */
package net.sourceforge.fenixedu.util.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class CorrectionAvailability extends FenixUtil {
    public static final int NEVER = 1;

    public static final int ALWAYS = 2;

    public static final int AFTER_CLOSING = 3;

    public static final String NEVER_STRING = "Nunca";

    public static final String ALWAYS_STRING = "Sempre";

    public static final String AFTER_CLOSING_STRING = "Depois do fecho";

    private final Integer availability;

    public CorrectionAvailability(int availability) {
        this.availability = new Integer(availability);
    }

    public CorrectionAvailability(Integer availability) {
        this.availability = availability;
    }

    public CorrectionAvailability(String availabilityString) {
        this.availability = getAvailabilityCode(availabilityString);
    }

    public Integer getAvailability() {
        return availability;
    }

    public static List<LabelValueBean> getAllAvailabilities() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        result.add(new LabelValueBean(NEVER_STRING, new Integer(NEVER).toString()));
        result.add(new LabelValueBean(ALWAYS_STRING, new Integer(ALWAYS).toString()));
        result.add(new LabelValueBean(AFTER_CLOSING_STRING, new Integer(AFTER_CLOSING).toString()));
        return result;
    }

    public String getTypeString() {
        if (availability.intValue() == NEVER) {
            return new String(NEVER_STRING);
        } else if (availability.intValue() == ALWAYS) {
            return new String(ALWAYS_STRING);
        } else if (availability.intValue() == AFTER_CLOSING) {
            return new String(AFTER_CLOSING_STRING);
        }
        return null;
    }

    public Integer getAvailabilityCode(String typeName) {
        if (typeName.equals(NEVER_STRING)) {
            return new Integer(NEVER);
        } else if (typeName.equals(ALWAYS_STRING)) {
            return new Integer(ALWAYS);
        } else if (typeName.equals(AFTER_CLOSING_STRING)) {
            return new Integer(AFTER_CLOSING);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof CorrectionAvailability) {
            CorrectionAvailability ca = (CorrectionAvailability) obj;
            result = this.getAvailability().equals(ca.getAvailability());
        }
        return result;
    }
}