/*
 * Created on 11/Nov/2003
 */

package ServidorAplicacao.Servicos.person.qualification;

import DataBeans.InfoPerson;
import DataBeans.person.InfoQualification;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 * @author Barbosa
 * @author Pica
 */

public class DeleteQualificationTest extends QualificationServiceNeedsAuthenticationTestCase
{

	public DeleteQualificationTest(java.lang.String testName)
	{
		super(testName);
	}

	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication()
	{
		return Autenticacao.EXTRANET;
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested()
	{
		return "DeleteQualification";
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/person/qualification/testDeleteQualificationDataSet.xml";
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_GrantOwnerManager()
	 */
	protected String[] getAuthorizedUserGrantOwnerManager()
	{
		String[] args = { "user_gom", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_Teacher()
	 */
	protected String[] getAuthorizedUserTeacher()
	{
		String[] args = { "user_t", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
	 */
	protected String[] getUnauthorizedUser()
	{
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_GrantOwnerManager()
	 */
	protected Object[] getAuthorizeArgumentsGrantOwnerManager()
	{
		//Grant Owner qualification
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(2));
		info.setPersonInfo(getInfoPersonGO());

		Integer infoManagerPersonKey = new Integer(17);

		Object[] args = { infoManagerPersonKey, info };
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_Teacher()
	 */
	protected Object[] getAuthorizeArgumentsTeacher()
	{
		//Teacher qualification
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(3));
		info.setPersonInfo(getInfoPersonT());

		Integer infoManagerPersonKey = new Integer(18);

		Object[] args = { infoManagerPersonKey, info };
		return args;
	}

	protected Object[] getAuthorizeArgumentsDeleteQualificationGrantOwner()
	{
		//valid Grant Owner qualification
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(1));
		info.setPersonInfo(getInfoPersonGO());
		Integer infoManagerPersonKey = new Integer(17);

		Object[] args = { infoManagerPersonKey, info };
		return args;
	}

	protected Object[] getAuthorizeArgumentsDeleteQualificationTeacher()
	{
		//valid teacher qualification
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(4));
		info.setPersonInfo(getInfoPersonT());

		Integer infoManagerPersonKey = new Integer(18);

		Object[] args = { infoManagerPersonKey, info };
		return args;
	}

	/**
	 * 
	 * Start of the tests
	 *  
	 */

	/**
	 * A Grant Owner Manager deletes a qualification of a Grant Owner
	 */
	public void testDeleteQualificationGOMSuccessfull()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsDeleteQualificationGrantOwner();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedDeleteGOMQualificationSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e)
		{
			fail("Deleting a Qualification for GrantOwner: " + e);
		} catch (Exception e)
		{
			fail("Deleting a Qualification for GrantOwner: " + e);
		}
	}

	/**
	 * A Teacher deletes a own qualification
	 */
	public void testDeleteQualificationTSuccessfull()
	{
		try
		{
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsDeleteQualificationTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedDeleteTQualificationSuccesfullDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e)
		{
			fail("Deleting a Qualification for Teacher: " + e);
		} catch (Exception e)
		{
			fail("Deleting Qualification for Teacher: " + e);
		}
	}

	/**
	 * A teacher tries to delete a qualification of a grant owner
	 */
	public void testDeleteQualificationUnsuccessfull1()
	{
		try
		{
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsDeleteQualificationGrantOwner();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (Exception e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		}
	}

	/**
	 * A Grant Owner Manager tries to delete a qualification of a teacher
	 */
	public void testDeleteQualificationUnsuccessfull2()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsDeleteQualificationTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (Exception e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		}
	}

	/**
	 * A Grant Owner Manager tries to delete an inexistent qualification
	 */
	public void testDeleteQualificationUnsuccessfull3()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsDeleteQualificationGrantOwner();

			//Invalid qualification
			 ((InfoQualification) argserv[1]).setIdInternal(new Integer(1220));

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (Exception e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		}

	}

	/**
	 * A Grant Owner Manager tries to delete an qualification (that has a null internalId)
	 */
	public void testDeleteQualificationUnsuccessfull4()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsDeleteQualificationGrantOwner();

			//Invalid qualification
			 ((InfoQualification) argserv[1]).setIdInternal(null);

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Delete Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		} catch (FenixServiceException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedDeleteQualificationUnsuccesfullDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception e)
		{
			fail("DeleteQualificationUnsuccessfull: " + e);
		}

	}

	/**
	 * 
	 * End of tests
	 *  
	 */

	//Return a valid GrantOwner Manager user
	protected InfoPerson getInfoPersonGO()
	{
		InfoPerson info = new InfoPerson();
		info.setIdInternal(new Integer(14));
		info.setUsername("user_gom");
		return info;
	}
	//Return a valid Teacher user
	protected InfoPerson getInfoPersonT()
	{
		InfoPerson info = new InfoPerson();
		info.setIdInternal(new Integer(18));
		info.setUsername("user_t");
		return info;
	}

}