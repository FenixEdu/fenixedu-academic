package ServidorAplicacao.Servicos.coordinator;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author Fernanda Quitério 20/Nov/2003
 *  
 */
public class LoggedCoordinatorCanEditTest extends ServiceTestCase {

    /**
     * @param testName
     */

    public LoggedCoordinatorCanEditTest(String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "LoggedCoordinatorCanEdit";
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

    protected String[] getAuthenticatedAndAuthorizedUserAnother() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer infoExecutionDegreeCode = new Integer(24);
        Integer infoCurricularCourseCode = new Integer(24);
        String username = new String("user");

        Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, username };
        return args;
    }

    protected Object[] getCurricularCourseUnsuccessfullArguments() {
        Integer infoExecutionDegreeCode = new Integer(24);
        Integer infoCurricularCourseCode = new Integer(15);
        String username = new String("user");

        Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, username };
        return args;
    }

    public void testLoggedCoordinatorCanEditSuccessfull() {
        try {

            Object[] args = getAuthorizeArguments();

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            Boolean canEdit = null;
            canEdit = (Boolean) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args);

            if (canEdit == null) {
                fail("Executing Service LoggedCoordinatorCanEdit with successfull arguments.");
            }

            System.out.println("valor de can edit: " + canEdit);
            //Check information read is correct
            assertTrue(canEdit.booleanValue());

            System.out
                    .println("testLoggedCoordinatorCanEditSuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Executing Service LoggedCoordinatorCanEdit with successfull arguments " + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Executing Service LoggedCoordinatorCanEdit with successfull arguments " + e);
        }
    }

    public void testLoggedCoordinatorCanEditUserUnsuccessfull() {
        try {
            Integer infoExecutionDegreeCode = new Integer(24);
            Integer infoCurricularCourseCode = new Integer(24);
            String username = new String("julia");

            Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, username };

            //Invalid user
            String[] argsUser2 = getAuthenticatedAndUnauthorizedUser();

            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser2);

            Boolean canEdit = null;
            canEdit = (Boolean) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args);

            if (canEdit.equals(Boolean.FALSE)) {
                System.out
                        .println("testLoggedCoordinatorCanEditUserUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Executing Service LoggedCoordinatorCanEdit with invalid user");
            }
        } catch (FenixServiceException e) {
            fail("Executing Service LoggedCoordinatorCanEdit with invalid user " + e);
        } catch (Exception e) {
            fail("Executing Service LoggedCoordinatorCanEdit with invalid user " + e);
        }
    }

    public void testLoggedCoordinatorCanEditNullCCCodeUnsuccessfull() {
        try {
            // null argument
            Integer infoExecutionDegreeCode = new Integer(24);
            String username = new String("user");
            Object[] args = { infoExecutionDegreeCode, null, username };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Executing Service LoggedCoordinatorCanEdit with null curricular course code");
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourseCode")) {
                System.out
                        .println("testLoggedCoordinatorCanEditNullCCCodeUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Executing Service LoggedCoordinatorCanEdit with null curricular course code " + e);
            }
        } catch (Exception e) {
            fail("Executing Service LoggedCoordinatorCanEdit with null curricular course code " + e);
        }
    }

    public void testLoggedCoordinatorCanEditNullEDCodeUnsuccessfull() {
        try {
            // null argument
            Integer infoCurricularCourseCode = new Integer(24);
            String username = new String("user");
            Object[] args = { null, infoCurricularCourseCode, username };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Executing Service LoggedCoordinatorCanEdit with null execution degree code");
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullExecutionDegreeCode")) {
                System.out
                        .println("testLoggedCoordinatorCanEditNullEDCodeUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Executing Service LoggedCoordinatorCanEdit with null execution degree code " + e);
            }
        } catch (Exception e) {
            fail("Executing Service LoggedCoordinatorCanEdit with null execution degree code " + e);
        }
    }

    public void testLoggedCoordinatorCanEditNullUsernameUnsuccessfull() {
        try {
            // null argument
            Integer infoCurricularCourseCode = new Integer(24);
            Integer infoExecutionDegreeCode = new Integer(24);
            Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, null };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Executing Service LoggedCoordinatorCanEdit with null username");
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullUsername")) {
                System.out
                        .println("testLoggedCoordinatorCanEditNullUsernameUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Executing Service LoggedCoordinatorCanEdit with null username " + e);
            }
        } catch (Exception e) {
            fail("Executing Service LoggedCoordinatorCanEdit with null username " + e);
        }
    }

    public void testLoggedCoordinatorCanEditNoCurricularCourseUnsuccessfull() {
        try {
            //				Non existing curricular course
            Object[] args = getCurricularCourseUnsuccessfullArguments();

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Executing Service LoggedCoordinatorCanEdit with non existent curricular course");
        } catch (NonExistingServiceException e) {
            System.out
                    .println("testLoggedCoordinatorCanEditNoCurricularCourseUnsuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Executing Service LoggedCoordinatorCanEdit with non existent curricular course " + e);
        } catch (Exception e) {
            fail("Executing Service LoggedCoordinatorCanEdit with non existent curricular course " + e);
        }
    }

    public void testLoggedCoordinatorCanEditNoCoordinatorEDUnsuccessfull() {
        try {
            Integer infoExecutionDegreeCode = new Integer(10);
            Integer infoCurricularCourseCode = new Integer(1);
            String username = new String("fiado");

            Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, username };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUserAnother();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            Boolean canEdit = null;
            canEdit = (Boolean) ServiceManagerServiceFactory.executeService(id2,
                    getNameOfServiceToBeTested(), args);

            if (canEdit.booleanValue() != Boolean.FALSE.booleanValue()) {
                fail("Executing Service LoggedCoordinatorCanEdit with no coordinator for that execution degree");
            } else {
                System.out
                        .println("testLoggedCoordinatorCanEditNoCoordinatorEDUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            }
        } catch (FenixServiceException e) {
            fail("Executing Service LoggedCoordinatorCanEdit with no coordinator for that execution degree "
                    + e);
        } catch (Exception e) {
            fail("Executing Service LoggedCoordinatorCanEdit with no coordinator for that execution degree "
                    + e);
        }
    }

    public void testLoggedCoordinatorCanEditEDNotCurrentUnsuccessfull() {
        try {
            Integer infoExecutionDegreeCode = new Integer(10);
            Integer infoCurricularCourseCode = new Integer(1);
            String username = new String("user");

            Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode, username };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            Boolean canEdit = null;
            canEdit = (Boolean) ServiceManagerServiceFactory.executeService(id2,
                    getNameOfServiceToBeTested(), args);

            if (canEdit.booleanValue() != Boolean.FALSE.booleanValue()) {
                fail("Executing Service LoggedCoordinatorCanEdit with execution degree not current");
            } else {
                System.out
                        .println("testLoggedCoordinatorCanEditEDNotCurrentUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            }
        } catch (FenixServiceException e) {
            fail("Executing Service LoggedCoordinatorCanEdit with execution degree not current " + e);
        } catch (Exception e) {
            fail("Executing Service LoggedCoordinatorCanEdit with execution degree not current " + e);
        }
    }
}