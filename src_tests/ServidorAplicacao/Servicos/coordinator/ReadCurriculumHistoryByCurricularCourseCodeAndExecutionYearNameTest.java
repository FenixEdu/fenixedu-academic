package net.sourceforge.fenixedu.applicationTier.Servicos.coordinator;

import java.util.Calendar;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;

/**
 * @author Fernanda Quitério 18/Nov/2003
 *  
 */
public class ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameTest extends ServiceTestCase {

    /**
     * @param testName
     */

    public ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameTest(String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/coordinator/testDataSetCurriculum.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer infoExecutionDegreeCode = new Integer(10);
        Integer infoCurricularCourseCode = new Integer(1);
        String stringExecutionYear = new String("2002/2003");

        Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, stringExecutionYear };
        return args;
    }

    protected Object[] getCurricularCourseUnsuccessfullArguments() {
        Integer infoExecutionDegreeCode = new Integer(10);
        Integer infoCurricularCourseCode = new Integer(15);
        String stringExecutionYear = new String("2002/2003");

        Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, stringExecutionYear };
        return args;
    }

    protected Object[] getExecutionYearUnsuccessfullArguments() {
        Integer infoExecutionDegreeCode = new Integer(10);
        Integer infoCurricularCourseCode = new Integer(1);
        String stringExecutionYear = new String("2005/2006");

        Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, stringExecutionYear };
        return args;
    }

    public void testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameSuccessfull() {
        try {
            Object[] args = getAuthorizeArguments();

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            InfoCurriculum infoCurriculum = null;
            infoCurriculum = (InfoCurriculum) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args);

            //read something?
            if (infoCurriculum == null) {
                fail("Reading a Curriculum.");
            }

            //Check information read is correct
            assertEquals(new String("objectivos gerais em portugues mais antigo"), infoCurriculum
                    .getGeneralObjectives());
            assertEquals(new String("objectivos gerais em ingles mais antigo"), infoCurriculum
                    .getGeneralObjectivesEn());
            assertEquals(new String("objectivos operacionais em portugues mais antigo"), infoCurriculum
                    .getOperacionalObjectives());
            assertEquals(new String("objectivos operacionais em ingles mais antigo"), infoCurriculum
                    .getOperacionalObjectivesEn());
            assertEquals(new String("programa mais antigo"), infoCurriculum.getProgram());
            assertEquals(new String("programa em ingles mais antigo"), infoCurriculum.getProgramEn());
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(2002, 10 - 1, 10, 0, 0, 0);
            assertEquals(calendar.getTime().getTime(), infoCurriculum.getLastModificationDate()
                    .getTime());

            assertEquals(infoCurriculum.getInfoCurricularCourse().getIdInternal(), new Integer(1));
            assertEquals(new Integer(infoCurriculum.getInfoCurricularCourse().getInfoScopes().size()),
                    new Integer(2));
            assertEquals(new Integer(infoCurriculum.getInfoCurricularCourse()
                    .getInfoAssociatedExecutionCourses().size()), new Integer(2));

            System.out
                    .println("testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameSuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Reading a Curriculum from database " + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Reading a Curriculum from database " + e);
        }
    }

    public void testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameUserUnsuccessfull() {
        try {
            Object[] args = getAuthorizeArguments();

            //Invalid user
            String[] argsUser = getAuthenticatedAndUnauthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

            fail("Reading Curriculum with invalid user");
        } catch (NotAuthorizedException e) {
            System.out
                    .println("testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameUserUnsuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading Curriculum with invalid user " + e);
        } catch (Exception e) {
            fail("Reading Curriculum with invalid user " + e);
        }
    }

    public void testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNoCurricularCourseUnsuccessfull() {
        try {
            //				Non existing curricular course
            Object[] args = getCurricularCourseUnsuccessfullArguments();

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Reading Curriculum with non existent curricular course");
        } catch (NonExistingServiceException e) {
            System.out
                    .println("testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNoCurricularCourseUnsuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading Curriculum with non existent curricular course " + e);
        } catch (Exception e) {
            fail("Reading Curriculum with non existent curricular course " + e);
        }
    }

    public void testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNoExecutionYearUnsuccessfull() {
        try {
            //				Non existing execution year
            Object[] args = getExecutionYearUnsuccessfullArguments();

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Reading Curriculum with non existing execution year");
        } catch (NonExistingServiceException e) {
            System.out
                    .println("testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNoExecutionYearUnsuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading Curriculum with non existing execution year " + e);
        } catch (Exception e) {
            fail("Reading Curriculum with non existing execution year " + e);
        }
    }

    public void testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNullCurriculumUnsuccessfull() {
        try {
            //	existing curricular course and execution year
            Integer infoExecutionDegreeCode = new Integer(10);
            Integer infoCurricularCourseCode = new Integer(2);
            String executionYear = new String("2001/2002");

            Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, executionYear };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            InfoCurriculum infoCurriculum = null;
            infoCurriculum = (InfoCurriculum) ServiceManagerServiceFactory.executeService(id2,
                    getNameOfServiceToBeTested(), args);

            if (infoCurriculum == null) {
                System.out
                        .println("testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNullCurriculumUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Reading Curriculum with non existing curriculum");
            }
        } catch (FenixServiceException e) {
            fail("Reading Curriculum with non existing curriculum " + e);
        } catch (Exception e) {
            fail("Reading Curriculum with non existing curriculum " + e);
        }
    }

    public void testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNullCodeUnsuccessfull() {
        try {
            // null argument
            Integer infoExecutionDegreeCode = new Integer(10);
            String executionYear = new String("3");

            Object[] args = { infoExecutionDegreeCode, null, executionYear };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Reading Curriculum with null curricular course code");
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                System.out
                        .println("testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNullCodeUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Reading Curriculum with null curricular course code " + e);
            }
        } catch (Exception e) {
            fail("Reading Curriculum with null curricular course code " + e);
        }
    }

    public void testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNullNameUnsuccessfull() {
        try {
            // null argument
            Integer infoExecutionDegreeCode = new Integer(10);
            Integer infoCurricularCourseCode = new Integer(2);

            Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, null };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Reading Curriculum with null execution year name");
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullExecutionYearName")) {
                System.out
                        .println("testReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearNameNullNameUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Reading Curriculum with null execution year name " + e);
            }
        } catch (Exception e) {
            fail("Reading Curriculum with null execution year name " + e);
        }
    }
}