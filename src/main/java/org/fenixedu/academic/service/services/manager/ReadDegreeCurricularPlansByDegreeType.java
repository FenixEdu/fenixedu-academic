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
/*
 * Created on 2005/03/30
 *
 */
package org.fenixedu.academic.service.services.manager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;

import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 * 
 */
public class ReadDegreeCurricularPlansByDegreeType {

    @Atomic
    public static List<InfoDegreeCurricularPlan> run(final DegreeType degreeType) {
        final List<DegreeCurricularPlan> dcps = DegreeCurricularPlan.readByCurricularStage(CurricularStage.OLD);
        final List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>(dcps.size());

        for (final DegreeCurricularPlan degreeCurricularPlan : dcps) {
            if (degreeCurricularPlan.getDegreeType().equals(degreeType)) {
                result.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
            }
        }

        return result;
    }

}