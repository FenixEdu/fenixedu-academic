/*
 * Created on 23/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.struts.upload.FormFile;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import Dominio.Question;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class InsertExercice implements IServico {
	private static InsertExercice service = new InsertExercice();
	private String path = new String();

	public static InsertExercice getService() {

		return service;
	}

	public InsertExercice() {
	}

	public String getNome() {
		return "InsertExercice";
	}

	public List run(
		Integer executionCourseId,
		FormFile metadataFile,
		FormFile xmlZipFile,
		String path)
		throws FenixServiceException, NotExecuteException {
		List badXmls = new ArrayList();
		this.path = path.replace('\\', '/');
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(executionCourseId);
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}

			ParseMetadata parseMetadata = new ParseMetadata();
			String metadataString = null;
			List xmls = null;
			try {
				//new String(metadataFile.getFileData()
				metadataString =
					changeDocumentType(
						new String(metadataFile.getFileData(),"ISO-8859-1"),
						true);
				xmls = parseMetadata.parseMetadata(metadataString);
			} catch (Exception e) {
				throw new FenixServiceException(e);
			}
			IPersistentMetadata persistentMetadata =
				persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata();
			metadata.setExecutionCourse(executionCourse);
			metadata.setMetadataFile(metadataString);
			persistentMetadata.simpleLockWrite(metadata);

			ZipInputStream zipFile = null;
			try {
				zipFile = new ZipInputStream(xmlZipFile.getInputStream());
				while (true) {
					ZipEntry entry = zipFile.getNextEntry();
					String xmlString = new String();
					if (entry == null)
						break;
					byte[] b = new byte[1000];
					int readed = 0;

					while ((readed = zipFile.read(b)) > -1)
						xmlString = xmlString.concat(new String(b, 0, readed));
					xmlString = changeDocumentType(xmlString, false);
					ParseQuestion parseQuestion = new ParseQuestion();
					IPersistentQuestion persistentQuestion =
						persistentSuport.getIPersistentQuestion();
					try {

						parseQuestion.parseFile(xmlString);

						IQuestion question = new Question();
						question.setMetadata(metadata);
						question.setXmlFile(xmlString);
						question.setXmlFileName(entry.getName());
						persistentQuestion.simpleLockWrite(question);
					} catch (Exception e) {
						zipFile.closeEntry();
						String newMetadataFile =
							removeLocation(metadataString, entry.getName());
						metadata.setMetadataFile(newMetadataFile);
						persistentMetadata.simpleLockWrite(metadata);
						badXmls.add(entry.getName());
						continue;
					}
					zipFile.closeEntry();
				}
				zipFile.close();
			} catch (FileNotFoundException e1) {
				throw new NotExecuteException("error.badMetadataFile");
			} catch (IOException e1) {
				throw new NotExecuteException("error.badMetadataFile");
			}

			return badXmls;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
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

	private String changeDocumentType(String file, boolean metadata) {
		String result = new String();
		String pathMetadata =
			new String(
				"file://" + path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
		String pathXml =
			new String("file://" + path.concat("WEB-INF/ims/qtiasiv1p2.dtd"));

		int index = file.indexOf("<!DOCTYPE");
		if (index == -1)
			return file;
		result = result.concat(file.substring(0, index));
		int index2 = file.indexOf("\"", index) + 1;
		if (index2 == -1)
			return file;
		result = result.concat(file.substring(index, index2));
		if (metadata)
			result = result.concat(pathMetadata);
		else
			result = result.concat(pathXml);
		int index3 = file.indexOf("\"", index2);
		if (index3 == -1)
			return file;
		result = result.concat(file.substring(index3, file.length()));
		return result;
	}
	//<?xml version="1.0" encoding="Latin1"?>
	//ISO-8859-1
	
}
