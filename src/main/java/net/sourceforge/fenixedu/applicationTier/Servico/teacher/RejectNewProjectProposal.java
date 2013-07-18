/*
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author joaosa & rmalo
 * 
 */
public class RejectNewProjectProposal {

    protected Boolean run(Integer executionCourseId, Integer groupPropertiesId, String rejectorUserName)
            throws FenixServiceException {

        if (groupPropertiesId == null) {
            return Boolean.FALSE;
        }

        final Grouping groupProperties = RootDomainObject.getInstance().readGroupingByOID(groupPropertiesId);
        if (groupProperties == null) {
            throw new NotAuthorizedException();
        }

        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);
        final ExportGrouping groupPropertiesExecutionCourse = executionCourse.getExportGrouping(groupProperties);
        if (groupPropertiesExecutionCourse == null) {
            throw new ExistingServiceException();
        }

        final Person receiverPerson = Teacher.readTeacherByUsername(rejectorUserName).getPerson();
        groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
        groupPropertiesExecutionCourse.setProposalState(new ProposalState(ProposalState.EM_ESPERA));

        List group = new ArrayList();

        List groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
        Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();

        while (iterGroupPropertiesExecutionCourseList.hasNext()) {

            ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList.next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
                    || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

                List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();

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

        List professorshipsAux = executionCourse.getProfessorships();

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
        GroupsAndShiftsManagementLog.createLog(executionCourse, "resources.MessagingResources",
                "log.executionCourse.groupAndShifts.grouping.exportGroup.rejected", groupProperties.getName(),
                executionCourse.getNome(), executionCourse.getDegreePresentationString());
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.groupAndShifts.grouping.exportGroup.rejected", groupProperties.getName(),
                    executionCourse.getNome(), executionCourse.getDegreePresentationString());
        }

        groupPropertiesExecutionCourse.delete();

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final RejectNewProjectProposal serviceInstance = new RejectNewProjectProposal();

    @Service
    public static Boolean runRejectNewProjectProposal(Integer executionCourseId, Integer groupPropertiesId,
            String rejectorUserName) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, groupPropertiesId, rejectorUserName);
    }

}