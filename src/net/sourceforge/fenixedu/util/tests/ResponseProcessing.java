/*
 * Created on 21/Apr/2004
 */

package net.sourceforge.fenixedu.util.tests;

import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */

public class ResponseProcessing extends FenixUtil {

    public static final int SET = 1;

    public static final int ADD = 2;

    public static final int SUBTRACT = 3;

    public static final int MULTIPLY = 4;

    public static final int DIVIDE = 5;

    public static final String SET_STRING = "Set";

    public static final String ADD_STRING = "Add";

    public static final String SUBTRACT_STRING = "Subtract";

    public static final String MULTIPLY_STRING = "Multiply";

    public static final String DIVIDE_STRING = "Divide";

    private List<ResponseCondition> responseConditions;

    private Double responseValue;

    private Integer action;

    private List<LabelValueBean> feedback;

    private int responseProcessingId;

    private boolean fenixCorrectResponse;

    private String nextItem;

    public ResponseProcessing(int id) {
        responseProcessingId = id;
    }

    public ResponseProcessing(List<ResponseCondition> responseConditions, Double responseValue,
            Integer action, List<LabelValueBean> feedback, boolean fenixCorrectResponse) {
        this.responseConditions = responseConditions;
        this.responseValue = responseValue;
        this.action = action;
        this.feedback = feedback;
        this.fenixCorrectResponse = fenixCorrectResponse;
    }

    public int getResponseProcessingId() {
        return responseProcessingId;
    }

    public void setResponseProcessingId(int responseProcessingId) {
        this.responseProcessingId = responseProcessingId;
    }

    public List<ResponseCondition> getResponseConditions() {
        return responseConditions;
    }

    public void setResponseConditions(List<ResponseCondition> responses) {
        this.responseConditions = responses;
    }

    public Double getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(Double responseValue) {
        this.responseValue = responseValue;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(String actionString) {
        this.action = getActionCode(actionString);
    }

    public List<LabelValueBean> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<LabelValueBean> feedback) {
        this.feedback = feedback;
    }

    public boolean isFenixCorrectResponse() {
        return fenixCorrectResponse;
    }

    public void setFenixCorrectResponse(boolean fenixCorrectResponse) {
        this.fenixCorrectResponse = fenixCorrectResponse;
    }

    public String getNextItem() {
        return nextItem;
    }

    public void setNextItem(String nextItem) {
        this.nextItem = nextItem;
    }

    private Integer getActionCode(String actionString) {
        if (actionString.equals(SET_STRING))
            return new Integer(SET);
        else if (actionString.equals(ADD_STRING))
            return new Integer(ADD);
        else if (actionString.equals(SUBTRACT_STRING))
            return new Integer(SUBTRACT);
        else if (actionString.equals(MULTIPLY_STRING))
            return new Integer(MULTIPLY);
        else if (actionString.equals(DIVIDE_STRING))
            return new Integer(DIVIDE);
        return null;
    }

    private String getActionString(Integer actionCode) {
        if (actionCode.intValue() == SET)
            return SET_STRING;
        else if (actionCode.intValue() == ADD)
            return ADD_STRING;
        else if (actionCode.intValue() == SUBTRACT)
            return SUBTRACT_STRING;
        else if (actionCode.intValue() == MULTIPLY)
            return MULTIPLY_STRING;
        else if (actionCode.intValue() == DIVIDE)
            return DIVIDE_STRING;
        return null;
    }

    public boolean isThisConditionListInResponseProcessingList(List<ResponseProcessing> rpList,
            boolean lidQuestion) {
        for (ResponseProcessing responseProcessing : rpList) {
            if (lidQuestion) {
                if (!hasEqualVAREQUALConditionList(responseProcessing.getResponseConditions())) {
                    return false;
                }
            } else if (!hasEqualConditionList(responseProcessing.getResponseConditions())) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEqualConditionList(List<ResponseCondition> rcList) {
        for (ResponseCondition responseCondition : rcList) {
            if (!hasThisCondition(responseConditions, responseCondition)) {
                return false;
            }
        }
        for (ResponseCondition responseCondition : responseConditions) {
            if (!hasThisCondition(rcList, responseCondition)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEqualVAREQUALConditionList(List<ResponseCondition> rcList) {
        for (ResponseCondition responseCondition : rcList) {
            if (responseCondition.getCondition().intValue() == ResponseCondition.VAREQUAL) {
                if (!hasThisCondition(responseConditions, responseCondition)) {
                    return false;
                }
            }
        }
        for (ResponseCondition responseCondition : responseConditions) {
            if (responseCondition.getCondition().intValue() == ResponseCondition.VAREQUAL) {
                if (!hasThisCondition(rcList, responseCondition)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasThisCondition(List<ResponseCondition> rcList, ResponseCondition rc) {
        for (ResponseCondition responseCondition : rcList) {
            if (responseCondition.equals(rc)) {
                return true;
            }
        }
        return false;
    }

    public String toXML(String feedback) {
        String result = new String("<respcondition>\n<conditionvar>\n");
        for (int i = 0; i < responseConditions.size(); i++)
            result = result.concat(((ResponseCondition) responseConditions.get(i)).toXML());
        result = result.concat("\n</conditionvar>\n<setvar action=\"" + getActionString(action) + "\">"
                + responseValue + "\n</setvar>\n");
        if (feedback != null)
            result = result.concat(feedback);
        result = result.concat("</respcondition>\n");

        return result;
    }
}