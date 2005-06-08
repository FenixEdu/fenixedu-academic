/*
 * Created on 28/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author asnr and scpo
 * 
 */
public class ReadShiftsAndGroups implements IServico {

    private static ReadShiftsAndGroups service = new ReadShiftsAndGroups();

    /**
     * The singleton access method of this class.
     */
    public static ReadShiftsAndGroups getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadShiftsAndGroups() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ReadShiftsAndGroups";
    }

    /**
     * Executes the service.
     */
    public ISiteComponent run(Integer groupPropertiesCode, String username) throws FenixServiceException {

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();
        List infoSiteShiftsAndGroupsList = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();

            IGroupProperties groupProperties = (IGroupProperties) sp.getIPersistentGroupProperties()
                    .readByOID(GroupProperties.class, groupPropertiesCode);

            if (groupProperties == null)
                throw new InvalidSituationServiceException();

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                    .getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                    .getGroupEnrolmentStrategyInstance(groupProperties);

            if (!strategy.checkStudentInAttendsSet(groupProperties, username)) {
                throw new NotAuthorizedException();
            }

            if (strategy.checkHasShift(groupProperties)) {
                infoSiteShiftsAndGroupsList = new ArrayList();
                Iterator iterExecutionCourses = groupProperties.getExecutionCourses().iterator();
                List allShifts = new ArrayList();
                List allShiftsAux = new ArrayList();
                while (iterExecutionCourses.hasNext()) {
                    IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
                    allShiftsAux = persistentShift.readByExecutionCourseAndType(executionCourse
                            .getIdInternal(), groupProperties.getShiftType());
                    allShifts.addAll(allShiftsAux);
                }
                List allStudentsGroup = groupProperties.getAttendsSet().getStudentGroupsWithShift();
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
                    infoSiteShiftsAndGroupsList = new ArrayList();
                    InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                    InfoSiteShift infoSiteShift = null;

                    while (iter.hasNext()) {

                        IShift shift = (IShift) iter.next();
                        List allStudentGroups = persistentStudentGroup
                                .readAllStudentGroupByAttendsSetAndShift(groupProperties.getAttendsSet()
                                        .getIdInternal(), shift.getIdInternal());

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

                        if (groupProperties.getGroupMaximumNumber() != null) {

                            int vagas = groupProperties.getGroupMaximumNumber().intValue()
                                    - allStudentGroups.size();

                            infoSiteShift.setNrOfGroups(new Integer(vagas));

                        } else
                            infoSiteShift.setNrOfGroups("Sem limite");

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

                        infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShift);
                    }
                    /* Sort the list of shifts */

                    ComparatorChain chainComparator = new ComparatorChain();
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.infoShift.tipo"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByWeekDay"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByBeginHour"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByEndHour"));
                    chainComparator.addComparator(new BeanComparator("infoSiteShift.orderByRoom"));

                    Collections.sort(infoSiteShiftsAndGroupsList, chainComparator);
                }

                if (!groupProperties.getAttendsSet().getStudentGroupsWithoutShift().isEmpty()) {
                    InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                    InfoSiteShift infoSiteShift = new InfoSiteShift();

                    infoSiteGroupsByShift = new InfoSiteGroupsByShift();
                    List allStudentGroups = groupProperties.getAttendsSet()
                            .getStudentGroupsWithoutShift();

                    if (groupProperties.getGroupMaximumNumber() != null) {

                        int vagas = groupProperties.getGroupMaximumNumber().intValue()
                                - allStudentGroups.size();
                        infoSiteShift.setNrOfGroups(new Integer(vagas));

                    } else {
                        infoSiteShift.setNrOfGroups("Sem limite");
                    }

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

                    infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShift);

                }
            } else {

                infoSiteShiftsAndGroupsList = new ArrayList();

                if (!groupProperties.getAttendsSet().getStudentGroupsWithShift().isEmpty()) {

                    List allShifts = new ArrayList();
                    List allStudentsGroup = groupProperties.getAttendsSet().getStudentGroupsWithShift();
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
                            List allStudentGroupsAux = persistentStudentGroup
                                    .readAllStudentGroupByAttendsSetAndShift(groupProperties
                                            .getAttendsSet().getIdInternal(), shift.getIdInternal());
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

                            if (groupProperties.getGroupMaximumNumber() != null) {

                                int vagas = groupProperties.getGroupMaximumNumber().intValue()
                                        - allStudentGroupsAux.size();
                                infoSiteShiftAux.setNrOfGroups(new Integer(vagas));
                            } else
                                infoSiteShiftAux.setNrOfGroups("Sem limite");
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

                            infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShiftAux);
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

                        Collections.sort(infoSiteShiftsAndGroupsList, chainComparator);
                    }
                }

                InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                InfoSiteShift infoSiteShift = new InfoSiteShift();

                infoSiteGroupsByShift = new InfoSiteGroupsByShift();

                List allStudentGroups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();

                if (groupProperties.getGroupMaximumNumber() != null) {

                    int vagas = groupProperties.getGroupMaximumNumber().intValue()
                            - allStudentGroups.size();
                    infoSiteShift.setNrOfGroups(new Integer(vagas));

                } else {
                    infoSiteShift.setNrOfGroups("Sem limite");
                }

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

                infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShift);
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadShiftsAndGroups");
        }
        infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroupsList);

        return infoSiteShiftsAndGroups;
    }
}