/*
 * Created on 26/Ago/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.tests.CardinalityType;
import Util.tests.QuestionDifficultyType;
import Util.tests.QuestionType;
import Util.tests.RenderChoise;
import Util.tests.ResponseCondition;
import Util.tests.ResponseProcessing;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class CreateExerciseTest extends ServiceNeedsAuthenticationTestCase {

    public CreateExerciseTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/teacher/testCreateExerciseDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets_templates/servicos/teacher/testExpectedCreateExerciseDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "CreateExercise";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "D3673", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "L46730", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "L46730", "pass", getApplication()};
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Integer executionCourseId = new Integer(34033);
        //METADATA
        Integer metadataId = new Integer(681);

        //QUESTION

        String questionText = new String("questionText");
        String secondQuestionText = null;
        String[] options = new String[] { "op1", "op2", "op3"};
        String[] correctOptions = new String[] { "1", "2"};
        String[] shuffle = new String[] { "1", "2", "3"};

        InfoQuestion infoQuestion = getLIDInfoQuestion(correctOptions);

        String correctFeedbackText = new String("correctFeedbackText");
        String wrongFeedbackText = new String("");
        Boolean breakLineBeforeResponseBox = new Boolean(false);
        Boolean breakLineAfterResponseBox = new Boolean(false);
        String path = new String(
                "e:\\eclipse-m8\\workspace\\fenix-head\\build\\standalone\\ciapl\\");

        Object[] args = { executionCourseId, metadataId, null, null, null,
                null, null, null, null, infoQuestion, questionText,
                secondQuestionText, options, correctOptions, shuffle,
                correctFeedbackText, wrongFeedbackText,
                breakLineBeforeResponseBox, breakLineAfterResponseBox, path};
        return args;
    }

    protected Object[] getArguments4NewExercise() {

        Integer executionCourseId = new Integer(34033);
        //METADATA

        Integer metadataId = null;
        String author = new String("author");
        String description = new String("description");
        QuestionDifficultyType difficulty = new QuestionDifficultyType(
                QuestionDifficultyType.EASY);
        String mainSubject = new String("Matéria Principal");
        String secondarySubject = new String("Matéria Secundária");
        Calendar learningTime = Calendar.getInstance();
        learningTime.set(Calendar.HOUR_OF_DAY, 20);
        learningTime.set(Calendar.MINUTE, 0);
        learningTime.set(Calendar.SECOND, 0);
        String level = new String("1");
        //QUESTION
        String[] options = new String[] { "op1", "op2", "op3"};
        String[] correctOptions = new String[] { "1", "2"};
        String[] shuffle = new String[] { "1", "2", "3"};

        InfoQuestion infoQuestion = getLIDInfoQuestion(correctOptions);
        String questionText = new String("questionText");
        String secondQuestionText = null;

        String correctFeedbackText = new String("correctFeedbackText");
        String wrongFeedbackText = new String("");
        Boolean breakLineBeforeResponseBox = null;
        Boolean breakLineAfterResponseBox = null;
        String path = new String(
                "e:\\eclipse-m8\\workspace\\fenix-head\\build\\standalone\\ciapl\\");

        Object[] args = { executionCourseId, metadataId, author, description,
                difficulty, mainSubject, secondarySubject, learningTime, level,
                infoQuestion, questionText, secondQuestionText, options,
                correctOptions, shuffle, correctFeedbackText,
                wrongFeedbackText, breakLineBeforeResponseBox,
                breakLineAfterResponseBox, path};
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {
            IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());

            System.out.println("Test args 4 new Variation");
            Object[] args = getAuthorizeArguments();
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);
            System.out.println("Test args 4 new Exercise");
            args = getArguments4NewExercise();
            ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);
            System.out.println("Compare");
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());

        } catch (FenixServiceException ex) {
            fail("CreateExerciseTest " + ex);
        } catch (Exception ex) {
            fail("CreateExerciseTest " + ex);
        }
    }

    private InfoQuestion getLIDInfoQuestion(String[] correctOptions) {
        InfoQuestion infoQuestion = new InfoQuestion();

        QuestionType questionType = new QuestionType(QuestionType.LID);
        RenderChoise render = new RenderChoise();
        render.setShuffle("YES");
        questionType.setRender(render);
        CardinalityType cardinalityType = new CardinalityType(
                CardinalityType.MULTIPLE);
        questionType.setCardinalityType(cardinalityType);

        Integer optionNumber = new Integer(3);
        //RESPROCESSING

        List responseConditionList = new ArrayList();
        List responseProcessingInstructionsList = new ArrayList();

        for (int i = 0; i < correctOptions.length; i++) {
            String correctResponse;
            correctResponse = correctOptions[i];

            ResponseCondition responseCondition = new ResponseCondition(
                    ResponseCondition.VAREQUAL_XML_STRING, correctResponse,
                    new String("1"));
            responseConditionList.add(responseCondition);
        }
        if (responseConditionList != null && responseConditionList.size() != 0) {
            ResponseProcessing responseProcessing = new ResponseProcessing(
                    responseConditionList, new Double(2), new Integer(
                            ResponseProcessing.SET), null, true);
            responseProcessingInstructionsList.add(responseProcessing);
        }

        infoQuestion.setOptionNumber(optionNumber);
        infoQuestion.setQuestionType(questionType);
        infoQuestion.setQuestionValue(new Double(2));

        infoQuestion
                .setResponseProcessingInstructions(responseProcessingInstructionsList);

        return infoQuestion;
    }
}