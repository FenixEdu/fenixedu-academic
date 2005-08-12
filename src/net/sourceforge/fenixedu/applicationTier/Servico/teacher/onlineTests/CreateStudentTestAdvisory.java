/*
 * Created on Oct 20, 2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class CreateStudentTestAdvisory implements IService {

    public Integer run(Integer executionCourseId, Integer distributedTestId, String contextPath) throws FenixServiceException, ExcepcaoPersistencia {
        String path = contextPath.replace('\\', '/');
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        // Create Advisory
        IAdvisory advisory = createTestAdvisory(distributedTest, path);

        // Create DistributedTestAdvisory
        IDistributedTestAdvisory distributedTestAdvisory = DomainFactory.makeDistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);

        return advisory.getIdInternal();
    }

    private IAdvisory createTestAdvisory(IDistributedTest distributedTest, String path) {
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");

        IAdvisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"), new Object[] { ((IExecutionCourse) distributedTest
                .getTestScope().getDomainObject()).getNome() }));

        final String beginHour = DateFormatUtils.format(distributedTest.getBeginHour().getTime(), "hh:mm");
        final String beginDate = DateFormatUtils.format(distributedTest.getBeginDate().getTime(), "dd/MM/yyyy");
        final String endHour = DateFormatUtils.format(distributedTest.getEndHour().getTime(), "hh:mm");
        final String endDate = DateFormatUtils.format(distributedTest.getEndDate().getTime(), "dd/MM/yyyy");

        Object[] args = { path, distributedTest.getIdInternal().toString(), beginHour, beginDate, endHour, endDate };
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setSubject(MessageFormat.format(bundle.getString(" message.distributedTest.changeInquirySubject"),
                    new Object[] { distributedTest.getTitle() }));
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.messageChangeInquiry"), args));
        } else {
            advisory.setSubject(MessageFormat.format(bundle.getString("message.distributedTest.subjectChangeDates"), new Object[] { distributedTest
                    .getTitle() }));
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.changeTestSubject"), args));
        }

        advisory.setOnlyShowOnce(new Boolean(false));
        return advisory;
    }
}