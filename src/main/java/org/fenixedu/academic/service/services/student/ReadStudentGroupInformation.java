/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 26/Ago/2003
 *

 */
package org.fenixedu.academic.service.services.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.dto.InfoSiteStudentGroup;
import org.fenixedu.academic.dto.InfoSiteStudentInformation;
import org.fenixedu.academic.dto.InfoStudentGroupWithAttendsAndGroupingAndShift;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentGroupInformation {

    @Atomic
    public static InfoSiteStudentGroup run(String studentGroupCode) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        StudentGroup studentGroup = null;
        Grouping grouping = null;
        Collection groupAttendsList = null;

        studentGroup = FenixFramework.getDomainObject(studentGroupCode);

        if (studentGroup == null) {
            return null;
        }

        List studentGroupInformationList = new ArrayList();
        grouping = studentGroup.getGrouping();
        groupAttendsList = studentGroup.getAttendsSet();

        Iterator iter = groupAttendsList.iterator();
        InfoSiteStudentInformation infoSiteStudentInformation = null;
        Attends attend = null;

        while (iter.hasNext()) {
            infoSiteStudentInformation = new InfoSiteStudentInformation();

            attend = (Attends) iter.next();

            infoSiteStudentInformation.setNumber(attend.getRegistration().getNumber());

            infoSiteStudentInformation.setName(attend.getRegistration().getPerson().getName());

            infoSiteStudentInformation.setEmail(attend.getRegistration().getPerson().getEmail());

            infoSiteStudentInformation.setUsername(attend.getRegistration().getPerson().getUsername());

            studentGroupInformationList.add(infoSiteStudentInformation);

        }

        Collections.sort(studentGroupInformationList, new BeanComparator("number"));
        infoSiteStudentGroup.setInfoSiteStudentInformationList(studentGroupInformationList);
        infoSiteStudentGroup.setInfoStudentGroup(InfoStudentGroupWithAttendsAndGroupingAndShift.newInfoFromDomain(studentGroup));

        if (grouping.getMaximumCapacity() != null) {

            int vagas = grouping.getMaximumCapacity().intValue() - groupAttendsList.size();

            infoSiteStudentGroup.setNrOfElements(Integer.valueOf(vagas));
        } else {
            infoSiteStudentGroup.setNrOfElements("Sem limite");
        }

        return infoSiteStudentGroup;
    }
}