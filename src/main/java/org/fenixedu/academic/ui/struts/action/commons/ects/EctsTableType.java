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
package net.sourceforge.fenixedu.presentationTier.Action.commons.ects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EctsTableType {
    ENROLMENT, GRADUATION;

    public List<EctsTableLevel> getAllowedLevels() {
        switch (this) {
        case ENROLMENT:
            return Collections.unmodifiableList(Arrays.asList(EctsTableLevel.COMPETENCE_COURSE, EctsTableLevel.DEGREE,
                    EctsTableLevel.CURRICULAR_YEAR, EctsTableLevel.SCHOOL));
        case GRADUATION:
            return Collections.unmodifiableList(Arrays.asList(EctsTableLevel.DEGREE, EctsTableLevel.CYCLE));
        default:
            return Collections.emptyList();
        }
    }

    public String getName() {
        return name();
    }
}
