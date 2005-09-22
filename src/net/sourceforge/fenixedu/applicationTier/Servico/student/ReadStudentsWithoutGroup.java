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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsWithoutGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentsWithoutGroup implements IService {

	public class NewStudentGroupAlreadyExists extends FenixServiceException {
	}

	public ISiteComponent run(final Integer groupPropertiesCode,
			final String username) throws FenixServiceException,
			ExcepcaoPersistencia {

		final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final IPersistentGrouping persistentGroupProperties = persistentSupport
				.getIPersistentGrouping();

		final InfoSiteStudentsWithoutGroup infoSiteStudentsWithoutGroup = new InfoSiteStudentsWithoutGroup();
		final IGrouping grouping = (IGrouping) persistentGroupProperties
				.readByOID(Grouping.class, groupPropertiesCode);

		if (grouping == null) {
			throw new ExistingServiceException();
		}
		
		final List allStudentsGroups = grouping.getStudentGroups();
			
		final Integer groupNumber = grouping.findMaxGroupNumber() + 1;
		
		infoSiteStudentsWithoutGroup.setGroupNumber(groupNumber);
        infoSiteStudentsWithoutGroup.setInfoGrouping(InfoGrouping.newInfoFromDomain(grouping));

		final List attends = grouping.getAttends();

		IStudent userStudent = null;
		for (final Iterator iterator = attends.iterator(); iterator.hasNext(); ) {
			final IAttends attend = (IAttends) iterator.next();
			final IStudent student = attend.getAluno();
			final IPerson person = student.getPerson();
			if (person.getUsername().equalsIgnoreCase(username)) {
				userStudent = student;
				break;
			}			
		}
		final InfoStudent infoStudent = getInfoStudentFromStudent(userStudent);
		infoSiteStudentsWithoutGroup.setInfoUserStudent(infoStudent);

		if (grouping.getEnrolmentPolicy().equals(
				new EnrolmentGroupPolicyType(2))) {
			return infoSiteStudentsWithoutGroup;
		}


		final Set attendsWithOutGroupsSet = new HashSet(attends);
		for (final Iterator iterator = allStudentsGroups.iterator(); iterator.hasNext(); ) {
			final IStudentGroup studentGroup = (IStudentGroup) iterator.next();
			
            final List allStudentGroupsAttends = studentGroup.getAttends();
			
            for (final Iterator iterator2 = allStudentGroupsAttends.iterator(); iterator2.hasNext(); ) {
				final IAttends studentGroupAttend = (IAttends) iterator2.next();
				attendsWithOutGroupsSet.remove(studentGroupAttend);
			}
		}

		final List infoStudentList = new ArrayList(attendsWithOutGroupsSet.size());
		for (final Iterator iterator = attendsWithOutGroupsSet.iterator(); iterator.hasNext(); ) {
			final IAttends attend = (IAttends) iterator.next();
			final IStudent student = attend.getAluno();

			if (!student.equals(userStudent)) {
				final InfoStudent infoStudent2 = getInfoStudentFromStudent(student);
				infoStudentList.add(infoStudent2);
			}

		}
		infoSiteStudentsWithoutGroup.setInfoStudentList(infoStudentList);

		return infoSiteStudentsWithoutGroup;
	}

	protected InfoStudent getInfoStudentFromStudent(IStudent userStudent) {
		final InfoStudent infoStudent = InfoStudent
				.newInfoFromDomain(userStudent);
		final InfoPerson infoPerson = InfoPerson.newInfoFromDomain(userStudent
				.getPerson());
		infoStudent.setInfoPerson(infoPerson);
		return infoStudent;
	}
}