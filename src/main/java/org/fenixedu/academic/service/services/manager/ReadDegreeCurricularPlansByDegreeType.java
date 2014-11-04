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
/*
 * Created on 2005/03/30
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
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