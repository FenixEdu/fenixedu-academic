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
 * Created on 12/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author ansr and scpo
 * 
 */
public class PrepareCreateStudentGroup {

    protected ISiteComponent run(String executionCourseCode, String groupPropertiesCode) throws ExistingServiceException {

        final Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final Collection<StudentGroup> allStudentsGroups = grouping.getStudentGroupsSet();
        final List<Attends> attendsGrouping = new ArrayList(grouping.getAttends());
        for (final StudentGroup studentGroup : allStudentsGroups) {
            for (Attends attend : studentGroup.getAttends()) {
                attendsGrouping.remove(attend);
            }
        }

        final List<InfoSiteStudentInformation> infoStudentInformationList =
                new ArrayList<InfoSiteStudentInformation>(attendsGrouping.size());
        for (Attends attend : attendsGrouping) {
            final Registration registration = attend.getRegistration();
            final Person person = registration.getPerson();
            InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
            infoSiteStudentInformation.setEmail(person.getEmail());
            infoSiteStudentInformation.setName(person.getName());
            infoSiteStudentInformation.setNumber(registration.getNumber());
            infoSiteStudentInformation.setUsername(person.getUsername());
            infoSiteStudentInformation.setPersonID(person.getExternalId());
            infoStudentInformationList.add(infoSiteStudentInformation);
        }

        Collections.sort(infoStudentInformationList, new BeanComparator("number"));

        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        infoSiteStudentGroup.setInfoSiteStudentInformationList(infoStudentInformationList);

        final int groupNumber = grouping.findMaxGroupNumber() + 1;
        infoSiteStudentGroup.setNrOfElements(Integer.valueOf(groupNumber));

        return infoSiteStudentGroup;

    }

    // Service Invokers migrated from Berserk

    private static final PrepareCreateStudentGroup serviceInstance = new PrepareCreateStudentGroup();

    @Atomic
    public static ISiteComponent runPrepareCreateStudentGroup(String executionCourseCode, String groupPropertiesCode)
            throws ExistingServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, groupPropertiesCode);
    }

}