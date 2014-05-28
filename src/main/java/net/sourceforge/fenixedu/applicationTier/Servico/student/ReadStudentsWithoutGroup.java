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
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsWithoutGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
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
    public static ISiteComponent run(final String groupPropertiesCode, final String username) throws FenixServiceException {
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

        final Collection<Attends> attends = grouping.getAttends();

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

            final Collection allStudentGroupsAttends = studentGroup.getAttends();

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