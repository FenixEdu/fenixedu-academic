/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.CorrectionAvailability;
import Util.TestType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTestTest extends ServiceNeedsAuthenticationTestCase
{
	public EditDistributedTestTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "EditDistributedTest";
	}

	protected String[] getAuthenticatedAndAuthorizedUser()
	{

		String[] args = { "D2543", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser()
	{

		String[] args = { "L48283", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser()
	{

		String[] args = { "L48283", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments()
	{
		Integer executionCourseId = new Integer(34882);
		Integer distributedTestId = new Integer(1);
		String info = new String("informação da ficha-alterada");

		Calendar beginDate = Calendar.getInstance();
		beginDate.set(Calendar.DAY_OF_MONTH, 1);
		beginDate.set(Calendar.MONTH, 1);
		beginDate.set(Calendar.YEAR, 2004);
		Calendar beginHour = Calendar.getInstance();
		beginHour.set(Calendar.HOUR_OF_DAY, 10);
		beginHour.set(Calendar.MINUTE, 0);
		beginHour.set(Calendar.SECOND, 0);

		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.DAY_OF_MONTH, 2);
		endDate.set(Calendar.MONTH, 2);
		endDate.set(Calendar.YEAR, 2004);
		Calendar endHour = Calendar.getInstance();
		endHour.set(Calendar.HOUR_OF_DAY, 20);
		endHour.set(Calendar.MINUTE, 0);
		endHour.set(Calendar.SECOND, 0);

		TestType testType = new TestType(1);
		CorrectionAvailability correctionAvailability = new CorrectionAvailability(1);
		Boolean studentFeedback = new Boolean(true);
		String[] selected = { new String("11355")};
		Boolean shifts = new Boolean(false);
		String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");
		Object[] args =
			{
				executionCourseId,
				distributedTestId,
				info,
				beginDate,
				beginHour,
				endDate,
				endHour,
				testType,
				correctionAvailability,
				studentFeedback,
				selected,
				shifts,
				path };
		return args;
	}

	protected String getApplication()
	{
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull()
	{

		try
		{
			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
			Object[] args = getAuthorizeArguments();
			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

			Criteria criteria = new Criteria();
			criteria.addEqualTo("idInternal", args[1]);
			Query queryCriteria = new QueryByCriteria(DistributedTest.class, criteria);
			IDistributedTest distributedTest = (IDistributedTest) broker.getObjectByQuery(queryCriteria);
			broker.close();

			//ver se os dados do distributed_test estão correctos
			Calendar expectedDistributedTestBeginDate = (Calendar) args[3];
			Calendar expectedDistributedTestBeginHour = (Calendar) args[4];
			Calendar expectedDistributedTestEndDate = (Calendar) args[5];
			Calendar expectedDistributedTestEndHour = (Calendar) args[6];

			//			assertEquals(distributedTest.getTestScope().getDomainObject().getIdInternal(), args[0]);
			assertEquals(distributedTest.getIdInternal(), args[1]);
			assertEquals(
				distributedTest.getBeginDate().get(Calendar.DAY_OF_MONTH),
				expectedDistributedTestBeginDate.get(Calendar.DAY_OF_MONTH));
			assertEquals(
				distributedTest.getBeginDate().get(Calendar.MONTH),
				expectedDistributedTestBeginDate.get(Calendar.MONTH));
			assertEquals(
				distributedTest.getBeginDate().get(Calendar.YEAR),
				expectedDistributedTestBeginDate.get(Calendar.YEAR));

			assertEquals(
				distributedTest.getBeginHour().get(Calendar.HOUR_OF_DAY),
				expectedDistributedTestBeginHour.get(Calendar.HOUR_OF_DAY));
			assertEquals(
				distributedTest.getBeginHour().get(Calendar.MINUTE),
				expectedDistributedTestBeginHour.get(Calendar.MINUTE));
			assertEquals(
				distributedTest.getBeginHour().get(Calendar.SECOND),
				expectedDistributedTestBeginHour.get(Calendar.SECOND));

			assertEquals(
				distributedTest.getEndDate().get(Calendar.DAY_OF_MONTH),
				expectedDistributedTestEndDate.get(Calendar.DAY_OF_MONTH));
			assertEquals(
				distributedTest.getEndDate().get(Calendar.MONTH),
				expectedDistributedTestEndDate.get(Calendar.MONTH));
			assertEquals(
				distributedTest.getEndDate().get(Calendar.YEAR),
				expectedDistributedTestEndDate.get(Calendar.YEAR));

			assertEquals(
				distributedTest.getEndHour().get(Calendar.HOUR_OF_DAY),
				expectedDistributedTestEndHour.get(Calendar.HOUR_OF_DAY));
			assertEquals(
				distributedTest.getEndHour().get(Calendar.MINUTE),
				expectedDistributedTestEndHour.get(Calendar.MINUTE));
			assertEquals(
				distributedTest.getEndHour().get(Calendar.SECOND),
				expectedDistributedTestEndHour.get(Calendar.SECOND));

			assertEquals(distributedTest.getTestType(), args[7]);
			assertEquals(distributedTest.getCorrectionAvailability(), args[8]);
			assertEquals(distributedTest.getStudentFeedback(), args[9]);

			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			criteria = new Criteria();
			criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
			queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
			List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);

			criteria = new Criteria();
			criteria.addEqualTo(
				"message",
				"Tem uma Ficha de Trabalho a realizar entre as 10:00 de 1/1/2004 até às 20:00 de 2/2/2004");
			criteria.addEqualTo("subject", "Ficha de Trabalho 1");
			queryCriteria = new QueryByCriteria(Advisory.class, criteria);
			IAdvisory advisory1 = (IAdvisory) broker.getObjectByQuery(queryCriteria);

			criteria = new Criteria();
			criteria.addEqualTo(
				"message",
				"As datas de realização da Ficha de Trabalho foram alteradas. Deverá realizar a ficha entre as 10:00 de 1/1/2004 até às 20:00 de 2/2/2004");
			criteria.addEqualTo("subject", "Ficha de Trabalho 1: Alteração de datas");
			queryCriteria = new QueryByCriteria(Advisory.class, criteria);
			IAdvisory advisory2 = (IAdvisory) broker.getObjectByQuery(queryCriteria);

			broker.close();
			assertEquals(studentTestQuestionList.size(), 2232);
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext())
			{
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
				assertEquals(
					studentTestQuestion.getKeyDistributedTest(),
					distributedTest.getIdInternal());
				if (studentTestQuestion.getKeyStudent() == new Integer(((String[]) args[10])[0]))
					if (!studentTestQuestion
						.getStudent()
						.getPerson()
						.getAdvisories()
						.contains(advisory2))
						fail("Edit Distributed Test " + "Advisory1");
					else if (
						!studentTestQuestion.getStudent().getPerson().getAdvisories().contains(
							advisory1))
						fail("Edit Distributed Test " + "Advisory2");
			}

			//			if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION)))
			//			{
			//				broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			//
			//				criteria = new Criteria();
			//				criteria.addEqualTo("chaveDisciplinaExecucao", args[0]);
			//				criteria.addEqualTo("chaveAluno", new Integer(((String[]) args[10])[0]));
			//				queryCriteria = new QueryByCriteria(Frequenta.class, criteria);
			//				IFrequenta frequenta = (IFrequenta) broker.getObjectByQuery(queryCriteria);
			//				assertNotNull(frequenta);
			//				criteria = new Criteria();
			//				criteria.addEqualTo("keyDistributedTest", args[1]);
			//				queryCriteria = new QueryByCriteria(OnlineTest.class, criteria);
			//				IOnlineTest onlineTest = (IOnlineTest) broker.getObjectByQuery(queryCriteria);
			//				assertNotNull(onlineTest);
			//				criteria = new Criteria();
			//				criteria.addEqualTo("keyAttend", frequenta.getIdInternal());
			//				criteria.addEqualTo("keyEvaluation", onlineTest.getIdInternal());
			//				queryCriteria = new QueryByCriteria(Mark.class, criteria);
			//				IMark mark = (IMark) broker.getObjectByQuery(queryCriteria);
			//				broker.close();
			//				assertNotNull(mark);
			//			}
		}
		catch (FenixServiceException ex)
		{
			fail("Edit Distributed Test " + ex);
		}
		catch (Exception ex)
		{
			fail("Edit Distributed Test " + ex);
		}
	}
}
