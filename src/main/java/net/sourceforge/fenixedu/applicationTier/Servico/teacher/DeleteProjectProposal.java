/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteProjectProposal {

    protected Boolean run(String objectCode, String groupPropertiesCode, String executionCourseCode,
            String withdrawalPersonUsername) throws FenixServiceException {

        Person withdrawalPerson = Teacher.readTeacherByUsername(withdrawalPersonUsername).getPerson();
        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);
        ExecutionCourse startExecutionCourse = FenixFramework.getDomainObject(objectCode);

        if (groupProperties == null) {
            throw new InvalidArgumentsServiceException("error.noGroupProperties");
        }
        if (executionCourse == null || startExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noExecutionCourse");
        }

        ExportGrouping groupingExecutionCourse = executionCourse.getExportGrouping(groupProperties);

        if (groupingExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noProjectProposal");
        }

        // List teachers to advise
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

                    if (!person.equals(withdrawalPerson) && !group.contains(person)) {
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
            if (!(teacherAux.getPerson()).equals(withdrawalPerson) && !group.contains(teacherAux.getPerson())) {
                group.add(teacherAux.getPerson());
            }
        }

        List<ExecutionCourse> ecs = groupProperties.getExecutionCourses();
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);

        // proposal deleted is in same executioCourse
        if (startExecutionCourse.getExternalId().compareTo(groupingExecutionCourse.getExecutionCourse().getExternalId()) == 0) {
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                        "log.executionCourse.groupAndShifts.grouping.exportGroup.droppedSelf", groupProperties.getName(),
                        startExecutionCourse.getNome(), startExecutionCourse.getDegreePresentationString());
            }
        } else {
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                        "log.executionCourse.groupAndShifts.grouping.exportGroup.dropped", groupProperties.getName(),
                        startExecutionCourse.getNome(), startExecutionCourse.getDegreePresentationString(),
                        groupingExecutionCourse.getExecutionCourse().getName(), groupingExecutionCourse.getExecutionCourse()
                                .getDegreePresentationString());
            }
        }
        groupingExecutionCourse.delete();

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteProjectProposal serviceInstance = new DeleteProjectProposal();

    @Atomic
    public static Boolean runDeleteProjectProposal(String objectCode, String groupPropertiesCode, String executionCourseCode,
            String withdrawalPersonUsername) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(objectCode);
        return serviceInstance.run(objectCode, groupPropertiesCode, executionCourseCode, withdrawalPersonUsername);
    }

}