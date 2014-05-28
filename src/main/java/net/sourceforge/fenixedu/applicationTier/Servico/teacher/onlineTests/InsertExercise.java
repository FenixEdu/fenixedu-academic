/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.utilTests.Element;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.util.LabelValueBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.servlets.commons.UploadedFile;
import pt.ist.fenixframework.Atomic;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class InsertExercise {

    private static final Logger logger = LoggerFactory.getLogger(InsertExercise.class);

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    public List<String> run(ExecutionCourse executionCourse, UploadedFile xmlZipFile) throws FenixServiceException {

        List<String> badXmls = new ArrayList<String>();
        boolean createAny = false;
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Collection<List<LabelValueBean>> allXmlList = getListOfExercisesList(xmlZipFile);
        for (List<LabelValueBean> xmlFilesList : allXmlList) {
            Map<String, Question> questionMap = new HashMap<String, Question>();
            List<LabelValueBean> metadatas = new ArrayList<LabelValueBean>();
            // if (xmlFilesList == null || xmlFilesList.size() == 0) {
            // throw new InvalidXMLFilesException();
            // }
            for (LabelValueBean labelValueBean : xmlFilesList) {
                String xmlFile = labelValueBean.getValue();
                String xmlFileName = labelValueBean.getLabel();
                try {
                    ParseSubQuestion parseQuestion = new ParseSubQuestion();
                    parseQuestion.parseSubQuestion(xmlFile);
                    Question question = new Question();
                    question.setXmlFile(xmlFile);
                    question.setXmlFileName(xmlFileName);
                    question.setVisibility(new Boolean("true"));
                    questionMap.put(xmlFileName, question);
                } catch (DomainException domainException) {
                    throw domainException;
                } catch (ParseQuestionException e) {
                    metadatas.add(labelValueBean);
                }
            }

            if (questionMap.size() != 0) {
                Metadata metadata = null;
                for (LabelValueBean labelValueBean : metadatas) {
                    String xmlFile = labelValueBean.getValue();
                    String xmlFileName = labelValueBean.getLabel();
                    ParseMetadata parse = new ParseMetadata();
                    try {
                        Vector<Element> vector = parse.parseMetadata(xmlFile);
                        List<Question> listToThisMetadata = getListToThisMetadata(questionMap, parse.getMembers());
                        if (listToThisMetadata.size() != 0) {
                            metadata = new Metadata(executionCourse, xmlFile, vector);
                            metadata.getQuestions().addAll(listToThisMetadata);
                        } else {
                            badXmls.add(xmlFileName + ": Metadata sem exercício associado.");
                        }
                    } catch (ParseException e) {
                        badXmls.add(xmlFileName + e);
                    }
                }
                if (metadata == null) {
                    metadata = new Metadata(executionCourse, null, null);
                    metadata.getQuestions().addAll(questionMap.values());
                }
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

    private List<Question> getListToThisMetadata(Map<String, Question> questionMap, List<String> members) {
        List<Question> result = new ArrayList<Question>();
        if (members.size() != 0) {
            for (String member : members) {
                Question question = questionMap.get(member);
                if (question != null) {
                    result.add(question);
                    questionMap.remove(member);
                }
            }
        } else {
            result.addAll(questionMap.values());
            questionMap = new HashMap<String, Question>();
        }
        return result;
    }

    private Collection<List<LabelValueBean>> getListOfExercisesList(UploadedFile xmlZipFile) {
        Map<String, List<LabelValueBean>> xmlListMap = new HashMap<String, List<LabelValueBean>>();
        try {
            if (validFileFormat(xmlZipFile.getContentType(), xmlZipFile.getName())) {
                xmlListMap = readFromZip(xmlListMap, xmlZipFile.getInputStream(), "");
            } else {
                List<LabelValueBean> xmlList = new ArrayList<LabelValueBean>();
                if (xmlZipFile.getContentType().equals("text/xml") || xmlZipFile.getContentType().equals("application/xml")) {
                    if (xmlZipFile.getSize() <= FILE_SIZE_LIMIT) {
                        xmlList.add(new LabelValueBean(xmlZipFile.getName(), new String(xmlZipFile.getFileData(), "ISO-8859-1")));
                    } else {
                        xmlList.add(new LabelValueBean(xmlZipFile.getName(), null));
                    }
                }
                xmlListMap.put("", xmlList);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        return xmlListMap.values();
    }

    private boolean validFileFormat(String contentType, String fileExtension) {
        //Zip file
        if (contentType.equals("application/zip") || contentType.equals("application/x-zip")
                || contentType.equals("application/x-zip-compressed") || contentType.equals("multipart/x-zip")) {
            return true;
        }
        //Zip file. Sometimes browser dont recognize the zip format and send like unknown application
        if (contentType.equals("application/octet-stream") || contentType.equals("application/x-compress")
                || contentType.equals("application/x-compressed")) {
            return fileExtension.endsWith(".zip");
        }
        return false;
    }

    private Map<String, List<LabelValueBean>> readFromZip(Map<String, List<LabelValueBean>> xmlListMap,
            InputStream zipInputStream, String dirBaseName) throws IOException {

        File zipFile = pt.utl.ist.fenix.tools.util.FileUtils.copyToTemporaryFile(zipInputStream);
        File unzipDir = null;
        try {
            unzipDir = pt.utl.ist.fenix.tools.util.FileUtils.unzipFile(zipFile);
            if (!unzipDir.isDirectory()) {
                throw new IOException("error");
            }
            for (final File ofile : unzipDir.listFiles()) {
                if (ofile.getName().equals(zipFile.getName())) {
                    ofile.delete();
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            zipFile.delete();
        }

        recursiveZipProcess(unzipDir, dirBaseName, xmlListMap);
        return xmlListMap;
    }

    private void recursiveZipProcess(File unzipDir, String dirBaseName, Map<String, List<LabelValueBean>> xmlListMap)
            throws IOException {
        File[] filesInZip = unzipDir.listFiles();
        Arrays.sort(filesInZip);
        for (File file : filesInZip) {
            if (file.isDirectory()) {
                recursiveZipProcess(file, file.getName(), xmlListMap);
            } else {
                FileInputStream is = new FileInputStream(file);
                if (file.getName().endsWith("zip")) {
                    xmlListMap = readFromZip(xmlListMap, is, file.getName());
                } else {
                    final int posSlash = file.getName().lastIndexOf('/');
                    final List<LabelValueBean> labelValueBeans;
                    final String dirName = (posSlash > 0) ? file.getName().substring(0, posSlash) : dirBaseName;
                    if (xmlListMap.containsKey(dirName)) {
                        labelValueBeans = xmlListMap.get(dirName);
                    } else {
                        labelValueBeans = new ArrayList<LabelValueBean>();
                        xmlListMap.put(dirName, labelValueBeans);
                    }
                    final StringBuilder stringBuilder = new StringBuilder();
                    final byte[] b = new byte[1000];

                    for (int readed = 0; (readed = is.read(b)) > -1; stringBuilder.append(new String(b, 0, readed, "ISO-8859-1"))) {
                        // nothing to do :o)
                    }
                    if (stringBuilder.length() <= FILE_SIZE_LIMIT) {
                        labelValueBeans.add(new LabelValueBean(file.getName(), stringBuilder.toString()));
                    } else {
                        labelValueBeans.add(new LabelValueBean(file.getName(), null));
                    }

                }
            }

            unzipDir.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final InsertExercise serviceInstance = new InsertExercise();

    @Atomic
    public static List<String> runInsertExercise(ExecutionCourse executionCourseId, UploadedFile xmlZipFile)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, xmlZipFile);
    }

}