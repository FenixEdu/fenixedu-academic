package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica
 */
public class DeleteAnnouncementTest
	extends ServiceNeedsAuthenticationTestCase {

	/**
		* @param testName
		*/
	public DeleteAnnouncementTest(java.lang.String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteAnnouncementService";
	}

	protected String getDataSetFilePath() {
		return "etc/testDeleteAnnouncementDataSet.xml";
	}

	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
	 */
	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
	 */
	protected String[] getUnauthorizedUser() {
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNonTeacherUser()
	 */
	protected String[] getNonTeacherUser() {
		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getAuthorizeArguments() {

		//user é prof da disciplina 24			
		Integer infoExecutionCourseCode = new Integer(24);

		//Anuncio do execution course 24
		Integer announcementCode = new Integer(2);

		Object[] args = { infoExecutionCourseCode, announcementCode };
		return args;
	}

	/************  Inicio dos testes ao serviço**************/

	/*
	 * Teste: Apagar anúncio com sucesso
	 */
	public void testDeleteAnnouncementSuccefull() {
		try {
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(1);

			//Criar a lista de argumentos que o servico recebe
			Object[] argserv = { infoExecutionCourseCode, announcementCode };

			//Criar o utilizador
			IUserView arguser = authenticateUser(getAuthorizedUser());

			//Executar o serviço	
			gestor.executar(arguser, getNameOfServiceToBeTested(), argserv);

			//Verificar se a base de dados foi alterada
			//compareDataSet("testDeleteAnnouncementSuccefull.xml");

		} catch (NotAuthorizedException ex) {
			fail("Deleting an announcement of a Site " + ex);
		} catch (FenixServiceException ex) {
			fail("Deleting an announcement of a Site " + ex);
		} catch (Exception ex) {
			fail("Deleting an announcument of a Site " + ex);
		}
	}

	/*
	 * Teste: Apagar anúncio não existente
	 */
	public void testDeleteInvalidAnnouncement() {
		try {
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(121221);

			//Criar a lista de argumentos que o servico recebe
			Object[] argserv = { infoExecutionCourseCode, announcementCode };

			//Criar o utilizador
			IUserView arguser = authenticateUser(getAuthorizedUser());

				gestor.executar(arguser, getNameOfServiceToBeTested(), argserv);
		} catch (NotAuthorizedException ex) {
			/*
			 * Erro nos pré-filtros.... esta é a excepcao lançada.
			 */ 
			//Comparacao do dataset
			//compareDataSet(getDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Deleting an announcument of a Site " + ex);
		} catch (Exception ex) {
			fail("Deleting an announcument of a Site " + ex);
		}
	}
}