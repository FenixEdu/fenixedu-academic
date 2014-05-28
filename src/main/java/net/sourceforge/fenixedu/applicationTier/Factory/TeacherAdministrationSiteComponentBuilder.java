/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroupAndStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndShiftByStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupWithAttendsAndGroupingAndShift;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quit�rio
 * 
 */
@Deprecated
public class TeacherAdministrationSiteComponentBuilder {

    /**
     * @param common
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    public static InfoSiteCommon getInfoSiteCommon(InfoSiteCommon component, ExecutionCourseSite site)
            throws FenixServiceException {

        final Set<Section> allSections = site.getAssociatedSectionSet();
        final List<InfoSection> infoSectionsList = new ArrayList<InfoSection>(allSections.size());
        for (final Section section : allSections) {
            infoSectionsList.add(InfoSectionWithAll.newInfoFromDomain(section));
        }
        Collections.sort(infoSectionsList);

        component.setTitle(site.getExecutionCourse().getNome());
        component.setMail(site.getMail());
        component.setSections(infoSectionsList);

        final ExecutionCourse executionCourse = site.getExecutionCourse();
        final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
        component.setExecutionCourse(infoExecutionCourse);

        final Collection<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>(curricularCourses.size());
        for (final CurricularCourse curricularCourse : curricularCourses) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }
        component.setAssociatedDegrees(infoCurricularCourses);

        return component;
    }

    public InfoSiteStudentGroup getInfoSiteStudentGroup(InfoSiteStudentGroup component, String studentGroupID)
            throws FenixServiceException {

        final StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupID);
        if (studentGroup == null) {
            return null;
        }

        final List<InfoSiteStudentInformation> infoSiteStudentInformations = new ArrayList<InfoSiteStudentInformation>();
        for (final Attends attend : studentGroup.getAttends()) {
            infoSiteStudentInformations.add(new InfoSiteStudentInformation(attend.getRegistration().getPerson().getName(), attend
                    .getRegistration().getPerson().getEmail(), attend.getRegistration().getPerson().getUsername(), attend
                    .getRegistration().getNumber()));
        }
        Collections.sort(infoSiteStudentInformations, InfoSiteStudentInformation.COMPARATOR_BY_NUMBER);
        component.setInfoSiteStudentInformationList(infoSiteStudentInformations);
        component.setInfoStudentGroup(InfoStudentGroupWithAttendsAndGroupingAndShift.newInfoFromDomain(studentGroup));

        if (studentGroup.getGrouping().getMaximumCapacity() != null) {
            int freeGroups = studentGroup.getGrouping().getMaximumCapacity() - studentGroup.getAttends().size();
            component.setNrOfElements(Integer.valueOf(freeGroups));
        } else {
            component.setNrOfElements("Sem limite");
        }
        return component;
    }

    public InfoSiteStudentGroupAndStudents getInfoSiteStudentGroupAndStudents(InfoSiteStudentGroupAndStudents component,
            String groupPropertiesCode, String shiftCode) throws FenixServiceException {
        List infoSiteStudentsAndShiftByStudentGroupList = readStudentGroupAndStudents(groupPropertiesCode, shiftCode);
        component.setInfoSiteStudentsAndShiftByStudentGroupList(infoSiteStudentsAndShiftByStudentGroupList);

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = readShiftAndGroups(groupPropertiesCode, shiftCode);
        component.setInfoSiteShiftsAndGroups(infoSiteShiftsAndGroups);
        return component;
    }

    private InfoSiteShiftsAndGroups readShiftAndGroups(String groupPropertiesCode, String shiftCode) throws ExcepcaoInexistente,
            FenixServiceException {

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();

        Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCode);
        if (grouping == null) {
            return null;
        }

        Shift shift = FenixFramework.getDomainObject(shiftCode);

        List<InfoSiteGroupsByShift> infoSiteGroupsByShiftList = new ArrayList<InfoSiteGroupsByShift>();
        InfoSiteShift infoSiteShift = new InfoSiteShift();
        infoSiteShift.setInfoShift(InfoShift.newInfoFromDomain(shift));

        List allStudentGroups = grouping.readAllStudentGroupsBy(shift);

        if (grouping.getDifferentiatedCapacity()) {
            Integer vagas = shift.getShiftGroupingProperties().getCapacity();
            infoSiteShift.setNrOfGroups(vagas);
        } else {
            if (grouping.getGroupMaximumNumber() != null) {
                int vagas = grouping.getGroupMaximumNumber().intValue() - allStudentGroups.size();
                infoSiteShift.setNrOfGroups(Integer.valueOf(vagas));
            } else {
                infoSiteShift.setNrOfGroups("Sem limite");
            }
        }
        InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();
        infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

        List<InfoSiteStudentGroup> infoSiteStudentGroupsList = null;
        if (allStudentGroups.size() != 0) {
            infoSiteStudentGroupsList = new ArrayList<InfoSiteStudentGroup>();
            Iterator iterGroups = allStudentGroups.iterator();
            while (iterGroups.hasNext()) {
                InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                infoStudentGroup = InfoStudentGroup.newInfoFromDomain((StudentGroup) iterGroups.next());
                infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                infoSiteStudentGroupsList.add(infoSiteStudentGroup);
            }
            Collections.sort(infoSiteStudentGroupsList, InfoSiteStudentGroup.COMPARATOR_BY_NUMBER);
        }
        infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

        infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);
        infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);

        return infoSiteShiftsAndGroups;
    }

    private List readStudentGroupAndStudents(String groupPropertiesCode, String shiftCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List<InfoSiteStudentsAndShiftByStudentGroup> infoSiteStudentsAndShiftByStudentGroupList =
                new ArrayList<InfoSiteStudentsAndShiftByStudentGroup>();

        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            return null;
        }

        Shift shift = FenixFramework.getDomainObject(shiftCode);

        List<StudentGroup> aux = new ArrayList<StudentGroup>();
        List studentGroupsWithShift = groupProperties.getStudentGroupsWithShift();
        Iterator iterStudentGroupsWithShift = studentGroupsWithShift.iterator();
        while (iterStudentGroupsWithShift.hasNext()) {
            StudentGroup studentGroup = (StudentGroup) iterStudentGroupsWithShift.next();
            if (studentGroup.getShift().equals(shift)) {
                aux.add(studentGroup);
            }
        }
        List<StudentGroup> allStudentGroups = new ArrayList<StudentGroup>();
        allStudentGroups.addAll(groupProperties.getStudentGroupsSet());

        Iterator iterAux = aux.iterator();
        while (iterAux.hasNext()) {
            StudentGroup studentGroup = (StudentGroup) iterAux.next();
            allStudentGroups.remove(studentGroup);
        }

        Iterator iterAllStudentGroups = allStudentGroups.iterator();
        InfoSiteStudentsAndShiftByStudentGroup infoSiteStudentsAndShiftByStudentGroup = null;
        while (iterAllStudentGroups.hasNext()) {
            infoSiteStudentsAndShiftByStudentGroup = new InfoSiteStudentsAndShiftByStudentGroup();

            StudentGroup studentGroup = (StudentGroup) iterAllStudentGroups.next();
            Shift turno = studentGroup.getShift();
            infoSiteStudentsAndShiftByStudentGroup.setInfoStudentGroup(InfoStudentGroup.newInfoFromDomain(studentGroup));
            infoSiteStudentsAndShiftByStudentGroup.setInfoShift(InfoShift.newInfoFromDomain(turno));

            Collection attendsList = studentGroup.getAttends();

            List<InfoSiteStudentInformation> studentGroupAttendInformationList = new ArrayList<InfoSiteStudentInformation>();
            Iterator iterAttendsList = attendsList.iterator();
            InfoSiteStudentInformation infoSiteStudentInformation = null;
            Attends attend = null;

            while (iterAttendsList.hasNext()) {
                infoSiteStudentInformation = new InfoSiteStudentInformation();

                attend = (Attends) iterAttendsList.next();

                infoSiteStudentInformation.setNumber(attend.getRegistration().getNumber());

                studentGroupAttendInformationList.add(infoSiteStudentInformation);

            }

            Collections.sort(studentGroupAttendInformationList, InfoSiteStudentInformation.COMPARATOR_BY_NUMBER);

            infoSiteStudentsAndShiftByStudentGroup.setInfoSiteStudentInformationList(studentGroupAttendInformationList);
            infoSiteStudentsAndShiftByStudentGroupList.add(infoSiteStudentsAndShiftByStudentGroup);

            Collections.sort(infoSiteStudentsAndShiftByStudentGroupList,
                    InfoSiteStudentsAndShiftByStudentGroup.COMPARATOR_BY_NUMBER);

        }

        return infoSiteStudentsAndShiftByStudentGroupList;
    }

    /**
     * @param shifts
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    public InfoSiteShifts getInfoSiteShifts(InfoSiteShifts component, String groupPropertiesCode, String studentGroupCode)
            throws FenixServiceException {
        List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        ExecutionCourse executionCourse = null;

        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            return null;
        }
        if (studentGroupCode != null) {
            StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
            if (studentGroup == null) {
                component.setShifts(null);
                return component;
            }

            component.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (strategy.checkHasShift(groupProperties)) {

            List executionCourses = new ArrayList();
            executionCourses = groupProperties.getExecutionCourses();

            Iterator iterExecutionCourses = executionCourses.iterator();
            List<Shift> shifts = new ArrayList<Shift>();
            while (iterExecutionCourses.hasNext()) {
                ExecutionCourse executionCourse2 = (ExecutionCourse) iterExecutionCourses.next();
                Set<Shift> someShifts = executionCourse2.getAssociatedShifts();

                shifts.addAll(someShifts);
            }

            if (shifts == null || shifts.isEmpty()) {

            } else {
                for (int i = 0; i < shifts.size(); i++) {
                    Shift shift = shifts.get(i);
                    if (strategy.checkShiftType(groupProperties, shift)) {
                        executionCourse = shift.getDisciplinaExecucao();
                        InfoShift infoShift = new InfoShift(shift);
                        infoShifts.add(infoShift);
                    }
                }
            }
        }

        component.setShifts(infoShifts);

        return component;
    }
}