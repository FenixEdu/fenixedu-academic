package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoSite;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditCustomizationOptionsTest
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public EditCustomizationOptionsTest(java.lang.String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "EditCustomizationOptions";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testEditCustomizationOptionsDataSet.xml";
	}
	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedEditCustomizationOptionsUnsuccefullDataSet.xml";
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
		/*
		 * O professor escolhido para autenticação é responsável pela disciplina 24.
		 * Site da disciplina 24 tem id 1.  
		 */
		Integer infoExecutionCourseCode = new Integer(24);
		InfoSite infoSiteNew = new InfoSite();
		infoSiteNew.setAlternativeSite("site");
		infoSiteNew.setMail("mail");
		infoSiteNew.setInitialStatement("inicio");
		infoSiteNew.setIntroduction("introducao");

		Object[] args = { infoExecutionCourseCode, infoSiteNew };
		return args;
	}

	/************  Inicio dos testes ao serviço**************/

	/*
	 * Teste: Editar opcções de custumização com sucesso
	 */
	public void testEditCustomizationOptionsSuccefull() {
		try {
			//Criar a lista de argumentos que o servico recebe
			Integer infoExecutionCourseCode = new Integer(24);
			InfoSite infoSiteNew = new InfoSite();
			infoSiteNew.setAlternativeSite("site");
			infoSiteNew.setMail("mail");
			infoSiteNew.setInitialStatement("inicio");
			infoSiteNew.setIntroduction("introducao");
			Object[] argserv = { infoExecutionCourseCode, infoSiteNew };

			//Criar o utilizador
			IUserView arguser = authenticateUser(getAuthorizedUser());

			//Executar o serviço	
			gestor.executar(arguser, getNameOfServiceToBeTested(), argserv);

			//Verificar se as alteracoes tiveram sucesso
			try {
				//Ler o anúncio da base de dados.
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
				ISite site = null;
				IDisciplinaExecucao sitecourse =
					(IDisciplinaExecucao) sp
						.getIDisciplinaExecucaoPersistente()
						.readByOId(
						new DisciplinaExecucao(infoExecutionCourseCode),
						false);
				site =
					sp.getIPersistentSite().readByExecutionCourse(sitecourse);

				sp.confirmarTransaccao();
				if (site == null) {
					fail("Editing Customization Options of a Site.");
				}
				//Efectuar as comparações necessárias
				assertEquals(
					site.getAlternativeSite(),
					infoSiteNew.getAlternativeSite());
				assertEquals(site.getMail(), infoSiteNew.getMail());
				assertEquals(
					site.getInitialStatement(),
					infoSiteNew.getInitialStatement());
				assertEquals(
					site.getIntroduction(),
					infoSiteNew.getIntroduction());

			} catch (ExcepcaoPersistencia ex) {
				fail("Editing Customization Options of a Site: " + ex);
			}
		} catch (NotAuthorizedException ex) {
			fail("Editing Customization Options of a Site " + ex);
		} catch (FenixServiceException ex) {
			fail("Editing Customization Options of a Site " + ex);
		} catch (Exception ex) {
			fail("Editing Customization Options of a Site " + ex);
		}
	}

	/*
	 * Teste: Editar site de disicplina inexistente
	 */
	public void testEditCustomizationOptionsOfInvalidCourse() {
		try {
			//Criar a lista de argumentos que o servico recebe
			Integer infoExecutionCourseCode = new Integer(34343434);
			InfoSite infoSiteNew = new InfoSite();
			infoSiteNew.setAlternativeSite("site");
			infoSiteNew.setMail("mail");
			infoSiteNew.setInitialStatement("inicio");
			infoSiteNew.setIntroduction("introducao");
			Object[] argserv = { infoExecutionCourseCode, infoSiteNew };

			//Criar o utilizador
			IUserView arguser = authenticateUser(getAuthorizedUser());

			//Executar o serviço
			gestor.executar(arguser, getNameOfServiceToBeTested(), argserv);

		} catch (NotAuthorizedException ex) {
			/*
			 * A disciplina não existe.
			 * Os pré-filtros lançam uma excepcao NotAuthorizedException,
			 * o serviço nem sequer chega a ser invocado
			 */
			//Comparacao do dataset
			compareDataSet(getExpectedDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Editing Customization Options of a Site " + ex);
		} catch (Exception ex) {
			fail("Editing Customization Options of a Site " + ex);
		}
	}
}