/*
 * Created on 8/Apr/2004
 */

package Util.tests;

import java.util.ArrayList;
import java.util.List;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 */

public class XMLQuestion extends FenixUtil {

    private String openTag = "<";

    private String openEndTag = "</";

    private String closeTag = ">\n";

    private String presentation = "presentation";

    private String resprocessing = "resprocessing";

    private String flow = "flow";

    private String material = "material";

    private String mattext = "mattext";

    //////////////////////////
    private String start = "<?xml version=\"1.0\" encoding=\"Latin1\"?>\n<!DOCTYPE questestinterop SYSTEM \"file:/qtiasiv1p2.dtd\">\n<questestinterop>\n<item ident=\"xxx\">\n";

    private String end = "</item>\n</questestinterop>";

    //////////////////////////
    private List allOptions = new ArrayList();

    public XMLQuestion() {
    }

    public String getXmlQuestion(String questionText, String secondQuestionText,
            QuestionType questionType, String[] options, String[] shuffle,
            List responseProcessingInstructionsList, String correctFeedbackText,
            String wrongFeedbackText, Boolean breakLineBeforeResponseBox,
            Boolean breakLineAfterResponseBox) {

        if (breakLineBeforeResponseBox == null)
            breakLineBeforeResponseBox = new Boolean(false);
        if (breakLineAfterResponseBox == null)
            breakLineAfterResponseBox = new Boolean(false);
        String result = start
                + getPresentation(questionText, secondQuestionText, breakLineBeforeResponseBox,
                        breakLineAfterResponseBox, questionType, options, shuffle);
        result = result.concat(getResponseProcessingInstructions(responseProcessingInstructionsList,
                correctFeedbackText, wrongFeedbackText, questionType));
        return result + end;
    }

    private String getPresentation(String questionText, String secondQuestionText,
            Boolean breakLineBeforeResponseBox, Boolean breakLineAfterResponseBox,
            QuestionType questionType, String[] options, String[] shuffle) {
        String result = new String(getStartElementTag(presentation) + getStartElementTag(flow));
        result = result.concat(getTextArea(questionText));
        if (breakLineBeforeResponseBox.booleanValue())
            result = result.concat(getStartElementTag(flow));
        result = result.concat(getResponseLabels(questionType, options, shuffle));
        if (breakLineAfterResponseBox.booleanValue()) {
            if (breakLineBeforeResponseBox.booleanValue())
                result = result.concat(getEndElementTag(flow));
            result = result.concat(getStartElementTag(flow));
        }
        if (secondQuestionText != null && !secondQuestionText.equals(""))
            result = result.concat(getTextArea(secondQuestionText));
        if (breakLineAfterResponseBox.booleanValue() || breakLineBeforeResponseBox.booleanValue())
            result = result.concat(getEndElementTag(flow));
        result = result.concat(getEndElementTag(flow) + getEndElementTag(presentation));

        return result;
    }

    private String getResponseLabels(QuestionType questionType, String[] options, String[] shuffle) {
        String result = new String();
        if (questionType.getType().intValue() == QuestionType.LID) {
            for (int i = 0; i < options.length; i++) {
                allOptions.add(new Integer(i + 1).toString());
                result = result.concat(getResponseLabelLID(options[i], i + 1, contains(shuffle,
                        new Integer(i + 1).toString())));
            }
        } else
            result = result.concat(getResponseLabel());
        result = questionType.toXML(result, 1);
        return result;
    }

    private String getResponseLabel() {
        return new String("\n<response_label ident=\"1\"/>\n");
    }

    private String getResponseLabelLID(String text, int ident, boolean shuffle) {
        String result = new String("<response_label ident=\"" + ident + "\" rshuffle=\""
                + RenderChoise.getShuffleString(shuffle) + "\">");
        String[] texts = text.split("\n");
        for (int i = 0; i < texts.length; i++)
            if (!texts[i].equals("\r"))
                result = result.concat(getMattextElement(texts[i]));
        return result.concat("</response_label>\n");
    }

