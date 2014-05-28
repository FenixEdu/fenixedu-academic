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
 * Created on 14/Ago/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.teacher.onlineTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidMetadataException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ChangeStudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.CreateExercise;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.DeleteExercise;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.DeleteExerciseVariation;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.EditExercise;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.InsertExercise;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.InsertExerciseVariation;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadExercise;
import net.sourceforge.fenixedu.dataTransferObject.comparators.MetadataComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionDifficultyType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.RenderChoise;
import net.sourceforge.fenixedu.util.tests.RenderFIB;
import net.sourceforge.fenixedu.util.tests.ResponseCondition;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.TestQuestionChangesType;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.XMLQuestion;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.servlets.commons.UploadedFile;
import pt.ist.fenixWebFramework.servlets.filters.RequestWrapperFilter;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Susana Fernandes
 */
@Mapping(path = "/exercisesManagement", module = "teacher", formBean = "exerciseForm",
        functionality = ManageExecutionCourseDA.class)
public class ExercisesManagementAction extends ExecutionCourseBaseAction {

    private ActionForward doForward(HttpServletRequest request, String testsPath) {
        request.setAttribute("teacher$actual$page", "/teacher/onlineTests/" + testsPath + ".jsp");
        return new ActionForward("/onlineTests/testsFrame.jsp");
    }

    private static final Logger logger = LoggerFactory.getLogger(ExercisesManagementAction.class);

