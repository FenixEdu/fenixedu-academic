/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertTest implements IService {

    public Integer run(Integer executionCourseId, String title, String information) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }
        ITestScope testScope = persistentSuport.getIPersistentTestScope().readByDomainObject(ExecutionCourse.class.getName(), executionCourseId);
        if (testScope == null) {
            testScope = new TestScope(ExecutionCourse.class.getName(), executionCourseId);
            persistentSuport.getIPersistentTestScope().simpleLockWrite(testScope);
        }
        ITest test = new Test();
        persistentSuport.getIPersistentTest().simpleLockWrite(test);
        test.setTitle(title);
        test.setInformation(information);
        test.setNumberOfQuestions(new Integer(0));
        Date date = Calendar.getInstance().getTime();
        test.setCreationDate(date);
        test.setLastModifiedDate(date);
        test.setTestScope(testScope);
        return test.getIdInternal();
    }
}