package ServidorAplicacao.Servicos.teacher;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Announcement;
import Dominio.IAnnouncement;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class CreateAnnouncementTest
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public CreateAnnouncementTest(String testName) {
		super(testName);
	}
	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testCreateAnnouncementDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testCreateAnnouncementExpectedDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "CreateAnnouncement";
	}

	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {
		String[] args = { "nmsn", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionCourseCode = new Integer(24);
		String newAnnouncementTitle = "Anuncio Teste SUCESSO!";
		String newAnnouncementInformation = "Sucesso! Over & Out :)";
		Object[] args =
			{
				infoExecutionCourseCode,
				newAnnouncementTitle,
				newAnnouncementInformation };
		return args;
	}

	public void testCreateAnnouncementSuccessful() {
		try {
			boolean result = false;
			String[] args = getAuthorizedUser();
			IUserView id = authenticateUser(args);
			Object[] args2 = getAuthorizeArguments();

			gestor.executar(id, getNameOfServiceToBeTested(), args2);

			PersistenceBroker broker =
				PersistenceBrokerFactory.defaultPersistenceBroker();

			Criteria criteria = new Criteria();
			criteria.addOrderBy("lastModifiedDate", false);
			Query queryCriteria =
				new QueryByCriteria(Announcement.class, criteria);
			IAnnouncement announcement =
				(IAnnouncement) broker.getObjectByQuery(queryCriteria);
			broker.close();

			assertEquals(announcement.getTitle(), args2[1]);
			assertEquals(announcement.getInformation(), args2[2]);

			//erase new created announcement to verify nothing else has changed
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			sp.getIPersistentAnnouncement().deleteByOID(
				Announcement.class,
				announcement.getIdInternal());
			sp.confirmarTransaccao();

			compareDataSet(getExpectedDataSetFilePath());
			System.out.println(
				"CreateAnnouncementSuccessfulTest was SUCCESSFULY runned by service: "
					+ this.getClass().getName());

		} catch (FenixServiceException e) {
			fail("Creating a new Announcement for a Site " + e);
		} catch (Exception e) {
			fail("Creating a new Announcement for a Site " + e);
		}
	}

	public void testCreateExistingAnnouncement() {
		Integer infoExecutionCourseCode = new Integer(24);
		String newAnnouncementTitle = "announcement1deTFCI";
		String newAnnouncementInformation = "information1";
		Object[] args =
			{
				infoExecutionCourseCode,
				newAnnouncementTitle,
				newAnnouncementInformation };

		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testCreateExistingAnnouncement was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testCreateExistingAnnouncement");

		} catch (ExistingServiceException e) {
			compareDataSet("etc/datasets/servicos/teacher/testCreateExistingAnnouncementExpectedDataSet.xml");
			System.out.println(
				"testCreateExistingAnnouncement was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			System.out.println(
				"testCreateExistingAnnouncement was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testCreateExistingAnnouncement");
		}
	}
}