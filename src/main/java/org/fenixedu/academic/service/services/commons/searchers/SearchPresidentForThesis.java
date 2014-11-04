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
package org.fenixedu.academic.service.services.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import pt.ist.fenixframework.FenixFramework;

public class SearchPresidentForThesis implements AutoCompleteProvider<Person> {

    @Override
    public Collection<Person> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {

        final String thesisIdString = argsMap.get("thesis");
        if (thesisIdString == null) {
            return null;
        }

        final Thesis thesis = FenixFramework.getDomainObject(thesisIdString);
        if (thesis == null) {
            return null;
        }

        final List<Person> result = new ArrayList<Person>();
        final Enrolment enrolment = thesis.getEnrolment();
        final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfDegreeModule();
        final ExecutionYear executionYear = enrolment.getExecutionYear();
        final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
        if (executionDegree != null) {
            for (ScientificCommission member : executionDegree.getScientificCommissionMembersSet()) {
                result.add(member.getPerson());
            }
        }

        return result;
    }

}
