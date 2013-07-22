package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExecutionCoursesByExecutionPeriod {

    @Atomic
    public static Collection run(String executionPeriodId) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        Collection allExecutionCoursesFromExecutionPeriod = null;
        List<InfoExecutionCourse> allExecutionCourses = null;

        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);

        if (executionSemester == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }
        allExecutionCoursesFromExecutionPeriod = executionSemester.getAssociatedExecutionCourses();

        if (allExecutionCoursesFromExecutionPeriod == null || allExecutionCoursesFromExecutionPeriod.isEmpty()) {
            return allExecutionCoursesFromExecutionPeriod;
        }
        allExecutionCourses = new ArrayList<InfoExecutionCourse>(allExecutionCoursesFromExecutionPeriod.size());
        Iterator iter = allExecutionCoursesFromExecutionPeriod.iterator();
        while (iter.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iter.next();
            allExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }

        return allExecutionCourses;
    }
}