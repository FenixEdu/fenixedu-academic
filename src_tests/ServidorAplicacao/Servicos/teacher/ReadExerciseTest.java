/*
 * Created on 25/Set/2003
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoMetadata;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteExercise;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.Metadata;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import UtilTests.ParseQuestion;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class ReadExerciseTest extends ServiceNeedsAuthenticationTestCase {

    public ReadExerciseTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadExerciseDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExercise";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "D3673", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "L46730", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34033);
        Integer metadataId = new Integer(133);
        Integer variationId = new Integer(9333);
        String path = new String("e:\\eclipse-m7\\workspace\\fenix\\build\\standalone\\");

        Object[] args = { executionCourseId, metadataId, variationId, path };
        return args;
    }

    protected Object[] getAuthorizeArguments1() {
        Integer executionCourseId = new Integer(34033);
        Integer metadataId = new Integer(133);
        Integer variationId = new Integer(-1);
        String path = new String("e:\\eclipse-m7\\workspace\\fenix\\build\\standalone\\");

        Object[] args = { executionCourseId, metadataId, variationId, path };
        return args;
    }

    protected Object[] getAuthorizeArguments2() {
        Integer executionCourseId = new Integer(34033);
        Integer metadataId = new Integer(133);
        Integer variationId = new Integer(-2);
        String path = new String("e:\\eclipse-m7\\workspace\\fenix\\build\\standalone\\");

        Object[] args = { executionCourseId, metadataId, variationId, path };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {
            authenticateUser(getAuthenticatedAndAuthorizedUser());
            Object[] args = getAuthorizeArguments();
            Object[] args1 = getAuthorizeArguments1();
            Object[] args2 = getAuthorizeArguments2();

            execute(args);
            execute(args1);
            execute(args2);

        } catch (FenixServiceException ex) {
            fail("ReadExerciseTest " + ex);
        } catch (Exception ex) {
            fail("ReadExerciseTest " + ex);
        }
    }

    private void execute(Object[] args) throws FenixServiceException, Exception {
        SiteView serviceSiteView = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                getNameOfServiceToBeTested(), args);

        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", args[1]);
        Query queryCriteria = new QueryByCriteria(Metadata.class, criteria);
        IMetadata metadata = (IMetadata) broker.getObjectByQuery(queryCriteria);

        criteria = new Criteria();
        criteria.addEqualTo("idInternal", args[0]);
        queryCriteria = new QueryByCriteria(ExecutionCourse.class, criteria);
        IExecutionCourse executionCourse = (IExecutionCourse) broker.getObjectByQuery(queryCriteria);
        broker.close();

        InfoMetadata infoMetadata = InfoMetadata.newInfoFromDomain(metadata);
        Iterator it = metadata.getVisibleQuestions().iterator();
        List visibleInfoQuestions = new ArrayList();
        List xmlNames = new ArrayList();
        while (it.hasNext()) {
            IQuestion question = (IQuestion) it.next();
            xmlNames.add(new LabelValueBean(question.getXmlFileName(), question.getIdInternal()
                    .toString()));
            if ((question.getIdInternal().equals(args[2]))
                    || ((Integer) args[2]).equals(new Integer(-2))) {
                InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
                ParseQuestion parse = new ParseQuestion();
                try {
                    infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion,
                            (String) args[3]);
                    infoQuestion.setResponseProcessingInstructions(parse.newResponseList(infoQuestion
                            .getResponseProcessingInstructions(), infoQuestion.getOptions()));
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }

                visibleInfoQuestions.add(infoQuestion);
            }
        }
        infoMetadata.setVisibleQuestions(visibleInfoQuestions);

        InfoMetadata serviceInfoMetadata = ((InfoSiteExercise) serviceSiteView.getComponent())
                .getInfoMetadata();
        if (!infoMetadata.getIdInternal().equals(serviceInfoMetadata.getIdInternal()))
            fail("ReadExerciseTest " + "InfoMetadata notEqual");

        InfoExecutionCourse serviceInfoExecutionCourse = ((InfoSiteExercise) serviceSiteView
                .getComponent()).getExecutionCourse();
        if (!((InfoExecutionCourse) Cloner.get(executionCourse)).equals(serviceInfoExecutionCourse))
            fail("ReadExerciseTest " + "InfoExecutionCourse notEqual");

        /*
         * List serviceQuestionNames = ((InfoSiteExercise)
         * serviceSiteView.getComponent()) .getQuestionNames(); if
         * (!(serviceQuestionNames.equals(xmlNames))) fail("ReadExerciseTest " +
         * "QuestionNamesList notEqual");
         */
    }
}