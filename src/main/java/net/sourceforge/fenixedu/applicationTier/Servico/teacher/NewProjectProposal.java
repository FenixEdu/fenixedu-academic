/*
 * Created on 9/Set/2004
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
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
import net.sourceforge.fenixedu.util.ProposalState;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author joaosa & rmalo
 * 
 */
public class NewProjectProposal {

    protected Boolean run(Integer objectCode, Integer goalExecutionCourseId, Integer groupPropertiesId,
            String senderPersonUsername) throws FenixServiceException {

        Boolean result = Boolean.FALSE;

        if (groupPropertiesId == null) {
            return result;
        }

        Grouping groupProperties = AbstractDomainObject.fromExternalId(groupPropertiesId);
        ExecutionCourse goalExecutionCourse = AbstractDomainObject.fromExternalId(goalExecutionCourseId);
        ExecutionCourse startExecutionCourse = AbstractDomainObject.fromExternalId(objectCode);
        Person senderPerson = Teacher.readTeacherByUsername(senderPersonUsername).getPerson();

        if (groupProperties == null) {
            throw new InvalidArgumentsServiceException("error.noGroupProperties");
        }
        if (goalExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noGoalExecutionCourse");
        }
        if (startExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noSenderExecutionCourse");
        }
        if (senderPerson == null) {
            throw new InvalidArgumentsServiceException("error.noPerson");
        }

        List listaRelation = groupProperties.getExportGroupings();
        Iterator iterRelation = listaRelation.iterator();
        while (iterRelation.hasNext()) {
            ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterRelation.next();
            if (groupPropertiesExecutionCourse.getExecutionCourse().equals(goalExecutionCourse)
                    && groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1) {
                throw new InvalidSituationServiceException("error.GroupPropertiesCreator");
            }
            if (groupPropertiesExecutionCourse.getExecutionCourse().equals(goalExecutionCourse)
                    && groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2) {
                throw new InvalidSituationServiceException("error.AlreadyAcceptedProposal");
            }
            if (groupPropertiesExecutionCourse.getExecutionCourse().equals(goalExecutionCourse)
                    && groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 3) {
                throw new InvalidSituationServiceException("error.WaitingProposal");
            }
        }

        boolean acceptProposal = false;

        ExportGrouping groupPropertiesExecutionCourse = new ExportGrouping(groupProperties, goalExecutionCourse);
        groupPropertiesExecutionCourse.setProposalState(new ProposalState(Integer.valueOf(3)));
        groupPropertiesExecutionCourse.setSenderPerson(senderPerson);
        groupPropertiesExecutionCourse.setSenderExecutionCourse(startExecutionCourse);
        groupProperties.addExportGroupings(groupPropertiesExecutionCourse);
        goalExecutionCourse.addExportGroupings(groupPropertiesExecutionCourse);

        List group = new ArrayList();
        List allOtherProfessors = new ArrayList();

        List professorships = goalExecutionCourse.getProfessorships();
        Iterator iterProfessorship = professorships.iterator();
        while (iterProfessorship.hasNext()) {
            Professorship professorship = (Professorship) iterProfessorship.next();
            if (!professorship.getPerson().equals(senderPerson)) {
                group.add(professorship.getPerson());
            } else {
                acceptProposal = true;
            }
        }

        allOtherProfessors.addAll(group);

        List groupAux = new ArrayList();

        List professorshipsAux = startExecutionCourse.getProfessorships();
        Iterator iterProfessorshipAux = professorshipsAux.iterator();
        while (iterProfessorshipAux.hasNext()) {
            Professorship professorshipAux = (Professorship) iterProfessorshipAux.next();
            Person pessoa = professorshipAux.getPerson();
            if (!(pessoa.equals(senderPerson))) {
                groupAux.add(pessoa);
                if (!allOtherProfessors.contains(pessoa)) {

                    allOtherProfessors.add(pessoa);
                }
            }
        }

        // Create Advisory
        if (acceptProposal == true) {
            result = Boolean.TRUE;
            groupPropertiesExecutionCourse.setProposalState(new ProposalState(Integer.valueOf(2)));
            List groupingStudentNumbers = new ArrayList();

            Iterator iterAttends = groupPropertiesExecutionCourse.getGrouping().getAttends().iterator();

            while (iterAttends.hasNext()) {
                Attends attend = (Attends) iterAttends.next();
                groupingStudentNumbers.add(attend.getRegistration().getNumber());
            }

            Iterator iterAttends2 = goalExecutionCourse.getAttendsIterator();
            while (iterAttends2.hasNext()) {
                Attends attend = (Attends) iterAttends2.next();
                if (!groupingStudentNumbers.contains(attend.getRegistration().getNumber())) {
                    groupPropertiesExecutionCourse.getGrouping().addAttends(attend);
                }
            }
        }

        List<ExecutionCourse> ecs = groupProperties.getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.groupAndShifts.grouping.exportGroup.added", groupProperties.getName(),
                    startExecutionCourse.getNome(), startExecutionCourse.getDegreePresentationString(),
                    goalExecutionCourse.getName(), goalExecutionCourse.getDegreePresentationString());
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final NewProjectProposal serviceInstance = new NewProjectProposal();

    @Service
    public static Boolean runNewProjectProposal(Integer objectCode, Integer goalExecutionCourseId, Integer groupPropertiesId,
            String senderPersonUsername) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(objectCode);
        return serviceInstance.run(objectCode, goalExecutionCourseId, groupPropertiesId, senderPersonUsername);
    }

}