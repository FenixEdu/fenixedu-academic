package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudents;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServices;
import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadStudentByCurricularCourseTest extends TestCaseServices {
    /**
     * @param testName
     */
    public ReadStudentByCurricularCourseTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadStudentsByCurricularCourse";

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
     */
    //	protected String getDataSetFilePath() {
    //		return "etc/ReadStudentsEnrolledInExam.xml";
    //	}
    public void testSucessfullExecutionWithOutScope() {
        try {
            Integer executionCourseCode = new Integer(25);
            Integer scopeCode = null;

            Object[] args = { executionCourseCode, scopeCode };

            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory
                    .executeService(authorizedUserView(), "ReadStudentsByCurricularCourse", args);

            if (siteView == null) {
                fail("can't execute service");
            }
            InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
            assertTrue("size of List", infoSiteStudents.getStudents().size() == 4);

            //service: insert whith scope
            executionCourseCode = new Integer(25);
            scopeCode = new Integer(3);
            siteView = null;
            infoSiteStudents = null;

            siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    authorizedUserView(), "ReadStudentsByCurricularCourse", args);
            if (siteView == null) {
                fail("can't execute service");
            }

            infoSiteStudents = (InfoSiteStudents) siteView.getComponent();

            assertTrue("size of List", infoSiteStudents.getStudents().size() == 4);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Executing  Service!");
        }
    }

    public void testSucessfullExecutionWithScope() {
        try {
            Integer executionCourseCode = new Integer(25);
            Integer scopeCode = new Integer(3);

            Object[] args = { executionCourseCode, scopeCode };

            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory
                    .executeService(authorizedUserView(), "ReadStudentsByCurricularCourse", args);

            if (siteView == null) {
                fail("can't execute service");
            }
            InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
            assertTrue("size of List", infoSiteStudents.getStudents().size() == 1);

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Executing  Service!");
        }
    }

    public IUserView authorizedUserView() {
        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TEACHER);

        Collection roles = new ArrayList();
        roles.add(infoRole);

        UserView userView = new UserView("user", roles);

        return userView;
    }
}