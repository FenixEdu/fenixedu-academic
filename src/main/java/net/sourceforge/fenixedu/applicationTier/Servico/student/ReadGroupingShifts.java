/*
 * Created on 28/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author asnr and scpo
 * 
 */
public class ReadGroupingShifts {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static InfoSiteShifts run(String groupingCode, String studentGroupCode) throws FenixServiceException {

        InfoSiteShifts infoSiteShifts = new InfoSiteShifts();
        List infoShifts = new ArrayList();
        Grouping grouping = null;
        boolean result = false;

        StudentGroup studentGroup = null;
        grouping = AbstractDomainObject.fromExternalId(groupingCode);
        if (grouping == null) {
            throw new ExistingServiceException();
        }
        if (studentGroupCode != null) {

            studentGroup = AbstractDomainObject.fromExternalId(studentGroupCode);

            if (studentGroup == null) {
                throw new InvalidSituationServiceException();
            }

            infoSiteShifts.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (strategy.checkHasShift(grouping)) {
            List executionCourses = new ArrayList();
            executionCourses = grouping.getExecutionCourses();

            Iterator iterExecutionCourses = executionCourses.iterator();
            List executionCourseShifts = new ArrayList();
            while (iterExecutionCourses.hasNext()) {
                ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();

                Set<Shift> someShifts = executionCourse.getAssociatedShifts();

                executionCourseShifts.addAll(someShifts);
            }

            List shifts = strategy.checkShiftsType(grouping, executionCourseShifts);
            if (shifts == null || shifts.isEmpty()) {

            } else {

                for (int i = 0; i < shifts.size(); i++) {
                    Shift shift = (Shift) shifts.get(i);
                    result = strategy.checkNumberOfGroups(grouping, shift);

                    if (result) {
                        infoShifts.add(InfoShift.newInfoFromDomain(shift));
                    }
                }
            }
        }

        infoSiteShifts.setShifts(infoShifts);
        return infoSiteShifts;

    }

}