    private String getResponseProcessingInstructions(List rpList, String correctFeedbackText,
            String wrongFeedbackText, QuestionType questionType) {
        if (rpList == null || rpList.size() == 0)
            return "";
        String result = new String(
                getStartElementTag(resprocessing)
                        + "<outcomes>\n<decvar varname=\"SCORE\" vartype=\"Decimal\" defaultval=\"0\"/>\n</outcomes>\n");
        String displayfeedbackCorrect = "";
        String displayfeedbackIncorrect = "";
        String feedback = "";
        if (correctFeedbackText != null && !correctFeedbackText.equals("")) {
            displayfeedbackCorrect = "<displayfeedback feedbacktype=\"Response\" linkrefid=\"Correct\"/>\n";
            feedback = feedback.concat(new String("<itemfeedback ident = \"Correct\" view = \"All\">\n"
                    + getMattextElement(correctFeedbackText) + getEndElementTag("itemfeedback")));
        }
        if (wrongFeedbackText != null && !wrongFeedbackText.equals("")) {
            if (questionType.getType().intValue() == QuestionType.LID)
                rpList.addAll(getIncorrectResponseProcessingLID(rpList, questionType
                        .getCardinalityType().getType().intValue()));
            else
                rpList.add(getIncorrectResponseProcessing(rpList));
            displayfeedbackIncorrect = "<displayfeedback feedbacktype=\"Response\" linkrefid=\"Incorrect\"/>\n";
            feedback = feedback.concat(new String(
                    "<itemfeedback ident = \"Incorrect\" view = \"All\">\n"
                            + getMattextElement(wrongFeedbackText) + getEndElementTag("itemfeedback")));
        }

        for (int i = 0; i < rpList.size(); i++) {
            ResponseProcessing responseProcessing = (ResponseProcessing) rpList.get(i);
            if (responseProcessing.isFenixCorrectResponse())
                result = result.concat(responseProcessing.toXML(displayfeedbackCorrect));
            else
                result = result.concat(responseProcessing.toXML(displayfeedbackIncorrect));
        }

        result = result.concat(getEndElementTag(resprocessing) + feedback);
        return result;
    }

    //ResponseProcessingList
    private List getIncorrectResponseProcessingLID(List rpList, int cardinality) {
        List newRpList = new ArrayList();
        for (int i = 0; i < rpList.size(); i++) {
            ResponseProcessing responseProcessing = (ResponseProcessing) rpList.get(i);
            if (responseProcessing.isFenixCorrectResponse()) {
                ResponseProcessing newResponseProcessing = new ResponseProcessing(new ArrayList(),
                        new Double(0), new Integer(ResponseProcessing.SET), null, false);
                if (cardinality == CardinalityType.MULTIPLE)
                    newRpList = getAllMultipleIncorrectResponseProcessingList(newResponseProcessing,
                            responseProcessing.getResponseConditions());
                else {
                    newResponseProcessing.setResponseConditions(responseProcessing
                            .getResponseConditions());
                    newRpList = getAllSingleIncorrectResponseProcessingList(newResponseProcessing);
                }
            }
        }
        return newRpList;
    }

    private List getAllSingleIncorrectResponseProcessingList(ResponseProcessing rp) {

        List all = new ArrayList();
        for (int i = 0; i < allOptions.size(); i++) {
            String thisResponse = (String) allOptions.get(i);
            ResponseProcessing newRp = new ResponseProcessing(new ArrayList(), rp.getResponseValue(), rp
                    .getAction(), null, false);
            if (!(thisResponse.equals(((ResponseCondition) rp.getResponseConditions().get(0))
                    .getResponse()))) {
                newRp.getResponseConditions().add(
                        new ResponseCondition(ResponseCondition.VAREQUAL_XML_STRING, thisResponse, "1"));
                all.add(newRp);
            }
        }
        return all;
    }

