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
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentAndGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
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
import org.apache.commons.collections.comparators.ComparatorChain;

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
            throws FenixServiceException {

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
     */

    private ISiteComponent getInfoSiteProjectsName(InfoSiteProjects component,
            Integer executionCourseCode) throws FenixServiceException {

        List infoGroupPropertiesList = readExecutionCourseProjects(executionCourseCode);
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    public List readExecutionCourseProjects(Integer executionCourseCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List projects = null;
        IGrouping groupProperties;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseCode);

            List executionCourseProjects = executionCourse.getGroupings();

            projects = new ArrayList();
            Iterator iterator = executionCourseProjects.iterator();

            while (iterator.hasNext()) {

                groupProperties = (IGrouping) iterator.next();

                InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping.newInfoFromDomain(groupProperties);

                projects.add(infoGroupProperties);

            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
        }

        return projects;
    }

    /**
     * @param component
     * @param site
     * @param groupPropertiesCode
     * @param shiftCode
     * @return
     */

    private ISiteComponent getInfoSiteStudentsAndGroups(InfoSiteStudentsAndGroups component,
            Integer groupPropertiesCode, Integer shiftCode, Integer value) throws FenixServiceException {

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
            throws ExcepcaoInexistente, FenixServiceException {

        List infoSiteStudentsAndGroupsList = new ArrayList();
        try {
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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException();
        }
        return infoSiteStudentsAndGroupsList;
    }

    private List readStudentsAndGroupsWithoutShift(Integer groupPropertiesCode)
            throws ExcepcaoInexistente, FenixServiceException {

        List infoSiteStudentsAndGroupsList = new ArrayList();
        try {
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
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }

        return infoSiteStudentsAndGroupsList;
    }

    private List readAllStudentsAndGroups(Integer groupPropertiesCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List infoSiteStudentsAndGroupsList = new ArrayList();
        try {
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
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }

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

    private IShift readShift(Integer shiftCode) throws ExcepcaoInexistente, FenixServiceException {
        IShift shift = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();

            shift = (IShift) persistentShift.readByOID(Shift.class, shiftCode);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException();
        }
        return shift;
    }

    /**
     * @param component
     * @param site
     * @param groupPropertiesCode
     * @return
     */

    private ISiteComponent getInfoSiteShiftsAndGroups(InfoSiteShiftsAndGroups component,
            Integer groupPropertiesCode) throws FenixServiceException {

        List infoSiteShiftsAndGroups = readShiftsAndGroups(groupPropertiesCode);
        component.setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroups);

        InfoGrouping infoGrouping = readGrouping(groupPropertiesCode);
        component.setInfoGrouping(infoGrouping);

        return component;
    }

    public List readShiftsAndGroups(Integer groupPropertiesCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List infoSiteShiftsAndGroups = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();            

            IGrouping grouping = (IGrouping) sp.getIPersistentGrouping().readByOID(
                    Grouping.class, groupPropertiesCode);

            if (grouping == null)
                return null;

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                    .getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                    .getGroupEnrolmentStrategyInstance(grouping);

            if (strategy.checkHasShift(grouping)) {
                infoSiteShiftsAndGroups = new ArrayList();

                Iterator iterExecutionCourses = grouping.getExecutionCourses().iterator();
                List allShifts = new ArrayList();
                while (iterExecutionCourses.hasNext()) {
                    IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
                    List someShifts = persistentShift.readByExecutionCourseAndType(executionCourse
                            .getIdInternal(), grouping.getShiftType());

                    allShifts.addAll(someShifts);
                }

                List allStudentsGroup = grouping.getStudentGroupsWithShift();

                if (allStudentsGroup.size() != 0) {

                    Iterator iterator = allStudentsGroup.iterator();
                    while (iterator.hasNext()) {
                        IShift shift = ((IStudentGroup) iterator.next()).getShift();
                        if (!allShifts.contains(shift)) {
                            allShifts.add(shift);

                        }
                    }
                }

                if (allShifts.size() != 0) {
                    Iterator iter = allShifts.iterator();
                    infoSiteShiftsAndGroups = new ArrayList();
                    InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                    InfoSiteShift infoSiteShift = null;

                    while (iter.hasNext()) {
                        IShift shift = (IShift) iter.next();
                        List allStudentGroups = grouping.readAllStudentGroupsBy(shift);

                        infoSiteShift = new InfoSiteShift();
                        infoSiteShift.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
                        List infoLessons = infoSiteShift.getInfoShift().getInfoLessons();

                        ComparatorChain chainComparator = new ComparatorChain();
                        chainComparator.addComparator(new BeanComparator("diaSemana.diaSemana"));
                        chainComparator.addComparator(new BeanComparator("inicio"));
                        chainComparator.addComparator(new BeanComparator("fim"));
                        chainComparator.addComparator(new BeanComparator("infoSala.nome"));

                        Collections.sort(infoLessons, chainComparator);

                        Iterator iterLessons = infoLessons.iterator();
                        StringBuffer weekDay = new StringBuffer();
                        StringBuffer beginDay = new StringBuffer();
                        StringBuffer endDay = new StringBuffer();
                        StringBuffer room = new StringBuffer();
                        while (iterLessons.hasNext()) {
                            InfoLesson infoLesson = (InfoLesson) iterLessons.next();
                            weekDay.append(infoLesson.getDiaSemana().getDiaSemana());
                            beginDay.append(infoLesson.getInicio().getTimeInMillis());
                            endDay.append(infoLesson.getFim().getTimeInMillis());
                            room.append(infoLesson.getInfoSala().getNome());
                        }

                        infoSiteShift.setOrderByWeekDay(weekDay.toString());
                        infoSiteShift.setOrderByBeginHour(beginDay.toString());
                        infoSiteShift.setOrderByEndHour(endDay.toString());
                        infoSiteShift.setOrderByRoom(room.toString());

                        infoSiteShift.setNrOfGroups(new Integer(allStudentGroups.size()));

                        infoSiteGroupsByShift = new InfoSiteGroupsByShift();
                        infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

                        List infoSiteStudentGroupsList = null;
                        if (allStudentGroups.size() != 0) {
                            infoSiteStudentGroupsList = new ArrayList();
                            Iterator iterGroups = allStudentGroups.iterator();

                            while (iterGroups.hasNext()) {
                                InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                                InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                                infoStudentGroup = InfoStudentGroup
                                        .newInfoFromDomain((IStudentGroup) iterGroups.next());
                                infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                                infoSiteStudentGroupsList.add(infoSiteStudentGroup);
                            }
                            Collections.sort(infoSiteStudentGroupsList, new BeanComparator(
                                    "infoStudentGroup.groupNumber"));

                        }

                        infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                        infoSiteShiftsAndGroups.add(infoSiteGroupsByShift);
                    }
                    /* Sort the list of shifts */

                    ComparatorChain chainComparator = new ComparatorChain();
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.infoShift.tipo"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByWeekDay"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByBeginHour"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByEndHour"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByRoom"));

                    Collections.sort(infoSiteShiftsAndGroups, chainComparator);
                }

                if (!grouping.getStudentGroupsWithoutShift().isEmpty()) {
                    InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                    InfoSiteShift infoSiteShift = new InfoSiteShift();

                    infoSiteGroupsByShift = new InfoSiteGroupsByShift();
                    List allStudentGroups = grouping.getStudentGroupsWithoutShift();

                    infoSiteShift.setNrOfGroups(new Integer(allStudentGroups.size()));

                    infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

                    List infoSiteStudentGroupsList = null;
                    if (allStudentGroups.size() != 0) {
                        infoSiteStudentGroupsList = new ArrayList();
                        Iterator iterGroups = allStudentGroups.iterator();

                        while (iterGroups.hasNext()) {
                            InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                            InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                            infoStudentGroup = InfoStudentGroup
                                    .newInfoFromDomain((IStudentGroup) iterGroups.next());
                            infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                            infoSiteStudentGroupsList.add(infoSiteStudentGroup);
                        }
                    }

                    infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                    infoSiteShiftsAndGroups.add(infoSiteGroupsByShift);

                }

            }

            else {

                infoSiteShiftsAndGroups = new ArrayList();

                if (!grouping.getStudentGroupsWithShift().isEmpty()) {

                    List allShifts = new ArrayList();
                    List allStudentsGroup = grouping.getStudentGroupsWithShift();
                    if (allStudentsGroup.size() != 0) {
                        Iterator iterator = allStudentsGroup.iterator();
                        while (iterator.hasNext()) {
                            IShift shift = ((IStudentGroup) iterator.next()).getShift();
                            if (!allShifts.contains(shift)) {
                                allShifts.add(shift);
                            }
                        }
                    }

                    if (allShifts.size() != 0) {
                        Iterator iter = allShifts.iterator();
                        InfoSiteGroupsByShift infoSiteGroupsByShiftAux = null;
                        InfoSiteShift infoSiteShiftAux = null;

                        while (iter.hasNext()) {
                            IShift shift = (IShift) iter.next();
                            List allStudentGroupsAux = grouping.readAllStudentGroupsBy(shift); 
                                
                            infoSiteShiftAux = new InfoSiteShift();
                            infoSiteShiftAux.setInfoShift(InfoShiftWithInfoLessons
                                    .newInfoFromDomain(shift));
                            List infoLessons = infoSiteShiftAux.getInfoShift().getInfoLessons();
                            ComparatorChain chainComparator = new ComparatorChain();
                            chainComparator.addComparator(new BeanComparator("diaSemana.diaSemana"));
                            chainComparator.addComparator(new BeanComparator("inicio"));
                            chainComparator.addComparator(new BeanComparator("fim"));
                            chainComparator.addComparator(new BeanComparator("infoSala.nome"));
                            Collections.sort(infoLessons, chainComparator);

                            Iterator iterLessons = infoLessons.iterator();
                            StringBuffer weekDay = new StringBuffer();
                            StringBuffer beginDay = new StringBuffer();
                            StringBuffer endDay = new StringBuffer();
                            StringBuffer room = new StringBuffer();
                            while (iterLessons.hasNext()) {
                                InfoLesson infoLesson = (InfoLesson) iterLessons.next();
                                weekDay.append(infoLesson.getDiaSemana().getDiaSemana());
                                beginDay.append(infoLesson.getInicio().getTimeInMillis());
                                endDay.append(infoLesson.getFim().getTimeInMillis());
                                room.append(infoLesson.getInfoSala().getNome());
                            }

                            infoSiteShiftAux.setOrderByWeekDay(weekDay.toString());
                            infoSiteShiftAux.setOrderByBeginHour(beginDay.toString());
                            infoSiteShiftAux.setOrderByEndHour(endDay.toString());
                            infoSiteShiftAux.setOrderByRoom(room.toString());

                            infoSiteShiftAux.setNrOfGroups(new Integer(allStudentGroupsAux.size()));

                            infoSiteGroupsByShiftAux = new InfoSiteGroupsByShift();
                            infoSiteGroupsByShiftAux.setInfoSiteShift(infoSiteShiftAux);
                            List infoSiteStudentGroupsListAux = null;
                            if (allStudentGroupsAux.size() != 0) {
                                infoSiteStudentGroupsListAux = new ArrayList();
                                Iterator iterGroups = allStudentGroupsAux.iterator();
                                while (iterGroups.hasNext()) {
                                    InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                                    InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                                    infoStudentGroup = InfoStudentGroup
                                            .newInfoFromDomain((IStudentGroup) iterGroups.next());
                                    infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                                    infoSiteStudentGroupsListAux.add(infoSiteStudentGroup);
                                }
                                Collections.sort(infoSiteStudentGroupsListAux, new BeanComparator(
                                        "infoStudentGroup.groupNumber"));

                            }
                            infoSiteGroupsByShiftAux
                                    .setInfoSiteStudentGroupsList(infoSiteStudentGroupsListAux);

                            infoSiteShiftsAndGroups.add(infoSiteGroupsByShiftAux);
                        }

                        /* Sort the list of shifts */

                        ComparatorChain chainComparator = new ComparatorChain();
                        chainComparator
                                .addComparator(new BeanComparator("infoSiteShift.infoShift.tipo"));
                        chainComparator
                                .addComparator(new BeanComparator("infoSiteShift.orderByWeekDay"));
                        chainComparator.addComparator(new BeanComparator(
                                "infoSiteShift.orderByBeginHour"));
                        chainComparator
                                .addComparator(new BeanComparator("infoSiteShift.orderByEndHour"));
                        chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByRoom"));

                        Collections.sort(infoSiteShiftsAndGroups, chainComparator);
                    }
                }

                InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                InfoSiteShift infoSiteShift = new InfoSiteShift();

                infoSiteGroupsByShift = new InfoSiteGroupsByShift();

                List allStudentGroups = grouping.getStudentGroupsWithoutShift();

                infoSiteShift.setNrOfGroups(new Integer(allStudentGroups.size()));

                infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

                List infoSiteStudentGroupsList = null;
                if (allStudentGroups.size() != 0) {
                    infoSiteStudentGroupsList = new ArrayList();
                    Iterator iterGroups = allStudentGroups.iterator();

                    while (iterGroups.hasNext()) {
                        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                        InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                        infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups
                                .next());
                        infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                        infoSiteStudentGroupsList.add(infoSiteStudentGroup);

                    }
                }

                infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                infoSiteShiftsAndGroups.add(infoSiteGroupsByShift);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadProjectShifts");
        }

        return infoSiteShiftsAndGroups;
    }

    public InfoGrouping readGrouping(Integer groupPropertiesCode) throws FenixServiceException {

        InfoGrouping infoGroupProperties = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IGrouping grouping = (IGrouping) sp.getIPersistentGrouping().readByOID(
                    Grouping.class, groupPropertiesCode);

            infoGroupProperties = InfoGroupingWithExportGrouping.newInfoFromDomain(grouping);
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new FenixServiceException("error.impossibleReadGroupName");
        }

        return infoGroupProperties;
    }

    /**
     * @param component
     * @param site
     * @param groupPropertiesCode
     * @return
     */

    private ISiteComponent getInfoSiteStudentGroupInformation(InfoSiteStudentGroup component,
            Integer studentGroupCode) throws FenixServiceException {

        List infoSiteStudents = readStudentGroupInformation(studentGroupCode);
        component.setInfoSiteStudentInformationList(infoSiteStudents);

        InfoStudentGroup studentGroup = readStudentGroupNumber(studentGroupCode);
        component.setInfoStudentGroup(studentGroup);

        return component;
    }

    public List readStudentGroupInformation(Integer studentGroupCode) throws FenixServiceException {

        List studentGroupAttendInformationList = null;
        try {
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

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new FenixServiceException("error.impossibleReadStudentGroupInformation");
        }
        return studentGroupAttendInformationList;
    }

    public InfoStudentGroup readStudentGroupNumber(Integer studentGroupID)
            throws FenixServiceException {

        InfoStudentGroup infoStudentGroup = null;
        try {
            final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IStudentGroup studentGroup = (IStudentGroup) persistentSupport.getIPersistentStudentGroup().readByOID(StudentGroup.class, studentGroupID);
            infoStudentGroup = InfoStudentGroup.newInfoFromDomain(studentGroup);

        } catch (ExcepcaoPersistencia ex) {            
            throw new FenixServiceException("error.impossibleReadStudentGroupNumber");
        }
        return infoStudentGroup;
    }

}
