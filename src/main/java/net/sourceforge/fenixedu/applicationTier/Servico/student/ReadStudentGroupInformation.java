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

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupWithAttendsAndGroupingAndShift;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentGroupInformation {

    @Atomic
    public static ISiteComponent run(String studentGroupCode) throws FenixServiceException {
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
        groupAttendsList = studentGroup.getAttends();

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