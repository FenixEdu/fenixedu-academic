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
package org.fenixedu.academic.service.services.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControlPredicate;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveDegreeCurricularPlansByDegreeType {

    @Atomic
    public static Collection<InfoDegreeCurricularPlan> run(Predicate<DegreeType> degreeType) {
        return getActiveDegreeCurricularPlansByDegreeType(degreeType, null);
    }

    @Atomic
    public static Collection<InfoDegreeCurricularPlan> runForAcademicAdmin(Predicate<DegreeType> degreeType) {
        return getActiveDegreeCurricularPlansByDegreeType(degreeType, AcademicPredicates.MANAGE_EXECUTION_COURSES);
    }

    private static Collection<InfoDegreeCurricularPlan> getActiveDegreeCurricularPlansByDegreeType(
            Predicate<DegreeType> degreeType, AccessControlPredicate<Object> permission) {
        List<DegreeCurricularPlan> degreeCurricularPlans = new ArrayList<DegreeCurricularPlan>();
        for (DegreeCurricularPlan dcp : DegreeCurricularPlan.readByDegreeTypeAndState(degreeType,
                DegreeCurricularPlanState.ACTIVE)) {
            if (permission != null) {
                if (!permission.evaluate(dcp.getDegree())) {
                    continue;
                }
            }
            degreeCurricularPlans.add(dcp);
        }

        return CollectionUtils.collect(degreeCurricularPlans, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) arg0;
                return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            }

        });
    }

}