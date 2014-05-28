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
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Filtro.PublishedExamsMapAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingAssociatedCurricularCoursesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class ExecutionCourseSiteComponentService {

    protected ExecutionCourseSiteView run(ISiteComponent commonComponent, ISiteComponent bodyComponent, String infoSiteCode,
            String infoExecutionCourseCode, Integer sectionIndex, Integer curricularCourseId) throws FenixServiceException,
            NonExistingAssociatedCurricularCoursesServiceException {
        final ExecutionCourseSite site;
        if (infoSiteCode != null) {
            site = ExecutionCourseSite.readExecutionCourseSiteByOID(infoSiteCode);
        } else {
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourseCode);
            site = executionCourse.getSite();
        }

        if (site == null) {
            throw new NonExistingServiceException();
        }

        ExecutionCourseSiteComponentBuilder componentBuilder = ExecutionCourseSiteComponentBuilder.getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent, sectionIndex, curricularCourseId);
        final ExecutionCourseSiteView executionCourseSiteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);
        executionCourseSiteView.setExecutionCourse(site.getExecutionCourse());

        if (!Authenticate.getUser().getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            PublishedExamsMapAuthorizationFilter.execute(executionCourseSiteView);
        }

        return executionCourseSiteView;
    }

    // Service Invokers migrated from Berserk

    private static final ExecutionCourseSiteComponentService serviceInstance = new ExecutionCourseSiteComponentService();

    @Atomic
    public static ExecutionCourseSiteView runExecutionCourseSiteComponentService(ISiteComponent commonComponent,
            ISiteComponent bodyComponent, String infoSiteCode, String infoExecutionCourseCode, Integer sectionIndex,
            Integer curricularCourseId) throws FenixServiceException, NonExistingAssociatedCurricularCoursesServiceException {
        return serviceInstance.run(commonComponent, bodyComponent, infoSiteCode, infoExecutionCourseCode, sectionIndex,
                curricularCourseId);
    }

}