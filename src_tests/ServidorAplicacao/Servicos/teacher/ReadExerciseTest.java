/*
 * Created on 25/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteExercise;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import UtilTests.ParseQuestion;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class ReadExerciseTest extends ServiceNeedsAuthenticationTestCase
{

	public ReadExerciseTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testReadMetadatasByTestTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadExercise";
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
		String path = new String("e:\\eclipse\\workspace\\fenix-exams3\\build\\standalone\\");

		Object[] args = { executionCourseId, metadataId, path };
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
			SiteView serviceSiteView =
				(SiteView) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					args);

			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Criteria criteria = new Criteria();
			criteria.addEqualTo("idInternal", args[1]);
			Query queryCriteria = new QueryByCriteria(Metadata.class, criteria);
			IMetadata metadata = (IMetadata) broker.getObjectByQuery(queryCriteria);

			criteria = new Criteria();
			criteria.addEqualTo("idInternal", args[0]);
			queryCriteria = new QueryByCriteria(ExecutionCourse.class, criteria);
			IExecutionCourse executionCourse = (IExecutionCourse) broker.getObjectByQuery(queryCriteria);
			broker.close();

			InfoMetadata infoMetadata = Cloner.copyIMetadata2InfoMetadata(metadata);
			Iterator it = metadata.getVisibleQuestions().iterator();
			List visibleInfoQuestions = new ArrayList();

			while (it.hasNext())
			{
				InfoQuestion infoQuestion = Cloner.copyIQuestion2InfoQuestion((IQuestion) it.next());
				ParseQuestion parse = new ParseQuestion();
				try
				{
					infoQuestion =
						parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, (String) args[2]);
					infoQuestion.setCorrectResponse(
						parse.newResponseList(
							infoQuestion.getCorrectResponse(),
							infoQuestion.getOptions()));
				}
				catch (Exception e)
				{
					throw new FenixServiceException(e);
				}

				visibleInfoQuestions.add(infoQuestion);
			}
			infoMetadata.setVisibleQuestions(visibleInfoQuestions);

			InfoMetadata serviceInfoMetadata =
				((InfoSiteExercise) serviceSiteView.getComponent()).getInfoMetadata();
			if (!infoMetadata.getIdInternal().equals(serviceInfoMetadata.getIdInternal()))
				fail("ReadExerciseTest " + "InfoMetadata notEqual");

			InfoExecutionCourse serviceInfoExecutionCourse =
				((InfoSiteExercise) serviceSiteView.getComponent()).getExecutionCourse();
			if (!((InfoExecutionCourse) Cloner.get(executionCourse)).equals(serviceInfoExecutionCourse))
				fail("ReadExerciseTest " + "InfoExecutionCourse notEqual");

		}
		catch (FenixServiceException ex)
		{
			fail("ReadExerciseTest " + ex);
		}
		catch (Exception ex)
		{
			fail("ReadExerciseTest " + ex);
		}
	}
}
