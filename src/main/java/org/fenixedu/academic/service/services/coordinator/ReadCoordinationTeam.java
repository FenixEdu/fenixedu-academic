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
 * Created on 17/Set/2003
 *
 */
package org.fenixedu.academic.service.services.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.dto.InfoCoordinator;
import org.fenixedu.academic.service.filter.DegreeCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationTeam {

    protected List run(String executionDegreeId) throws FenixServiceException {
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("errors.invalid.execution.degree");
        }
        Collection<Coordinator> coordinators = executionDegree.getCoordinatorsListSet();
        Iterator iterator = coordinators.iterator();
        List infoCoordinators = new ArrayList();
        while (iterator.hasNext()) {
            Coordinator coordinator = (Coordinator) iterator.next();
            InfoCoordinator infoCoordinator = InfoCoordinator.newInfoFromDomain(coordinator);
            infoCoordinators.add(infoCoordinator);
        }
        return infoCoordinators;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCoordinationTeam serviceInstance = new ReadCoordinationTeam();

    @Atomic
    public static List runReadCoordinationTeam(String executionDegreeId) throws FenixServiceException, NotAuthorizedException {
        DegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return serviceInstance.run(executionDegreeId);
    }

}