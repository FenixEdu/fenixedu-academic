/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

/**
 * @author Susana Fernandes
 */
public class InsertTest extends Service {

    public Integer run(Integer executionCourseId, String title, String information) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }
        TestScope testScope = persistentSupport.getIPersistentTestScope().readByDomainObject(ExecutionCourse.class.getName(), executionCourseId);
        if (testScope == null) {
            testScope = DomainFactory.makeTestScope(ExecutionCourse.class.getName(), executionCourseId);
        }
        Test test = DomainFactory.makeTest();
        test.setTitle(title);
        test.setInformation(information);
        Date date = Calendar.getInstance().getTime();
        test.setCreationDate(date);
        test.setLastModifiedDate(date);
        test.setTestScope(testScope);
        return test.getIdInternal();
    }
}