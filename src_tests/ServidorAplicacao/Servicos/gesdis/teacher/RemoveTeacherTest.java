/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import java.util.HashMap;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 */
public class RemoveTeacherTest extends TestCaseCreateServices {

	/**
	 * @param testName
	 */
	public RemoveTeacherTest(String testName) {
		super(testName);
		
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "RemoveTeacher";

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		ISuportePersistente sp = null;
		IExecutionYear executionYear =null;
		IExecutionPeriod executionPeriod =null;
		IDisciplinaExecucao executionCourse =null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear =
				ieyp.readExecutionYearByName("2002/2003");
			
			IPersistentExecutionPeriod iepp =
				sp.getIPersistentExecutionPeriod();
			
			executionPeriod =
				iepp.readByNameAndExecutionYear("2º Semestre", executionYear);
			
			IDisciplinaExecucaoPersistente idep =
				sp.getIDisciplinaExecucaoPersistente();
			executionCourse =
				idep.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}

		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
		
		Integer teacherNumber = new Integer(1);
		Object[] testArgs ={infoExecutionCourse,teacherNumber};
		 
		return testArgs;

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente sp = null;
		IExecutionYear executionYear =null;
		IExecutionPeriod executionPeriod =null;
		IDisciplinaExecucao executionCourse =null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear =
				ieyp.readExecutionYearByName("2002/2003");
			
			IPersistentExecutionPeriod iepp =
				sp.getIPersistentExecutionPeriod();
			
			executionPeriod =
				iepp.readByNameAndExecutionYear("2º Semestre", executionYear);
			
			IDisciplinaExecucaoPersistente idep =
				sp.getIDisciplinaExecucaoPersistente();
			executionCourse =
				idep.readByExecutionCourseInitialsAndExecutionPeriod(
					"TFCI",
					executionPeriod);
			
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}

		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
			
		Integer teacherNumber = new Integer(2);
		Object[] testArgs ={infoExecutionCourse,teacherNumber};
		 
		return testArgs;
		
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
	 */
	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	
}
