/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.InfoExecutionCourse;
import DataBeans.gesdis.InfoBibliographicReference;
import DataBeans.util.Cloner;
import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DeleteBibliographicReferenceTest
	extends TestCaseDeleteAndEditServices {

	public DeleteBibliographicReferenceTest(String testName) {
		super(testName);
		// TODO Auto-generated constructor stub
	}

	protected String getNameOfServiceToBeTested() {
		return "gesdis.teacher.DeleteBibliographicReference";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		IBibliographicReference biblioRef = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
			executionYear = ieyp.readExecutionYearByName("2002/2003");

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

			IPersistentBibliographicReference ipbr =
				sp.getIPersistentBibliographicReference();
			biblioRef =
				ipbr.readBibliographicReference(
					executionCourse,
					"xpto",
					"pedro",
					"ref",
					"2002");
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}

		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
		InfoBibliographicReference infoBiblioRef =
			Cloner.copyIBibliographicReference2InfoBibliographicReference(
				biblioRef);		
		Object[] testArgs = { infoExecutionCourse, infoBiblioRef };

		return testArgs;		
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
			ISuportePersistente sp = null;
			IExecutionYear executionYear = null;
			IExecutionPeriod executionPeriod = null;
			IDisciplinaExecucao executionCourse = null;
			IBibliographicReference biblioRef = null;
			try {
				sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
				IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
				executionYear = ieyp.readExecutionYearByName("2002/2003");
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
				IPersistentBibliographicReference ipbr =
					sp.getIPersistentBibliographicReference();
				biblioRef = new BibliographicReference();
				biblioRef.setTitle("xpto");
				biblioRef.setAuthors("pedro");
				biblioRef.setReference("ref");
				biblioRef.setYear("2002");				
				biblioRef.setOptional(new Boolean(false));
				biblioRef.setExecutionCourse(executionCourse);					
				sp.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				System.out.println("failed setting up the test data");
				e.printStackTrace();
			}

			InfoExecutionCourse infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
			InfoBibliographicReference infoBiblioRef =
				Cloner.copyIBibliographicReference2InfoBibliographicReference(
					biblioRef);		
			Object[] testArgs = { infoExecutionCourse, infoBiblioRef };

			return testArgs;		
		}


}
