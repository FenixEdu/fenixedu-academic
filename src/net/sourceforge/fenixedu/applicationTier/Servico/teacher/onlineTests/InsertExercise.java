/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidMetadataException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.xml.sax.SAXParseException;

/**
 * @author Susana Fernandes
 */
public class InsertExercise extends Service {

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    public List<String> run(Integer executionCourseId, FormFile xmlZipFile, String path)
            throws FenixServiceException, ExcepcaoPersistencia {
        List<String> badXmls = new ArrayList<String>();
        int xmlNumber = 0;
        String replacedPath = path.replace('\\', '/');

        ExecutionCourse executionCourse = (ExecutionCourse) rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Collection<List<LabelValueBean>> allXmlList = getListOfExercisesList(xmlZipFile);
        for (List<LabelValueBean> xmlFilesList : allXmlList) {
            if (xmlFilesList == null || xmlFilesList.size() == 0) {
                throw new InvalidXMLFilesException();
            }

            Metadata metadata = null;
            try {
                metadata = DomainFactory.makeMetadata(executionCourse, null, replacedPath);
            } catch (DomainException e) {
                throw new InvalidMetadataException();
            }

            for (LabelValueBean labelValueBean : xmlFilesList) {
                String xmlFile = labelValueBean.getValue();
                String xmlFileName = labelValueBean.getLabel();

                try {
                    ParseQuestion parseQuestion = new ParseQuestion();

                    parseQuestion.parseQuestion(xmlFile, new InfoQuestion(), replacedPath);
                    Question question = DomainFactory.makeQuestion();
                    question.setMetadata(metadata);
                    question.setXmlFile(xmlFile);
                    question.setXmlFileName(xmlFileName);
                    question.setVisibility(new Boolean("true"));
                    xmlNumber++;
                } catch (DomainException domainException) {
                    throw domainException;
                } catch (SAXParseException e) {
                    try {
                        metadata.parseAndSetMetadataFile(xmlFile, replacedPath);
                    } catch (DomainException e1) {
                        badXmls.add(xmlFileName);
                    }
                } catch (ParseQuestionException e) {
                    badXmls.add(xmlFileName + e);
                } catch (Exception e) {
                    badXmls.add(xmlFileName);
                }
            }
        }
        if (xmlNumber == 0) {
            throw new InvalidXMLFilesException();
        }
        return badXmls;
    }

    private Collection<List<LabelValueBean>> getListOfExercisesList(FormFile xmlZipFile) {
        List<List<LabelValueBean>> allXmlList = new ArrayList<List<LabelValueBean>>();

        Map<String, List<LabelValueBean>> xmlListMap = new HashMap<String, List<LabelValueBean>>();
        try {
            if (xmlZipFile.getContentType().equals("application/x-zip-compressed")
                    || xmlZipFile.getContentType().equals("application/zip")) {
                ZipInputStream zipFile = new ZipInputStream(xmlZipFile.getInputStream());
                for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile
                        .getNextEntry()) {
                    if (entry.isDirectory()) {
                        throw new RuntimeException("unsupported.if.this.happens.rethink");
                    }
                    final int posSlash = entry.getName().indexOf('/');
                    final List<LabelValueBean> labelValueBeans;
                    final String dirName = (posSlash > 0) ? entry.getName().substring(0, posSlash) : "";
                    if (xmlListMap.containsKey(dirName)) {
                        labelValueBeans = xmlListMap.get(dirName);
                    } else {
                        labelValueBeans = new ArrayList<LabelValueBean>();
                        xmlListMap.put(dirName, labelValueBeans);
                    }

                    final StringBuilder stringBuilder = new StringBuilder();
                    final byte[] b = new byte[1000];
                    for (int readed = 0; (readed = zipFile.read(b)) > -1; stringBuilder .append(new String(b, 0, readed, "ISO-8859-1"))) {
                            // nothing to do :o)
                    }
                    if (stringBuilder.length() <= FILE_SIZE_LIMIT) {
                        labelValueBeans.add(new LabelValueBean(entry.getName(), stringBuilder.toString()));
                    } else {
                        labelValueBeans.add(new LabelValueBean(entry.getName(), null));
                    }
                }
                zipFile.close();
            } else {
                List<LabelValueBean> xmlList = new ArrayList<LabelValueBean>();
                if (xmlZipFile.getContentType().equals("text/xml")
                        || xmlZipFile.getContentType().equals("application/xml")) {
                    if (xmlZipFile.getFileSize() <= FILE_SIZE_LIMIT) {
                        xmlList.add(new LabelValueBean(xmlZipFile.getFileName(), new String(xmlZipFile
                                .getFileData(), "ISO-8859-1")));
                    } else {
                        xmlList.add(new LabelValueBean(xmlZipFile.getFileName(), null));
                    }
                }
                xmlListMap.put("", xmlList);
            }
        } catch (Exception e) {
            return null;
        }

        return xmlListMap.values();
    }
}