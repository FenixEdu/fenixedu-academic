/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.faces.bean.manager.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.ui.faces.bean.scientificCouncil.curricularPlans.DegreeManagementBackingBean;

public class ManagerDegreeManagementBackingBean extends DegreeManagementBackingBean {

    public List<Degree> getFilteredPreBolonhaDegrees() {
        return List.of();
    }

    @Override
    public List<Degree> getFilteredBolonhaDegrees() {
        final List<Degree> orderedResult = new ArrayList<Degree>(Degree.readBolonhaDegrees());
        Collections.sort(orderedResult, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        return orderedResult;
    }

}
