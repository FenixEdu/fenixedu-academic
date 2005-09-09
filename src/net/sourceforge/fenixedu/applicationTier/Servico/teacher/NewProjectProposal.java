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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 *  
 */
public class NewProjectProposal implements IService {

    public Boolean run(Integer objectCode, Integer goalExecutionCourseId, Integer groupPropertiesId,
            String senderPersonUsername) throws FenixServiceException, ExcepcaoPersistencia {

        Boolean result = Boolean.FALSE;

        if (groupPropertiesId == null) {
            return result;
        }
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentGrouping persistentGroupProperties = sp.getIPersistentGrouping();   
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
      
        IGrouping groupProperties = (IGrouping) persistentGroupProperties.readByOID(
                Grouping.class, groupPropertiesId);
        IExecutionCourse goalExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, goalExecutionCourseId);
        IExecutionCourse startExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, objectCode);
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        IPerson senderPerson = persistentTeacher.readTeacherByUsername(senderPersonUsername).getPerson();

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
            IExportGrouping groupPropertiesExecutionCourse = (IExportGrouping) iterRelation
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

        IExportGrouping groupPropertiesExecutionCourse = DomainFactory.makeExportGrouping(
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
            IProfessorship professorship = (IProfessorship) iterProfessorship.next();
            ITeacher teacher = professorship.getTeacher();
            if (!(teacher.getPerson()).equals(senderPerson)) {

                group.add(teacher.getPerson());
            } else {
                acceptProposal = true;
            }
        }

        allOtherProfessors.addAll(group);

        // Create Advisory
        if (acceptProposal == false) {
            IAdvisory advisory = createNewProjectProposalAdvisory(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisory);
                advisory.getPeople().add(person);
            }
        }

        List groupAux = new ArrayList();

        List professorshipsAux = startExecutionCourse.getProfessorships();
        Iterator iterProfessorshipAux = professorshipsAux.iterator();
        while (iterProfessorshipAux.hasNext()) {
            IProfessorship professorshipAux = (IProfessorship) iterProfessorshipAux.next();
            ITeacher teacherAux = professorshipAux.getTeacher();
            IPerson pessoa = teacherAux.getPerson();
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
                IAttends attend = (IAttends) iterAttends.next();
                groupingStudentNumbers.add(attend.getAluno().getNumber());
            }
            
            Iterator iterAttends2 = persistentAttend.readByExecutionCourse(goalExecutionCourse.getIdInternal()).iterator();
            while (iterAttends2.hasNext()) {
                IAttends attend = (IAttends) iterAttends2.next();
                if (!groupingStudentNumbers.contains(attend.getAluno().getNumber())) {
                    groupPropertiesExecutionCourse.getGrouping().addAttends(attend);                    
                }
            }

            IAdvisory advisoryAux = createNewProjectProposalAcceptedAdvisory(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            for (final Iterator iterator = allOtherProfessors.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisoryAux);
                advisoryAux.getPeople().add(person);
            }

        } else {
            IAdvisory advisoryAux = createNewProjectProposalAdvisoryAux(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            for (final Iterator iterator = groupAux.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisoryAux);
                advisoryAux.getPeople().add(person);
            }
        }

        return result;
    }

    private IAdvisory createNewProjectProposalAdvisory(IExecutionCourse goalExecutionCourse,
            IExecutionCourse startExecutionCourse, IGrouping groupProperties, IPerson senderPerson) {
        IAdvisory advisory = DomainFactory.makeAdvisory();
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
                + "<br>Para mais informações dirija-se à área de gestão de grupos da disciplina "
                + goalExecutionCourse.getNome() + ".");

        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }

    private IAdvisory createNewProjectProposalAdvisoryAux(IExecutionCourse goalExecutionCourse,
            IExecutionCourse startExecutionCourse, IGrouping groupProperties, IPerson senderPerson) {
        IAdvisory advisory = DomainFactory.makeAdvisory();
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
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }

    private IAdvisory createNewProjectProposalAcceptedAdvisory(IExecutionCourse goalExecutionCourse,
            IExecutionCourse startExecutionCourse, IGrouping groupProperties, IPerson senderPerson) {
        IAdvisory advisory = DomainFactory.makeAdvisory();
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
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }

}