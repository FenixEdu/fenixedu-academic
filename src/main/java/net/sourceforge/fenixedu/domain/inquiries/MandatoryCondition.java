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
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ArrayList;
import java.util.List;

public class MandatoryCondition extends MandatoryCondition_Base {

    public MandatoryCondition() {
        super();
    }

    public List<String> getConditionValuesAsList() {
        List<String> result = new ArrayList<String>();
        String[] values = getConditionValues().split("_#_");
        for (String value : values) {
            result.add(value);
        }
        return result;
    }

    @Deprecated
    public boolean hasMinimumNumberOfChars() {
        return getMinimumNumberOfChars() != null;
    }

    @Deprecated
    public boolean hasConditionValues() {
        return getConditionValues() != null;
    }

    @Deprecated
    public boolean hasShowIfConditionIsFalse() {
        return getShowIfConditionIsFalse() != null;
    }

    @Deprecated
    public boolean hasDependsResult() {
        return getDependsResult() != null;
    }

}
