/*
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
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
	    throws FenixServiceException {

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

	    ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList.next();
	    if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
		    || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

		List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();

		Iterator iterProfessorship = professorships.iterator();
		while (iterProfessorship.hasNext()) {
		    Professorship professorship = (Professorship) iterProfessorship.next();
		    Teacher teacher = professorship.getTeacher();

		    if (!(teacher.getPerson()).equals(receiverPerson) && !group.contains(teacher.getPerson())) {
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
	    if (!(teacherAux.getPerson()).equals(receiverPerson) && !group.contains(teacherAux.getPerson())) {
		group.add(teacherAux.getPerson());
	    }
	}

	Person senderPerson = groupPropertiesExecutionCourse.getSenderPerson();

	groupPropertiesExecutionCourse.delete();

	return Boolean.TRUE;
    }

}