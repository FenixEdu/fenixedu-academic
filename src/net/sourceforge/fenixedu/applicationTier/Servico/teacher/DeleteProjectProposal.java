/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteProjectProposal extends Service {

    public boolean run(Integer objectCode, Integer groupPropertiesCode, Integer executionCourseCode,
            String withdrawalPersonUsername) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        IPersistentExportGrouping persistentExportGrouping = persistentSupport.getIPersistentExportGrouping();
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

        Person withdrawalPerson = persistentTeacher.readTeacherByUsername(withdrawalPersonUsername).getPerson();
        Grouping groupProperties = (Grouping) persistentObject.readByOID(Grouping.class, groupPropertiesCode);
        ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseCode);
        ExecutionCourse startExecutionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, objectCode);

        if (groupProperties == null) {
            throw new InvalidArgumentsServiceException("error.noGroupProperties");
        }
        if (executionCourse == null || startExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noExecutionCourse");
        }

        ExportGrouping groupingExecutionCourse = persistentExportGrouping
                .readBy(groupProperties.getIdInternal(), executionCourse.getIdInternal());

        if (groupingExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noProjectProposal");
        }

        groupProperties.removeExportGroupings(groupingExecutionCourse);
        executionCourse.removeExportGroupings(groupingExecutionCourse);
        persistentExportGrouping.deleteByOID(ExportGrouping.class, groupingExecutionCourse.getIdInternal());

        // List teachers to advise
        List group = new ArrayList();

        List groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
        Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();

        while (iterGroupPropertiesExecutionCourseList.hasNext()) {

            ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList
                    .next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
                    || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

                List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();

                Iterator iterProfessorship = professorships.iterator();
                while (iterProfessorship.hasNext()) {
                    Professorship professorship = (Professorship) iterProfessorship.next();
                    Teacher teacher = professorship.getTeacher();

                    if (!(teacher.getPerson()).equals(withdrawalPerson)
                            && !group.contains(teacher.getPerson())) {
                        group.add(teacher.getPerson());
                    }
                }
            }
        }

        List professorshipsAux = executionCourse.getProfessorships();

        Iterator iterProfessorshipsAux = professorshipsAux.iterator();
        while (iterProfessorshipsAux.hasNext()) {
            Professorship professorshipAux = (Professorship) iterProfessorshipsAux.next();
            Teacher teacherAux = professorshipAux.getTeacher();
            if (!(teacherAux.getPerson()).equals(withdrawalPerson)
                    && !group.contains(teacherAux.getPerson())) {
                group.add(teacherAux.getPerson());
            }
        }

        // Create Advisory
        Advisory advisory = createDeleteProjectProposalAdvisory(executionCourse, startExecutionCourse,
                withdrawalPerson, groupingExecutionCourse, groupProperties);
        for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
            final Person person = (Person) iterator.next();

            person.getAdvisories().add(advisory);
            advisory.getPeople().add(person);
        }

        return true;
    }

    private Advisory createDeleteProjectProposalAdvisory(ExecutionCourse goalExecutionCourse,
            ExecutionCourse startExecutionCourse, Person withdrawalPerson,
            ExportGrouping groupPropertiesExecutionCourse, Grouping grouping) {
        Advisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (grouping.getEnrolmentEndDay() != null) {
            advisory.setExpires(grouping.getEnrolmentEndDay()
                    .getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + withdrawalPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome());

        advisory.setSubject("Desistência de proposta de Co-Avaliação");

        String msg;
        msg = new String("O Docente " + withdrawalPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome()
                + " desistiu da proposta de co-avaliação para a disciplina "
                + goalExecutionCourse.getNome() + " relativa ao agrupamento "
                + grouping.getName()
                + " previamente enviada pelo docente "
                + groupPropertiesExecutionCourse.getSenderPerson().getNome() + " da disciplina "
                + groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome() + "!");

        advisory.setMessage(msg);        
        return advisory;
    }

}