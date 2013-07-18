/*
 * Created on 23/Set/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.servlets.commons.UploadedFile;

/**
 * @author Susana Fernandes
 */
public class InsertExerciseVariation {

    private static final double FILE_SIZE_LIMIT = Math.pow(2, 20);

    protected List run(Integer executionCourseId, Integer metadataId, UploadedFile xmlZipFile, String path)
            throws FenixServiceException, NotExecuteException {
        List<String> badXmls = new ArrayList<String>();
        String replacedPath = path.replace('\\', '/');
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Metadata metadata = RootDomainObject.getInstance().readMetadataByOID(metadataId);
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
                ParseSubQuestion parseQuestion = new ParseSubQuestion();

                parseQuestion.parseSubQuestion(xmlFile, replacedPath);
                Question question = new Question();
                question.setMetadata(metadata);
                question.setXmlFile(xmlFile);
                question.setXmlFileName(metadata.correctFileName(xmlFileName));
                question.setVisibility(new Boolean("true"));
            } catch (ParseQuestionException e) {
                badXmls.add(xmlFileName + e);
            }
        }

        return badXmls;
    }

    private List<LabelValueBean> getXmlFilesList(UploadedFile xmlZipFile) {
        List<LabelValueBean> xmlFilesList = new ArrayList<LabelValueBean>();
        ZipInputStream zipFile = null;

        try {
            if (xmlZipFile.getContentType().equals("text/xml") || xmlZipFile.getContentType().equals("application/xml")) {
                if (xmlZipFile.getSize() <= FILE_SIZE_LIMIT) {
                    xmlFilesList
                            .add(new LabelValueBean(xmlZipFile.getName(), new String(xmlZipFile.getFileData(), "ISO-8859-1")));
                }
            } else {
                zipFile = new ZipInputStream(xmlZipFile.getInputStream());
                for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile.getNextEntry()) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    final byte[] b = new byte[1000];
                    for (int readed = 0; (readed = zipFile.read(b)) > -1; stringBuilder.append(new String(b, 0, readed,
                            "ISO-8859-1"))) {
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

    // Service Invokers migrated from Berserk

    private static final InsertExerciseVariation serviceInstance = new InsertExerciseVariation();

    @Service
    public static List runInsertExerciseVariation(Integer executionCourseId, Integer metadataId, UploadedFile xmlZipFile,
            String path) throws FenixServiceException, NotExecuteException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, metadataId, xmlZipFile, path);
    }

}