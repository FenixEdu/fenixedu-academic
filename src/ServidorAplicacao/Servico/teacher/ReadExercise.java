/*
 * Created on 27/Fev/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
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
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 *
 * @author Susana Fernandes
 *
 */
public class ReadExercise implements IServico
{
	private static ReadExercise service = new ReadExercise();
	private String path = new String();
	public static ReadExercise getService()
	{
		return service;
	}

	public String getNome()
	{
		return "ReadExercise";
	}

	public SiteView run(Integer executionCourseId, Integer metadataId, String path)
		throws FenixServiceException
	{
		this.path = path.replace('\\', '/');
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				persistentSuport.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
			if (executionCourse == null)
			{
				throw new InvalidArgumentsServiceException();
			}
			IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(metadataId);
			metadata = (IMetadata) persistentMetadata.readByOId(metadata, false);
			if (metadata == null)
			{
				throw new InvalidArgumentsServiceException();
			}
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
						parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, this.path);
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
			InfoSiteExercise infoSiteExercise = new InfoSiteExercise();
			infoSiteExercise.setInfoMetadata(infoMetadata);
			infoSiteExercise.setExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));

			SiteView siteView = new ExecutionCourseSiteView(infoSiteExercise, infoSiteExercise);
			return siteView;

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}
