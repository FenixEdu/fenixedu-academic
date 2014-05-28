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
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID {

    @Atomic
    public static InfoExecutionCourse run(String executionCourseOID) throws FenixServiceException {

        if (executionCourseOID == null) {
            throw new FenixServiceException("nullId");
        }

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseOID);
        return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

}