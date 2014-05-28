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
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Factory.PublicSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class ClassSiteComponentService {

    @Atomic
    public static Object run(ISiteComponent bodyComponent, String executionYearName, String executionPeriodName,
            String degreeInitials, String nameDegreeCurricularPlan, String className, Integer curricularYear, String classId)
            throws FenixServiceException {

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearName);

        ExecutionSemester executionSemester =
                ExecutionSemester.readByNameAndExecutionYear(executionPeriodName, executionYear.getYear());

        DegreeCurricularPlan degreeCurricularPlan =
                DegreeCurricularPlan.readByNameAndDegreeSigla(nameDegreeCurricularPlan, degreeInitials);
        ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear.getYear());
        PublicSiteComponentBuilder componentBuilder = PublicSiteComponentBuilder.getInstance();
        SchoolClass domainClass;
        if (classId == null) {
            domainClass = getDomainClass(className, curricularYear, executionSemester, executionDegree);
            if (domainClass == null) {
                throw new NonExistingServiceException();
            }
        } else {

            domainClass = FenixFramework.getDomainObject(classId);
        }
        bodyComponent = componentBuilder.getComponent(bodyComponent, domainClass);
        SiteView siteView = new SiteView(bodyComponent);

        return siteView;
    }

    private static SchoolClass getDomainClass(String className, Integer curricularYear, ExecutionSemester executionSemester,
            ExecutionDegree executionDegree) {

        SchoolClass domainClass = null;
        if (curricularYear == null) {
            domainClass = executionDegree.findSchoolClassesByExecutionPeriodAndName(executionSemester, className);
        } else {
            if (className == null && curricularYear == null) {
                Set<SchoolClass> domainList = executionDegree.findSchoolClassesByExecutionPeriod(executionSemester);
                if (domainList.size() != 0) {
                    domainClass = domainList.iterator().next();
                }
            }
        }
        return domainClass;
    }
}