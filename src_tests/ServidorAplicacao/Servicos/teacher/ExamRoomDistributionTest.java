package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ExamRoomDistribution;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */
public class ExamRoomDistributionTest extends ServiceNeedsAuthenticationTestCase {
    /**
     * @param testName
     */
    public ExamRoomDistributionTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ExamRoomDistribution";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "3", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(1), rooms, new Boolean(false), new Boolean(true) };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExamRoomDistributionDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "13", "pass", getApplication() };
        return args;
    }

    public void testSuccessfullEnrolledStudents() {

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            sp.iniciarTransaccao();
            IExam exam = (IExam) persistentExam.readByOID(Exam.class,
                    (Integer) getAuthorizeArguments()[1]);
            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
            List examStudentRoomList = persistentExamStudentRoom.readBy(exam);
            assertEquals(examStudentRoomList.size(), 4);
            List rooms = (List) getAuthorizeArguments()[2];
            Iterator iter = examStudentRoomList.iterator();
            while (iter.hasNext()) {
                IExamStudentRoom examStudentRoom = (IExamStudentRoom) iter.next();
                assertTrue(rooms.contains(examStudentRoom.getRoom().getIdInternal()));
            }
            sp.confirmarTransaccao();
            System.out.println("testReadExecutionCourseExam was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testReadExecutionCourseExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReadExecutionCourseExam");
        }
    }

    public void testSuccessfullNotEnrolledStudents() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(5), rooms, new Boolean(false), new Boolean(false) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            sp.iniciarTransaccao();
            IExam exam = (IExam) persistentExam.readByOID(Exam.class, (Integer) args[1]);
            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
            List examStudentRoomList = persistentExamStudentRoom.readBy(exam);
            assertEquals(examStudentRoomList.size(), 4);
            Iterator iter = examStudentRoomList.iterator();
            while (iter.hasNext()) {
                IExamStudentRoom examStudentRoom = (IExamStudentRoom) iter.next();
                assertTrue(rooms.contains(examStudentRoom.getRoom().getIdInternal()));
            }
            sp.confirmarTransaccao();
            System.out.println("testSuccessfullWithNoEnrollment was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println("testSuccessfullWithNoEnrollment was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testSuccessfullWithNoEnrollment");
        }
    }

    public void testNonExistingRoom() {
        List rooms = new ArrayList();
        rooms.add(new Integer(100));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(1), rooms, new Boolean(false), new Boolean(true) };
        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingRoom was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingRoom");

        } catch (InvalidArgumentsServiceException ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingRoom was SUCCESSFULY runned by service: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingRoom was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testRoomNonExistsInExamAssociatedRooms() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(47));

        Object[] args = { new Integer(27), new Integer(1), rooms, new Boolean(false), new Boolean(true) };
        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingRoom was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingRoom");

        } catch (InvalidArgumentsServiceException ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingRoom was SUCCESSFULY runned by service: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingRoom was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testInsuficientRooms() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));

        Object[] args = { new Integer(27), new Integer(1), rooms, new Boolean(false), new Boolean(true) };
        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingRoom was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingRoom");

        } catch (InvalidArgumentsServiceException ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingRoom was SUCCESSFULY runned by service: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingRoom was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testNonExistingExecutionCourse() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(100), new Integer(1), rooms, new Boolean(false), new Boolean(true) };
        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingExecutionCourse was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingExecutionCourse");

        } catch (NotAuthorizedException ex) {
            ex.printStackTrace();
            System.out.println("testNonAuthenticatedUser was SUCCESSFULY runned by service: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonAuthenticatedUser was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testNonExistingExam() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(100), rooms, new Boolean(false),
                new Boolean(true) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingExam");
        } catch (NotAuthorizedException ex) {
            System.out.println("testNonExistingExam was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingExam was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testExamNotBelongsExecutionCourse() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(2), rooms, new Boolean(false), new Boolean(true) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testExamNotBelongsExecutionCourse was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testExamNotBelongsExecutionCourse");
        } catch (NotAuthorizedException ex) {
            System.out.println("testExamNotBelongsExecutionCourse was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testExamNotBelongsExecutionCourse was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testNonDefinedEnrollmentPeriod() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(6), rooms, new Boolean(false), new Boolean(true) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExam persistentExam = sp.getIPersistentExam();
            sp.iniciarTransaccao();
            IExam exam = (IExam) persistentExam.readByOID(Exam.class, (Integer) args[1]);
            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
            List examStudentRoomList = persistentExamStudentRoom.readBy(exam);
            assertEquals(examStudentRoomList.size(), 4);
            Iterator iter = examStudentRoomList.iterator();
            while (iter.hasNext()) {
                IExamStudentRoom examStudentRoom = (IExamStudentRoom) iter.next();
                assertTrue(rooms.contains(examStudentRoom.getRoom().getIdInternal()));
            }
            sp.confirmarTransaccao();
            System.out.println("testSuccessfullWithNoEnrollment was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println("testSuccessfullWithNoEnrollment was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testSuccessfullWithNoEnrollment");
        }

    }

    public void testOutofEnrollmentPeriod() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(4), rooms, new Boolean(false), new Boolean(true) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testOutofEnrollmentPeriod was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testOutofEnrollmentPeriod");
        } catch (FenixServiceException ex) {
            if (ex.getErrorType() == ExamRoomDistribution.OUT_OF_ENROLLMENT_PERIOD) {
                System.out.println("testOutofEnrollmentPeriod was SUCCESSFULY runned by class: "
                        + this.getClass().getName());
            } else {
                System.out.println("testOutofEnrollmentPeriod was UNSUCCESSFULY runned by class: "
                        + this.getClass().getName());
                fail("testOutofEnrollmentPeriod");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testOutofEnrollmentPeriod was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testDistribute2WeeksBeforeExam() {
        List rooms = new ArrayList();
        rooms.add(new Integer(44));
        rooms.add(new Integer(45));
        rooms.add(new Integer(46));

        Object[] args = { new Integer(27), new Integer(7), rooms, new Boolean(false), new Boolean(false) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testDistribute2WeeksBeforeExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testDistribute2WeeksBeforeExam");
        } catch (FenixServiceException ex) {
            if (ex.getErrorType() == ExamRoomDistribution.OUT_OF_ENROLLMENT_PERIOD) {
                System.out.println("testDistribute2WeeksBeforeExam was SUCCESSFULY runned by class: "
                        + this.getClass().getName());
            } else {
                System.out.println("testDistribute2WeeksBeforeExam was UNSUCCESSFULY runned by class: "
                        + this.getClass().getName());
                fail("testDistribute2WeeksBeforeExam");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testDistribute2WeeksBeforeExam was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }
}