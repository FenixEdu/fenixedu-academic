/*
 * Created on 14/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.InfoBibliographicReference;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
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
public class EditBibliographicReferenceTest
	extends TestCaseDeleteAndEditServices {

	public EditBibliographicReferenceTest(String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "EditBibliographicReference";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente sp = null;
		IExecutionYear executionYear = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao executionCourse = null;
		IBibliographicReference biblioRef = null;
		InfoExecutionCourse infoExecutionCourse = null;
		InfoBibliographicReference infoBibliographicReferenceOld = null;
		InfoBibliographicReference infoBibliographicReferenceNew = null;
		
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
			
			infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);								
			
			IPersistentBibliographicReference ipbr =
							sp.getIPersistentBibliographicReference();
						biblioRef =
							ipbr.readBibliographicReference(
								executionCourse,
								"xpto",
								"pedro",
								"ref",
								"2002");	
																																
			infoBibliographicReferenceOld = Cloner.copyIBibliographicReference2InfoBibliographicReference(biblioRef);
						
			biblioRef.setAuthors("manuel");
						
			infoBibliographicReferenceNew = Cloner.copyIBibliographicReference2InfoBibliographicReference(biblioRef);						
		}
		catch(ExcepcaoPersistencia e) {System.out.println("rebenta"+e.toString());}
			Object[] args = {infoExecutionCourse, infoBibliographicReferenceOld, infoBibliographicReferenceNew};
			return args;
		}

		protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
			return null;
		}
}
