package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServices;
import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author Tânia
 *  
 */
public class ReadCurricularCourseListByExecutionCourseCodeTest extends TestCaseServices {
    /**
     * @param testName
     */
    public ReadCurricularCourseListByExecutionCourseCodeTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadCurricularCourseListByExecutionCourseCode";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/testDataSetForGesDis.xml";
    }

    public void readTest() {
        //Exectuion Course 24

        Object args[] = { new Integer(24) };

        List curricularCourseList = new ArrayList();

        try {
            curricularCourseList = (List) ServiceManagerServiceFactory.executeService(
                    authorizedUserView(), getNameOfServiceToBeTested(), args);
        } catch (FenixServiceException e) {
            fail("Executing  Service!");
            e.printStackTrace();
        }

        assertEquals("curricularCourseNumber", 1, curricularCourseList.size());
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