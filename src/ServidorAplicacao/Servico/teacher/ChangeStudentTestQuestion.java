/*
 * Created on Oct 20, 2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Metadata;
import Dominio.Question;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TestQuestionChangesType;
import Util.TestQuestionStudentsChangesType;

/**
 * @author Susana Fernandes
 *
 */
public class ChangeStudentTestQuestion implements IServico {
	private static ChangeStudentTestQuestion service =
		new ChangeStudentTestQuestion();
	private String path = new String();

	public static ChangeStudentTestQuestion getService() {
		return service;
	}

	public ChangeStudentTestQuestion() {
	}

	public String getNome() {
		return "ChangeStudentTestQuestion";
	}

	public boolean run(
		Integer executionCourseId,
		Integer distributedTestId,
		Integer oldQuestionId,
		Integer newMetadataId,
		Integer studentId,
		TestQuestionChangesType changesType,
		Boolean delete,
		TestQuestionStudentsChangesType studentsType,
		String path)
		throws FenixServiceException {
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();

			IQuestion oldQuestion = new Question(oldQuestionId);
			oldQuestion =
				(IQuestion) persistentQuestion.readByOId(oldQuestion, true);
			if (oldQuestion == null)
				throw new InvalidArgumentsServiceException();

			boolean canDelete = true;

			List studentsTestQuestionList = new ArrayList();
			IPersistentStudentTestQuestion persistentStudentTestQuestion =
				persistentSuport.getIPersistentStudentTestQuestion();
			if (studentsType.getType().intValue() == 3)
				studentsTestQuestionList =
					persistentStudentTestQuestion.readByQuestion(oldQuestion);
			else if (studentsType.getType().intValue() == 2) {
				IDistributedTest distributedTest =
					new DistributedTest(distributedTestId);
				distributedTest =
					(IDistributedTest) persistentSuport
						.getIPersistentDistributedTest()
						.readByOId(
						distributedTest,
						false);
				if (distributedTest == null)
					throw new InvalidArgumentsServiceException();
				studentsTestQuestionList =
					(
						List) persistentStudentTestQuestion
							.readByQuestionAndDistributedTest(
						oldQuestion,
						distributedTest);
			} else if (studentsType.getType().intValue() == 1) {
				IStudent student = new Student(studentId);
				student =
					(IStudent) persistentSuport
						.getIPersistentStudent()
						.readByOId(
						student,
						false);
				if (student == null)
					throw new InvalidArgumentsServiceException();
				IDistributedTest distributedTest =
					new DistributedTest(distributedTestId);
				distributedTest =
					(IDistributedTest) persistentSuport
						.getIPersistentDistributedTest()
						.readByOId(
						distributedTest,
						false);
				if (distributedTest == null)
					throw new InvalidArgumentsServiceException();
				studentsTestQuestionList.add(
					persistentStudentTestQuestion
						.readByQuestionAndStudentAndDistributedTest(
						oldQuestion,
						student,
						distributedTest));
			}

			Iterator studentsTestQuestionIt =
				studentsTestQuestionList.iterator();
			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = null;
			if (newMetadataId != null) {
				metadata = new Metadata(newMetadataId);
				metadata =
					(IMetadata) persistentMetadata.readByOId(metadata, false);
				if (metadata == null)
					throw new InvalidArgumentsServiceException();
			}

			while (studentsTestQuestionIt.hasNext()) {
				IStudentTestQuestion studentTestQuestion =
					(IStudentTestQuestion) studentsTestQuestionIt.next();

				if (!compareDates(studentTestQuestion
					.getDistributedTest()
					.getEndDate(),
					studentTestQuestion.getDistributedTest().getEndHour()))
					canDelete = false;
				else {
					IQuestion newQuestion = new Question();
					if (newMetadataId == null) {
						newQuestion =
							getNewQuestion(
								persistentQuestion,
								oldQuestion.getMetadata(),
								oldQuestion);
						if (newQuestion == null
							|| newQuestion.equals(oldQuestion))
							return false;
					} else {
						newQuestion =
							getNewQuestion(persistentQuestion, metadata, null);
						if (newQuestion == null)
							throw new InvalidArgumentsServiceException();
					}
					System.out.println(
						"Troquei exercicio. AlunoNum: "
							+ studentTestQuestion.getStudent().getNumber()
							+ " ExercicioOld:"
							+ oldQuestion.getIdInternal()
							+ " ExercicioNew: "
							+ newQuestion.getIdInternal()
							+ " RespostaOld:"
							+ studentTestQuestion.getResponse()
							+ " Shuffle: "
							+ studentTestQuestion.getOptionShuffle()
							+ " OldMark: "
							+ studentTestQuestion.getTestQuestionMark());
					studentTestQuestion.setQuestion(newQuestion);
					studentTestQuestion.setResponse(new Integer(0));
					studentTestQuestion.setTestQuestionMark(new Double(0));
					persistentStudentTestQuestion.simpleLockWrite(
						studentTestQuestion);
				}
			}

			if (delete.booleanValue()) {
				metadata = new Metadata(oldQuestion.getKeyMetadata());
				metadata =
					(IMetadata) persistentMetadata.readByOId(metadata, true);
				if (metadata == null)
					throw new InvalidArgumentsServiceException();
				metadata.setMetadataFile(
					removeLocation(
						oldQuestion.getMetadata().getMetadataFile(),
						oldQuestion.getXmlFileName()));
				List metadataQuestions =
					persistentQuestion.readByMetadata(metadata);
				if (canDelete) {
					persistentQuestion.delete(oldQuestion);
					if (metadataQuestions == null
						|| metadataQuestions.size() <= 1)
						persistentMetadata.delete(metadata);
				} else {
					oldQuestion.setVisibility(new Boolean(false));
					if (metadataQuestions == null
						|| metadataQuestions.size() <= 1)
						metadata.setVisibility(new Boolean(false));
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}

	private IQuestion getNewQuestion(
		IPersistentQuestion persistentQuestion,
		IMetadata metadata,
		IQuestion oldQuestion)
		throws ExcepcaoPersistencia {

		List questions = new ArrayList();
		IQuestion question = null;
		questions = persistentQuestion.readByMetadataAndVisibility(metadata);

		if (questions.size() != 0) {
			if (questions.size() == 1)
				return (IQuestion) questions.get(0);
			do {
				Random r = new Random();
				int questionIndex = r.nextInt(questions.size());
				question = (IQuestion) questions.get(questionIndex);
			} while (question.equals(oldQuestion));
		}
		return question;
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

	private boolean compareDates(Calendar date, Calendar hour) {
		Calendar calendar = Calendar.getInstance();
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (dateComparator.compare(calendar, date) <= 0) {
			if (dateComparator.compare(calendar, date) == 0)
				if (hourComparator.compare(calendar, hour) <= 0)
					return true;
				else
					return false;
			return true;
		}
		return false;
	}
}