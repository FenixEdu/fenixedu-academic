/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DataBeans.InfoPerson;
import DataBeans.gesdis.InfoSite;
import DataBeans.gesdis.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IPessoa;
import Dominio.IProfessorship;
import Dominio.ISite;
import Dominio.ITeacher;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TeacherLoginActionTest extends TestCasePresentationTeacherPortal {

	/**
	 * @param testName
	 */
	public TeacherLoginActionTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		InfoPerson infoPerson = new InfoPerson();
		InfoTeacher infoTeacher = new InfoTeacher();
		ISuportePersistente sp;
		IPessoa person;
		List infoSites = new ArrayList();
		try {
			sp = SuportePersistenteOJB.getInstance();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentTeacher persistentTeacher=sp.getIPersistentTeacher();
			IPersistentSite persistentSite=sp.getIPersistentSite();
			IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
			sp.iniciarTransaccao();
			person = persistentPerson.lerPessoaPorUsername("user");
			sp.confirmarTransaccao();
			infoPerson = Cloner.copyIPerson2InfoPerson(person);
			infoTeacher.setInfoPerson(infoPerson);
			infoTeacher.setTeacherNumber(new Integer(1));
			sp.iniciarTransaccao();
			ITeacher teacher = persistentTeacher.readTeacherByNumber(infoTeacher.getTeacherNumber());
			List professorships = persistentProfessorship.readByTeacher(teacher);
			Iterator iter = professorships.iterator();
			while (iter.hasNext()){
				IProfessorship professorship=(IProfessorship) iter.next();
				IDisciplinaExecucao executionCourse = professorship.getExecutionCourse();
				ISite site = persistentSite.readByExecutionCourse(executionCourse);
				InfoSite infoSite = Cloner.copyISite2InfoSite(site);
				infoSites.add(infoSite);
			}
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
		
				
			
				
		Map result = new HashMap();
		result.put(SessionConstants.INFO_TEACHER,infoTeacher);
		result.put(SessionConstants.INFO_SITES_LIST,infoSites);
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/teacher";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/teacherLogin";
	}

}
