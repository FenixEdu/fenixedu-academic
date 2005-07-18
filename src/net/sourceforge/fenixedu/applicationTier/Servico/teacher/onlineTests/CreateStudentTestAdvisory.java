/*
 * Created on Oct 20, 2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class CreateStudentTestAdvisory implements IService {

    public CreateStudentTestAdvisory() {
    }

    public Integer run(Integer executionCourseId, Integer distributedTestId, String contextPath) throws FenixServiceException {
        String path = contextPath.replace('\\', '/');

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                    executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();

            // Create Advisory
            IAdvisory advisory = createTestAdvisory(distributedTest, path);
            persistentSuport.getIPersistentAdvisory().simpleLockWrite(advisory);

            // Create DistributedTestAdvisory
            IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
            persistentSuport.getIPersistentDistributedTestAdvisory().simpleLockWrite(distributedTestAdvisory);
            distributedTestAdvisory.setAdvisory(advisory);
            distributedTestAdvisory.setDistributedTest(distributedTest);

            return advisory.getIdInternal();
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
    }

    private IAdvisory createTestAdvisory(IDistributedTest distributedTest, String path) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender("Docente da disciplina " + ((IExecutionCourse) distributedTest.getTestScope().getDomainObject()).getNome());

        advisory.setSubject(distributedTest.getTitle() + ": Alteração na ficha");
        String msgBeginning;
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
            msgBeginning = new String("Uma pergunta do seu <a href='" + path + "/student/studentTests.do?method=prepareToDoTest&testCode="
                    + distributedTest.getIdInternal() + "'>Questionário</a> foi alterada. Deverá realizar o Questionário");
        else
            msgBeginning = new String("Uma pergunta da sua <a href='" + path + "/student/studentTests.do?method=prepareToDoTest&testCode="
                    + distributedTest.getIdInternal() + "'>Ficha de Trabalho</a> foi alterada. Deverá realizar a Ficha de Trabalho");

        advisory.setMessage(msgBeginning + " até às " + getHourFormatted(distributedTest.getEndHour()) + " de "
                + getDateFormatted(distributedTest.getEndDate()));
        advisory.setOnlyShowOnce(new Boolean(false));
        return advisory;
    }

    private String getDateFormatted(Calendar date) {
        String result = new String();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    private String getHourFormatted(Calendar hour) {
        String result = new String();
        result += hour.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (hour.get(Calendar.MINUTE) < 10)
            result += "0";
        result += hour.get(Calendar.MINUTE);
        return result;
    }
}