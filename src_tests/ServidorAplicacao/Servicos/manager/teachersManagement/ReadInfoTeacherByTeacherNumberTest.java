package net.sourceforge.fenixedu.applicationTier.Servicos.manager.teachersManagement;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Fernanda Quitério 09/Dez/2003
 *  
 */
public class ReadInfoTeacherByTeacherNumberTest extends ServiceTestCase {

    /**
     * @param testName
     */

    public ReadInfoTeacherByTeacherNumberTest(String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadInfoTeacherByTeacherNumber";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/manager/testDataSetTeacher.xml";
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
        Integer teacherNumber = new Integer(2282);

        Object[] args = { teacherNumber };
        return args;
    }

    protected Object[] getUnsuccessfullArguments() {
        Integer teacherNumber = new Integer(1000);

        Object[] args = { teacherNumber };
        return args;
    }

    public void testReadInfoTeacherByTeacherNumberSuccessfull() {
        try {

            Object[] args = getAuthorizeArguments();

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            InfoTeacher infoTeacher = null;
            infoTeacher = (InfoTeacher) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args);

            //read something?
            if (infoTeacher == null) {
                fail("Reading a Teacher.");
            }

            assertEquals("AS1", infoTeacher.getInfoCategory().getCode());
            assertEquals("Nome da Pessoa", infoTeacher.getInfoPerson().getNome());
            assertEquals("user", infoTeacher.getInfoPerson().getUsername());
            assertEquals(new Integer(2282), infoTeacher.getTeacherNumber());
            assertEquals("2", String.valueOf((infoTeacher.getProfessorShipsExecutionCourses().size())));
            assertEquals("1", String.valueOf((infoTeacher.getResponsibleForExecutionCourses().size())));

            System.out
                    .println("testReadInfoTeacherByTeacherNumberSuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Reading a Teacher from database " + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Reading a Teacher from database " + e);
        }
    }

    public void testReadInfoTeacherByTeacherNumberUserUnsuccessfull() {
        try {
            Object[] args = getAuthorizeArguments();

            //Invalid user
            String[] argsUser = getAuthenticatedAndUnauthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

            fail("Reading Teacher with invalid user");
        } catch (NotAuthorizedException e) {
            System.out
                    .println("testReadInfoTeacherByTeacherNumberUserUnsuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading Teacher with invalid user " + e);
        } catch (Exception e) {
            fail("Reading Teacher with invalid user " + e);
        }
    }

    public void testReadInfoTeacherByTeacherNumberNoTeacherUnsuccessfull() {
        try {
            // Non existing teacher
            Object[] args = getUnsuccessfullArguments();

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Reading Teacher with non existent teacher");
        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("noTeacher")) {
                System.out
                        .println("testReadInfoTeacherByTeacherNumberNoTeacherUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Reading Teacher with non existent teacher");
            }
        } catch (FenixServiceException e) {
            fail("Reading Teacher with non existent teacher " + e);
        } catch (Exception e) {
            fail("Reading Teacher with non existent teacher " + e);
        }
    }

    public void testReadInfoTeacherByTeacherNumberNoPSNorRFUnsuccessfull() {
        try {
            Integer teacherNumber = new Integer(2222);
            Object[] args = { teacherNumber };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Reading Teacher with non existent professorships nor responsibility for");
        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("noPSnorRF")) {
                System.out
                        .println("testReadInfoTeacherByTeacherNumberNoPSNorRFUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Reading Teacher with non existent professorships nor responsibility for");
            }
        } catch (FenixServiceException e) {
            fail("Reading Teacher with non existent professorships nor responsibility for " + e);
        } catch (Exception e) {
            fail("Reading Teacher with non existent professorships nor responsibility for " + e);
        }
    }

    public void testReadInfoTeacherByTeacherNumberNullTeacherNumberUnsuccessfull() {
        try {
            // null argument
            Object[] args = { null };

            //Valid user
            String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
            IUserView id2 = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsUser2);

            ServiceManagerServiceFactory.executeService(id2, getNameOfServiceToBeTested(), args);

            fail("Reading Teacher with null number");
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullTeacherNumber")) {
                System.out
                        .println("testReadInfoTeacherByTeacherNumberNullTeacherNumberUnsuccessfull was SUCCESSFULY runned by service: "
                                + getNameOfServiceToBeTested());
            } else {
                fail("Reading Teacher with null number " + e);
            }
        } catch (Exception e) {
            fail("Reading Teacher with null number " + e);
        }
    }
}