/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class AssociateTeacherTest extends TestCaseCreateServices {

	/**
	 * @param testName
	 */
	public AssociateTeacherTest(String testName) {
		super(testName);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "AssociateTeacher";
		

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		
		return null;

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentsOfServiceToBeTestedSuccessfuly()
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
			
		Integer teacherNumber = new Integer(5);
		Object[] testArgs ={infoExecutionCourse,teacherNumber};
		 
		return testArgs;

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
	 */
	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		HashMap args = new HashMap();
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

		//The Teacher does not exists

		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
		Integer nonExistingTeacherNumber = new Integer(424322343);
		List testArgs1 =new ArrayList();
		 testArgs1.add(infoExecutionCourse);
		 testArgs1.add(nonExistingTeacherNumber);
		args.put("Non-Existing Teacher Test", testArgs1);

		//The teacher already lectures the execution Course
		InfoExecutionCourse infoExecutionCourse1= Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
		Integer teacherNumber1=new Integer(1);
		List testArgs2 =new ArrayList();
				 testArgs1.add(infoExecutionCourse1);
				 testArgs1.add(teacherNumber1);
	
		args.put(
			"The teacher already lectures the execution Course Test",
			testArgs2);

		return args;

	}

}
