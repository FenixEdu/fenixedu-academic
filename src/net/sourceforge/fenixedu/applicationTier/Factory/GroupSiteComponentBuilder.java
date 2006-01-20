/*
 * Created on 4/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentAndGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author asnr and scpo
 * 
 */
public class GroupSiteComponentBuilder {

	private static GroupSiteComponentBuilder instance = null;

	public GroupSiteComponentBuilder() {
	}

	public static GroupSiteComponentBuilder getInstance() {
		if (instance == null) {
			instance = new GroupSiteComponentBuilder();
		}
		return instance;
	}

	public ISiteComponent getComponent(ISiteComponent component, Integer executionCourseCode,
			Integer groupPropertiesCode, Integer code, Integer shiftCode, Integer value)
			throws FenixServiceException, ExcepcaoPersistencia {

		if (component instanceof InfoSiteProjects) {
			return getInfoSiteProjectsName((InfoSiteProjects) component, executionCourseCode);
		} else if (component instanceof InfoSiteShiftsAndGroups) {
			return getInfoSiteShiftsAndGroups((InfoSiteShiftsAndGroups) component, groupPropertiesCode);

		} else if (component instanceof InfoSiteStudentGroup) {
			return getInfoSiteStudentGroupInformation((InfoSiteStudentGroup) component, code);
		} else if (component instanceof InfoSiteStudentsAndGroups) {
			return getInfoSiteStudentsAndGroups((InfoSiteStudentsAndGroups) component,
					groupPropertiesCode, shiftCode, value);
		}
		return null;
	}

	/**
	 * @param component
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */

	private ISiteComponent getInfoSiteProjectsName(InfoSiteProjects component,
			Integer executionCourseCode) throws FenixServiceException, ExcepcaoPersistencia {

		List infoGroupPropertiesList = readExecutionCourseProjects(executionCourseCode);
		component.setInfoGroupPropertiesList(infoGroupPropertiesList);
		return component;
	}

	public List readExecutionCourseProjects(Integer executionCourseCode) throws ExcepcaoInexistente,
			FenixServiceException, ExcepcaoPersistencia {

		List projects = null;
		Grouping groupProperties;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		ExecutionCourse executionCourse = (ExecutionCourse) persistentSupport.getIPersistentExecutionCourse()
				.readByOID(ExecutionCourse.class, executionCourseCode);

		List executionCourseProjects = executionCourse.getGroupings();

		projects = new ArrayList();
		Iterator iterator = executionCourseProjects.iterator();

		while (iterator.hasNext()) {

			groupProperties = (Grouping) iterator.next();

			InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping
					.newInfoFromDomain(groupProperties);

			projects.add(infoGroupProperties);

		}

		return projects;
	}

	/**
	 * @param component
	 * @param site
	 * @param groupPropertiesCode
	 * @param shiftCode
	 * @return
	 * @throws ExcepcaoPersistencia
	 */

	private ISiteComponent getInfoSiteStudentsAndGroups(InfoSiteStudentsAndGroups component,
			Integer groupPropertiesCode, Integer shiftCode, Integer value) throws FenixServiceException,
			ExcepcaoPersistencia {

		List infoSiteStudentsAndGroups = new ArrayList();
		Shift shift = null;
		if (value.intValue() == 1) {
			infoSiteStudentsAndGroups = readStudentsAndGroupsByShift(groupPropertiesCode, shiftCode);
			shift = readShift(shiftCode);
		}

		if (value.intValue() == 2) {
			infoSiteStudentsAndGroups = readStudentsAndGroupsWithoutShift(groupPropertiesCode);
		}

		if (value.intValue() == 3) {
			infoSiteStudentsAndGroups = readAllStudentsAndGroups(groupPropertiesCode);
		}

		component.setInfoSiteStudentsAndGroupsList(infoSiteStudentsAndGroups);

		if (shift != null) {
			component.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
		}

		return component;
	}

	private List readStudentsAndGroupsByShift(Integer groupPropertiesCode, Integer shiftCode)
			throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

		List infoSiteStudentsAndGroupsList = new ArrayList();

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

