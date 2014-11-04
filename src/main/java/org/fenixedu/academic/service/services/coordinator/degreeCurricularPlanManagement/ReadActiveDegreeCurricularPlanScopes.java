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
package org.fenixedu.academic.service.services.coordinator.degreeCurricularPlanManagement;

import java.util.List;

import org.fenixedu.academic.service.filter.CoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.services.coordinator.ReadDegreeCurricularPlanBaseService;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

/**
 * @author Fernanda Quitério 5/Nov/2003
 * 
 * @modified <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a> 23/11/2004
 * @modified <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 *           23/11/2004
 * 
 */

public class ReadActiveDegreeCurricularPlanScopes extends ReadDegreeCurricularPlanBaseService {

    protected List run(final String degreeCurricularPlanId) {
        return super.readActiveCurricularCourseScopes(degreeCurricularPlanId);
    }

    // Service Invokers migrated from Berserk

    private static final ReadActiveDegreeCurricularPlanScopes serviceInstance = new ReadActiveDegreeCurricularPlanScopes();

    @Atomic
    public static List runReadActiveDegreeCurricularPlanScopes(String degreeCurricularPlanId) throws NotAuthorizedException {
        CoordinatorAuthorizationFilter.instance.execute();
        return serviceInstance.run(degreeCurricularPlanId);
    }

}