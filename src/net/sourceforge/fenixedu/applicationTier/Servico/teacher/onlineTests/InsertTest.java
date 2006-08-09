package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertTest extends Service {

    public Integer run(Integer executionCourseId, String title, String information) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }
        TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class, executionCourseId);
        if (testScope == null) {
            testScope = new TestScope(ExecutionCourse.class.getName(), executionCourseId);
        }
        Test test = new Test();
        test.setTitle(title);
        test.setInformation(information);
        Date date = Calendar.getInstance().getTime();
        test.setCreationDate(date);
        test.setLastModifiedDate(date);
        test.setTestScope(testScope);
        return test.getIdInternal();
    }

}
