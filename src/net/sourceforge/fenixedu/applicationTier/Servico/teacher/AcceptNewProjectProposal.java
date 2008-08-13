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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.util.ProposalState;

/**
 * @author joaosa & rmalo
 * 
 */
public class AcceptNewProjectProposal extends Service {

    public Boolean run(Integer executionCourseId, Integer groupPropertiesId, String acceptancePersonUserName)
	    throws FenixServiceException {

	if (groupPropertiesId == null) {
	    return Boolean.FALSE;
	}

	final Grouping grouping = rootDomainObject.readGroupingByOID(groupPropertiesId);
	if (grouping == null) {
	    throw new NotAuthorizedException();
	}

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	final ExportGrouping groupPropertiesExecutionCourse = executionCourse.getExportGrouping(grouping);

	if (groupPropertiesExecutionCourse == null) {
	    throw new ExistingServiceException();
	}

	Person receiverPerson = Teacher.readTeacherByUsername(acceptancePersonUserName).getPerson();

	ExecutionCourse executionCourseAux = groupPropertiesExecutionCourse.getExecutionCourse();
	if (executionCourseAux.getGroupingByName(groupPropertiesExecutionCourse.getGrouping().getName()) != null) {
	    String name = groupPropertiesExecutionCourse.getGrouping().getName();
	    throw new InvalidSituationServiceException(name);
	}

	List attendsStudentNumbers = new ArrayList();
	List attends = groupPropertiesExecutionCourse.getGrouping().getAttends();
	Iterator iterAttendsInAttendsSet = attends.iterator();
	while (iterAttendsInAttendsSet.hasNext()) {
	    Attends attend = (Attends) iterAttendsInAttendsSet.next();
	    attendsStudentNumbers.add(attend.getRegistration().getNumber());
	}

	List attendsAux = executionCourse.getAttends();
	Iterator iterAttends = attendsAux.iterator();
	while (iterAttends.hasNext()) {
	    Attends attend = (Attends) iterAttends.next();
	    if (!attendsStudentNumbers.contains(attend.getRegistration().getNumber()))
		grouping.addAttends(attend);
	}

	Person senderPerson = groupPropertiesExecutionCourse.getSenderPerson();
	List groupPropertiesExecutionCourseList = grouping.getExportGroupings();
	Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();
	List groupTeachers = new ArrayList();
	while (iterGroupPropertiesExecutionCourseList.hasNext()) {
	    ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList.next();
	    if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
		    || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {
		ExecutionCourse personExecutionCourse = groupPropertiesExecutionCourseAux.getExecutionCourse();
		List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();
		Iterator iterProfessorship = professorships.iterator();
		while (iterProfessorship.hasNext()) {
		    Professorship professorship = (Professorship) iterProfessorship.next();
		    Teacher teacher = professorship.getTeacher();
		    if (!(teacher.getPerson()).equals(receiverPerson) && !groupTeachers.contains(teacher.getPerson())) {
			groupTeachers.add(teacher.getPerson());
		    }
		}
	    }
	}

	List groupAux = new ArrayList();
	List professorshipsAux = executionCourse.getProfessorships();

	Iterator iterProfessorshipsAux = professorshipsAux.iterator();
	while (iterProfessorshipsAux.hasNext()) {
	    Professorship professorshipAux = (Professorship) iterProfessorshipsAux.next();
	    Teacher teacherAux = professorshipAux.getTeacher();
	    if (!(teacherAux.getPerson()).equals(receiverPerson)) {
		groupAux.add(teacherAux.getPerson());
	    }
	}

	groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(2)));
	groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);

	return Boolean.TRUE;
    }

}