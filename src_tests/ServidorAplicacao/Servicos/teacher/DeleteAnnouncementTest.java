package ServidorAplicacao.Servicos.teacher;

import Dominio.Announcement;
import Dominio.IAnnouncement;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */
public class DeleteAnnouncementTest
	extends AnnouncementBelongsToExecutionCourseTest {

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
		return "etc/datasets/servicos/teacher/testDeleteAnnouncementDataSet.xml";
	}
	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedDeleteAnnouncementDataSet.xml";
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getExpectedUnsuccefullDataSetFilePath()
	 */
	protected String getExpectedUnsuccessfullDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedDeleteAnnouncementUnsuccefullDataSet.xml";
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
	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
	 */
	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNonTeacherUser()
	 */
	protected String[] getNotAuthenticatedUser() {
		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getAuthorizeArguments() {
		/*
		 * O professor escolhido para autenticação é responsável pela disciplina 24.
		 * O anúncio 2 pertence à disciplina 24 
		 * (ver etc/testDeleteAnnouncementDataSet.xml)
		 */
		Integer infoExecutionCourseCode = new Integer(24);
		Integer announcementCode = new Integer(2);

		Object[] args = { infoExecutionCourseCode, announcementCode };
		return args;
	}

	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getAnnouncementUnsuccessfullArguments()
	 */
	protected Object[] getAnnouncementUnsuccessfullArguments() {
		/*
		 * O professor escolhido para autenticação é responsável pela disciplina 24.
		 * O anúncio 3 pertence à disciplina 27 
		 * (ver etc/testDeleteAnnouncementDataSet.xml)
		 */
		Integer infoExecutionCourseCode = new Integer(24);
		Integer announcementCode = new Integer(3);

		Object[] args = { infoExecutionCourseCode, announcementCode };
		return args;
	}

	/************  Inicio dos testes ao serviço**************/

	/*
	 * Teste: Apagar anúncio com sucesso
	 */
	public void testDeleteAnnouncementSuccefull() {
		try {
			//Criar a lista de argumentos que o servico recebe
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(1);
			Object[] argserv = { infoExecutionCourseCode, announcementCode };

			//Criar o utilizador
			IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

			//Executar o serviço	
			gestor.executar(arguser, getNameOfServiceToBeTested(), argserv);

			//Verificar se o anuncio foi realmente apagado
			try {
				//Anuncio apagado anteriormente
				Announcement readannouncement =
					new Announcement(announcementCode);
				IAnnouncement iAnnouncement = null;

				//Ler o anúncio da base de dados.
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
				iAnnouncement =
					(IAnnouncement) sp.getIPersistentAnnouncement().readByOId(
						readannouncement,
						false);
				sp.confirmarTransaccao();

				//Se o anúncio ainda existir o serviço não decorreu como esperado
				if (iAnnouncement != null) {
					fail("Deleting an announcement of a Site.");
				}

			} catch (ExcepcaoPersistencia ex) {
				fail("Deleting an announcement of a Site " + ex);
			}

			//Verificar se a base de dados foi alterada
			compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
		} catch (NotAuthorizedException ex) {
			fail("Deleting an announcement of a Site " + ex);
		} catch (FenixServiceException ex) {
			fail("Deleting an announcement of a Site " + ex);
		} catch (Exception ex) {
			fail("Deleting an announcument of a Site " + ex);
		}
	}

	/*
		 * Teste: Anúncio a apagar não existe
		 */
	public void testDeleteAnnouncementUnsuccefull() {
		try {
			//Criar a lista de argumentos que o servico recebe
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(12121212);
			Object[] argserv = { infoExecutionCourseCode, announcementCode };

			//Criar o utilizador
			IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

			//Executar o serviço	
			gestor.executar(arguser, getNameOfServiceToBeTested(), argserv);

		} catch (NotAuthorizedException ex) {
			/*
			 * O anúncio não pertence à disciplina (pois não existe).
			 * Os pré-filtros lançam uma excepcao NotAuthorizedException,
			 * o serviço nem sequer chega a ser invocado
			 */
			//Comparacao do dataset
			compareDataSetUsingExceptedDataSetTableColumns(getExpectedUnsuccessfullDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Deleting an announcement of a Site " + ex);
		} catch (Exception ex) {
			fail("Deleting an announcument of a Site " + ex);
		}
	}
}