/*
 * Created on 24/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Metadata;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTestQuestion;
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
		Integer metadataId,
		String path)
		throws FenixServiceException {
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata(metadataId);
			metadata = (IMetadata) persistentMetadata.readByOId(metadata, true);
			if (metadata == null)
				throw new InvalidArgumentsServiceException();

			List questionList =
				persistentSuport.getIPersistentQuestion().readByMetadata(
					metadata);
			boolean delete = true;
			Iterator questionIt = questionList.iterator();
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			while (questionIt.hasNext()) {
				IQuestion question = (IQuestion) questionIt.next();

				List testQuestionList =
					persistentSuport
						.getIPersistentTestQuestion()
						.readByQuestion(
						question);
				Iterator testQuestionIt = testQuestionList.iterator();
				while (testQuestionIt.hasNext())
					removeTestQuestionFromTest(
						persistentSuport,
						(ITestQuestion) testQuestionIt.next());

				List studentTestQuestionList =
					persistentSuport
						.getIPersistentStudentTestQuestion()
						.readByQuestion(
						question);
				if (studentTestQuestionList == null
					|| studentTestQuestionList.size() == 0) {
					persistentQuestion.delete(question);
					metadata.setMetadataFile(
						removeLocation(
							metadata.getMetadataFile(),
							question.getXmlFileName()));
					persistentMetadata.simpleLockWrite(metadata);
				} else {
					question.setVisibility(new Boolean("false"));
					persistentQuestion.lockWrite(question);
					delete = false;
				}

			}

			if (delete) {
				persistentQuestion.deleteByMetadata(metadata);
				persistentMetadata.delete(metadata);
			} else
				metadata.setVisibility(new Boolean("false"));
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}

	private void removeTestQuestionFromTest(
		ISuportePersistente persistentSuport,
		ITestQuestion testQuestion)
		throws ExcepcaoPersistencia {

		IPersistentTestQuestion persistentTestQuestion =
			persistentSuport.getIPersistentTestQuestion();

		ITest test = new Test(testQuestion.getKeyTest());
		test =
			(ITest) persistentSuport.getIPersistentTest().readByOId(test, true);
		if (test == null)
			throw new ExcepcaoPersistencia();

		List testQuestionList = persistentTestQuestion.readByTest(test);
		Integer questionOrder = testQuestion.getTestQuestionOrder();

		if (testQuestionList != null) {
			Iterator it = testQuestionList.iterator();
			while (it.hasNext()) {
				ITestQuestion iterTestQuestion = (ITestQuestion) it.next();
				Integer iterQuestionOrder =
					iterTestQuestion.getTestQuestionOrder();

				if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
					persistentTestQuestion.simpleLockWrite(iterTestQuestion);
					iterTestQuestion.setTestQuestionOrder(
						new Integer(iterQuestionOrder.intValue() - 1));
				}
			}
		}
		persistentTestQuestion.delete(testQuestion);
		test.setNumberOfQuestions(
			new Integer(test.getNumberOfQuestions().intValue() - 1));
		test.setLastModifiedDate(null);
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
			String doctypePublic =
				new String("-//Technical Superior Institute//DTD Test Metadata 1.1//EN");
			String doctypeSystem =
				new String(
					"metadataFile://"
						+ path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
			String auxFile = new String();
			int index = metadataFile.indexOf("<!DOCTYPE");
			if (index != -1) {
				auxFile = metadataFile.substring(0, index);
				int index2 = metadataFile.indexOf(">", index) + 1;
				auxFile = metadataFile.substring(index2, metadataFile.length());
			}
			metadataFile = auxFile;

			Transformer transformer =
				tf.newTransformer(new StreamSource(xsl.openStream()));
			transformer.setOutputProperty(
				OutputKeys.DOCTYPE_SYSTEM,
				doctypeSystem);
			transformer.setOutputProperty(
				OutputKeys.DOCTYPE_PUBLIC,
				doctypePublic);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-LATIN-1");
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
		} catch (Exception e) {
			throw new FenixServiceException(e);
		}
		return result.toString();
	}
}
