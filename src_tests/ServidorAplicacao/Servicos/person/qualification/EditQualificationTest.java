/*
 * Created on 05/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.person.qualification;

import DataBeans.InfoPerson;
import DataBeans.person.InfoQualification;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditQualificationTest
	extends QualificationServiceNeedsAuthenticationTestCase {

	public EditQualificationTest(java.lang.String testName) {
		super(testName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditQualification";
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/person/qualification/testEditQualificationDataSet.xml";
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_GrantOwnerManager()
	 */
	protected String[] getAuthorizedUserGrantOwnerManager() {
		String[] args = { "user_gom", "pass", getApplication()};
		return args;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_Teacher()
	 */
	protected String[] getAuthorizedUserTeacher() {
		String[] args = { "user_t", "pass", getApplication()};
		return args;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
	 */
	protected String[] getUnauthorizedUser() {
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_GrantOwnerManager()
	 */
	protected Object[] getAuthorizeArgumentsGrantOwnerManager() {
		//Qualificação de um bolseiro
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(1));
		info.setQualificationMark("mark");
		info.setQualificationSchool("tagus");
		info.setQualificationTitle("title");
		info.setQualificationYear(new Integer(2001));
		info.setPersonInfo(getInfoPersonGO());

		Integer infoManagerPersonCode = new Integer(17);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_Teacher()
	 */
	protected Object[] getAuthorizeArgumentsTeacher() {
		//Qualificação de um professor
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(1));
		info.setQualificationMark("mark");
		info.setQualificationSchool("tagus");
		info.setQualificationTitle("Sr. Dr. Eng.");
		info.setQualificationYear(new Integer(2001));
		info.setPersonInfo(getInfoPersonT());

		Integer infoManagerPersonCode = new Integer(18);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_GrantOwnerManager()
	 */
	protected Object[] getAuthorizeArgumentsCreateQualificationGrantOwner() {
		//Qualificação de um bolseiro
		InfoQualification info = new InfoQualification();
		info.setQualificationMark("mark");
		info.setQualificationSchool("tagus");
		info.setQualificationTitle("title");
		info.setQualificationYear(new Integer(2001));
		info.setPersonInfo(getInfoPersonGO());

		Integer infoManagerPersonCode = new Integer(17);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	protected Object[] getAuthorizeArgumentsCreateQualificationTeacher() {
		//Qualificação de um professor
		InfoQualification info = new InfoQualification();
		info.setQualificationMark("mark");
		info.setQualificationSchool("tagus");
		info.setQualificationTitle("Sr. Dr. Eng.");
		info.setQualificationYear(new Integer(2001));
		info.setPersonInfo(getInfoPersonT());

		Integer infoManagerPersonCode = new Integer(18);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	protected Object[] getAuthorizeArgumentsEditQualificationGrantOwner() {
		//Qualificação de um bolseiro já existente

		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(2));
		info.setQualificationSchool("NewSchool");
		info.setQualificationYear(new Integer(2003));
		info.setPersonInfo(getInfoPersonGO());
		Integer infoManagerPersonCode = new Integer(17);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	protected Object[] getAuthorizeArgumentsEditQualificationTeacher() {
		//Qualificação de um professor já existente

		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(1));
		info.setQualificationYear(new Integer(2000));
		info.setQualificationSchool("tagus");
		info.setPersonInfo(getInfoPersonT());
		Integer infoManagerPersonCode = new Integer(18);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	/** **************************** INICIO DOS TESTES******************* */

	/*
	 * Um Grant Owner Manager tenta criar com sucesso uma qualificação a um
	 * Grant Owner
	 */
	public void testCreateQualificationGOMSuccessfull() {
		try {
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsCreateQualificationGrantOwner();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditCreateGOMQualificationSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Creating a new Qualification for GrantOwner: " + e);
		} catch (Exception e) {
			fail("Creating a new Qualification for GrantOwner: " + e);
		}
	}

	/*
	 * Um Grant Owner Manager tenta editar com sucesso uma qualificação de um
	 * Grant Owner
	 */
	public void testEditQualificationGOMSuccessfull() {
		try {
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsEditQualificationGrantOwner();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationGOMSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Editing a Qualification for a GrantOwner: " + e);
		} catch (Exception e) {
			fail("Editing a Qualification for a GrantOwner: " + e);
		}
	}

	/*
	 * Um professor tenta criar com sucesso uma qualificação sua.
	 */
	public void testCreateQualificationTSuccessfull() {
		try {
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsCreateQualificationTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditCreateTQualificationSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Creating a new Qualification for Teacher: " + e);
		} catch (Exception e) {
			fail("Creating a new Qualification for Teacher: " + e);
		}
	}

	/*
	 * Um professor tenta editar com sucesso uma qualificação sua
	 */
	public void testEditQualificationTSuccessfull() {
		try {
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsEditQualificationTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationTSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Editing a Qualification for a teacher: " + e);
		} catch (Exception e) {
			fail("Editing a Qualification for a teacher: " + e);
		}
	}

	/*
	 * Valid user(teacher), but wrong arguments(Grant Owner)
	 */
	public void testCreateQualificationUnsuccessfull1() {

		try {
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsCreateQualificationGrantOwner();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("CreateQualificationUnsuccessfull.");

		} catch (NotAuthorizedException e) {
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e) {
			fail("CreateQualificationUnsuccessfull: " + e);
		} catch (Exception e) {
			fail("CreateQualificationUnsuccessfull: " + e);
		}
	}

	/*
	 * Valid user(Grant Owner), but wrong arguments(Teacher)
	 */
	public void testCreateQualificationUnsuccessfull2() {
		try {
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsCreateQualificationTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("CreateQualificationUnsuccessfull.");

		} catch (NotAuthorizedException e) {
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e) {
			fail("CreateQualificationUnsuccessfull: " + e);
		} catch (Exception e) {
			fail("CreateQualificationUnsuccessfull: " + e);
		}
	}

	/*
	 * Valid user, but wrong arguments (editing a qualification that does't
	 * exists)
	 */
	public void testCreateQualificationUnsuccessfull3() {
		try {
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsEditQualificationGrantOwner();

			//Invalid qualification
			 ((InfoQualification) argserv[1]).setIdInternal(new Integer(1220));

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("CreateQualificationUnsuccessfull.");

		} catch (NotAuthorizedException e) {
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedEditQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e) {
			fail("CreateQualificationUnsuccessfull: " + e);
		} catch (Exception e) {
			fail("CreateQualificationUnsuccessfull: " + e);
		}

	}

	/** **************************** FIM DOS TESTES******************* */

	protected InfoPerson getInfoPersonGO() {
		InfoPerson info = new InfoPerson();
		info.setIdInternal(new Integer(14));
		info.setUsername("user_gom");
		return info;
	}
	protected InfoPerson getInfoPersonT() {
		InfoPerson info = new InfoPerson();
		info.setIdInternal(new Integer(18));
		info.setUsername("user_t");
		return info;
	}
	

}