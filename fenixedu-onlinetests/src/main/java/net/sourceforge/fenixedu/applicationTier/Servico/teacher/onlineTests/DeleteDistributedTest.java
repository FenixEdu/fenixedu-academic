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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteDistributedTest {

    protected void run(String executionCourseId, final String distributedTestId) {
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);

        for (Metadata metadata : Bennu.getInstance().getMetadatasSet()) {
            if (metadata.getVisibility() != null && !metadata.getVisibility().booleanValue()
                    && metadata.getQuestionsSet().size() == 0) {
                metadata.delete();
            }
        }

        distributedTest.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteDistributedTest serviceInstance = new DeleteDistributedTest();

    @Atomic
    public static void runDeleteDistributedTest(String executionCourseId, String distributedTestId) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, distributedTestId);
    }

}