    public ActionForward prepareChooseExerciseType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("exerciseCode", getFromRequest(request, "exerciseCode"));
        request.setAttribute("questionsTypes", QuestionType.getAllTypes());
        request.setAttribute("cardinalityTypes", CardinalityType.getAllTypes());
        return doForward(request, "chooseExerciseType");
    }

    public ActionForward chooseQuestionType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Integer questionType = (Integer) ((DynaActionForm) form).get("questionType");
        if (questionType.intValue() == QuestionType.LID) {
            Integer cardinalityTypeId = (Integer) ((DynaActionForm) form).get("cardinalityType");

            if (cardinalityTypeId == null) {
                error(request, "chooseQuestionType", "message.cardinalityTypeRequired");
                return prepareChooseExerciseType(mapping, form, request, response);
            }
            ((DynaActionForm) form).set("optionNumber", new Integer(4));
            return prepareCreateExercise(mapping, form, request, response);
        } else if (questionType.intValue() == QuestionType.STR || questionType.intValue() == QuestionType.NUM) {
            ((DynaActionForm) form).set("cardinalityType", new Integer(CardinalityType.SINGLE));
            ((DynaActionForm) form).set("optionNumber", new Integer(1));
            return prepareCreateExercise(mapping, form, request, response);
        } else {
            error(request, "chooseQuestionType", "message.exerciseTypeRequired");
            return prepareChooseExerciseType(mapping, form, request, response);
        }

    }

    @Input
    public ActionForward prepareCreateExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Integer questionType = (Integer) ((DynaActionForm) form).get("questionType");
        // Integer cardinalityType = (Integer) ((DynaActionForm) form)
        // .get("cardinalityType");
        Integer optionNumber = (Integer) ((DynaActionForm) form).get("optionNumber");

        String[] options = (String[]) ((DynaActionForm) form).get("options");
        if (options == null || options.length == 0) {
            ((DynaActionForm) form).set("options", new String[optionNumber.intValue()]);
        }
        String[] correctOptions = (String[]) ((DynaActionForm) form).get("correctOptions");
        if (correctOptions == null || correctOptions.length == 0) {
            ((DynaActionForm) form).set("correctOptions", new String[optionNumber.intValue()]);
        }

        Integer[] signal = (Integer[]) ((DynaActionForm) form).get("signal");
        if (signal == null || signal.length == 0) {
            ((DynaActionForm) form).set("signal", new Integer[] { new Integer(ResponseCondition.VAREQUAL) });
        }

        List questionDifficultyList = (new QuestionDifficultyType()).getAllTypes();
        request.setAttribute("questionDifficultyList", questionDifficultyList);

        if (questionType.intValue() == QuestionType.STR) {
            request.setAttribute("signals", ResponseCondition.getConditionSignalsToStringQuestion());
        } else if (questionType.intValue() == QuestionType.NUM) {
            request.setAttribute("signals", ResponseCondition.getConditionSignalsToNumericalQuestion());
        }

        String maxCharString = (String) ((DynaActionForm) form).get("maxChar");
        String rowsString = (String) ((DynaActionForm) form).get("rows");

        String checkTextBoxParams = null;
        if (maxCharString != null && !maxCharString.equals("")) {
            checkTextBoxParams = "false";
        } else if (rowsString != null && !rowsString.equals("")) {
            checkTextBoxParams = "true";
        }

        ((DynaActionForm) form).set("response", new String[] { checkTextBoxParams });
        return doForward(request, "prepareCreateExercise");
    }

    public ActionForward createExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        // METADATA
        String author = (String) ((DynaActionForm) form).get("author");
        String description = (String) ((DynaActionForm) form).get("description");
        Integer difficulty = (Integer) ((DynaActionForm) form).get("difficulty");
        String mainSubject = (String) ((DynaActionForm) form).get("mainSubject");
        String secondarySubject = (String) ((DynaActionForm) form).get("secondarySubject");
        String learningHour = (String) ((DynaActionForm) form).get("learningHour");
        String learningMinute = (String) ((DynaActionForm) form).get("learningMinute");
        String level = (String) ((DynaActionForm) form).get("level");

        Calendar learningTime = null;
        if (learningHour != null || learningHour.equals("") || learningMinute != null || learningMinute.equals("")) {
            if (learningHour == null || learningHour.equals("")) {
                learningHour = "0";
            }
            if (learningMinute == null || learningMinute.equals("")) {
                learningMinute = "0";
            }
            learningTime = string2Hour(learningHour.concat(":" + learningMinute));
        }
        QuestionDifficultyType questionDifficultyType = null;
        if (difficulty != null) {
            questionDifficultyType = new QuestionDifficultyType(difficulty);
        }
        // QUESTION
        Integer questionTypeId = (Integer) ((DynaActionForm) form).get("questionType");
        Integer cardinalityTypeId = (Integer) ((DynaActionForm) form).get("cardinalityType");
        Integer optionNumber = (Integer) ((DynaActionForm) form).get("optionNumber");
        String questionValueString = (String) ((DynaActionForm) form).get("questionValue");
        Double questionValue = new Double(0);
        if (questionValueString != null && !questionValueString.equals("")) {
            questionValue = new Double(questionValueString);
        }
        String questionText = (String) ((DynaActionForm) form).get("questionText");
        String secondQuestionText = (String) ((DynaActionForm) form).get("secondQuestionText");
        String correctFeedbackText = (String) ((DynaActionForm) form).get("correctFeedbackText");
        String wrongFeedbackText = (String) ((DynaActionForm) form).get("wrongFeedbackText");

        String[] options = (String[]) ((DynaActionForm) form).get("options");
        String[] correctOptions = (String[]) ((DynaActionForm) form).get("correctOptions");
        String[] shuffle = (String[]) ((DynaActionForm) form).get("shuffle");

        Integer[] signal = (Integer[]) ((DynaActionForm) form).get("signal");
        String colsString = (String) ((DynaActionForm) form).get("cols");
        Integer cols = null;
        if (colsString != null && !colsString.equals("")) {
            cols = new Integer(colsString);
        }
        String rowsString = (String) ((DynaActionForm) form).get("rows");
        Integer rows = null;
        if (rowsString != null && !rowsString.equals("")) {
            rows = new Integer(rowsString);
        }
        String maxCharString = (String) ((DynaActionForm) form).get("maxChar");
        Integer maxChar = null;
        if (maxCharString != null && !maxCharString.equals("")) {
            maxChar = new Integer(maxCharString);
        }

        Boolean caseSensitive = (Boolean) ((DynaActionForm) form).get("caseSensitive");
        Boolean integerType = (Boolean) ((DynaActionForm) form).get("integerType");
        Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form).get("evaluationQuestion");

        Boolean breakLineBeforeResponseBox = (Boolean) ((DynaActionForm) form).get("breakLineBeforeResponseBox");
        Boolean breakLineAfterResponseBox = (Boolean) ((DynaActionForm) form).get("breakLineAfterResponseBox");

        QuestionType questionType = new QuestionType(questionTypeId);
        CardinalityType cardinalityType = null;
        if (cardinalityTypeId == null) {
            cardinalityType = new CardinalityType(CardinalityType.SINGLE);
        } else {
            cardinalityType = new CardinalityType(cardinalityTypeId);
        }
        questionType.setCardinalityType(cardinalityType);

        SubQuestion subQuestion = null;
        try {
            subQuestion =
                    setSubQuestion(questionType, questionValue, optionNumber, options, correctOptions, shuffle, signal,
                            integerType, evaluationQuestion, caseSensitive, maxChar, cols, rows);
        } catch (ParseQuestionException e) {
            error(request, "createExercise", e.getMessage());
            return prepareCreateExercise(mapping, form, request, response);
        }

        String metadataIdString = (String) ((DynaActionForm) form).get("exerciseCode");
        String metadataId = null;
        if (metadataIdString != null && metadataIdString.length() != 0) {
            metadataId = metadataIdString;
        }
        try {
            CreateExercise.runCreateExercise(getExecutionCourse(request), metadataId, author, description,
                    questionDifficultyType, mainSubject, secondarySubject, learningTime, level, subQuestion, questionText,
                    secondQuestionText, options, correctOptions, shuffle, correctFeedbackText, wrongFeedbackText,
                    breakLineBeforeResponseBox, breakLineAfterResponseBox);
        } catch (FenixServiceException e) {
            error(request, "createExercise", "error.exerciseCreationError");
            return prepareCreateExercise(mapping, form, request, response);
        }

        request.setAttribute("successfulCreation", new Boolean(true));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward previewExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        // QUESTION
        Integer questionTypeId = (Integer) ((DynaActionForm) form).get("questionType");
        Integer cardinalityTypeId = (Integer) ((DynaActionForm) form).get("cardinalityType");
        Integer optionNumber = (Integer) ((DynaActionForm) form).get("optionNumber");
        String questionValueString = (String) ((DynaActionForm) form).get("questionValue");
        Double questionValue = new Double(0);
        if (questionValueString != null && !questionValueString.equals("")) {
            questionValue = new Double(questionValueString);
        }
        String questionText = (String) ((DynaActionForm) form).get("questionText");
        String secondQuestionText = (String) ((DynaActionForm) form).get("secondQuestionText");

        String correctFeedbackText = (String) ((DynaActionForm) form).get("correctFeedbackText");
        String wrongFeedbackText = (String) ((DynaActionForm) form).get("wrongFeedbackText");
        String[] options = (String[]) ((DynaActionForm) form).get("options");
        String[] correctOptions = (String[]) ((DynaActionForm) form).get("correctOptions");
        String[] shuffle = (String[]) ((DynaActionForm) form).get("shuffle");

        Integer[] signal = (Integer[]) ((DynaActionForm) form).get("signal");
        String colsString = (String) ((DynaActionForm) form).get("cols");
        Integer cols = null;
        if (colsString != null && !colsString.equals("")) {
            cols = new Integer(colsString);
        }
        String rowsString = (String) ((DynaActionForm) form).get("rows");
        Integer rows = null;
        if (rowsString != null && !rowsString.equals("")) {
            rows = new Integer(rowsString);
        }
        String maxCharString = (String) ((DynaActionForm) form).get("maxChar");
        Integer maxChar = null;
        if (maxCharString != null && !maxCharString.equals("")) {
            maxChar = new Integer(maxCharString);
        }
        Boolean caseSensitive = (Boolean) ((DynaActionForm) form).get("caseSensitive");
        Boolean integerType = (Boolean) ((DynaActionForm) form).get("integerType");
        Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form).get("evaluationQuestion");

        Boolean breakLineBeforeResponseBox = (Boolean) ((DynaActionForm) form).get("breakLineBeforeResponseBox");
        Boolean breakLineAfterResponseBox = (Boolean) ((DynaActionForm) form).get("breakLineAfterResponseBox");

        QuestionType questionType = new QuestionType(questionTypeId);
        CardinalityType cardinalityType = null;
        if (cardinalityTypeId == null) {
            cardinalityType = new CardinalityType(CardinalityType.SINGLE);
        } else {
            cardinalityType = new CardinalityType(cardinalityTypeId);
        }
        questionType.setCardinalityType(cardinalityType);

        SubQuestion subQuestion = null;
        try {
            subQuestion =
                    setSubQuestion(questionType, questionValue, optionNumber, options, correctOptions, shuffle, signal,
                            integerType, evaluationQuestion, caseSensitive, maxChar, cols, rows);
        } catch (ParseQuestionException e) {
            error(request, "createExercise", e.getMessage());
            return prepareCreateExercise(mapping, form, request, response);
        }

        XMLQuestion xmlQuestion = new XMLQuestion();
        String xmlFile =
                xmlQuestion.getXmlQuestion(questionText, secondQuestionText, subQuestion.getQuestionType(), options, shuffle,
                        subQuestion.getResponseProcessingInstructions(), correctFeedbackText, wrongFeedbackText,
                        breakLineBeforeResponseBox, breakLineAfterResponseBox);
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            subQuestion = parse.parseSubQuestion(xmlFile);
        } catch (Exception e) {
            error(request, "createExercise", "error.exerciseCreationError");
            return prepareCreateExercise(mapping, form, request, response);
        }
        ((DynaActionForm) form).set("response", null);
        request.setAttribute("subQuestion", subQuestion);
        return doForward(request, "previewExercise");
    }

    private SubQuestion setSubQuestion(QuestionType questionType, Double questionValue, Integer optionNumber, String[] options,
            String[] correctOptions, String[] shuffle, Integer[] signal, Boolean integerType, Boolean evaluationQuestion,
            Boolean caseSensitive, Integer maxChar, Integer cols, Integer rows) throws ParseQuestionException {
        // RENDER
        if (questionType.getType().intValue() == QuestionType.LID) {
            RenderChoise render = new RenderChoise();
            if (shuffle == null || shuffle.length == 0) {
                render.setShuffle("NO");
            } else {
                render.setShuffle("YES");
            }
            questionType.setRender(render);
        } else {
            RenderFIB render = new RenderFIB();
            if (questionType.getType().intValue() == QuestionType.STR) {
                render.setFibtype(RenderFIB.STRING);
            } else if (questionType.getType().intValue() == QuestionType.NUM) {
                if (integerType.booleanValue()) {
                    render.setFibtype(RenderFIB.INTEGER);
                } else {
                    render.setFibtype(RenderFIB.DECIMAL);
                }
            }
            if (maxChar != null && !maxChar.equals(new Integer(0))) {
                render.setMaxchars(maxChar);
            }
            if (cols != null && !cols.equals(new Integer(0))) {
                render.setColumns(cols);
            }
            if (rows != null && !rows.equals(new Integer(0))) {
                render.setRows(rows);
            }

            questionType.setRender(render);
        }

        // RESPROCESSING
        if (questionType.getType().intValue() == QuestionType.LID) {
            for (String option : options) {
                if (option == null || option.equals("")) {
                    throw new ParseQuestionException("error.needOptionText");
                }
            }
        }

        List<ResponseCondition> responseConditionList = new ArrayList<ResponseCondition>();
        List<ResponseProcessing> responseProcessingInstructionsList = new ArrayList<ResponseProcessing>();
        if (evaluationQuestion.booleanValue()) {
            if (correctOptions == null || correctOptions.length == 0) {
                throw new ParseQuestionException("error.needCorrectResponse");
            }

            for (int i = 0; i < correctOptions.length; i++) {
                String condition = null;
                String correctResponse;
                correctResponse = convertCharacters(correctOptions[i]);
                if (questionType.getType().intValue() == QuestionType.STR) {
                    if (correctResponse.equals("")) {
                        throw new ParseQuestionException("error.needCorrectResponse");
                    }
                    condition = ResponseCondition.getConditionString(signal[i]);
                    if (caseSensitive != null && caseSensitive.booleanValue() == true) {
                        condition = condition.concat("ignorecase");
                    }
                } else if (questionType.getType().intValue() == QuestionType.NUM) {
                    if (correctResponse.equals("")) {
                        throw new ParseQuestionException("error.needResponse");
                    }
                    condition = ResponseCondition.getConditionString(signal[i]);
                    if (((RenderFIB) questionType.getRender()).getFibtype().intValue() == RenderFIB.INTEGER_CODE) {
                        try {
                            new Integer(correctOptions[i]);
                        } catch (NumberFormatException ex) {
                            throw new ParseQuestionException("error.responseInvalidIntegerFormat");
                        }
                    } else {
                        try {
                            correctResponse = correctOptions[i].replace(',', '.');
                            new Double(correctResponse);
                        } catch (NumberFormatException ex) {
                            throw new ParseQuestionException("error.responseInvalidDecimalFormat");
                        }
                    }

                } else if (questionType.getType().intValue() == QuestionType.LID) {
                    condition = ResponseCondition.VAREQUAL_XML_STRING;
                }
                if (condition != null) {
                    ResponseCondition responseCondition = new ResponseCondition(condition, correctResponse, "1");
                    responseConditionList.add(responseCondition);
                }
            }
            if (responseConditionList != null && responseConditionList.size() != 0) {
                ResponseProcessing responseProcessing =
                        new ResponseProcessing(responseConditionList, questionValue, new Integer(ResponseProcessing.SET), null,
                                true);
                responseProcessingInstructionsList.add(responseProcessing);
            }

        }
        SubQuestion subQuestion = new SubQuestion();
        // subQuestion.setOptionNumber(optionNumber);
        subQuestion.setQuestionType(questionType);
        subQuestion.setQuestionValue(questionValue);

        subQuestion.setResponseProcessingInstructions(responseProcessingInstructionsList);

        return subQuestion;
    }

    public ActionForward addNewCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ((DynaActionForm) form).set("optionNumber",
                new Integer(((Integer) ((DynaActionForm) form).get("optionNumber")).intValue() + 1));
        Integer[] signal = (Integer[]) ((DynaActionForm) form).get("signal");
        if (signal != null && signal.length != 0) {
            Integer[] newSignal = new Integer[signal.length + 1];
            for (int i = 0; i < signal.length; i++) {
                newSignal[i] = signal[i];
            }
            newSignal[newSignal.length - 1] = signal[signal.length - 1];
            ((DynaActionForm) form).set("signal", newSignal);
        }

        Integer questionType = (Integer) ((DynaActionForm) form).get("questionType");
        String[] options;
        if (questionType.intValue() == QuestionType.LID) {
            options = (String[]) ((DynaActionForm) form).get("options");
        } else {
            options = (String[]) ((DynaActionForm) form).get("correctOptions");
        }

        String[] newOptions = new String[options.length + 1];
        for (int i = 0; i < options.length; i++) {
            newOptions[i] = options[i];
        }
        newOptions[newOptions.length - 1] = "";

        if (questionType.intValue() == QuestionType.LID) {
            ((DynaActionForm) form).set("options", newOptions);
        } else {
            ((DynaActionForm) form).set("correctOptions", newOptions);
        }

        // Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form)
        // .get("evaluationQuestion");
        return prepareCreateExercise(mapping, form, request, response);
    }

    public ActionForward removeCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Integer conditionId = (Integer) (((DynaActionForm) form).get("conditionId"));
        Integer[] signal = (Integer[]) ((DynaActionForm) form).get("signal");
        if (signal != null && signal.length > 1) {
            Integer[] newSignal = new Integer[signal.length - 1];
            for (int i = 0, newIndex = 0; i < signal.length; i++) {
                if (i != conditionId.intValue()) {
                    newSignal[newIndex] = signal[i];
                    newIndex++;
                }
            }
            ((DynaActionForm) form).set("signal", newSignal);
        }
        Integer questionType = (Integer) ((DynaActionForm) form).get("questionType");
        String[] options;
        if (questionType.intValue() == QuestionType.LID) {
            options = (String[]) ((DynaActionForm) form).get("options");
        } else {
            options = (String[]) ((DynaActionForm) form).get("correctOptions");
        }

        if (options != null && options.length > 1) {
            String[] newOptions = new String[options.length - 1];
            for (int i = 0, newIndex = 0; i < options.length; i++) {
                if (i != conditionId.intValue()) {
                    newOptions[newIndex] = options[i];
                    newIndex++;
                }
            }
            if (questionType.intValue() == QuestionType.LID) {
                ((DynaActionForm) form).set("options", newOptions);
            } else {
                ((DynaActionForm) form).set("correctOptions", newOptions);
            }
        }

        Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form).get("evaluationQuestion");
        if (questionType.intValue() == QuestionType.LID && evaluationQuestion.booleanValue()) {
            String[] shuffle = (String[]) ((DynaActionForm) form).get("shuffle");

            for (int i = 0; i < shuffle.length; i++) {
                int value = new Integer(shuffle[i]).intValue() - 1;
                if (value == conditionId.intValue()) {
                    shuffle[i] = "";
                } else if (value > conditionId.intValue()) {
                    shuffle[i] = new Integer(value).toString();
                }
            }
            ((DynaActionForm) form).set("shuffle", shuffle);

            String[] correctOptions = (String[]) ((DynaActionForm) form).get("correctOptions");
            for (int i = 0; i < correctOptions.length; i++) {
                int value = new Integer(correctOptions[i]).intValue() - 1;
                if (value == conditionId.intValue()) {
                    correctOptions[i] = "";
                } else if (value > conditionId.intValue()) {
                    correctOptions[i] = new Integer(value).toString();
                }
            }
            ((DynaActionForm) form).set("correctOptions", correctOptions);
        }
        ((DynaActionForm) form).set("optionNumber",
                new Integer(((Integer) ((DynaActionForm) form).get("optionNumber")).intValue() - 1));
        return prepareCreateExercise(mapping, form, request, response);
    }

    public ActionForward exercisesFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final ExecutionCourse executionCourse = getExecutionCourse(request);
        List<Metadata> metadataList = new ArrayList<Metadata>();
        for (Metadata metadata : executionCourse.getMetadatasSet()) {
            if (metadata.getVisibility()) {
                metadataList.add(metadata);
            }
        }
        final String order = request.getParameter("order");
        final String asc = request.getParameter("asc");

        if (order != null) {
            MetadataComparator metadataComparator = new MetadataComparator(order, asc);
            Collections.sort(metadataList, metadataComparator);
        }

        request.setAttribute("badXmls", request.getAttribute("badXmls"));
        request.setAttribute("metadataList", metadataList);
        request.setAttribute("order", order);
        request.setAttribute("asc", asc);
        return doForward(request, "exercisesFirstPage");
    }

    public ActionForward chooseNewExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final ExecutionCourse executionCourse = getExecutionCourse(request);
        Set<Metadata> metadataList = executionCourse.findVisibleMetadata();

        final String order = request.getParameter("order");
        final String asc = request.getParameter("asc");

        if (order != null) {
            // MetadataComparator metadataComparator = new
            // MetadataComparator(order, asc);
            // Collections.sort(infoMetadataList, metadataComparator);
        }
        request.setAttribute("metadataList", metadataList);
        request.setAttribute("order", order);
        request.setAttribute("asc", asc);
        return doForward(request, "chooseNewExercise");
    }

    public ActionForward prepareAddExerciseVariation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("exerciseCode", getFromRequest(request, "exerciseCode"));
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return doForward(request, "addExerciseVariation");
    }

    public ActionForward loadExerciseVariationsFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String metadataId = getStringFromRequest(request, "exerciseCode");
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));

        // FormFile xmlZipFile = (FormFile) ((DynaActionForm)
        // form).get("xmlZipFile");
        final String xmlZipFileName = (String) ((DynaActionForm) form).get("xmlZipFile");
        if (xmlZipFileName == null) {
            error(request, "FileNotExist", "error.nullXmlZipFile");
            return doForward(request, "addExerciseVariation");
        }

        final UploadedFile xmlZipFile =
                ((Hashtable<String, UploadedFile>) request
                        .getAttribute(RequestWrapperFilter.FenixHttpServletRequestWrapper.ITEM_MAP_ATTRIBUTE)).get("xmlZipFile");
        if (xmlZipFile == null || xmlZipFile.getSize() == 0) {
            error(request, "FileNotExist", "error.nullXmlZipFile");
            return doForward(request, "addExerciseVariation");
        } else if (!(validFileFormat(xmlZipFile.getContentType(), xmlZipFileName))) {
            error(request, "FileNotExist", "error.badXmlZipFile");
            return doForward(request, "addExerciseVariation");
        }

        try {
            List badXmls =
                    InsertExerciseVariation.runInsertExerciseVariation(getExecutionCourse(request), metadataId, xmlZipFile);
            request.setAttribute("badXmls", badXmls);
        } catch (InvalidXMLFilesException e) {
            error(request, "FileNotExist", "error.badXmlFiles");
            return doForward(request, "addExerciseVariation");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward insertNewExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return doForward(request, "insertNewExercise");
    }

    public ActionForward loadExerciseFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // final FormFile xmlZipFile = (FormFile) ((DynaActionForm)
        // form).get("xmlZipFile");
        final String xmlZipFileName = (String) ((DynaActionForm) form).get("xmlZipFile");

        if (xmlZipFileName == null) {
            error(request, "FileNotExist", "error.nullXmlZipFile");
            return doForward(request, "insertNewExercise");
        }

        final UploadedFile xmlZipFile =
                ((Hashtable<String, UploadedFile>) request
                        .getAttribute(RequestWrapperFilter.FenixHttpServletRequestWrapper.ITEM_MAP_ATTRIBUTE)).get("xmlZipFile");
        for (final Entry<String, UploadedFile> entry : ((Hashtable<String, UploadedFile>) request
                .getAttribute(RequestWrapperFilter.FenixHttpServletRequestWrapper.ITEM_MAP_ATTRIBUTE)).entrySet()) {
            logger.info(entry.getKey() + " " + entry.getValue());
            logger.info("   " + entry.getValue().getName());
            logger.info("   " + entry.getValue().getContentType());
            logger.info("   " + entry.getValue().getSize());
            logger.info("   " + entry.getValue().getInputStream());
            logger.info("   " + entry.getValue().getFileData());
        }
        if (xmlZipFile == null || xmlZipFile.getFileData() == null || xmlZipFile.getFileData().length == 0) {
            error(request, "FileNotExist", "error.nullXmlZipFile");
            return doForward(request, "insertNewExercise");
        } else if (!(validFileFormat(xmlZipFile.getContentType(), xmlZipFileName))) {
            error(request, "FileNotExist", "error.badXmlZipFile");
            return doForward(request, "insertNewExercise");
        }
        List badXmls = null;
        try {
            badXmls = InsertExercise.runInsertExercise(getExecutionCourse(request), xmlZipFile);
        } catch (InvalidMetadataException e) {
            error(request, "FileNotExist", "error.badMetadataFile");
            return doForward(request, "insertNewExercise");
        } catch (InvalidXMLFilesException e) {
            error(request, "FileNotExist", "error.badXmlFiles");
            return doForward(request, "insertNewExercise");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("badXmls", badXmls);
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward prepareRemoveExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("exerciseCode", getFromRequest(request, "exerciseCode"));
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return doForward(request, "prepareRemoveExercise");
    }

    public ActionForward removeExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String metadataId = getStringFromRequest(request, "exerciseCode");
        try {
            DeleteExercise.runDeleteExercise(getExecutionCourse(request), metadataId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("successfulDeletion", "true");
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward prepareEditExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String exerciseId = getStringFromRequest(request, "exerciseCode");
        String variationCode = getStringFromRequest(request, "variationCode");
        Metadata metadata = null;
        try {
            metadata = ReadExercise.runReadExercise(getExecutionCourse(request), exerciseId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (metadata == null || !metadata.getVisibility().booleanValue()) {
            return exercisesFirstPage(mapping, form, request, response);
        }
        ParseSubQuestion parse = new ParseSubQuestion();
        if (variationCode != null) {
            for (Question question : metadata.getVisibleQuestions()) {
                if ((question.getExternalId().equals(variationCode) || variationCode.equals("-2"))
                        && (question.getSubQuestions() == null || question.getSubQuestions().size() == 0)) {
                    try {
                        question = parse.parseSubQuestion(question);
                    } catch (ParseQuestionException e) {
                        throw new FenixActionException();
                    }
                }
            }
        } else {
            variationCode = "-1";
        }

        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("exerciseCode", exerciseId);
        request.setAttribute("variationCode", variationCode);
        request.setAttribute("metadata", metadata);
        List questionDifficultyList = (new QuestionDifficultyType()).getAllTypes();
        request.setAttribute("questionDifficultyList", questionDifficultyList);
        return doForward(request, "editExercise");
    }

    public ActionForward editExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String exerciseId = getStringFromRequest(request, "exerciseCode");
        final String author = request.getParameter("author");
        final String description = request.getParameter("description");
        String difficulty = request.getParameter("difficulty");
        final String learningTime = request.getParameter("learningTimeFormatted");
        final String level = request.getParameter("level");
        final String mainSubject = request.getParameter("mainSubject");
        final String secondarySubject = request.getParameter("secondarySubject");
        if (!difficulty.equals("-1")) {
            difficulty = new QuestionDifficultyType(new Integer(difficulty)).getTypeString();
        }
        Boolean result = null;
        try {
            result =
                    EditExercise.runEditExercise(getExecutionCourse(request), exerciseId, author, description, difficulty,
                            string2Hour(learningTime), level, mainSubject, secondarySubject);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("successfulEdition", result);
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward prepareRemoveExerciseVariation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String variationCode = getStringFromRequest(request, "variationCode");
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        try {
            List<LabelValueBean> result =
                    DeleteExerciseVariation.runDeleteExerciseVariation(getExecutionCourse(request), variationCode);
            if (result == null || result.size() == 0) {
                return prepareEditExercise(mapping, form, request, response);
            }
            request.setAttribute("studentsList", result);
            InfoQuestion infoQuestion = null;
            // FIXME Service "ReadQuestion" no longer exists
            //        (InfoQuestion) ServiceUtils.executeService("ReadQuestion", new Object[] { executionCourseId, null,
            //                variationCode, getServlet().getServletContext().getRealPath("/") });
            request.setAttribute("infoQuestion", infoQuestion);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return doForward(request, "removeExerciseVariation");
    }

    public ActionForward removeExerciseVariation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String exerciseId = getStringFromRequest(request, "exerciseCode");
        final String variationCode = getStringFromRequest(request, "variationCode");
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("exerciseCode", exerciseId);

        try {
            ChangeStudentTestQuestion.runChangeStudentTestQuestion(getExecutionCourse(request), null, variationCode, null, null,
                    new TestQuestionChangesType(TestQuestionChangesType.CHANGE_VARIATION), new Boolean(true),
                    new TestQuestionStudentsChangesType(TestQuestionStudentsChangesType.ALL_STUDENTS), request.getContextPath());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareEditExercise(mapping, form, request, response);
    }

    public ActionForward exportExerciseVariation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Question question = getDomainObject(request, "variationCode");
        response.setContentType("text/plain; charset=utf-8");
        response.setHeader("Content-disposition", "attachment; filename=" + question.getXmlFileName());
        try {
            final ServletOutputStream writer = response.getOutputStream();
            writer.write(question.getXmlFile().getBytes());
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    private Calendar string2Hour(String hour) {
        if (hour.equals("")) {
            return null;
        }
        String[] hourTokens = hour.split(":");
        Calendar result = Calendar.getInstance();
        result.set(Calendar.HOUR_OF_DAY, (new Integer(hourTokens[0])).intValue());
        result.set(Calendar.MINUTE, (new Integer(hourTokens[1])).intValue());
        result.set(Calendar.SECOND, new Integer(0).intValue());
        return result;
    }

    private String convertCharacters(String text) {
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
    }

    private void error(HttpServletRequest request, String errorProperty, String error) {
        ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(errorProperty, new ActionError(error));
        saveErrors(request, actionErrors);
    }

    private boolean validFileFormat(String contentType, String fileExtension) {
        //XML file
        if (contentType.equals("text/xml") || contentType.equals("application/xml")) {
            return true;
        }
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
}
