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
package org.fenixedu.academic.ui.renderers.providers.executionDegree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

import pt.ist.fenixframework.FenixFramework;

public class ExecutionDegreeAutoCompleteProvider implements AutoCompleteProvider<ExecutionDegree> {

    @Override
    public Collection<ExecutionDegree> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String executionYearOid = argsMap.get("executionYearOid");
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearOid);

        final String searchValue = StringNormalizer.normalize(value);

        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            if (match(searchValue, degreeCurricularPlan.getName())
                    || match(searchValue, degree.getNameI18N(executionYear).getContent())
                    || match(searchValue, degree.getSigla())) {
                result.add(executionDegree);
                if (result.size() >= maxCount) {
                    break;
                }
            }
        }
        return result;
    }

    private boolean match(final String stringToMatch, final String content) {
        final String normalizedContent = StringNormalizer.normalize(content);
        return normalizedContent.indexOf(stringToMatch) >= 0;
    }
}
