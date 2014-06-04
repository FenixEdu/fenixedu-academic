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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
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
public class RejectNewProjectProposal {

    protected Boolean run(String executionCourseId, String groupPropertiesId, String rejectorUserName)
            throws FenixServiceException {

        if (groupPropertiesId == null) {
            return Boolean.FALSE;
        }

        final Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesId);
        if (groupProperties == null) {
            throw new NotAuthorizedException();
        }

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        final ExportGrouping groupPropertiesExecutionCourse = executionCourse.getExportGrouping(groupProperties);
        if (groupPropertiesExecutionCourse == null) {
            throw new ExistingServiceException();
        }

        final Person receiverPerson = Teacher.readTeacherByUsername(rejectorUserName).getPerson();
        groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
        groupPropertiesExecutionCourse.setProposalState(new ProposalState(ProposalState.EM_ESPERA));

        List group = new ArrayList();

        Collection groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
        Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();

        while (iterGroupPropertiesExecutionCourseList.hasNext()) {

            ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList.next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
                    || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

                Collection professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();

                Iterator iterProfessorship = professorships.iterator();
                while (iterProfessorship.hasNext()) {
                    final Professorship professorship = (Professorship) iterProfessorship.next();
                    final Person person = professorship.getPerson();

                    if (!person.equals(receiverPerson) && !group.contains(person)) {
                        group.add(person);
                    }
                }
            }
        }

        Collection professorshipsAux = executionCourse.getProfessorships();

        Iterator iterProfessorshipsAux = professorshipsAux.iterator();
        while (iterProfessorshipsAux.hasNext()) {
            Professorship professorshipAux = (Professorship) iterProfessorshipsAux.next();
            Teacher teacherAux = professorshipAux.getTeacher();
            if (!(teacherAux.getPerson()).equals(receiverPerson) && !group.contains(teacherAux.getPerson())) {
                group.add(teacherAux.getPerson());
            }
        }

        Person senderPerson = groupPropertiesExecutionCourse.getSenderPerson();

        List<ExecutionCourse> ecs = groupProperties.getExecutionCourses();
        GroupsAndShiftsManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.groupAndShifts.grouping.exportGroup.rejected", groupProperties.getName(),
                executionCourse.getNome(), executionCourse.getDegreePresentationString());
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING,
                    "log.executionCourse.groupAndShifts.grouping.exportGroup.rejected", groupProperties.getName(),
                    executionCourse.getNome(), executionCourse.getDegreePresentationString());
        }

        groupPropertiesExecutionCourse.delete();

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final RejectNewProjectProposal serviceInstance = new RejectNewProjectProposal();

    @Atomic
    public static Boolean runRejectNewProjectProposal(String executionCourseId, String groupPropertiesId, String rejectorUserName)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, groupPropertiesId, rejectorUserName);
    }

}