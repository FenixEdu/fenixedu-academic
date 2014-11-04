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
 * Created on 28/Ago/2003
 *
 */
package org.fenixedu.academic.service.services.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoGrouping;
import org.fenixedu.academic.dto.InfoSiteStudentsWithoutGroup;
import org.fenixedu.academic.dto.InfoStudent;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.EnrolmentGroupPolicyType;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentsWithoutGroup {

    public class NewStudentGroupAlreadyExists extends FenixServiceException {
    }

    @Atomic
    public static InfoSiteStudentsWithoutGroup run(final String groupPropertiesCode, final String username)
            throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        final InfoSiteStudentsWithoutGroup infoSiteStudentsWithoutGroup = new InfoSiteStudentsWithoutGroup();
        final Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCode);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final Collection allStudentsGroups = grouping.getStudentGroupsSet();

        final Integer groupNumber = grouping.findMaxGroupNumber() + 1;

        infoSiteStudentsWithoutGroup.setGroupNumber(groupNumber);
        infoSiteStudentsWithoutGroup.setInfoGrouping(InfoGrouping.newInfoFromDomain(grouping));

        final Collection<Attends> attends = grouping.getAttendsSet();

        Registration userStudent = null;
        for (Object element : attends) {
            final Attends attend = (Attends) element;
            final Registration registration = attend.getRegistration();
            final Person person = registration.getPerson();
            if (person.getUser().getUsername().equals(username)) {
                userStudent = registration;
                break;
            }
        }
        final InfoStudent infoStudent = getInfoStudentFromStudent(userStudent);
        infoSiteStudentsWithoutGroup.setInfoUserStudent(infoStudent);

        if (grouping.getEnrolmentPolicy().equals(new EnrolmentGroupPolicyType(2))) {
            return infoSiteStudentsWithoutGroup;
        }

        final Set<Attends> attendsWithOutGroupsSet = new HashSet<Attends>(attends);
        for (final Iterator iterator = allStudentsGroups.iterator(); iterator.hasNext();) {
            final StudentGroup studentGroup = (StudentGroup) iterator.next();

            final Collection allStudentGroupsAttends = studentGroup.getAttendsSet();

            for (final Iterator iterator2 = allStudentGroupsAttends.iterator(); iterator2.hasNext();) {
                final Attends studentGroupAttend = (Attends) iterator2.next();
                attendsWithOutGroupsSet.remove(studentGroupAttend);
            }
        }

        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>(attendsWithOutGroupsSet.size());
        for (Object element : attendsWithOutGroupsSet) {
            final Attends attend = (Attends) element;
            final Registration registration = attend.getRegistration();

            if (!registration.equals(userStudent)) {
                final InfoStudent infoStudent2 = getInfoStudentFromStudent(registration);
                infoStudentList.add(infoStudent2);
            }

        }
        infoSiteStudentsWithoutGroup.setInfoStudentList(infoStudentList);

        return infoSiteStudentsWithoutGroup;
    }

    protected static InfoStudent getInfoStudentFromStudent(Registration userStudent) {
        return InfoStudent.newInfoFromDomain(userStudent);
    }
}