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
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationResponsibility {

    protected Boolean run(String executionDegreeId, User userView) throws FenixServiceException {
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(userView.getPerson());

        if (coordinator == null || !coordinator.getResponsible().booleanValue()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCoordinationResponsibility serviceInstance = new ReadCoordinationResponsibility();

    @Atomic
    public static Boolean runReadCoordinationResponsibility(String executionDegreeId, User userView)
            throws FenixServiceException, NotAuthorizedException {
        DegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return serviceInstance.run(executionDegreeId, userView);
    }

}