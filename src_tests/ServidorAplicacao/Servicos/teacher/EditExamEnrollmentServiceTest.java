package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class EditExamEnrollmentServiceTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public EditExamEnrollmentServiceTest(String name) {
        super(name);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditExamEnrollment";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditExamEnrollmentDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "nmsn", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "13", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Calendar beginDate = Calendar.getInstance();
        beginDate.set(Calendar.YEAR, 2004);
        beginDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        beginDate.set(Calendar.DATE, 15);
        beginDate.set(Calendar.HOUR_OF_DAY, 9);
        beginDate.set(Calendar.MINUTE, 0);
        beginDate.set(Calendar.SECOND, 0);
        beginDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, 2004);
        endDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        endDate.set(Calendar.DATE, 22);
        endDate.set(Calendar.HOUR_OF_DAY, 11);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(beginDate.getTimeInMillis());

        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(endDate.getTimeInMillis());

        Object[] args = { new Integer(24), // executionCourseCode
                new Integer(1), // examCode
                beginDate, endDate, beginTime, endTime };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testEditNonExistingExamEnrollment() {
        Object[] args = getAuthorizeArguments();
        args[1] = new Integer(7); // non-existing exam

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println("testEditNonExistingExamEnrollment was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEditNonExistingExamEnrollment");

        } catch (InvalidArgumentsServiceException ex) {
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/"
                    + "testExpectedTestEditExamEnrollmentDataSet.xml");
            ex.printStackTrace();
            System.out.println("testEditNonExistingExamEnrollment was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testEditNonExistingExamEnrollment was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEditNonExistingExamEnrollment");
        }
    }

    public void testUpdateInvalidEnrollmentBeginDate() {
        Object[] args = getAuthorizeArguments();

        Calendar beginDate = Calendar.getInstance();
        beginDate.set(Calendar.YEAR, 2004);
        beginDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        beginDate.set(Calendar.DATE, 23);
        beginDate.set(Calendar.HOUR_OF_DAY, 9);
        beginDate.set(Calendar.MINUTE, 0);
        beginDate.set(Calendar.SECOND, 0);
        beginDate.set(Calendar.MILLISECOND, 0);
        args[2] = beginDate;

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out
                    .println("testUpdateInvalidEnrollmentBeginDate was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testUpdateInvalidEnrollmentBeginDate");

        } catch (InvalidTimeIntervalServiceException ex) {
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/"
                    + "testExpectedTestEditExamEnrollmentDataSet.xml");
            ex.printStackTrace();
            System.out.println("testUpdateInvalidEnrollmentBeginDate was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out
                    .println("testUpdateInvalidEnrollmentBeginDate was UNSUCCESSFULY runned by class: "
                            + this.getClass().getName());
            fail("testUpdateInvalidEnrollmentBeginDate");
        }
    }

    public void testUpdateInvalidEnrollmentEndDate() {
        Object[] args = getAuthorizeArguments();

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, 2004);
        endDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        endDate.set(Calendar.DATE, 28);
        endDate.set(Calendar.HOUR_OF_DAY, 11);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);
        args[3] = endDate;

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println("testUpdateInvalidEnrollmentEndDate was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testUpdateInvalidEnrollmentEndDate");

        } catch (InvalidTimeIntervalServiceException ex) {
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/"
                    + "testExpectedTestEditExamEnrollmentDataSet.xml");
            ex.printStackTrace();
            System.out.println("testUpdateInvalidEnrollmentEndDate was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testUpdateInvalidEnrollmentEndDate was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testUpdateInvalidEnrollmentEndDate");
        }
    }

    public void testEditExamEnrollment() {
        Boolean result;
        try {
            result = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), getAuthorizeArguments());
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/"
                    + "testExpectedEditExamEnrollmentDataSet.xml");
            assertTrue(result.booleanValue());
            System.out.println("testEditExamEnrollment was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testEditExamEnrollment was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEditExamEnrollment");
        }
    }

}