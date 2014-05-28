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
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */
public class ReadExecutionCourseProjects {

    @Atomic
    public static ISiteComponent run(String executionCourseID, String userName) throws FenixServiceException {

        InfoSiteProjects infoSiteProjects = null;

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        final List<Grouping> executionCourseProjects = executionCourse.getGroupings();

        if (executionCourseProjects.size() != 0) {
            infoSiteProjects = new InfoSiteProjects();
            List infoGroupPropertiesList = new ArrayList();

            for (final Grouping grouping : executionCourseProjects) {
                IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
                IGroupEnrolmentStrategy strategy =
                        enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);
                if (strategy.checkEnrolmentDate(grouping, Calendar.getInstance())
                        && strategy.checkStudentInGrouping(grouping, userName)) {
                    InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping.newInfoFromDomain(grouping);
                    infoGroupPropertiesList.add(infoGroupProperties);
                }
            }
            infoSiteProjects.setInfoGroupPropertiesList(infoGroupPropertiesList);
        }
        return infoSiteProjects;
    }
}