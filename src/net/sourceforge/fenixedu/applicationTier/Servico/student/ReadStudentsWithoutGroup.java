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
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsWithoutGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
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
		final IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSupport
				.getIPersistentStudentGroupAttend();
		final IPersistentGroupProperties persistentGroupProperties = persistentSupport
				.getIPersistentGroupProperties();

		final InfoSiteStudentsWithoutGroup infoSiteStudentsWithoutGroup = new InfoSiteStudentsWithoutGroup();

		final IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
				.readByOID(GroupProperties.class, groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		final IAttendsSet attendsSet = groupProperties.getAttendsSet();
		final List allStudentsGroups = attendsSet.getStudentGroups();
		
		int nextGroupNumber = 1;
		for (final Iterator iterator = allStudentsGroups.iterator(); iterator.hasNext(); ) {
			final IStudentGroup studentGroup = (IStudentGroup) iterator.next();
			final int studentGroupNumber = studentGroup.getGroupNumber().intValue();
			if (studentGroupNumber > nextGroupNumber) {
				nextGroupNumber = studentGroupNumber;
			}
		}
		final Integer groupNumber = new Integer(nextGroupNumber + 1);
		
//		 if (allStudentsGroups.size() != 0) {
//		 Collections.max() sort(allStudentsGroups, new
//		 BeanComparator("groupNumber"));
//		 Integer lastGroupNumber = ((IStudentGroup) allStudentsGroups
//		 .get(allStudentsGroups.size() - 1)).getGroupNumber();
//		 groupNumber = new Integer(lastGroupNumber.intValue() + 1);
//		
//		 }

//		IStudentGroup newStudentGroup = persistentStudentGroup
//		.readStudentGroupByAttendsSetAndGroupNumber(groupProperties
//				.getAttendsSet(), groupNumber);
//		final IStudentGroup studentGroup = (IStudentGroup) CollectionUtils
//				.find(allStudentsGroups, new Predicate() {
//					public boolean evaluate(Object arg0) {
//						final IStudentGroup studentGroup = (IStudentGroup) arg0;
//						return studentGroup.getAttendsSet().getIdInternal()
//								.equals(attendsSet.getIdInternal())
//								&& studentGroup.getGroupNumber().equals(
//										groupNumber);
//					}
//				});
//
//		if (studentGroup != null) {
//			throw new NewStudentGroupAlreadyExists();
//		}


		infoSiteStudentsWithoutGroup.setGroupNumber(groupNumber);

		final List attends = attendsSet.getAttends();

//		userStudent = persistentStudent.readByUsername(username);
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

		if (groupProperties.getEnrolmentPolicy().equals(
				new EnrolmentGroupPolicyType(2))) {
			return infoSiteStudentsWithoutGroup;
		}


		final Set attendsWithOutGroupsSet = new HashSet(attends);
		for (final Iterator iterator = allStudentsGroups.iterator(); iterator.hasNext(); ) {
			final IStudentGroup studentGroup = (IStudentGroup) iterator.next();
			final List allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);
			for (final Iterator iterator2 = allStudentGroupAttend.iterator(); iterator2.hasNext(); ) {
				final IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) iterator2.next();
				attendsWithOutGroupsSet.remove(studentGroupAttend.getAttend());
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