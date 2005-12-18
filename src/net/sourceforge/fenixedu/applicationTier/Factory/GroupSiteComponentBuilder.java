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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
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
		IGrouping groupProperties;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
				.readByOID(ExecutionCourse.class, executionCourseCode);

		List executionCourseProjects = executionCourse.getGroupings();

		projects = new ArrayList();
		Iterator iterator = executionCourseProjects.iterator();

		while (iterator.hasNext()) {

			groupProperties = (IGrouping) iterator.next();

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
		IShift shift = null;
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

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGrouping persistentGrouping = sp.getIPersistentGrouping();
		ITurnoPersistente persistentShift = sp.getITurnoPersistente();

		IGrouping groupProperties = (IGrouping) persistentGrouping.readByOID(Grouping.class,
				groupPropertiesCode);
		IShift shift = (IShift) persistentShift.readByOID(Shift.class, shiftCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		List studentGroups = getStudentGroupsByShiftAndGrouping(groupProperties, shift);
		Iterator iterStudentGroups = studentGroups.iterator();
		while (iterStudentGroups.hasNext()) {

			List studentGroupAttendList = new ArrayList();
			IStudentGroup studentGroup = (IStudentGroup) iterStudentGroups.next();

			studentGroupAttendList = studentGroup.getAttends();

			Iterator iterAttendsList = studentGroupAttendList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;

			IAttends attend = null;

			while (iterAttendsList.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();
				infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

				attend = (IAttends) iterAttendsList.next();

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

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGrouping persistentGrouping = sp.getIPersistentGrouping();
		IGrouping groupProperties = (IGrouping) persistentGrouping.readByOID(Grouping.class,
				groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		List studentGroups = getStudentGroupsWithoutShiftByGrouping(groupProperties);
		Iterator iterStudentGroups = studentGroups.iterator();
		while (iterStudentGroups.hasNext()) {

			List studentGroupAttendList = new ArrayList();
			IStudentGroup studentGroup = (IStudentGroup) iterStudentGroups.next();

			studentGroupAttendList = studentGroup.getAttends();

			Iterator iterAttendsList = studentGroupAttendList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;
			IAttends attend = null;

			while (iterAttendsList.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();
				infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

				attend = (IAttends) iterAttendsList.next();

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

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGrouping persistentGrouping = sp.getIPersistentGrouping();

		IGrouping groupProperties = (IGrouping) persistentGrouping.readByOID(Grouping.class,
				groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		List studentGroups = getAllStudentGroups(groupProperties);
		Iterator iterStudentGroups = studentGroups.iterator();
		while (iterStudentGroups.hasNext()) {

			List studentGroupAttendList = new ArrayList();
			IStudentGroup studentGroup = (IStudentGroup) iterStudentGroups.next();

			studentGroupAttendList = studentGroup.getAttends();

			Iterator iterAttendsList = studentGroupAttendList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;
			IAttends attend = null;

			while (iterAttendsList.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();
				infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

				attend = (IAttends) iterAttendsList.next();

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

	private List getStudentGroupsByShiftAndGrouping(IGrouping groupProperties, IShift shift) {
		List result = new ArrayList();
		List studentGroups = groupProperties.getStudentGroupsWithShift();
		Iterator iter = studentGroups.iterator();
		while (iter.hasNext()) {
			IStudentGroup sg = (IStudentGroup) iter.next();
			if (sg.getShift().equals(shift)) {
				result.add(sg);
			}
		}
		return result;
	}

	private List getStudentGroupsWithoutShiftByGrouping(IGrouping groupProperties) {
		List result = new ArrayList();
		List studentGroups = groupProperties.getStudentGroupsWithoutShift();
		result.addAll(studentGroups);
		return result;
	}

	private List getAllStudentGroups(IGrouping groupProperties) {
		List result = new ArrayList();
		List studentGroups = groupProperties.getStudentGroups();
		result.addAll(studentGroups);
		return result;
	}

	private IShift readShift(Integer shiftCode) throws ExcepcaoInexistente, FenixServiceException,
			ExcepcaoPersistencia {
		IShift shift = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		ITurnoPersistente persistentShift = sp.getITurnoPersistente();

		shift = (IShift) persistentShift.readByOID(Shift.class, shiftCode);

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

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IGrouping grouping = (IGrouping) sp.getIPersistentGrouping().readByOID(Grouping.class,
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

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IGrouping grouping = (IGrouping) sp.getIPersistentGrouping().readByOID(Grouping.class,
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

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IStudentGroup studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOID(
				StudentGroup.class, studentGroupCode);

		List studentGroupAttendList = studentGroup.getAttends();

		studentGroupAttendInformationList = new ArrayList(studentGroupAttendList.size());
		Iterator iter = studentGroupAttendList.iterator();
		InfoSiteStudentInformation infoSiteStudentInformation = null;
		IAttends attend = null;

		while (iter.hasNext()) {

			infoSiteStudentInformation = new InfoSiteStudentInformation();

			attend = (IAttends) iter.next();

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

		IStudentGroup studentGroup = (IStudentGroup) persistentSupport.getIPersistentStudentGroup()
				.readByOID(StudentGroup.class, studentGroupID);
		infoStudentGroup = InfoStudentGroup.newInfoFromDomain(studentGroup);

		return infoStudentGroup;
	}

}
