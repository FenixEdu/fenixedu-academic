/*
 * Created on 111/Nov/2003
 */

package ServidorAplicacao.Servicos.person;

import java.util.Date;

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import DataBeans.person.InfoQualification;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Barbosa
 * @author Pica
 */

public class DeleteQualificationTest
	extends QualificationServiceNeedsAuthenticationTestCase {

	public DeleteQualificationTest(java.lang.String testName) {
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
		return "DeleteQualification";
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/person/testDeleteQualificationDataSet.xml";
		//TODO MUDAR PARA O XML CORRECTO
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
		info.setIdInternal(new Integer(2));
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
		info.setIdInternal(new Integer(3));
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
	protected Object[] getAuthorizeArgumentsDeleteQualificationGrantOwner() {
		//Qualificação de um bolseiro
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(1));
		info.setPersonInfo(getInfoPersonGO());

		Integer infoManagerPersonCode = new Integer(17);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	protected Object[] getAuthorizeArgumentsDeleteQualificationTeacher() {
		//Qualificação de um professor
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(4));
		info.setPersonInfo(getInfoPersonT());

		Integer infoManagerPersonCode = new Integer(18);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	/** **************************** INICIO DOS TESTES******************* */

	/*
	 * Um Grant Owner Manager tenta apagar com sucesso uma qualificação a um
	 * Grant Owner
	 */
	public void testCreateQualificationGOMSuccessfull() {
		try {
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsDeleteQualificationGrantOwner();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/person/testExpectedDeleteGOMQualificationSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Deleting a Qualification for GrantOwner: " + e);
		} catch (Exception e) {
			fail("Deleting a Qualification for GrantOwner: " + e);
		}
	}

	/*
	 * Um professor tenta apagar com sucesso uma qualificação sua.
	 */
	public void testCreateQualificationTSuccessfull() {
		try {
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsDeleteQualificationTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/person/testExpectedDeleteTQualificationSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Deleting a Qualification for Teacher: " + e);
		} catch (Exception e) {
			fail("Deleting Qualification for Teacher: " + e);
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
				getAuthorizeArgumentsDeleteQualificationGrantOwner();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e) {
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/person/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (Exception e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
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
				getAuthorizeArgumentsDeleteQualificationTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e) {
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/person/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (Exception e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
		}
	}

	/*
	 * Valid user, but wrong arguments (deleting a qualification that does't
	 * exists)
	 */
	public void testCreateQualificationUnsuccessfull3() {
		try {
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsDeleteQualificationGrantOwner();

			//Invalid qualification
			 ((InfoQualification) argserv[1]).setIdInternal(new Integer(1220));

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e) {
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/person/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (Exception e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
		}

	}

	/*
	 * Valid user, but wrong arguments (qualification id is null)
	 */
	public void testCreateQualificationUnsuccessfull4() {
		try {
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv =
				getAuthorizeArgumentsDeleteQualificationGrantOwner();

			//Invalid qualification
			 ((InfoQualification) argserv[1]).setIdInternal(null);

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (FenixServiceException e) {
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/person/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception e) {
			fail("DeleteQualificationUnsuccessfull: " + e);
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