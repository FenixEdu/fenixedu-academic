package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoAnnouncement;
import DataBeans.util.Cloner;
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
public class EditAnnouncementTest
	extends AnnouncementBelongsToExecutionCourseTest {

	/**
	 * @param testName
	 */
	public EditAnnouncementTest(String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "EditAnnouncementService";
	}
	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testEditAnnouncementDataSet.xml";
	}
	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedEditAnnouncementDataSet.xml";
	}
	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getExpectedUnsuccefullDataSetFilePath()
	 */
	protected String getExpectedUnsuccessfullDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedEditAnnouncementUnsuccefullDataSet.xml";
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
		 * O anúncio 1 pertence à disciplina 24.  
		 * (ver etc/testDeleteAnnouncementDataSet.xml)
		 */
		Integer infoExecutionCourseCode = new Integer(24);
		Integer announcementCode = new Integer(2);
		String announcementNewTitle = "Titulo-teste-teste";
		String announcementNewInformation = "Informacao-teste-teste";

		Object[] args =
			{
				infoExecutionCourseCode,
				announcementCode,
				announcementNewTitle,
				announcementNewInformation };
		return args;
	}

	/*
	 *  (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getTestAnnouncementUnsuccessfullArguments()
	 */
	protected Object[] getAnnouncementUnsuccessfullArguments() {
		/*
		 * A anúncio 3 pertence à disciplina 27
		 */
		Integer infoExecutionCourseCode = new Integer(24);
		Integer announcementCode = new Integer(3);
		String announcementNewTitle = "Titulo-teste-teste";
		String announcementNewInformation = "Informacao-teste-teste";

		Object[] args =
			{
				infoExecutionCourseCode,
				announcementCode,
				announcementNewTitle,
				announcementNewInformation };
		return args;
	}

	/************  Inicio dos testes ao serviço**************/

	/*
	 * Teste: Editar um anúncio com sucesso
	 */
	public void testEditAnnouncementSuccefull() {
		try {
			//Criar a lista de argumentos que o servico recebe
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(1);
			String announcementNewTitle = "Titulo-teste-teste";
			String announcementNewInformation = "Informacao-teste-teste";
			Object[] argserv =
				{
					infoExecutionCourseCode,
					announcementCode,
					announcementNewTitle,
					announcementNewInformation };

			//Criar o utilizador
			IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

			//Executar o serviço	
			gestor.executar(arguser, getNameOfServiceToBeTested(), argserv);

			//Verificar se o anuncio foi alterado com sucesso na Base de Dados.
			try {
				//Ler o anúncio da base de dados.
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
				Announcement readannouncement =
					new Announcement(announcementCode);
				IAnnouncement iAnnouncement = null;
				iAnnouncement =
					(IAnnouncement) sp.getIPersistentAnnouncement().readByOId(
						readannouncement,
						false);
				if (iAnnouncement == null) {
					fail("Editing an announcement of a Site.");
				}
				InfoAnnouncement infoAnnouncement =
					Cloner.copyIAnnouncement2InfoAnnouncement(iAnnouncement);
				sp.confirmarTransaccao();

				//Efectuar as comparações necessárias
				assertEquals(
					infoAnnouncement.getInformation(),
					announcementNewInformation);
				assertEquals(infoAnnouncement.getTitle(), announcementNewTitle);
				assertEquals(
					infoAnnouncement
						.getInfoSite()
						.getInfoExecutionCourse()
						.getIdInternal(),
					infoExecutionCourseCode);
				assertEquals(
					infoAnnouncement.getIdInternal(),
					announcementCode);
			} catch (ExcepcaoPersistencia ex) {
				fail("Editing an announcement of a Site " + ex);
			}

			/*
			 * Apagar o anúncio editado da base de dados de modo a verificar
			 * se não ocorreram alterações colaterais indesejadas.
			 */
			try {
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
				IAnnouncement announcement = new Announcement(announcementCode);
				sp.getIPersistentAnnouncement().delete(announcement);
				sp.confirmarTransaccao();

				//Comparacao com o data set
				compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
			} catch (ExcepcaoPersistencia ex) {
				fail("Editing an announcement of a Site " + ex);
			}

		} catch (FenixServiceException ex) {
			fail("Editing an announcement of a Site " + ex);
		} catch (Exception ex) {
			fail("Editing an announcument of a Site " + ex);
		}
	}

	/*
	 * Teste: Anúncio a editar não existe
	 */
	public void testEditAnnouncementUnsuccefull() {
		try {

			//Criar a lista de argumentos que o servico recebe
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(12121212);
			String announcementNewTitle = "Novo titulo";
			String announcementNewInformation = "Nova Informacao";
			Object[] argserv =
				{
					infoExecutionCourseCode,
					announcementCode,
					announcementNewTitle,
					announcementNewInformation };

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
			fail("Editing an announcement of a Site " + ex);
		} catch (Exception ex) {
			fail("Editing an announcument of a Site " + ex);
		}
	}
}