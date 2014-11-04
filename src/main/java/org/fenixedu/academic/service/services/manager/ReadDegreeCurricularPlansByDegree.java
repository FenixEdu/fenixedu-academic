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
 * Created on 29/Jul/2003
 */
package org.fenixedu.academic.service.services.manager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansByDegree {

    @Atomic
    public static List run(String idDegree) throws FenixServiceException {
        final Degree degree = FenixFramework.getDomainObject(idDegree);

        List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>();

        for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlansSet()) {
            result.add(InfoDegreeCurricularPlan.newInfoFromDomain(dcp));
        }

        return result;
    }

}