/*
 * Created on 24/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.IStudentTestQuestion;
import Dominio.Metadata;
import Dominio.Question;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class DeleteExercice implements IServico {
	private static DeleteExercice service = new DeleteExercice();
	private String path = new String();

	public static DeleteExercice getService() {
		return service;
	}

	public DeleteExercice() {
	}

	public String getNome() {
		return "DeleteExercice";
	}

	public boolean run(
		Integer executionCourseId,
		Integer oldQuestionId,
		String path)
		throws FenixServiceException {
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IQuestion oldQuestion = new Question(oldQuestionId);
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			oldQuestion =
				(IQuestion) persistentQuestion.readByOId(oldQuestion, true);
			if (oldQuestion == null)
				throw new FenixServiceException();
			IMetadata newMetadata =
				removeExercice(persistentSuport, oldQuestion);
			List studentTestQuestionList =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByQuestion(
					oldQuestion);

			if (studentTestQuestionList.size() != 0) {

				IQuestion newQuestion = null;

				newQuestion =
					getNewQuestion(
						persistentQuestion,
						newMetadata,
						oldQuestion);
				if (newQuestion == null)
					throw new FenixServiceException();

				Iterator it = studentTestQuestionList.iterator();
				IPersistentStudentTestQuestion persistentStudentTestQuestion =
					persistentSuport.getIPersistentStudentTestQuestion();
				while (it.hasNext()) {
					IStudentTestQuestion studentTestQuestion =
						(StudentTestQuestion) it.next();

					if (!newQuestion.equals(oldQuestion)) {

						studentTestQuestion.setQuestion(newQuestion);
						studentTestQuestion.setResponse(new Integer(0));
						persistentStudentTestQuestion.simpleLockWrite(
							studentTestQuestion);
					}
				}

				if (!newQuestion.equals(oldQuestion))
					persistentQuestion.delete(oldQuestion);

			}
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

	private IMetadata removeExercice(
		ISuportePersistente persistentSuport,
		IQuestion oldQuestion)
		throws ExcepcaoPersistencia, FenixServiceException {
		IMetadata metadata = new Metadata(oldQuestion.getKeyMetadata());
		IPersistentMetadata persistentMetadata =
			persistentSuport.getIPersistentMetadata();
		metadata = (IMetadata) persistentMetadata.readByOId(metadata, true);
		if(getMembersNumber(persistentSuport.getIPersistentQuestion(), metadata)!=1){
			metadata.setMetadataFile(removeLocation(metadata.getMetadataFile(),	oldQuestion.getXmlFileName()));
			persistentMetadata.simpleLockWrite(metadata);
		}
		return metadata;
	}

	private IQuestion getNewQuestion(
		IPersistentQuestion persistentQuestion,
		IMetadata metadata,
		IQuestion oldQuestion)
		throws ExcepcaoPersistencia {
		List questions = new ArrayList();
		IQuestion question = null;
		questions = persistentQuestion.readByMetadata(metadata);

		if (questions.size() != 0) {
			if (questions.size() == 1)
				return oldQuestion;

			do {
				Random r = new Random();
				int questionIndex = r.nextInt(questions.size());
				question = (IQuestion) questions.get(questionIndex);
			} while (question.equals(oldQuestion));
		}
		return question;
	}

	private int getMembersNumber(
		IPersistentQuestion persistentQuestion,
		IMetadata metadata)
		throws ExcepcaoPersistencia {
		List questions = persistentQuestion.readByMetadata(metadata);
		return questions.size();
	}

	private String removeLocation(String metadataFile, String xmlName)
		throws FenixServiceException {
		TransformerFactory tf = TransformerFactory.newInstance();
		java.io.StringWriter result = new java.io.StringWriter();
		try {
			URL xsl =
				new URL(
					"file://"
						+ path.concat("WEB-INF/ims/removeXmlLocation.xsl"));
			Transformer transformer =
				tf.newTransformer(new StreamSource(xsl.openStream()));
			transformer.setParameter("xmlDocument", xmlName);

			Source source = new StreamSource(new StringReader(metadataFile));
			transformer.transform(source, new StreamResult(result));

		} catch (javax.xml.transform.TransformerConfigurationException e) {
			throw new FenixServiceException(e);
		} catch (javax.xml.transform.TransformerException e) {
			throw new FenixServiceException(e);
		} catch (FileNotFoundException e) {
			throw new FenixServiceException(e);
		} catch (IOException e) {
			throw new FenixServiceException(e);
		}
		return result.toString();
	}
}