		Grouping groupProperties = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);
		Shift shift = (Shift) persistentShift.readByOID(Shift.class, shiftCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		List studentGroups = getStudentGroupsByShiftAndGrouping(groupProperties, shift);
		Iterator iterStudentGroups = studentGroups.iterator();
		while (iterStudentGroups.hasNext()) {

			List studentGroupAttendList = new ArrayList();
			StudentGroup studentGroup = (StudentGroup) iterStudentGroups.next();

			studentGroupAttendList = studentGroup.getAttends();

			Iterator iterAttendsList = studentGroupAttendList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;

			Attends attend = null;

			while (iterAttendsList.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();
				infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

				attend = (Attends) iterAttendsList.next();

				infoSiteStudentAndGroup.setInfoStudentGroup(InfoStudentGroup
						.newInfoFromDomain(studentGroup));

				infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

				infoSiteStudentInformation.setName(attend.getAluno().getPerson().getNome());

				infoSiteStudentInformation.setEmail(attend.getAluno().getPerson().getEmail());

				infoSiteStudentAndGroup.setInfoSiteStudentInformation(infoSiteStudentInformation);

				infoSiteStudentsAndGroupsList.add(infoSiteStudentAndGroup);
			}
		}

		Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator(
				"infoSiteStudentInformation.number"));

		return infoSiteStudentsAndGroupsList;
	}

	private List readStudentsAndGroupsWithoutShift(Integer groupPropertiesCode)
			throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

		List infoSiteStudentsAndGroupsList = new ArrayList();

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		Grouping groupProperties = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		List studentGroups = getStudentGroupsWithoutShiftByGrouping(groupProperties);
		Iterator iterStudentGroups = studentGroups.iterator();
		while (iterStudentGroups.hasNext()) {

			List studentGroupAttendList = new ArrayList();
			StudentGroup studentGroup = (StudentGroup) iterStudentGroups.next();

			studentGroupAttendList = studentGroup.getAttends();

			Iterator iterAttendsList = studentGroupAttendList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;
			Attends attend = null;

			while (iterAttendsList.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();
				infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

				attend = (Attends) iterAttendsList.next();

				infoSiteStudentAndGroup.setInfoStudentGroup(InfoStudentGroup
						.newInfoFromDomain(studentGroup));

				infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

				infoSiteStudentInformation.setName(attend.getAluno().getPerson().getNome());

				infoSiteStudentInformation.setEmail(attend.getAluno().getPerson().getEmail());

				infoSiteStudentAndGroup.setInfoSiteStudentInformation(infoSiteStudentInformation);

				infoSiteStudentsAndGroupsList.add(infoSiteStudentAndGroup);
			}
		}

		Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator(
				"infoSiteStudentInformation.number"));

		return infoSiteStudentsAndGroupsList;
	}

	private List readAllStudentsAndGroups(Integer groupPropertiesCode) throws ExcepcaoInexistente,
			FenixServiceException, ExcepcaoPersistencia {

		List infoSiteStudentsAndGroupsList = new ArrayList();

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		Grouping groupProperties = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		List studentGroups = getAllStudentGroups(groupProperties);
		Iterator iterStudentGroups = studentGroups.iterator();
		while (iterStudentGroups.hasNext()) {

			List studentGroupAttendList = new ArrayList();
			StudentGroup studentGroup = (StudentGroup) iterStudentGroups.next();

			studentGroupAttendList = studentGroup.getAttends();

			Iterator iterAttendsList = studentGroupAttendList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;
			Attends attend = null;

			while (iterAttendsList.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();
				infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

				attend = (Attends) iterAttendsList.next();

				infoSiteStudentAndGroup.setInfoStudentGroup(InfoStudentGroup
						.newInfoFromDomain(studentGroup));

				infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

				infoSiteStudentInformation.setName(attend.getAluno().getPerson().getNome());

				infoSiteStudentInformation.setEmail(attend.getAluno().getPerson().getEmail());

				infoSiteStudentAndGroup.setInfoSiteStudentInformation(infoSiteStudentInformation);

				infoSiteStudentsAndGroupsList.add(infoSiteStudentAndGroup);
			}
		}

		Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator(
				"infoSiteStudentInformation.number"));

		return infoSiteStudentsAndGroupsList;
	}

	private List getStudentGroupsByShiftAndGrouping(Grouping groupProperties, Shift shift) {
		List result = new ArrayList();
		List studentGroups = groupProperties.getStudentGroupsWithShift();
		Iterator iter = studentGroups.iterator();
		while (iter.hasNext()) {
			StudentGroup sg = (StudentGroup) iter.next();
			if (sg.getShift().equals(shift)) {
				result.add(sg);
			}
		}
		return result;
	}

	private List getStudentGroupsWithoutShiftByGrouping(Grouping groupProperties) {
		List result = new ArrayList();
		List studentGroups = groupProperties.getStudentGroupsWithoutShift();
		result.addAll(studentGroups);
		return result;
	}

	private List getAllStudentGroups(Grouping groupProperties) {
		List result = new ArrayList();
		List studentGroups = groupProperties.getStudentGroups();
		result.addAll(studentGroups);
		return result;
	}

	private Shift readShift(Integer shiftCode) throws ExcepcaoInexistente, FenixServiceException,
			ExcepcaoPersistencia {
		Shift shift = null;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

		shift = (Shift) persistentShift.readByOID(Shift.class, shiftCode);

		return shift;
	}

	/**
	 * @param component
	 * @param site
	 * @param groupPropertiesCode
	 * @return
	 * @throws ExcepcaoPersistencia
	 */

	private ISiteComponent getInfoSiteShiftsAndGroups(InfoSiteShiftsAndGroups component,
			Integer groupPropertiesCode) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		Grouping grouping = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		List infoSiteShiftsAndGroups = ReadShiftsAndGroups.run(grouping).getInfoSiteGroupsByShiftList();
		component.setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroups);

		InfoGrouping infoGrouping = readGrouping(groupPropertiesCode);
		component.setInfoGrouping(infoGrouping);

		return component;
	}

	public InfoGrouping readGrouping(Integer groupPropertiesCode) throws FenixServiceException,
			ExcepcaoPersistencia {

		InfoGrouping infoGroupProperties = null;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		Grouping grouping = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		infoGroupProperties = InfoGroupingWithExportGrouping.newInfoFromDomain(grouping);

		return infoGroupProperties;
	}

	/**
	 * @param component
	 * @param site
	 * @param groupPropertiesCode
	 * @return
	 * @throws ExcepcaoPersistencia 
	 */

	private ISiteComponent getInfoSiteStudentGroupInformation(InfoSiteStudentGroup component,
			Integer studentGroupCode) throws FenixServiceException, ExcepcaoPersistencia {

		List infoSiteStudents = readStudentGroupInformation(studentGroupCode);
		component.setInfoSiteStudentInformationList(infoSiteStudents);

		InfoStudentGroup studentGroup = readStudentGroupNumber(studentGroupCode);
		component.setInfoStudentGroup(studentGroup);

		return component;
	}

	public List readStudentGroupInformation(Integer studentGroupCode) throws FenixServiceException,
			ExcepcaoPersistencia {

		List studentGroupAttendInformationList = null;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		StudentGroup studentGroup = (StudentGroup) persistentSupport.getIPersistentObject().readByOID(
				StudentGroup.class, studentGroupCode);

		List studentGroupAttendList = studentGroup.getAttends();

		studentGroupAttendInformationList = new ArrayList(studentGroupAttendList.size());
		Iterator iter = studentGroupAttendList.iterator();
		InfoSiteStudentInformation infoSiteStudentInformation = null;
		Attends attend = null;

		while (iter.hasNext()) {

			infoSiteStudentInformation = new InfoSiteStudentInformation();

			attend = (Attends) iter.next();

			infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

			infoSiteStudentInformation.setName(attend.getAluno().getPerson().getNome());

			infoSiteStudentInformation.setEmail(attend.getAluno().getPerson().getEmail());

			infoSiteStudentInformation.setUsername(attend.getAluno().getPerson().getUsername());

			studentGroupAttendInformationList.add(infoSiteStudentInformation);

		}

		Collections.sort(studentGroupAttendInformationList, new BeanComparator("number"));

		return studentGroupAttendInformationList;
	}

	public InfoStudentGroup readStudentGroupNumber(Integer studentGroupID) throws FenixServiceException, ExcepcaoPersistencia {

		InfoStudentGroup infoStudentGroup = null;

		final ISuportePersistente persistentSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();

		StudentGroup studentGroup = (StudentGroup) persistentSupport.getIPersistentObject()
				.readByOID(StudentGroup.class, studentGroupID);
		infoStudentGroup = InfoStudentGroup.newInfoFromDomain(studentGroup);

		return infoStudentGroup;
	}

}
