/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.fenixedu.dataTransferObject.InfoQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.xml.sax.SAXParseException;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertExercise implements IService {
    private String path = new String();

    public InsertExercise() {
    }

    public List run(Integer executionCourseId, FormFile metadataFile, FormFile xmlZipFile, String path)
            throws FenixServiceException, NotExecuteException {
        List badXmls = new ArrayList();
        int xmlNumber = 0;
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            ParseMetadata parseMetadata = new ParseMetadata();
            String metadataString = null;
            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            IMetadata metadata = new Metadata();
            metadata.setExecutionCourse(executionCourse);
            metadata.setVisibility(new Boolean("true"));
            try {
                if (metadataFile != null && metadataFile.getFileData().length != 0) {
                    metadataString = new String(metadataFile.getFileData(), "ISO-8859-1");
                    metadata.setMetadataFile(metadataString);
                    metadata = parseMetadata.parseMetadata(metadataString, metadata, this.path);
                }
            } catch (SAXParseException e) {
                badXmls.add(new String("badMetadata"));
                return badXmls;
            } catch (Exception e) {
                badXmls.add(new String("badMetadata"));
                return badXmls;
            }

            persistentMetadata.simpleLockWrite(metadata);

            List xmlFilesList = getXmlFilesList(xmlZipFile);
            if (xmlFilesList == null)
                throw new NotExecuteException("error.badMetadataFile");
            Iterator it = xmlFilesList.iterator();

            while (it.hasNext()) {
                LabelValueBean labelValueBean = (LabelValueBean) it.next();
                String xmlFile = labelValueBean.getValue();
                String xmlFileName = labelValueBean.getLabel();

                try {
                    ParseQuestion parseQuestion = new ParseQuestion();
                    IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();

                    parseQuestion.parseQuestion(xmlFile, new InfoQuestion(), this.path);
                    IQuestion question = new Question();
                    question.setMetadata(metadata);
                    question.setXmlFile(xmlFile);
                    question.setXmlFileName(xmlFileName);
                    question.setVisibility(new Boolean("true"));
                    persistentQuestion.simpleLockWrite(question);
                    xmlNumber++;
                } catch (SAXParseException e) {
                    if (metadataString != null) {
                        persistentMetadata.simpleLockWrite(metadata);
                        metadata
                                .setMetadataFile(removeLocation(metadataString, xmlZipFile.getFileName()));
                    }
                    badXmls.add(xmlFileName);
                } catch (ParseQuestionException e) {
                    if (metadataString != null) {
                        persistentMetadata.simpleLockWrite(metadata);
                        metadata
                                .setMetadataFile(removeLocation(metadataString, xmlZipFile.getFileName()));
                    }
                    badXmls.add(xmlFileName + e);
                } catch (Exception e) {
                    if (metadataString != null) {
                        persistentMetadata.simpleLockWrite(metadata);
                        metadata
                                .setMetadataFile(removeLocation(metadataString, xmlZipFile.getFileName()));
                    }
                    badXmls.add(xmlFileName);
                }
            }

            if (xmlNumber == 0) {
                persistentMetadata.simpleLockWrite(metadata);
                persistentMetadata.delete(metadata);
            } else {
                persistentMetadata.simpleLockWrite(metadata);
                metadata.setNumberOfMembers(new Integer(xmlNumber));
            }

            return badXmls;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private String removeLocation(String metadataFile, String xmlName) throws FenixServiceException {
        TransformerFactory tf = TransformerFactory.newInstance();
        java.io.StringWriter result = new java.io.StringWriter();
        try {
            URL xsl = new URL("file:///" + path.concat("WEB-INF/ims/removeXmlLocation.xsl"));
            String doctypePublic = new String(
                    "-//Technical Superior Institute//DTD Test Metadata 1.1//EN");
            String doctypeSystem = new String("metadataFile://"
                    + path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
            String auxFile = new String();

            //			<!DOCTYPE questestinterop SYSTEM "ims_qtiasiv1p2p1.dtd" [
            //			<!ELEMENT ims_render_object (response_label+)>
            //			<!ATTLIST ims_render_object
            //					shuffle (Yes|No) 'No'
            //					orientation (Row|Column) #REQUIRED>
            //			]>

            int index = metadataFile.indexOf("<!DOCTYPE");
            if (index != -1) {
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

    //	private String changeDocumentType(String file, boolean metadata) throws
    // FenixServiceException
    //	{
    //		TransformerFactory tf = TransformerFactory.newInstance();
    //		java.io.StringWriter result = new java.io.StringWriter();
    //		try
    //		{
    //			URL xsl = new URL("file:///" +
    // path.concat("WEB-INF/ims/changeDocumentType.xsl"));
    //			String doctypePublic = null;
    //			String doctypeSystem = null;
    //			if (metadata)
    //			{
    //				doctypePublic = new String("-//Technical Superior Institute//DTD Test
    // Metadata 1.1//EN");
    //				doctypeSystem = new String("file:///" +
    // path.concat("WEB-INF/ims/imsmd2_rootv1p2.dtd"));
    //			}
    //			else
    //			{
    //				doctypePublic =
    //					new String("-//Technical Superior Institute//DTD Test XmlDocument
    // 1.1//EN");
    //				doctypeSystem = new String("file:///" +
    // path.concat("WEB-INF/ims/qtiasiv1p2.dtd"));
    //			}
    //
    //			String auxFile = new String();
    //			int index = file.indexOf("<!DOCTYPE");
    //			if (index != -1)
    //			{
    //				auxFile = file.substring(0, index);
    //				int index2 = file.indexOf(">", index) + 1;
    //				auxFile = file.substring(index2, file.length());
    //			}
    //			file = auxFile;
    //			Transformer transformer = tf.newTransformer(new
    // StreamSource(xsl.openStream()));
    //			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctypePublic);
    //			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctypeSystem);
    //			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-LATIN-1");
    //			Source source = new StreamSource(new StringReader(file));
    //			transformer.transform(source, new StreamResult(result));
    //		}
    //		catch (javax.xml.transform.TransformerConfigurationException e)
    //		{
    //			throw new FenixServiceException(e);
    //		}
    //		catch (javax.xml.transform.TransformerException e)
    //		{
    //			throw new FenixServiceException(e);
    //		}
    //		catch (FileNotFoundException e)
    //		{
    //			throw new FenixServiceException(e);
    //		}
    //		catch (IOException e)
    //		{
    //			throw new FenixServiceException(e);
    //		}
    //		catch (Exception e)
    //		{
    //			throw new FenixServiceException(e);
    //		}
    //
    //		return result.toString();
    //	}

    private List getXmlFilesList(FormFile xmlZipFile) throws FenixServiceException {
        List xmlFilesList = new ArrayList();
        ZipInputStream zipFile = null;

        try {
            if (xmlZipFile.getContentType().equals("text/xml")) {
                xmlFilesList.add(new LabelValueBean(xmlZipFile.getFileName(), new String(xmlZipFile
                        .getFileData(), "ISO-8859-1")));
                //						changeDocumentType(new String(xmlZipFile.getFileData(),
                // "ISO-8859-1"), false)));
            } else {
                zipFile = new ZipInputStream(xmlZipFile.getInputStream());
                while (true) {

                    ZipEntry entry = zipFile.getNextEntry();
                    String xmlString = new String();
                    if (entry == null)
                        break;
                    byte[] b = new byte[1000];
                    int readed = 0;
                    while ((readed = zipFile.read(b)) > -1)
                        xmlString = xmlString.concat(new String(b, 0, readed, "ISO-8859-1"));
                    xmlFilesList.add(new LabelValueBean(entry.getName(), xmlString));
                }
                zipFile.close();
            }
        } catch (Exception e) {
            return null;
        }
        return xmlFilesList;
    }
}