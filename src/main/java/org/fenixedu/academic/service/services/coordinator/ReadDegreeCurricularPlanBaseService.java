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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 5/Dez/2003
 * 
 * @modified <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a> 23/11/2004
 * @modified <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 *           23/11/2004
 * 
 */
abstract public class ReadDegreeCurricularPlanBaseService {

    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopes(final String degreeCurricularPlanId) {
        List<InfoCurricularCourseScope> infoActiveScopes = null;

        if (degreeCurricularPlanId != null) {

            DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
            List<CurricularCourseScope> allActiveScopes = degreeCurricularPlan.getActiveCurricularCourseScopes();

            if (allActiveScopes != null && allActiveScopes.size() > 0) {
                infoActiveScopes = new ArrayList<InfoCurricularCourseScope>();

                CollectionUtils.collect(allActiveScopes, new Transformer() {
                    @Override
                    public Object transform(Object input) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) input;

                        return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
                    }
                }, infoActiveScopes);
            }
        }

        return infoActiveScopes;

    }

    // Read all curricular course scope of this year
    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopesInExecutionYear(
            final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear) {
        final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();

        if (degreeCurricularPlan != null) {
            for (final CurricularCourseScope curricularCourseScope : degreeCurricularPlan
                    .findCurricularCourseScopesIntersectingPeriod(executionYear.getBeginDate(), executionYear.getEndDate())) {
                result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
        }

        return result;
    }

    // Read all curricular course scope of this year and curricular year
    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopesInCurricularYearAndExecutionPeriodAndExecutionDegree(
            final ExecutionSemester executionSemester, final ExecutionDegree executionDegree, final Integer curricularYear) {
        final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();

        if (executionSemester != null) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final ExecutionYear executionYear = executionSemester.getExecutionYear();
            final Set<CurricularCourseScope> curricularCourseScopes =
                    degreeCurricularPlan.findCurricularCourseScopesIntersectingPeriod(executionYear.getBeginDate(),
                            executionYear.getEndDate());

            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(curricularYear)) {
                    result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
                }
            }
        }

        return result;
    }

}
