/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
import net.sourceforge.fenixedu.persistenceTier.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.xml.sax.SAXParseException;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertExerciseVariation implements IService {

    private String path = new String();

    public InsertExerciseVariation() {
    }

    public List run(Integer executionCourseId, Integer metadataId, FormFile xmlZipFile, String path)
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

            IMetadata metadata = (IMetadata) persistentSuport.getIPersistentMetadata().readByOID(
                    Metadata.class, metadataId);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
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
                    question.setXmlFileName(persistentQuestion.correctFileName(xmlFileName, metadataId));
                    question.setVisibility(new Boolean("true"));
                    persistentQuestion.simpleLockWrite(question);
                    xmlNumber++;
                } catch (SAXParseException e) {
                    badXmls.add(xmlFileName);
                } catch (ParseQuestionException e) {
                    badXmls.add(xmlFileName + e);
                } catch (Exception e) {
                    badXmls.add(xmlFileName);
                }
            }

            if (xmlNumber != 0)
                metadata.setNumberOfMembers(new Integer(metadata.getNumberOfMembers().intValue()
                        + xmlNumber));

            return badXmls;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    public List getXmlFilesList(FormFile xmlZipFile) {
        List xmlFilesList = new ArrayList();
        ZipInputStream zipFile = null;

        try {
            if (xmlZipFile.getContentType().equals("text/xml")) {
                xmlFilesList.add(new LabelValueBean(xmlZipFile.getFileName(), new String(xmlZipFile
                        .getFileData(), "ISO-8859-1")));
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