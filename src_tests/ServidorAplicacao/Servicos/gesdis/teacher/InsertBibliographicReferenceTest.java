/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import java.util.HashMap;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.Servicos.TestCaseCreateServices;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InsertBibliographicReferenceTest extends TestCaseCreateServices {

	public InsertBibliographicReferenceTest(String testName) {
		super(testName);		
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertBibliographicReference";
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		IDisciplinaExecucao executionCourse = null;
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
				InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
				InfoExecutionCourse infoExecutionCourse =
					new InfoExecutionCourse(
						"Trabalho Final de Curso I",
						"TFCI",
						"programa1",
						new Double(1.5),
						new Double(2),
						new Double(1.5),
						new Double(2),
						infoExecutionPeriod);						 
		Object[] args = {infoExecutionCourse, "xpto","pedrorg","ref","2002",new Boolean("false")};																										
		return args;								 
	}
			
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {		
		IDisciplinaExecucao executionCourse = null;
				InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
						InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
						InfoExecutionCourse infoExecutionCourse =
							new InfoExecutionCourse(
								"Trabalho Final de Curso I",
								"TFCI",
								"programa1",
								new Double(1.5),
								new Double(2),
								new Double(1.5),
								new Double(2),
								infoExecutionPeriod);						 
				Object[] args = {infoExecutionCourse, "xpto","pedro","ref","2002",new Boolean("false")};																										
				return args;						
	}
	
	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
