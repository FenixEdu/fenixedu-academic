/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.Calendar;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IMetadata;
import Dominio.Metadata;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class EditExerciseTest extends ServiceNeedsAuthenticationTestCase
{
	public EditExerciseTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testReadMetadatasByTestTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "EditExercise";
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
		Integer metadataId = new Integer(121);

		String author = new String("author");
		String description = new String("description");
		String difficulty = new String("difficulty");

		Calendar learningTime = Calendar.getInstance();
		learningTime.set(Calendar.HOUR_OF_DAY, 20);
		learningTime.set(Calendar.MINUTE, 0);
		learningTime.set(Calendar.SECOND, 0);

		String level = new String("1");
		String mainSubject = new String("Matéria Principal");
		String secondarySubject = new String("Matéria Secundária");

		Object[] args =
			{
				executionCourseId,
				metadataId,
				author,
				description,
				difficulty,
				learningTime,
				level,
				mainSubject,
				secondarySubject };
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
			Query queryCriteria = new QueryByCriteria(Metadata.class, criteria);
			IMetadata metadata = (IMetadata) broker.getObjectByQuery(queryCriteria);
			broker.close();

			//ver se os dados do metadata estão correctos
			assertEquals(metadata.getExecutionCourse().getIdInternal(), args[0]);
			assertEquals(metadata.getIdInternal(), args[1]);
			assertEquals(metadata.getAuthor(), args[2]);
			assertEquals(metadata.getDescription(), args[3]);
			assertEquals(metadata.getDifficulty(), args[4]);

			Calendar expectedDistributedLearningTime = (Calendar) args[5];

			assertEquals(
				metadata.getLearningTime().get(Calendar.HOUR_OF_DAY),
				expectedDistributedLearningTime.get(Calendar.HOUR_OF_DAY));
			assertEquals(
				metadata.getLearningTime().get(Calendar.MINUTE),
				expectedDistributedLearningTime.get(Calendar.MINUTE));
			assertEquals(
				metadata.getLearningTime().get(Calendar.SECOND),
				expectedDistributedLearningTime.get(Calendar.SECOND));

			assertEquals(metadata.getLevel(), args[6]);
			assertEquals(metadata.getMainSubject(), args[7]);
			assertEquals(metadata.getSecondarySubject(), args[8]);

		}
		catch (FenixServiceException ex)
		{
			fail("EditExercise " + ex);
		}
		catch (Exception ex)
		{
			fail("EditExercise " + ex);
		}
	}
}
