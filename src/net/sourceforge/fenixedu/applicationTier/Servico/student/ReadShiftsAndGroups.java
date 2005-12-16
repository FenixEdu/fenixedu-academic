package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadShiftsAndGroups implements IService {

    public static ISiteComponent run(Integer groupingCode, String username) throws ExcepcaoPersistencia, FenixServiceException {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IGrouping grouping = (IGrouping) sp.getIPersistentGrouping().readByOID(Grouping.class, groupingCode);
        if (grouping == null) {
            throw new InvalidSituationServiceException();
        }

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentInGrouping(grouping, username)) {
            throw new NotAuthorizedException();
        }

        return run(grouping);
    }

    public static InfoSiteShiftsAndGroups run(IGrouping grouping) throws ExcepcaoPersistencia, FenixServiceException {
        final InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();

        final List<InfoSiteGroupsByShift> infoSiteGroupsByShiftList = new ArrayList<InfoSiteGroupsByShift>();
        infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);
        infoSiteShiftsAndGroups.setInfoGrouping(InfoGrouping.newInfoFromDomain(grouping));

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (strategy.checkHasShift(grouping)) {
            for (final IExportGrouping exportGrouping : grouping.getExportGroupings()) {
                final IExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
                for (final IShift shift : executionCourse.getAssociatedShifts()) {
                    if (shift.getTipo() == grouping.getShiftType()) {
                        infoSiteGroupsByShiftList.add(createInfoSiteGroupByShift(shift, grouping));
                    }
                }
            }
            Collections.sort(infoSiteGroupsByShiftList, new BeanComparator("infoSiteShift.infoShift.nome"));

            if (!grouping.getStudentGroupsWithoutShift().isEmpty()) {
                infoSiteGroupsByShiftList.add(createInfoSiteGroupByShift(grouping));
            }
        } else {
            infoSiteGroupsByShiftList.add(createInfoSiteGroupByShift(grouping));
        }

        return infoSiteShiftsAndGroups;

    }

    private static InfoSiteGroupsByShift createInfoSiteGroupByShift(final IShift shift, final IGrouping grouping) {
        final InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();

        final InfoSiteShift infoSiteShift = new InfoSiteShift();
        infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
        infoSiteShift.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
        Collections.sort(infoSiteShift.getInfoShift().getInfoLessons());
        final List<IStudentGroup> studentGroups = grouping.readAllStudentGroupsBy(shift);
        infoSiteShift.setNrOfGroups(calculateVacancies(grouping.getGroupMaximumNumber(), studentGroups.size()));
        
        infoSiteGroupsByShift.setInfoSiteStudentGroupsList(createInfoStudentGroupsList(studentGroups));

        return infoSiteGroupsByShift;
    }

    private static InfoSiteGroupsByShift createInfoSiteGroupByShift(final IGrouping grouping) {
        final InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();

        final InfoSiteShift infoSiteShift = new InfoSiteShift();
        infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
        final List<IStudentGroup> studentGroups = grouping.getStudentGroupsWithoutShift();
        infoSiteShift.setNrOfGroups(calculateVacancies(grouping.getGroupMaximumNumber(), studentGroups.size()));
        
        infoSiteGroupsByShift.setInfoSiteStudentGroupsList(createInfoStudentGroupsList(studentGroups));

        return infoSiteGroupsByShift;
    }

    private static Object calculateVacancies(Integer groupMaximumNumber, int studentGroupsCount) {
        return (groupMaximumNumber != null) ?
            Integer.valueOf((groupMaximumNumber.intValue() - studentGroupsCount))
            : "Sem limite";
    }

    private static List<InfoSiteStudentGroup> createInfoStudentGroupsList(final List<IStudentGroup> studentGroups) {
        final List<InfoSiteStudentGroup> infoSiteStudentGroups = new ArrayList<InfoSiteStudentGroup>();
        for (final IStudentGroup studentGroup : studentGroups) {
            final InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
            infoSiteStudentGroup.setInfoStudentGroup(InfoStudentGroup.newInfoFromDomain(studentGroup));
            infoSiteStudentGroups.add(infoSiteStudentGroup);
        }
        Collections.sort(infoSiteStudentGroups, new BeanComparator("infoStudentGroup.groupNumber"));
        return infoSiteStudentGroups;
    }

}