/*
 * Created on 25/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import Dominio.Question;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadQuestion implements IServico
{
	private static ReadQuestion service = new ReadQuestion();
	private String path = new String();

	public static ReadQuestion getService()
	{
		return service;
	}

	public String getNome()
	{
		return "ReadQuestion";
	}

	public SiteView run(Integer executionCourseId, Integer metadataId, Integer questionId, String path)
			throws FenixServiceException
	{
		this.path = path.replace('\\', '/');
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
			executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse()
					.readByOId(executionCourse, false);
			if (executionCourse == null)
			{
				throw new InvalidArgumentsServiceException();
			}
			IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
			IQuestion question = null;
			if (questionId == null || questionId.equals(new Integer(-1)))
			{
				if (metadataId == null)
					throw new InvalidArgumentsServiceException();
				IMetadata metadata = new Metadata(metadataId);
				metadata = (IMetadata) persistentSuport.getIPersistentMetadata().readByOId(metadata,
						false);
				if (metadata == null)
					throw new InvalidArgumentsServiceException();
				if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0)
					question = (IQuestion) metadata.getVisibleQuestions().get(0);
				else
					throw new InvalidArgumentsServiceException();
			}
			else
			{
				question = new Question(questionId);
				question = (IQuestion) persistentQuestion.readByOId(question, false);
			}
			if (question == null)
			{
				throw new InvalidArgumentsServiceException();
			}
			InfoQuestion infoQuestion = Cloner.copyIQuestion2InfoQuestion(question);
			ParseQuestion parse = new ParseQuestion();
			try
			{
				infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, this.path);
			}
			catch (Exception e)
			{
				throw new FenixServiceException(e);
			}
			InfoSiteQuestion bodyComponent = new InfoSiteQuestion();
			bodyComponent.setInfoQuestion(infoQuestion);
			bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));
			SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
			return siteView;
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}
