/*
 * Created on Oct 20, 2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.DistributedTestAdvisory;
import Dominio.ExecutionCourse;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IDistributedTestAdvisory;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class CreateStudentTestAdvisory implements IService {

    public CreateStudentTestAdvisory() {
    }

    public Integer run(Integer executionCourseId, Integer distributedTestId, String contextPath)
            throws FenixServiceException {
        String path = contextPath.replace('\\', '/');

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();

            // Create Advisory
            IAdvisory advisory = createTestAdvisory(distributedTest, path);
            persistentSuport.getIPersistentAdvisory().simpleLockWrite(advisory);

            // Create DistributedTestAdvisory
            IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
            persistentSuport.getIPersistentDistributedTestAdvisory().simpleLockWrite(
                    distributedTestAdvisory);
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
        advisory.setSender("Docente da disciplina "
                + ((IExecutionCourse) distributedTest.getTestScope().getDomainObject()).getNome());

        advisory.setSubject(distributedTest.getTitle() + ": Alteração na ficha");
        String msgBeginning;
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
            msgBeginning = new String("Uma pergunta do seu <a href='" + path
                    + "/student/studentTests.do?method=prepareToDoTest&testCode="
                    + distributedTest.getIdInternal()
                    + "'>Questionário</a> foi alterada. Deverá realizar o Questionário");
        else
            msgBeginning = new String("Uma pergunta da sua <a href='" + path
                    + "/student/studentTests.do?method=prepareToDoTest&testCode="
                    + distributedTest.getIdInternal()
                    + "'>Ficha de Trabalho</a> foi alterada. Deverá realizar a Ficha de Trabalho");

        advisory.setMessage(msgBeginning + " até às " + getHourFormatted(distributedTest.getEndHour())
                + " de " + getDateFormatted(distributedTest.getEndDate()));
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