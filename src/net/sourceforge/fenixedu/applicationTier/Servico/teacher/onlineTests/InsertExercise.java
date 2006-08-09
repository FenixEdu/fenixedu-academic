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
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.utilTests.Element;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class InsertExercise extends Service {

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    public List<String> run(Integer executionCourseId, FormFile xmlZipFile, String path)
            throws FenixServiceException, ExcepcaoPersistencia {
        List<String> badXmls = new ArrayList<String>();
        String replacedPath = path.replace('\\', '/');
        boolean createAny = false;
        ExecutionCourse executionCourse = (ExecutionCourse) rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Collection<List<LabelValueBean>> allXmlList = getListOfExercisesList(xmlZipFile);
        for (List<LabelValueBean> xmlFilesList : allXmlList) {
            List<Question> questionList = new ArrayList<Question>();
            List<LabelValueBean> metadatas = new ArrayList<LabelValueBean>();
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
                    question.setXmlFile(xmlFile);
                    question.setXmlFileName(xmlFileName);
                    question.setVisibility(new Boolean("true"));
                    questionList.add(question);
                } catch (DomainException domainException) {
                    throw domainException;
                } catch (ParseException e) {
                    metadatas.add(labelValueBean);
                } catch (ParseQuestionException e) {
                    badXmls.add(xmlFileName + e);
                }
            }

            if (questionList.size() != 0) {
                Metadata metadata = null;
                for (LabelValueBean labelValueBean : metadatas) {
                    String xmlFile = labelValueBean.getValue();
                    String xmlFileName = labelValueBean.getLabel();
                    ParseMetadata parse = new ParseMetadata();
                    try {
                        Vector<Element> vector = parse.parseMetadata(xmlFile, path);
                        if (metadata == null) {
                            metadata = new Metadata(executionCourse, xmlFile, vector);
                        } else {
                            badXmls.add(xmlFileName
                                    + ": Já foi assumido outro ficheiro como ficheiro de metadata.");
                        }
                    } catch (ParseException e) {
                        badXmls.add(xmlFileName);
                    }
                }
                if (metadata == null) {
                    metadata = new Metadata(executionCourse, null, null);
                }
                metadata.getQuestions().addAll(questionList);
                createAny = true;
            } else {
                for (LabelValueBean labelValueBean : metadatas) {
                    badXmls.add(labelValueBean.getLabel());
                }
            }

        }
        if (!createAny) {
            throw new InvalidXMLFilesException();
        }
        return badXmls;
    }

    private Collection<List<LabelValueBean>> getListOfExercisesList(FormFile xmlZipFile) {
        Map<String, List<LabelValueBean>> xmlListMap = new HashMap<String, List<LabelValueBean>>();
        try {
            if (xmlZipFile.getContentType().equals("application/x-zip-compressed")
                    || xmlZipFile.getContentType().equals("application/zip")) {
                ZipInputStream zipFile = new ZipInputStream(xmlZipFile.getInputStream());
                for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile
                        .getNextEntry()) {
                    final int posSlash = entry.getName().lastIndexOf('/');
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
                    for (int readed = 0; (readed = zipFile.read(b)) > -1; stringBuilder
                            .append(new String(b, 0, readed, "ISO-8859-1"))) {
                        // nothing to do :o)
                    }
                    if (stringBuilder.length() <= FILE_SIZE_LIMIT) {
                        labelValueBeans
                                .add(new LabelValueBean(entry.getName(), stringBuilder.toString()));
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