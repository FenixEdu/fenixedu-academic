package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServices;
import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author Fernanda Quitério
 *  
 */
public class PublishMarksTest extends TestCaseServices {
    /**
     * @param testName
     */
    public PublishMarksTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "PublishMarks";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/testDataSetForPublishMarks.xml";
    }

    public void testSucessfullExecution() {
        try {
            //Service

            Object[] args = { new Integer(3), new Integer(1), "mensagem de publicacao", Boolean.TRUE,
                    "Publicacao de Notas" };

            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory
                    .executeService(authorizedUserView(), getNameOfServiceToBeTested(), args);

            assertEquals("Error in Exam !", ((InfoSiteMarks) siteView.getComponent())
                    .getInfoEvaluation().getIdInternal().intValue(), 1);
            assertEquals("Error in marks list !", ((InfoSiteMarks) siteView.getComponent())
                    .getMarksList().size(), 3);

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