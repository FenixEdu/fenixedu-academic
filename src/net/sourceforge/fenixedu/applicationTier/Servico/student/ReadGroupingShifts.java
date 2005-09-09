/*
 * Created on 28/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */
public class ReadGroupingShifts implements IService {

    public InfoSiteShifts run(Integer groupingCode, Integer studentGroupCode)
            throws FenixServiceException {

        InfoSiteShifts infoSiteShifts = new InfoSiteShifts();
        List infoShifts = new ArrayList();
        IGrouping grouping = null;
        boolean result = false;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudentGroup studentGroup = null;
            grouping = (IGrouping) sp.getIPersistentGrouping().readByOID(
                    Grouping.class, groupingCode);
            if (grouping == null) {
                throw new ExistingServiceException();
            }
            if (studentGroupCode != null) {

                studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOID(
                        StudentGroup.class, studentGroupCode);

                if (studentGroup == null) {
                    throw new InvalidSituationServiceException();
                }

                infoSiteShifts.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
            }

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                    .getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                    .getGroupEnrolmentStrategyInstance(grouping);

            if (strategy.checkHasShift(grouping)) {
                ITurnoPersistente persistentShift = sp.getITurnoPersistente();

                List executionCourses = new ArrayList();
                executionCourses = grouping.getExecutionCourses();

                Iterator iterExecutionCourses = executionCourses.iterator();
                List executionCourseShifts = new ArrayList();
                while (iterExecutionCourses.hasNext()) {
                    IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();

                    List someShifts = persistentShift.readByExecutionCourse(executionCourse
                            .getIdInternal());

                    executionCourseShifts.addAll(someShifts);
                }

                List shifts = strategy.checkShiftsType(grouping, executionCourseShifts);
                if (shifts == null || shifts.isEmpty()) {

                } else {

                    for (int i = 0; i < shifts.size(); i++) {
                        IShift shift = (IShift) shifts.get(i);
                        result = strategy.checkNumberOfGroups(grouping, shift);

                        if (result) {
                            infoShifts.add(InfoShift.newInfoFromDomain(shift));
                        }
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        infoSiteShifts.setShifts(infoShifts);
        return infoSiteShifts;

    }

}