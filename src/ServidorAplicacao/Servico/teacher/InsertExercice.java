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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.struts.upload.FormFile;
import org.xml.sax.SAXParseException;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import Dominio.Question;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class InsertExercice implements IServico
{
	private static InsertExercice service = new InsertExercice();
	private String path = new String();

	public static InsertExercice getService()
	{

		return service;
	}

	public InsertExercice()
	{
	}

	public String getNome()
	{
		return "InsertExercice";
	}

	public List run(Integer executionCourseId, FormFile metadataFile, FormFile xmlZipFile, String path)
		throws FenixServiceException, NotExecuteException
	{
		List badXmls = new ArrayList();
		boolean atLeastOneXml = false;
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
			ParseMetadata parseMetadata = new ParseMetadata();
			String metadataString = null;

			IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
			IMetadata metadata = new Metadata();
			metadata.setExecutionCourse(executionCourse);
			metadata.setVisibility(new Boolean("true"));
			try
			{
				metadataString =
					changeDocumentType(new String(metadataFile.getFileData(), "ISO-8859-1"), true);
				metadata.setMetadataFile(metadataString);
				metadata = parseMetadata.parseMetadata(metadataString, metadata, this.path);
			}
			catch (SAXParseException e)
			{
				badXmls.add(new String("badMetadata"));
				return badXmls;
			}

			catch (Exception e)
			{
				badXmls.add(new String("badMetadata"));
				return badXmls;
			}
			persistentMetadata.simpleLockWrite(metadata);
			ZipInputStream zipFile = null;
			try
			{
				zipFile = new ZipInputStream(xmlZipFile.getInputStream());
				while (true)
				{
					ZipEntry entry = zipFile.getNextEntry();
					String xmlString = new String();
					if (entry == null)
						break;
					byte[] b = new byte[1000];
					int readed = 0;
					while ((readed = zipFile.read(b)) > -1)
						xmlString = xmlString.concat(new String(b, 0, readed, "ISO-8859-1"));
					xmlString = changeDocumentType(xmlString, false);
					ParseQuestion parseQuestion = new ParseQuestion();
					IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
					try
					{
						parseQuestion.parseFile(xmlString, this.path);
						IQuestion question = new Question();
						question.setMetadata(metadata);
						question.setXmlFile(xmlString);
						question.setXmlFileName(entry.getName());
						question.setVisibility(new Boolean("true"));
						persistentQuestion.simpleLockWrite(question);
						atLeastOneXml = true;
					}
					catch (SAXParseException e)
					{
						zipFile.closeEntry();
						String newMetadataFile = removeLocation(metadataString, entry.getName());
						persistentMetadata.simpleLockWrite(metadata);
						metadata.setMetadataFile(newMetadataFile);
						metadata.setNumberOfMembers(
							new Integer(metadata.getNumberOfMembers().intValue() - 1));
						badXmls.add(entry.getName());
						continue;
					}
					catch (Exception e)
					{
						zipFile.closeEntry();
						String newMetadataFile = removeLocation(metadataString, entry.getName());
						persistentMetadata.simpleLockWrite(metadata);
						metadata.setMetadataFile(newMetadataFile);
						metadata.setNumberOfMembers(
							new Integer(metadata.getNumberOfMembers().intValue() - 1));

						badXmls.add(entry.getName());
						continue;
					}
					zipFile.closeEntry();
				}
				zipFile.close();
			}
			catch (FileNotFoundException e1)
			{
				throw new NotExecuteException("error.badMetadataFile");
			}
			catch (IOException e1)
			{
				throw new NotExecuteException("error.badMetadataFile");
			}

			if (!atLeastOneXml)
			{
				persistentMetadata.simpleLockWrite(metadata);
				persistentMetadata.delete(metadata);
			}
			return badXmls;
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}

	private String removeLocation(String metadataFile, String xmlName) throws FenixServiceException
	{
		TransformerFactory tf = TransformerFactory.newInstance();
		java.io.StringWriter result = new java.io.StringWriter();
		try
		{
			URL xsl = new URL("file:///" + path.concat("WEB-INF/ims/removeXmlLocation.xsl"));
			String doctypePublic =
				new String("-//Technical Superior Institute//DTD Test Metadata 1.1//EN");
			String doctypeSystem =
				new String("metadataFile://" + path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
			String auxFile = new String();
			int index = metadataFile.indexOf("<!DOCTYPE");
			if (index != -1)
			{
				auxFile = metadataFile.substring(0, index);
				int index2 = metadataFile.indexOf(">", index) + 1;
				auxFile = metadataFile.substring(index2, metadataFile.length());
			}
			metadataFile = auxFile;

			Transformer transformer = tf.newTransformer(new StreamSource(xsl.openStream()));
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctypeSystem);
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctypePublic);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-LATIN-1");
			transformer.setParameter("xmlDocument", xmlName);

			Source source = new StreamSource(new StringReader(metadataFile));

			transformer.transform(source, new StreamResult(result));

		}
		catch (javax.xml.transform.TransformerConfigurationException e)
		{
			throw new FenixServiceException(e);
		}
		catch (javax.xml.transform.TransformerException e)
		{
			throw new FenixServiceException(e);
		}
		catch (FileNotFoundException e)
		{
			throw new FenixServiceException(e);
		}
		catch (IOException e)
		{
			throw new FenixServiceException(e);
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
		return result.toString();
	}

	private String changeDocumentType(String file, boolean metadata) throws FenixServiceException
	{
		TransformerFactory tf = TransformerFactory.newInstance();
		java.io.StringWriter result = new java.io.StringWriter();
		try
		{
			URL xsl = new URL("file:///" + path.concat("WEB-INF/ims/changeDocumentType.xsl"));
			String doctypePublic = null;
			String doctypeSystem = null;
			if (metadata)
			{
				doctypePublic = new String("-//Technical Superior Institute//DTD Test Metadata 1.1//EN");
				doctypeSystem = new String("file:///" + path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
			}
			else
			{
				doctypePublic =
					new String("-//Technical Superior Institute//DTD Test XmlDocument 1.1//EN");
				doctypeSystem = new String("file:///" + path.concat("WEB-INF/ims/qtiasiv1p2.dtd"));
			}

			String auxFile = new String();
			int index = file.indexOf("<!DOCTYPE");
			if (index != -1)
			{
				auxFile = file.substring(0, index);
				int index2 = file.indexOf(">", index) + 1;
				auxFile = file.substring(index2, file.length());
			}
			file = auxFile;
			Transformer transformer = tf.newTransformer(new StreamSource(xsl.openStream()));
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctypePublic);
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctypeSystem);
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-LATIN-1");
			Source source = new StreamSource(new StringReader(file));
			transformer.transform(source, new StreamResult(result));
		}
		catch (javax.xml.transform.TransformerConfigurationException e)
		{
			throw new FenixServiceException(e);
		}
		catch (javax.xml.transform.TransformerException e)
		{
			throw new FenixServiceException(e);
		}
		catch (FileNotFoundException e)
		{
			throw new FenixServiceException(e);
		}
		catch (IOException e)
		{
			throw new FenixServiceException(e);
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}

		return result.toString();
	}
}
