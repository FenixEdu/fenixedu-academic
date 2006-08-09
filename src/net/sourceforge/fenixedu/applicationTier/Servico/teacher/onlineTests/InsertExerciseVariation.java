/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class InsertExerciseVariation extends Service {

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    public List run(Integer executionCourseId, Integer metadataId, FormFile xmlZipFile, String path)
            throws FenixServiceException, NotExecuteException, ExcepcaoPersistencia {
        List<String> badXmls = new ArrayList<String>();
        String replacedPath = path.replace('\\', '/');
        ExecutionCourse executionCourse = (ExecutionCourse) rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Metadata metadata = (Metadata) rootDomainObject.readMetadataByOID(metadataId);
        if (metadata == null) {
            throw new InvalidArgumentsServiceException();
        }
        List<LabelValueBean> xmlFilesList = getXmlFilesList(xmlZipFile);
        if (xmlFilesList == null || xmlFilesList.size() == 0) {
            throw new InvalidXMLFilesException();
        }

        for (LabelValueBean labelValueBean : xmlFilesList) {
            String xmlFile = labelValueBean.getValue();
            String xmlFileName = labelValueBean.getLabel();

            try {
                ParseQuestion parseQuestion = new ParseQuestion();

                parseQuestion.parseQuestion(xmlFile, new InfoQuestion(), replacedPath);
                Question question = new Question();
                question.setMetadata(metadata);
                question.setXmlFile(xmlFile);
                question.setXmlFileName(metadata.correctFileName(xmlFileName));
                question.setVisibility(new Boolean("true"));
            } catch (ParseException e) {
                badXmls.add(xmlFileName);
            } catch (ParseQuestionException e) {
                badXmls.add(xmlFileName + e);
            } catch (Exception e) {
                badXmls.add(xmlFileName);
            }
        }

        return badXmls;
    }

    private List<LabelValueBean> getXmlFilesList(FormFile xmlZipFile) {
        List<LabelValueBean> xmlFilesList = new ArrayList<LabelValueBean>();
        ZipInputStream zipFile = null;

        try {
            if (xmlZipFile.getContentType().equals("text/xml")
                    || xmlZipFile.getContentType().equals("application/xml")) {
                if (xmlZipFile.getFileSize() <= FILE_SIZE_LIMIT) {
                    xmlFilesList.add(new LabelValueBean(xmlZipFile.getFileName(), new String(xmlZipFile
                            .getFileData(), "ISO-8859-1")));
                }
            } else {
                zipFile = new ZipInputStream(xmlZipFile.getInputStream());
                for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile
                        .getNextEntry()) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    final byte[] b = new byte[1000];
                    for (int readed = 0; (readed = zipFile.read(b)) > -1; stringBuilder
                            .append(new String(b, 0, readed, "ISO-8859-1"))) {
                        // nothing to do :o)
                    }
                    if (stringBuilder.length() <= FILE_SIZE_LIMIT) {
                        xmlFilesList.add(new LabelValueBean(entry.getName(), stringBuilder.toString()));
                    }
                }
                zipFile.close();
            }
        } catch (Exception e) {
            return null;
        }
        return xmlFilesList;
    }
}