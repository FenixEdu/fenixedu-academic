/*
 * Created on 14/Ago/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidMetadataException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.InvalidXMLFilesException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.comparators.MetadataComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class ExercisesManagementAction extends FenixDispatchAction {

    public ActionForward prepareChooseExerciseType(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("exerciseCode", getCodeFromRequest(request, "exerciseCode"));
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("questionsTypes", QuestionType.getAllTypes());
        request.setAttribute("cardinalityTypes", CardinalityType.getAllTypes());
        return mapping.findForward("chooseExerciseType");
    }

    public ActionForward chooseQuestionType(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        Integer questionType = (Integer) ((DynaActionForm) form).get("questionType");
        if (questionType.intValue() == QuestionType.LID) {
            Integer cardinalityTypeId = (Integer) ((DynaActionForm) form).get("cardinalityType");

            if (cardinalityTypeId == null) {
                error(request, "chooseQuestionType", "message.cardinalityTypeRequired");
                return prepareChooseExerciseType(mapping, form, request, response);
            }
            ((DynaActionForm) form).set("optionNumber", new Integer(4));
            return prepareCreateExercise(mapping, form, request, response);
        } else if (questionType.intValue() == QuestionType.STR
                || questionType.intValue() == QuestionType.NUM) {
            ((DynaActionForm) form).set("cardinalityType", new Integer(CardinalityType.SINGLE));
            ((DynaActionForm) form).set("optionNumber", new Integer(1));
            return prepareCreateExercise(mapping, form, request, response);
        } else {
            error(request, "chooseQuestionType", "message.exerciseTypeRequired");
            return prepareChooseExerciseType(mapping, form, request, response);
        }

    }

    public ActionForward prepareCreateExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));

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
            ((DynaActionForm) form).set("signal",
                    new Integer[] { new Integer(ResponseCondition.VAREQUAL) });
        }

        List questionDifficultyList = (new QuestionDifficultyType()).getAllTypes();
        request.setAttribute("questionDifficultyList", questionDifficultyList);

        if (questionType.intValue() == QuestionType.STR)
            request.setAttribute("signals", ResponseCondition.getConditionSignalsToStringQuestion());
        else if (questionType.intValue() == QuestionType.NUM)
            request.setAttribute("signals", ResponseCondition.getConditionSignalsToNumericalQuestion());

        String maxCharString = (String) ((DynaActionForm) form).get("maxChar");
        String rowsString = (String) ((DynaActionForm) form).get("rows");

        String checkTextBoxParams = null;
        if (maxCharString != null && !maxCharString.equals(""))
            checkTextBoxParams = "false";
        else if (rowsString != null && !rowsString.equals(""))
            checkTextBoxParams = "true";

        ((DynaActionForm) form).set("response", new String[] { checkTextBoxParams });
        return mapping.findForward("prepareCreateExercise");
    }

    public ActionForward createExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final IUserView userView = getUserView(request);
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
        if (learningHour != null || learningHour.equals("") || learningMinute != null
                || learningMinute.equals("")) {
            if (learningHour == null || learningHour.equals(""))
                learningHour = "0";
            if (learningMinute == null || learningMinute.equals(""))
                learningMinute = "0";
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
        if (questionValueString != null && !questionValueString.equals(""))
            questionValue = new Double(questionValueString);
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
        if (colsString != null && !colsString.equals(""))
            cols = new Integer(colsString);
        String rowsString = (String) ((DynaActionForm) form).get("rows");
        Integer rows = null;
        if (rowsString != null && !rowsString.equals(""))
            rows = new Integer(rowsString);
        String maxCharString = (String) ((DynaActionForm) form).get("maxChar");
        Integer maxChar = null;
        if (maxCharString != null && !maxCharString.equals(""))
            maxChar = new Integer(maxCharString);

        Boolean caseSensitive = (Boolean) ((DynaActionForm) form).get("caseSensitive");
        Boolean integerType = (Boolean) ((DynaActionForm) form).get("integerType");
        Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form).get("evaluationQuestion");

        Boolean breakLineBeforeResponseBox = (Boolean) ((DynaActionForm) form)
                .get("breakLineBeforeResponseBox");
        Boolean breakLineAfterResponseBox = (Boolean) ((DynaActionForm) form)
                .get("breakLineAfterResponseBox");

        QuestionType questionType = new QuestionType(questionTypeId);
        CardinalityType cardinalityType = null;
        if (cardinalityTypeId == null)
            cardinalityType = new CardinalityType(CardinalityType.SINGLE);
        else
            cardinalityType = new CardinalityType(cardinalityTypeId);
        questionType.setCardinalityType(cardinalityType);

        SubQuestion subQuestion = null;
        try {
            subQuestion = setSubQuestion(questionType, questionValue, optionNumber, options,
                    correctOptions, shuffle, signal, integerType, evaluationQuestion, caseSensitive,
                    maxChar, cols, rows);
        } catch (ParseQuestionException e) {
            error(request, "createExercise", e.getMessage());
            return prepareCreateExercise(mapping, form, request, response);
        }

        String metadataIdString = (String) ((DynaActionForm) form).get("exerciseCode");
        Integer metadataId = null;
        if (metadataIdString != null && metadataIdString.length() != 0)
            metadataId = new Integer(metadataIdString);
        try {
            Object[] args = { executionCourseId, metadataId, author, description,
                    questionDifficultyType, mainSubject, secondarySubject, learningTime, level,
                    subQuestion, questionText, secondQuestionText, options, correctOptions, shuffle,
                    correctFeedbackText, wrongFeedbackText, breakLineBeforeResponseBox,
                    breakLineAfterResponseBox, getServlet().getServletContext().getRealPath("/") };
            ServiceUtils.executeService(userView, "CreateExercise", args);
        } catch (FenixServiceException e) {
            error(request, "createExercise", "error.exerciseCreationError");
            return prepareCreateExercise(mapping, form, request, response);
        }

        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("successfulCreation", new Boolean(true));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward previewExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // QUESTION
        Integer questionTypeId = (Integer) ((DynaActionForm) form).get("questionType");
        Integer cardinalityTypeId = (Integer) ((DynaActionForm) form).get("cardinalityType");
        Integer optionNumber = (Integer) ((DynaActionForm) form).get("optionNumber");
        String questionValueString = (String) ((DynaActionForm) form).get("questionValue");
        Double questionValue = new Double(0);
        if (questionValueString != null && !questionValueString.equals(""))
            questionValue = new Double(questionValueString);
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
        if (colsString != null && !colsString.equals(""))
            cols = new Integer(colsString);
        String rowsString = (String) ((DynaActionForm) form).get("rows");
        Integer rows = null;
        if (rowsString != null && !rowsString.equals(""))
            rows = new Integer(rowsString);
        String maxCharString = (String) ((DynaActionForm) form).get("maxChar");
        Integer maxChar = null;
        if (maxCharString != null && !maxCharString.equals(""))
            maxChar = new Integer(maxCharString);
        Boolean caseSensitive = (Boolean) ((DynaActionForm) form).get("caseSensitive");
        Boolean integerType = (Boolean) ((DynaActionForm) form).get("integerType");
        Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form).get("evaluationQuestion");
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));

        Boolean breakLineBeforeResponseBox = (Boolean) ((DynaActionForm) form)
                .get("breakLineBeforeResponseBox");
        Boolean breakLineAfterResponseBox = (Boolean) ((DynaActionForm) form)
                .get("breakLineAfterResponseBox");

        QuestionType questionType = new QuestionType(questionTypeId);
        CardinalityType cardinalityType = null;
        if (cardinalityTypeId == null)
            cardinalityType = new CardinalityType(CardinalityType.SINGLE);
        else
            cardinalityType = new CardinalityType(cardinalityTypeId);
        questionType.setCardinalityType(cardinalityType);

        SubQuestion subQuestion = null;
        try {
            subQuestion = setSubQuestion(questionType, questionValue, optionNumber, options,
                    correctOptions, shuffle, signal, integerType, evaluationQuestion, caseSensitive,
                    maxChar, cols, rows);
        } catch (ParseQuestionException e) {
            error(request, "createExercise", e.getMessage());
            return prepareCreateExercise(mapping, form, request, response);
        }

        XMLQuestion xmlQuestion = new XMLQuestion();
        String xmlFile = xmlQuestion.getXmlQuestion(questionText, secondQuestionText, subQuestion
                .getQuestionType(), options, shuffle, subQuestion.getResponseProcessingInstructions(),
                correctFeedbackText, wrongFeedbackText, breakLineBeforeResponseBox,
                breakLineAfterResponseBox);
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            subQuestion = parse.parseSubQuestion(xmlFile, getServlet().getServletContext().getRealPath(
                    "/"));
        } catch (Exception e) {
            error(request, "createExercise", "error.exerciseCreationError");
            return prepareCreateExercise(mapping, form, request, response);
        }
        ((DynaActionForm) form).set("response", null);
        request.setAttribute("subQuestion", subQuestion);
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        return mapping.findForward("previewExercise");
    }

    private SubQuestion setSubQuestion(QuestionType questionType, Double questionValue,
            Integer optionNumber, String[] options, String[] correctOptions, String[] shuffle,
            Integer[] signal, Boolean integerType, Boolean evaluationQuestion, Boolean caseSensitive,
            Integer maxChar, Integer cols, Integer rows) throws ParseQuestionException {
        // RENDER
        if (questionType.getType().intValue() == QuestionType.LID) {
            RenderChoise render = new RenderChoise();
            if (shuffle == null || shuffle.length == 0)
                render.setShuffle("NO");
            else
                render.setShuffle("YES");
            questionType.setRender(render);
        } else {
            RenderFIB render = new RenderFIB();
            if (questionType.getType().intValue() == QuestionType.STR) {
                render.setFibtype(RenderFIB.STRING);
            } else if (questionType.getType().intValue() == QuestionType.NUM)
                if (integerType.booleanValue())
                    render.setFibtype(RenderFIB.INTEGER);
                else
                    render.setFibtype(RenderFIB.DECIMAL);
            if (maxChar != null && !maxChar.equals(new Integer(0)))
                render.setMaxchars(maxChar);
            if (cols != null && !cols.equals(new Integer(0)))
                render.setColumns(cols);
            if (rows != null && !rows.equals(new Integer(0)))
                render.setRows(rows);

            questionType.setRender(render);
        }

        // RESPROCESSING
        if (questionType.getType().intValue() == QuestionType.LID) {
            for (int i = 0; i < options.length; i++) {
                if (options[i] == null || options[i].equals("")) {
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
                    if (correctResponse.equals(""))
                        throw new ParseQuestionException("error.needCorrectResponse");
                    condition = ResponseCondition.getConditionString(signal[i]);
                    if (caseSensitive != null && caseSensitive.booleanValue() == true)
                        condition = condition.concat("ignorecase");
                } else if (questionType.getType().intValue() == QuestionType.NUM) {
                    if (correctResponse.equals(""))
                        throw new ParseQuestionException("error.needResponse");
                    condition = ResponseCondition.getConditionString(signal[i]);
                    if (((RenderFIB) questionType.getRender()).getFibtype().intValue() == RenderFIB.INTEGER_CODE)
                        try {
                            new Integer(correctOptions[i]);
                        } catch (NumberFormatException ex) {
                            throw new ParseQuestionException("error.responseInvalidIntegerFormat");
                        }
                    else
                        try {
                            correctResponse = correctOptions[i].replace(',', '.');
                            new Double(correctResponse);
                        } catch (NumberFormatException ex) {
                            throw new ParseQuestionException("error.responseInvalidDecimalFormat");
                        }

                } else if (questionType.getType().intValue() == QuestionType.LID) {
                    condition = ResponseCondition.VAREQUAL_XML_STRING;
                }
                if (condition != null) {
                    ResponseCondition responseCondition = new ResponseCondition(condition,
                            correctResponse, new String("1"));
                    responseConditionList.add(responseCondition);
                }
            }
            if (responseConditionList != null && responseConditionList.size() != 0) {
                ResponseProcessing responseProcessing = new ResponseProcessing(responseConditionList,
                        questionValue, new Integer(ResponseProcessing.SET), null, true);
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

    public ActionForward addNewCondition(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        ((DynaActionForm) form).set("optionNumber", new Integer(((Integer) ((DynaActionForm) form)
                .get("optionNumber")).intValue() + 1));
        Integer[] signal = (Integer[]) ((DynaActionForm) form).get("signal");
        if (signal != null && signal.length != 0) {
            Integer[] newSignal = new Integer[signal.length + 1];
            for (int i = 0; i < signal.length; i++)
                newSignal[i] = signal[i];
            newSignal[newSignal.length - 1] = signal[signal.length - 1];
            ((DynaActionForm) form).set("signal", newSignal);
        }

        Integer questionType = (Integer) ((DynaActionForm) form).get("questionType");
        String[] options;
        if (questionType.intValue() == QuestionType.LID) {
            options = (String[]) ((DynaActionForm) form).get("options");
        } else
            options = (String[]) ((DynaActionForm) form).get("correctOptions");

        String[] newOptions = new String[options.length + 1];
        for (int i = 0; i < options.length; i++)
            newOptions[i] = options[i];
        newOptions[newOptions.length - 1] = "";

        if (questionType.intValue() == QuestionType.LID) {
            ((DynaActionForm) form).set("options", newOptions);
        } else
            ((DynaActionForm) form).set("correctOptions", newOptions);

        // Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form)
        // .get("evaluationQuestion");
        return prepareCreateExercise(mapping, form, request, response);
    }

    public ActionForward removeCondition(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Integer conditionId = (Integer) (((DynaActionForm) form).get("conditionId"));
        Integer[] signal = (Integer[]) ((DynaActionForm) form).get("signal");
        if (signal != null && signal.length > 1) {
            Integer[] newSignal = new Integer[signal.length - 1];
            for (int i = 0, newIndex = 0; i < signal.length; i++)
                if (i != conditionId.intValue()) {
                    newSignal[newIndex] = signal[i];
                    newIndex++;
                }
            ((DynaActionForm) form).set("signal", newSignal);
        }
        Integer questionType = (Integer) ((DynaActionForm) form).get("questionType");
        String[] options;
        if (questionType.intValue() == QuestionType.LID) {
            options = (String[]) ((DynaActionForm) form).get("options");
        } else
            options = (String[]) ((DynaActionForm) form).get("correctOptions");

        if (options != null && options.length > 1) {
            String[] newOptions = new String[options.length - 1];
            for (int i = 0, newIndex = 0; i < options.length; i++)
                if (i != conditionId.intValue()) {
                    newOptions[newIndex] = options[i];
                    newIndex++;
                }
            if (questionType.intValue() == QuestionType.LID)
                ((DynaActionForm) form).set("options", newOptions);
            else
                ((DynaActionForm) form).set("correctOptions", newOptions);
        }

        Boolean evaluationQuestion = (Boolean) ((DynaActionForm) form).get("evaluationQuestion");
        if (questionType.intValue() == QuestionType.LID && evaluationQuestion.booleanValue()) {
            String[] shuffle = (String[]) ((DynaActionForm) form).get("shuffle");

            for (int i = 0; i < shuffle.length; i++) {
                int value = new Integer(shuffle[i]).intValue() - 1;
                if (value == conditionId.intValue())
                    shuffle[i] = "";
                else if (value > conditionId.intValue())
                    shuffle[i] = new Integer(value).toString();
            }
            ((DynaActionForm) form).set("shuffle", shuffle);

            String[] correctOptions = (String[]) ((DynaActionForm) form).get("correctOptions");
            for (int i = 0; i < correctOptions.length; i++) {
                int value = new Integer(correctOptions[i]).intValue() - 1;
                if (value == conditionId.intValue())
                    correctOptions[i] = "";
                else if (value > conditionId.intValue())
                    correctOptions[i] = new Integer(value).toString();
            }
            ((DynaActionForm) form).set("correctOptions", correctOptions);
        }
        ((DynaActionForm) form).set("optionNumber", new Integer(((Integer) ((DynaActionForm) form)
                .get("optionNumber")).intValue() - 1));
        return prepareCreateExercise(mapping, form, request, response);
    }

    public ActionForward exercisesFirstPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        List<Metadata> metadataList = new ArrayList<Metadata>();
        for (Metadata metadata : executionCourse.getMetadatas()) {
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

        request.setAttribute("badXmls", (List) request.getAttribute("badXmls"));
        request.setAttribute("metadataList", metadataList);
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("order", order);
        request.setAttribute("asc", asc);
        return mapping.findForward("exercisesFirstPage");
    }

    public ActionForward chooseNewExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final IUserView userView = getUserView(request);

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        Set<Metadata> metadataList = executionCourse.findVisibleMetadata();

        final String order = request.getParameter("order");
        final String asc = request.getParameter("asc");

        if (order != null) {
            // MetadataComparator metadataComparator = new
            // MetadataComparator(order, asc);
            // Collections.sort(infoMetadataList, metadataComparator);
        }
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("metadataList", metadataList);
        request.setAttribute("order", order);
        request.setAttribute("asc", asc);
        return mapping.findForward("chooseNewExercise");
    }

    public ActionForward prepareAddExerciseVariation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("exerciseCode", getCodeFromRequest(request, "exerciseCode"));
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return mapping.findForward("addExerciseVariation");
    }

    public ActionForward loadExerciseVariationsFile(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        request.setAttribute("objectCode", executionCourseId);
        final Integer metadataId = getCodeFromRequest(request, "exerciseCode");
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));

        FormFile xmlZipFile = (FormFile) ((DynaActionForm) form).get("xmlZipFile");
        if (xmlZipFile == null || xmlZipFile.getFileData() == null
                || xmlZipFile.getFileData().length == 0) {
            error(request, "FileNotExist", "error.nullXmlZipFile");
            return mapping.findForward("addExerciseVariation");
        } else if (!(xmlZipFile.getContentType().equals("application/x-zip-compressed")
                || xmlZipFile.getContentType().equals("text/xml")
                || xmlZipFile.getContentType().equals("application/xml") || (xmlZipFile.getContentType()
                .equals("application/zip")))) {
            error(request, "FileNotExist", "error.badXmlZipFile");
            return mapping.findForward("addExerciseVariation");
        }

        try {
            Object[] args = { executionCourseId, metadataId, xmlZipFile,
                    getServlet().getServletContext().getRealPath("/") };

            List badXmls = (List) ServiceUtils.executeService(userView, "InsertExerciseVariation", args);
            request.setAttribute("badXmls", badXmls);
        } catch (InvalidXMLFilesException e) {
            error(request, "FileNotExist", "error.badXmlFiles");
            return mapping.findForward("addExerciseVariation");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward insertNewExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return mapping.findForward("insertNewExercise");
    }

    public ActionForward loadExerciseFiles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        final FormFile metadataFile = (FormFile) ((DynaActionForm) form).get("metadataFile");
        final FormFile xmlZipFile = (FormFile) ((DynaActionForm) form).get("xmlZipFile");
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        if (metadataFile != null)
            if ((metadataFile.getFileData().length != 0)
                    && !(metadataFile.getContentType().equals("text/xml") || metadataFile
                            .getContentType().equals("application/xml"))) {
                error(request, "FileNotExist", "error.badMetadataFile");
                return mapping.findForward("insertNewExercise");
            }
        if (xmlZipFile == null || xmlZipFile.getFileData() == null
                || xmlZipFile.getFileData().length == 0) {
            error(request, "FileNotExist", "error.nullXmlZipFile");
            return mapping.findForward("insertNewExercise");
        } else if (!(xmlZipFile.getContentType().equals("application/x-zip-compressed")
                || xmlZipFile.getContentType().equals("text/xml")
                || xmlZipFile.getContentType().equals("application/xml") || (xmlZipFile.getContentType()
                .equals("application/zip")))) {
            error(request, "FileNotExist", "error.badXmlZipFile");
            return mapping.findForward("insertNewExercise");
        }
        final String path = getServlet().getServletContext().getRealPath("/");
        List badXmls = null;
        try {
            Object[] args = { executionCourseId, xmlZipFile, path };
            badXmls = (List) ServiceUtils.executeService(userView, "InsertExercise", args);
        } catch (InvalidMetadataException e) {
            error(request, "FileNotExist", "error.badMetadataFile");
            return mapping.findForward("insertNewExercise");
        } catch (InvalidXMLFilesException e) {
            error(request, "FileNotExist", "error.badXmlFiles");
            return mapping.findForward("insertNewExercise");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("badXmls", badXmls);
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward prepareRemoveExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("exerciseCode", getCodeFromRequest(request, "exerciseCode"));
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return mapping.findForward("prepareRemoveExercise");
    }

    public ActionForward removeExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer metadataId = getCodeFromRequest(request, "exerciseCode");
        try {
            ServiceUtils.executeService(userView, "DeleteExercise", new Object[] { executionCourseId,
                    metadataId });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("successfulDeletion", "true");
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward prepareEditExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer exerciseId = getCodeFromRequest(request, "exerciseCode");
        Integer variationCode = getCodeFromRequest(request, "variationCode");
        Metadata metadata = null;
        try {
            metadata = (Metadata) ServiceUtils.executeService(userView, "ReadExercise", new Object[] {
                    executionCourseId, exerciseId });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (metadata == null || !metadata.getVisibility().booleanValue()) {
            return exercisesFirstPage(mapping, form, request, response);
        }
        String path = getServlet().getServletContext().getRealPath("/");
        ParseSubQuestion parse = new ParseSubQuestion();
        if (variationCode != null) {
            for (Question question : metadata.getVisibleQuestions()) {
                if ((question.getIdInternal().equals(variationCode) || variationCode.intValue() == -2)
                        && (question.getSubQuestions() == null || question.getSubQuestions().size() == 0)) {
                    try {
                        question = parse.parseSubQuestion(question, path);
                    } catch (ParseQuestionException e) {
                        throw new FenixActionException();
                    } catch (ParseException e) {
                        throw new FenixActionException();
                    }
                }
            }
        } else {
            variationCode = new Integer(-1);
        }

        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("exerciseCode", exerciseId);
        request.setAttribute("variationCode", variationCode);
        request.setAttribute("metadata", metadata);
        List questionDifficultyList = (new QuestionDifficultyType()).getAllTypes();
        request.setAttribute("questionDifficultyList", questionDifficultyList);
        return mapping.findForward("editExercise");
    }

    public ActionForward editExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer exerciseId = getCodeFromRequest(request, "exerciseCode");
        final String author = request.getParameter("author");
        final String description = request.getParameter("description");
        String difficulty = request.getParameter("difficulty");
        final String learningTime = request.getParameter("learningTimeFormatted");
        final String level = request.getParameter("level");
        final String mainSubject = request.getParameter("mainSubject");
        final String secondarySubject = request.getParameter("secondarySubject");
        if (!difficulty.equals("-1"))
            difficulty = new QuestionDifficultyType(new Integer(difficulty)).getTypeString();
        Boolean result = null;
        try {
            Object[] args = { executionCourseId, exerciseId, author, description, difficulty,
                    string2Hour(learningTime), level, mainSubject, secondarySubject };
            result = (Boolean) ServiceUtils.executeService(userView, "EditExercise", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("successfulEdition", result);
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        return exercisesFirstPage(mapping, form, request, response);
    }

    public ActionForward prepareRemoveExerciseVariation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = (IUserView) request.getSession(false).getAttribute(
                SessionConstants.U_VIEW);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer variationCode = getCodeFromRequest(request, "variationCode");
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        try {
            List<LabelValueBean> result = (List<LabelValueBean>) ServiceUtils.executeService(userView,
                    "DeleteExerciseVariation", new Object[] { executionCourseId, variationCode });
            if (result == null || result.size() == 0) {
                return prepareEditExercise(mapping, form, request, response);
            }
            request.setAttribute("studentsList", result);
            InfoQuestion infoQuestion = (InfoQuestion) ServiceUtils.executeService(userView,
                    "ReadQuestion", new Object[] { executionCourseId, null, variationCode,
                            getServlet().getServletContext().getRealPath("/") });
            request.setAttribute("infoQuestion", infoQuestion);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return mapping.findForward("removeExerciseVariation");
    }

    public ActionForward removeExerciseVariation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = (IUserView) request.getSession(false).getAttribute(
                SessionConstants.U_VIEW);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer exerciseId = getCodeFromRequest(request, "exerciseCode");
        final Integer variationCode = getCodeFromRequest(request, "variationCode");
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("exerciseCode", exerciseId);

        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();
        try {
            infoSiteDistributedTestAdvisoryList = (List<InfoSiteDistributedTestAdvisory>) ServiceUtils
                    .executeService(userView, "ChangeStudentTestQuestion", new Object[] {
                            executionCourseId,
                            null,
                            variationCode,
                            null,
                            null,
                            new TestQuestionChangesType(TestQuestionChangesType.CHANGE_VARIATION),
                            new Boolean(true),
                            new TestQuestionStudentsChangesType(
                                    TestQuestionStudentsChangesType.ALL_STUDENTS),
                            request.getContextPath() });

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("successfulChanged", infoSiteDistributedTestAdvisoryList);

        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (InfoSiteDistributedTestAdvisory infoSiteDistributedTestAdvisory : infoSiteDistributedTestAdvisoryList) {
            List<InfoStudent> studentWithoutAdvisory = infoSiteDistributedTestAdvisory
                    .getInfoStudentList();
            List<InfoStudent> result = new ArrayList<InfoStudent>();
            for (int times = 0; times < 3; times++) {
                for (InfoStudent student : studentWithoutAdvisory) {
                    try {
                        ServiceUtils.executeService(userView, "InsertStudentDistributedTestAdvisory",
                                new Object[] {
                                        executionCourseId,
                                        infoSiteDistributedTestAdvisory.getInfoAdvisory()
                                                .getIdInternal(), student.getIdInternal() });
                    } catch (FenixServiceException e) {
                        result.add(student);
                    }
                }
                studentWithoutAdvisory = result;
                if (studentWithoutAdvisory.size() == 0) {
                    break;
                }
            }
            infoStudentList.addAll(studentWithoutAdvisory);
        }

        request.setAttribute("infoStudentList", infoStudentList);

        return prepareEditExercise(mapping, form, request, response);
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        Object objectCode = request.getAttribute(codeString);
        if (objectCode != null) {
            if (objectCode instanceof String)
                code = new Integer((String) objectCode);
            else if (objectCode instanceof Integer)
                code = (Integer) objectCode;
        } else {
            String thisCodeString = request.getParameter(codeString);
            if (thisCodeString != null)
                code = new Integer(thisCodeString);
        }
        return code;
    }

    private Calendar string2Hour(String hour) {
        if (hour.equals(""))
            return null;
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
}