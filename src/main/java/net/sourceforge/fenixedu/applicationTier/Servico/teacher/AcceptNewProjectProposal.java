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
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa & rmalo
 * 
 */
public class AcceptNewProjectProposal {

    protected Boolean run(String executionCourseId, String groupPropertiesId, String acceptancePersonUserName)
            throws FenixServiceException {

        if (groupPropertiesId == null) {
            return Boolean.FALSE;
        }

        final Grouping grouping = FenixFramework.getDomainObject(groupPropertiesId);
        if (grouping == null) {
            throw new NotAuthorizedException();
        }

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        final ExportGrouping groupPropertiesExecutionCourse = executionCourse.getExportGrouping(grouping);

        if (groupPropertiesExecutionCourse == null) {
            throw new ExistingServiceException();
        }

        Person receiverPerson = Teacher.readTeacherByUsername(acceptancePersonUserName).getPerson();

        ExecutionCourse executionCourseAux = groupPropertiesExecutionCourse.getExecutionCourse();
        if (executionCourseAux.getGroupingByName(groupPropertiesExecutionCourse.getGrouping().getName()) != null) {
            String name = groupPropertiesExecutionCourse.getGrouping().getName();
            throw new InvalidSituationServiceException(name);
        }

        List attendsStudentNumbers = new ArrayList();
        Collection<Attends> attends = groupPropertiesExecutionCourse.getGrouping().getAttends();
        Iterator iterAttendsInAttendsSet = attends.iterator();
        while (iterAttendsInAttendsSet.hasNext()) {
            Attends attend = (Attends) iterAttendsInAttendsSet.next();
            attendsStudentNumbers.add(attend.getRegistration().getNumber());
        }

        Collection<Attends> attendsAux = executionCourse.getAttends();
        Iterator iterAttends = attendsAux.iterator();
        while (iterAttends.hasNext()) {
            Attends attend = (Attends) iterAttends.next();
            if (!attendsStudentNumbers.contains(attend.getRegistration().getNumber())) {
                grouping.addAttends(attend);
            }
        }

        Person senderPerson = groupPropertiesExecutionCourse.getSenderPerson();
        Collection groupPropertiesExecutionCourseList = grouping.getExportGroupings();
        Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();
        List groupTeachers = new ArrayList();
        while (iterGroupPropertiesExecutionCourseList.hasNext()) {
            ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList.next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
                    || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {
                ExecutionCourse personExecutionCourse = groupPropertiesExecutionCourseAux.getExecutionCourse();
                Collection professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();
                Iterator iterProfessorship = professorships.iterator();
                while (iterProfessorship.hasNext()) {
                    Professorship professorship = (Professorship) iterProfessorship.next();
                    final Person person = professorship.getPerson();
                    if (!person.equals(receiverPerson) && !groupTeachers.contains(person)) {
                        groupTeachers.add(person);
                    }
                }
            }
        }

        List groupAux = new ArrayList();
        Collection<Professorship> professorshipsAux = executionCourse.getProfessorships();

        Iterator iterProfessorshipsAux = professorshipsAux.iterator();
        while (iterProfessorshipsAux.hasNext()) {
            Professorship professorshipAux = (Professorship) iterProfessorshipsAux.next();
            Teacher teacherAux = professorshipAux.getTeacher();
            if (!(teacherAux.getPerson()).equals(receiverPerson)) {
                groupAux.add(teacherAux.getPerson());
            }
        }

        List<ExecutionCourse> ecs = grouping.getExecutionCourses();
        GroupsAndShiftsManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.groupAndShifts.grouping.exportGroup.accepted", grouping.getName(),
                executionCourse.getNome(), executionCourse.getDegreePresentationString());
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING,
                    "log.executionCourse.groupAndShifts.grouping.exportGroup.accepted", grouping.getName(),
                    executionCourse.getNome(), executionCourse.getDegreePresentationString());
        }

        groupPropertiesExecutionCourse.setProposalState(new ProposalState(Integer.valueOf(2)));
        groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final AcceptNewProjectProposal serviceInstance = new AcceptNewProjectProposal();

    @Atomic
    public static Boolean runAcceptNewProjectProposal(String executionCourseId, String groupPropertiesId,
            String acceptancePersonUserName) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, groupPropertiesId, acceptancePersonUserName);
    }

}