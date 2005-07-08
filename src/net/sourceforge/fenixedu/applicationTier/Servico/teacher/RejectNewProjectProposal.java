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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */
public class RejectNewProjectProposal implements IService {

    public Boolean run(Integer executionCourseId, Integer groupPropertiesId, String rejectorUserName)
            throws FenixServiceException {

        Boolean result = Boolean.FALSE;

        if (groupPropertiesId == null) {
            return result;
        }

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();
            IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = sp
                    .getIPersistentGroupPropertiesExecutionCourse();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties.readByOID(
                    GroupProperties.class, groupPropertiesId);

            if (groupProperties == null) {
                throw new NotAuthorizedException();
            }

            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = persistentGroupPropertiesExecutionCourse
                    .readByIDs(groupPropertiesId, executionCourseId);

            if (groupPropertiesExecutionCourse == null) {
                throw new ExistingServiceException();
            }

            IPerson receiverPerson = persistentTeacher.readTeacherByUsername(rejectorUserName)
                    .getPerson();

            IExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
            groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
            groupPropertiesExecutionCourse.getProposalState().setState(3);
            executionCourse.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
            groupProperties.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);

            persistentGroupPropertiesExecutionCourse.delete(groupPropertiesExecutionCourse);

            List group = new ArrayList();

            List groupPropertiesExecutionCourseList = groupProperties
                    .getGroupPropertiesExecutionCourse();
            Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList
                    .iterator();

            while (iterGroupPropertiesExecutionCourseList.hasNext()) {

                IGroupPropertiesExecutionCourse groupPropertiesExecutionCourseAux = (IGroupPropertiesExecutionCourse) iterGroupPropertiesExecutionCourseList
                        .next();
                if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
                        || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

                    List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();

                    Iterator iterProfessorship = professorships.iterator();
                    while (iterProfessorship.hasNext()) {
                        IProfessorship professorship = (IProfessorship) iterProfessorship.next();
                        ITeacher teacher = professorship.getTeacher();

                        if (!(teacher.getPerson()).equals(receiverPerson)
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
                if (!(teacherAux.getPerson()).equals(receiverPerson)
                        && !group.contains(teacherAux.getPerson())) {
                    group.add(teacherAux.getPerson());
                }
            }

            IPerson senderPerson = groupPropertiesExecutionCourse.getSenderPerson();

            // Create Advisory
            IAdvisory advisory = createRejectAdvisory(executionCourse, senderPerson, receiverPerson,
                    groupPropertiesExecutionCourse);
            sp.getIPersistentAdvisory().simpleLockWrite(advisory);
            for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisory);
                advisory.getPeople().add(person);
            }

            result = Boolean.TRUE;

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.groupPropertiesExecutionCourse.delete");
        }

        return result;
    }

    private IAdvisory createRejectAdvisory(IExecutionCourse executionCourse, IPerson senderPerson,
            IPerson receiverPerson, IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupPropertiesExecutionCourse.getGroupProperties().getEnrolmentEndDay() != null) {
            advisory.setExpires(groupPropertiesExecutionCourse.getGroupProperties().getEnrolmentEndDay()
                    .getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + receiverPerson.getNome() + " da disciplina "
                + executionCourse.getNome());

        advisory.setSubject("Proposta Enviada Rejeitada");

        String msg;
        msg = new String("A proposta de co-avalia��o do agrupamento "
                + groupPropertiesExecutionCourse.getGroupProperties().getName()
                + ", enviada pelo docente " + senderPerson.getNome() + " da disciplina "
                + groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome()
                + " foi rejeitada pelo docente " + receiverPerson.getNome() + " da disciplina "
                + executionCourse.getNome() + "!");

        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }
}