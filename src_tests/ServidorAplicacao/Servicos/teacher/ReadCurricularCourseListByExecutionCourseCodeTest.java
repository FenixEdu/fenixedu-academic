package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoRole;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServices;
import Util.RoleType;

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

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadCurricularCourseListByExecutionCourseCode";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForGesDis.xml";
	}

	public void readTest() {
		//Exectuion Course 24

		Object args[] = { new Integer(24)};

		List curricularCourseList = new ArrayList();
		
		try {
			curricularCourseList = (List) ServiceManagerServiceFactory.executeService(authorizedUserView(), getNameOfServiceToBeTested(), args);
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
