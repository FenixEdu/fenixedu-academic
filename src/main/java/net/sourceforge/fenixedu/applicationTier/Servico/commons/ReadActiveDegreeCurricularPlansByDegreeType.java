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
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveDegreeCurricularPlansByDegreeType {

    @Atomic
    public static Collection<InfoDegreeCurricularPlan> run(final DegreeType degreeType) {
        return getActiveDegreeCurricularPlansByDegreeType(degreeType, null);
    }

    @Atomic
    public static Collection<InfoDegreeCurricularPlan> runForAcademicAdmin(final DegreeType degreeType) {
        return getActiveDegreeCurricularPlansByDegreeType(degreeType, AcademicPredicates.MANAGE_EXECUTION_COURSES);
    }

    private static Collection<InfoDegreeCurricularPlan> getActiveDegreeCurricularPlansByDegreeType(final DegreeType degreeType,
            AccessControlPredicate<Object> permission) {
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