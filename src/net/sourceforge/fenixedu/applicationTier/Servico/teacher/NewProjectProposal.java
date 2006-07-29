/*
 * Created on 9/Set/2004
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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Attends;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.ProposalState;

/**
 * @author joaosa & rmalo
 *  
 */
public class NewProjectProposal extends Service {

    public Boolean run(Integer objectCode, Integer goalExecutionCourseId, Integer groupPropertiesId,
            String senderPersonUsername) throws FenixServiceException, ExcepcaoPersistencia {

        Boolean result = Boolean.FALSE;

        if (groupPropertiesId == null) {
            return result;
        }
 
        Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesId);
        ExecutionCourse goalExecutionCourse = rootDomainObject.readExecutionCourseByOID(goalExecutionCourseId);
        ExecutionCourse startExecutionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);
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
            ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterRelation
                    .next();
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

        ExportGrouping groupPropertiesExecutionCourse = new ExportGrouping(
                groupProperties, goalExecutionCourse);
        groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(3)));
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
            Teacher teacher = professorship.getTeacher();
            if (!(teacher.getPerson()).equals(senderPerson)) {

                group.add(teacher.getPerson());
            } else {
                acceptProposal = true;
            }
        }

        allOtherProfessors.addAll(group);

        // Create Advisory
        if (acceptProposal == false) {
            Advisory advisory = createNewProjectProposalAdvisory(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
                final Person person = (Person) iterator.next();

                person.getAdvisories().add(advisory);
                advisory.getPeople().add(person);
            }
        }

        List groupAux = new ArrayList();

        List professorshipsAux = startExecutionCourse.getProfessorships();
        Iterator iterProfessorshipAux = professorshipsAux.iterator();
        while (iterProfessorshipAux.hasNext()) {
            Professorship professorshipAux = (Professorship) iterProfessorshipAux.next();
            Teacher teacherAux = professorshipAux.getTeacher();
            Person pessoa = teacherAux.getPerson();
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
            groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(2)));
            List groupingStudentNumbers = new ArrayList();
     
            Iterator iterAttends = groupPropertiesExecutionCourse.getGrouping().getAttends().iterator();
                        
            while (iterAttends.hasNext()) {
                Attends attend = (Attends) iterAttends.next();
                groupingStudentNumbers.add(attend.getAluno().getNumber());
            }
            
            Iterator iterAttends2 = goalExecutionCourse.getAttendsIterator();
            while (iterAttends2.hasNext()) {
                Attends attend = (Attends) iterAttends2.next();
                if (!groupingStudentNumbers.contains(attend.getAluno().getNumber())) {
                    groupPropertiesExecutionCourse.getGrouping().addAttends(attend);                    
                }
            }

            Advisory advisoryAux = createNewProjectProposalAcceptedAdvisory(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            for (final Iterator iterator = allOtherProfessors.iterator(); iterator.hasNext();) {
                final Person person = (Person) iterator.next();
                person.getAdvisories().add(advisoryAux);
                advisoryAux.getPeople().add(person);
            }

        } else {
            Advisory advisoryAux = createNewProjectProposalAdvisoryAux(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            for (final Iterator iterator = groupAux.iterator(); iterator.hasNext();) {
                final Person person = (Person) iterator.next();
                person.getAdvisories().add(advisoryAux);
                advisoryAux.getPeople().add(person);
            }
        }

        return result;
    }

    private Advisory createNewProjectProposalAdvisory(ExecutionCourse goalExecutionCourse,
            ExecutionCourse startExecutionCourse, Grouping groupProperties, Person senderPerson) {
        Advisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupProperties.getEnrolmentEndDay() != null) {
            advisory.setExpires(groupProperties.getEnrolmentEndDay().getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome());

        advisory.setSubject("Proposta de Co-Avaliação");

        String msg;
        msg = new String("Recebeu uma proposta de co-avaliação da disciplina "
                + startExecutionCourse.getNome() + " para a disciplina " + goalExecutionCourse.getNome()
                + " relativa ao agrupamento " + groupProperties.getName() + "!"
                + "<br/>Para mais informações dirija-se à área de gestão de grupos da disciplina "
                + goalExecutionCourse.getNome() + ".");

        advisory.setMessage(msg);
        return advisory;
    }

    private Advisory createNewProjectProposalAdvisoryAux(ExecutionCourse goalExecutionCourse,
            ExecutionCourse startExecutionCourse, Grouping groupProperties, Person senderPerson) {
        Advisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupProperties.getEnrolmentEndDay() != null) {
            advisory.setExpires(groupProperties.getEnrolmentEndDay().getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome());

        advisory.setSubject("Proposta de Co-Avaliação");

        String msg;
        msg = new String("O Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome()
                + " fez uma proposta de co-avaliação para a disciplina " + goalExecutionCourse.getNome()
                + " relativa ao agrupamento " + groupProperties.getName() + "!");

        advisory.setMessage(msg);        
        return advisory;
    }

    private Advisory createNewProjectProposalAcceptedAdvisory(ExecutionCourse goalExecutionCourse,
            ExecutionCourse startExecutionCourse, Grouping groupProperties, Person senderPerson) {
        Advisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupProperties.getEnrolmentEndDay() != null) {
            advisory.setExpires(groupProperties.getEnrolmentEndDay().getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome());

        advisory.setSubject("Realização de Co-Avaliação");

        String msg;
        msg = new String("O Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome() + " criou uma co-avaliação para a disciplina "
                + goalExecutionCourse.getNome() + " relativa ao agrupamento "
                + groupProperties.getName() + "!");

        advisory.setMessage(msg);
        return advisory;
    }

}