    private List getAllMultipleIncorrectResponseProcessingList(ResponseProcessing rp,
            List correctResponseCondition) {
        List all = new ArrayList();
        all.add(rp);
        for (int i = 0; i < allOptions.size(); i++) {
            all = addNextIncorrectResponse(all, (String) allOptions.get(i), "1");
        }
        // remove correct condition
        for (int i = 0; i < all.size(); i++) {
            ResponseProcessing rpAux = (ResponseProcessing) all.get(i);
            if (rpAux.hasEqualVAREQUALConditionList(correctResponseCondition))
                all.remove(i);
        }
        return all;
    }

    private List addNextIncorrectResponse(List rpList, String value, String ident) {
        List newRpList = new ArrayList();
        for (int i = 0; i < rpList.size(); i++) {
            ResponseProcessing rp2Copy = (ResponseProcessing) rpList.get(i);

            ResponseProcessing rp = new ResponseProcessing(new ArrayList(), rp2Copy.getResponseValue(),
                    rp2Copy.getAction(), null, false);
            ResponseProcessing rpNot = new ResponseProcessing(new ArrayList(), rp2Copy
                    .getResponseValue(), rp2Copy.getAction(), null, false);

            for (int j = 0; j < rp2Copy.getResponseConditions().size(); j++) {
                ResponseCondition rc = (ResponseCondition) rp2Copy.getResponseConditions().get(j);
                rp.getResponseConditions().add(rc);
                rpNot.getResponseConditions().add(rc);
            }
            rp.getResponseConditions().add(
                    new ResponseCondition(ResponseCondition.VAREQUAL_XML_STRING, value, ident));
            rpNot.getResponseConditions().add(
                    new ResponseCondition(ResponseCondition.NOTVAREQUAL_XML_STRING, value, ident));
            newRpList.add(rp);
            newRpList.add(rpNot);
        }
        return newRpList;
    }

    private ResponseProcessing getIncorrectResponseProcessing(List rpList) {
        ResponseProcessing newRp = null;
        for (int i = 0; i < rpList.size(); i++) {
            ResponseProcessing responseProcessing = (ResponseProcessing) rpList.get(i);
            if (responseProcessing.isFenixCorrectResponse()) {
                newRp = new ResponseProcessing(getIncorrectResponseConditionsList(responseProcessing
                        .getResponseConditions()), new Double(0), new Integer(ResponseProcessing.SET),
                        null, false);
            }
        }
        return newRp;
    }

    private List getIncorrectResponseConditionsList(List correctRcList) {
        List newRcList = new ArrayList();
        for (int i = 0; i < correctRcList.size(); i++) {
            ResponseCondition responseCondition = (ResponseCondition) correctRcList.get(i);
            ResponseCondition newRc = new ResponseCondition(ResponseCondition
                    .getConditionString(responseCondition.getReverseCondition()), responseCondition
                    .getResponse(), responseCondition.getResponseLabelId());
            newRcList.add(newRc);
        }
        return newRcList;
    }

    private String getStartElementTag(String tag) {
        return openTag + tag + closeTag;
    }

    private String getEndElementTag(String tag) {
        return openEndTag + tag + closeTag;
    }

    private String getTextArea(String text) {
        text = text.replace('\r', '\n');
        String[] texts = text.split("\n");
        String result = new String();
        for (int i = 0; i < texts.length; i++) {
            if (!texts[i].equals("")) {
                if (i != 0 && i != texts.length - 1)
                    result = result.concat(getStartElementTag(flow));

                result = result.concat(getMattextElement(texts[i]));
                if (i != 0 && i != texts.length - 1)
                    result = result.concat(getEndElementTag(flow));
            }
        }
        return result;
    }

    private String getMattextElement(String text) {
        return new String(getStartElementTag(material) + getStartElementTag(mattext)
                + convertCharacters(text) + getEndElementTag(mattext) + getEndElementTag(material));
    }

    private boolean contains(String[] array, String value) {
        for (int i = 0; i < array.length; i++)
            if (array[i].equals(value))
                return true;
        return false;
    }

    private String convertCharacters(String text) {
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
    }
}