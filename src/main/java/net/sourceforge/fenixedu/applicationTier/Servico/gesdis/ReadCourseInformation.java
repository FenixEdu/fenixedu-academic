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
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gesdis.ReadCourseInformationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gesdis.ReadCourseInformationCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCourseInformation {

    protected TeacherAdministrationSiteView run(final String executionCourseOID) throws FenixServiceException {
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseOID);
        final InfoSiteCourseInformation resultComponent = new InfoSiteCourseInformation(executionCourse);

        final TeacherAdministrationSiteView result = new TeacherAdministrationSiteView();
        result.setCommonComponent(TeacherAdministrationSiteComponentBuilder.getInfoSiteCommon(new InfoSiteCommon(),
                executionCourse.getSite()));
        result.setComponent(resultComponent);

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCourseInformation serviceInstance = new ReadCourseInformation();

    @Atomic
    public static TeacherAdministrationSiteView runReadCourseInformation(String executionCourseOID) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ReadCourseInformationAuthorizationFilter.instance.execute(executionCourseOID);
            return serviceInstance.run(executionCourseOID);
        } catch (NotAuthorizedException ex1) {
            try {
                ReadCourseInformationCoordinatorAuthorizationFilter.instance.execute(executionCourseOID);
                return serviceInstance.run(executionCourseOID);
            } catch (NotAuthorizedException ex2) {
                try {
                    GEPAuthorizationFilter.instance.execute();
                    return serviceInstance.run(executionCourseOID);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}