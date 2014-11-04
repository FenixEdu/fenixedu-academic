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
 * Created on 9/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.fenixedu.academic.service.services.commons;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.InfoExecutionYear;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadExecutionYearsByDegreeCurricularPlanID {

    @Atomic
    public static List run(String degreeCurricularPlanID) {
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        List<ExecutionYear> executionYears =
                (List<ExecutionYear>) CollectionUtils.collect(degreeCurricularPlan.getExecutionDegreesSet(), new Transformer() {

                    @Override
                    public Object transform(Object arg0) {
                        ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                        return InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear());
                    }

                });

        return executionYears;
    }
}