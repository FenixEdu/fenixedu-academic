/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsWithoutGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentsWithoutGroup extends Service {

    public class NewStudentGroupAlreadyExists extends FenixServiceException {
    }

    public ISiteComponent run(final Integer groupPropertiesCode, final String username)
	    throws FenixServiceException, ExcepcaoPersistencia {

	final InfoSiteStudentsWithoutGroup infoSiteStudentsWithoutGroup = new InfoSiteStudentsWithoutGroup();
	final Grouping grouping = rootDomainObject.readGroupingByOID(groupPropertiesCode);
	if (grouping == null) {
	    throw new ExistingServiceException();
	}

	final List allStudentsGroups = grouping.getStudentGroups();

	final Integer groupNumber = grouping.findMaxGroupNumber() + 1;

	infoSiteStudentsWithoutGroup.setGroupNumber(groupNumber);
	infoSiteStudentsWithoutGroup.setInfoGrouping(InfoGrouping.newInfoFromDomain(grouping));

	final List<Attends> attends = grouping.getAttends();

	Registration userStudent = null;
	for (final Iterator iterator = attends.iterator(); iterator.hasNext();) {
	    final Attends attend = (Attends) iterator.next();
	    final Registration registration = attend.getAluno();
	    final Person person = registration.getPerson();
	    if (person.hasUsername(username)) {
		userStudent = registration;
		break;
	    }
	}
	final InfoStudent infoStudent = getInfoStudentFromStudent(userStudent);
	infoSiteStudentsWithoutGroup.setInfoUserStudent(infoStudent);

	if (grouping.getEnrolmentPolicy().equals(new EnrolmentGroupPolicyType(2))) {
	    return infoSiteStudentsWithoutGroup;
	}

	final Set<Attends> attendsWithOutGroupsSet = new HashSet<Attends>(attends);
	for (final Iterator iterator = allStudentsGroups.iterator(); iterator.hasNext();) {
	    final StudentGroup studentGroup = (StudentGroup) iterator.next();

	    final List allStudentGroupsAttends = studentGroup.getAttends();

	    for (final Iterator iterator2 = allStudentGroupsAttends.iterator(); iterator2.hasNext();) {
		final Attends studentGroupAttend = (Attends) iterator2.next();
		attendsWithOutGroupsSet.remove(studentGroupAttend);
	    }
	}

	final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>(attendsWithOutGroupsSet
		.size());
	for (final Iterator iterator = attendsWithOutGroupsSet.iterator(); iterator.hasNext();) {
	    final Attends attend = (Attends) iterator.next();
	    final Registration registration = attend.getAluno();

	    if (!registration.equals(userStudent)) {
		final InfoStudent infoStudent2 = getInfoStudentFromStudent(registration);
		infoStudentList.add(infoStudent2);
	    }

	}
	infoSiteStudentsWithoutGroup.setInfoStudentList(infoStudentList);

	return infoSiteStudentsWithoutGroup;
    }

    protected InfoStudent getInfoStudentFromStudent(Registration userStudent) {
	return InfoStudent.newInfoFromDomain(userStudent);
    }
}