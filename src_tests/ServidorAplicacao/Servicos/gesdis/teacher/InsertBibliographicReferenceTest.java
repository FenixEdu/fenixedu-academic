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
		// TODO Auto-generated constructor stub
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertBibliographicReference";
	}
	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {	
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear("2003");
		infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
		infoExecutionCourse.setLabHours(new Double(45));
		infoExecutionCourse.setNome("so");
		infoExecutionCourse.setPraticalHours(new Double(578));
		infoExecutionCourse.setSigla("so");
		infoExecutionCourse.setTheoPratHours(new Double(57));
		infoExecutionCourse.setTheoreticalHours(new Double(12));					
		Object[] args = {infoExecutionCourse, "xpto2","joao","fr","2007",new Boolean(true)}; 		
		return args; 
		}
			
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {		
		return null;
	}
	
	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}
