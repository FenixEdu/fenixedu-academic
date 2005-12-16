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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteProjectProposal implements IService {

    public boolean run(Integer objectCode, Integer groupPropertiesCode, Integer executionCourseCode,
            String withdrawalPersonUsername) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentGrouping persistentGrouping = persistentSupport.getIPersistentGrouping();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        IPersistentExportGrouping persistentExportGrouping = persistentSupport.getIPersistentExportGrouping();
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

        IPerson withdrawalPerson = persistentTeacher.readTeacherByUsername(withdrawalPersonUsername).getPerson();
        IGrouping groupProperties = (IGrouping) persistentGrouping.readByOID(Grouping.class, groupPropertiesCode);
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseCode);
        IExecutionCourse startExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, objectCode);

        if (groupProperties == null) {
            throw new InvalidArgumentsServiceException("error.noGroupProperties");
        }
        if (executionCourse == null || startExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noExecutionCourse");
        }

        IExportGrouping groupingExecutionCourse = persistentExportGrouping
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

            IExportGrouping groupPropertiesExecutionCourseAux = (IExportGrouping) iterGroupPropertiesExecutionCourseList
                    .next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
                    || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

                List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();

                Iterator iterProfessorship = professorships.iterator();
                while (iterProfessorship.hasNext()) {
                    IProfessorship professorship = (IProfessorship) iterProfessorship.next();
                    ITeacher teacher = professorship.getTeacher();

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
            IProfessorship professorshipAux = (IProfessorship) iterProfessorshipsAux.next();
            ITeacher teacherAux = professorshipAux.getTeacher();
            if (!(teacherAux.getPerson()).equals(withdrawalPerson)
                    && !group.contains(teacherAux.getPerson())) {
                group.add(teacherAux.getPerson());
            }
        }

        // Create Advisory
        IAdvisory advisory = createDeleteProjectProposalAdvisory(executionCourse, startExecutionCourse,
                withdrawalPerson, groupingExecutionCourse, groupProperties);
        for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
            final IPerson person = (IPerson) iterator.next();
            persistentSupport.getIPessoaPersistente().simpleLockWrite(person);

            person.getAdvisories().add(advisory);
            advisory.getPeople().add(person);
        }

        return true;
    }

    private IAdvisory createDeleteProjectProposalAdvisory(IExecutionCourse goalExecutionCourse,
            IExecutionCourse startExecutionCourse, IPerson withdrawalPerson,
            IExportGrouping groupPropertiesExecutionCourse, IGrouping grouping) {
        IAdvisory advisory = DomainFactory.makeAdvisory();
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