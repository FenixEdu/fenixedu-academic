/*
 * Created on 27/Ago/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentDistributedTests;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTests extends Service {

    public Object run(String userName, Integer executionCourseId) throws ExcepcaoPersistencia {
        InfoSiteStudentDistributedTests infoSite = new InfoSiteStudentDistributedTests();
        Registration student = Registration.readByUsername(userName);
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        Set<DistributedTest> distributedTestList = student.getDistributedTestsByExecutionCourse(executionCourse);
        List<InfoDistributedTest> testToDoList = new ArrayList<InfoDistributedTest>();
        List<InfoDistributedTest> doneTestsList = new ArrayList<InfoDistributedTest>();
        for (DistributedTest distributedTest : distributedTestList) {
            InfoDistributedTest infoDistributedTest = InfoDistributedTest
                    .newInfoFromDomain(distributedTest);
            if (testsToDo(distributedTest)) {
                if (!testToDoList.contains(infoDistributedTest))
                    testToDoList.add(infoDistributedTest);
            } else if (doneTests(distributedTest)) {
                if (!doneTestsList.contains(infoDistributedTest))
                    doneTestsList.add(infoDistributedTest);
            }
        }
        infoSite.setInfoDistributedTestsToDo(testToDoList);
        infoSite.setInfoDoneDistributedTests(doneTestsList);
        return infoSite;
    }

    private boolean testsToDo(DistributedTest distributedTest) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();

        if (dateComparator.compare(calendar, distributedTest.getBeginDate()) >= 0) {
            if (dateComparator.compare(calendar, distributedTest.getBeginDate()) == 0)
                if (hourComparator.compare(calendar, distributedTest.getBeginHour()) < 0)
                    return false;
            if (dateComparator.compare(calendar, distributedTest.getEndDate()) <= 0) {
                if (dateComparator.compare(calendar, distributedTest.getEndDate()) == 0) {
                    if (hourComparator.compare(calendar, distributedTest.getEndHour()) <= 0) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean doneTests(DistributedTest distributedTest) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        if (dateComparator.compare(calendar, distributedTest.getEndDate()) <= 0) {
            if (dateComparator.compare(calendar, distributedTest.getEndDate()) == 0) {
                if (hourComparator.compare(calendar, distributedTest.getEndHour()) <= 0) {
                    return false;
                }

                return true;
            }
            return false;
        }
        return true;
    }
}