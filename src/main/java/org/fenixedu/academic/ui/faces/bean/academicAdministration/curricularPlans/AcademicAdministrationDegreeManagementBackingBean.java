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
package net.sourceforge.fenixedu.presentationTier.backBeans.academicAdministration.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans.DegreeManagementBackingBean;

public class AcademicAdministrationDegreeManagementBackingBean extends DegreeManagementBackingBean {

    public List<Degree> getFilteredPreBolonhaDegrees() {
        final List<Degree> orderedResult = Degree.readOldDegrees();

        final Iterator<Degree> degrees = orderedResult.iterator();
        while (degrees.hasNext()) {
            final Degree degree = degrees.next();
            if (degree.getDegreeType() != DegreeType.DEGREE) {
                degrees.remove();
            }
        }

        Collections.sort(orderedResult, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        return orderedResult;
    }

    @Override
    public List<Degree> getFilteredBolonhaDegrees() {
        final List<Degree> orderedResult = new ArrayList<Degree>(Degree.readBolonhaDegrees());
        Collections.sort(orderedResult, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        return orderedResult;
    }

}
