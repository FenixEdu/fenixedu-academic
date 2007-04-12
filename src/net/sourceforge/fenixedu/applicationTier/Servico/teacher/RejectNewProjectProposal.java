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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa & rmalo
 * 
 */
public class RejectNewProjectProposal extends Service {

	public Boolean run(Integer executionCourseId, Integer groupPropertiesId, String rejectorUserName)
			throws FenixServiceException, ExcepcaoPersistencia {

		if (groupPropertiesId == null) {
			return Boolean.FALSE;
		}
		
		final Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesId);
		if (groupProperties == null) {
			throw new NotAuthorizedException();
		}
        
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
		final ExportGrouping groupPropertiesExecutionCourse = executionCourse.getExportGrouping(groupProperties);
		if (groupPropertiesExecutionCourse == null) {
			throw new ExistingServiceException();
		}

		final Person receiverPerson = Teacher.readTeacherByUsername(rejectorUserName).getPerson();
		groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
		groupPropertiesExecutionCourse.getProposalState().setState(3);

		List group = new ArrayList();

		List groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
		Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();

		while (iterGroupPropertiesExecutionCourseList.hasNext()) {

			ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList
					.next();
			if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
					|| groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

				List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse()
						.getProfessorships();

				Iterator iterProfessorship = professorships.iterator();
				while (iterProfessorship.hasNext()) {
					Professorship professorship = (Professorship) iterProfessorship.next();
					Teacher teacher = professorship.getTeacher();

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
			Professorship professorshipAux = (Professorship) iterProfessorshipsAux.next();
			Teacher teacherAux = professorshipAux.getTeacher();
			if (!(teacherAux.getPerson()).equals(receiverPerson)
					&& !group.contains(teacherAux.getPerson())) {
				group.add(teacherAux.getPerson());
			}
		}

		Person senderPerson = groupPropertiesExecutionCourse.getSenderPerson();

		// Create Advisory
		Advisory advisory = createRejectAdvisory(executionCourse, senderPerson, receiverPerson,
				groupPropertiesExecutionCourse);
		for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
			final Person person = (Person) iterator.next();

			person.getAdvisories().add(advisory);
			advisory.getPeople().add(person);
		}
        
        groupPropertiesExecutionCourse.delete();

		return Boolean.TRUE;
	}

	private Advisory createRejectAdvisory(ExecutionCourse executionCourse, Person senderPerson,
			Person receiverPerson, ExportGrouping groupPropertiesExecutionCourse) {
		Advisory advisory = new Advisory();
		advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
		if (groupPropertiesExecutionCourse.getGrouping().getEnrolmentEndDay() != null) {
			advisory.setExpires(groupPropertiesExecutionCourse.getGrouping().getEnrolmentEndDay()
					.getTime());
		} else {
			advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
		}
		advisory.setSender("Docente " + receiverPerson.getName() + " da disciplina "
				+ executionCourse.getNome());

		advisory.setSubject("Proposta Enviada Rejeitada");

		String msg;
		msg = new String("A proposta de co-avaliação do agrupamento "
				+ groupPropertiesExecutionCourse.getGrouping().getName() + ", enviada pelo docente "
				+ senderPerson.getName() + " da disciplina "
				+ groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome()
				+ " foi rejeitada pelo docente " + receiverPerson.getName() + " da disciplina "
				+ executionCourse.getNome() + "!");

		advisory.setMessage(msg);
		return advisory;
	}
}