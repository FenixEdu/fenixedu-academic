/*
 * Created on 21/Apr/2004
 */

package Util.tests;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 */

public class ResponseCondition extends FenixUtil {

    public static final int VAREQUAL = 1;

    public static final int VAREQUAL_IGNORE_CASE = 2;

    public static final int NOTVAREQUAL = 3;

    public static final int NOTVAREQUAL_IGNORE_CASE = 4;

    public static final int VARLT = 5;

    public static final int NOTVARLT = 6;

    public static final int VARLTE = 7;

    public static final int NOTVARLTE = 8;

    public static final int VARGT = 9;

    public static final int NOTVARGT = 10;

    public static final int VARGTE = 11;

    public static final int NOTVARGTE = 12;

    public static final int VARSUBSTRING = 13;

    public static final int NOTVARSUBSTRING = 14;

    public static final String VAREQUAL_STRING = "varequal";

    public static final String NOTVAREQUAL_STRING = "notvarequal";

    public static final String VAREQUAL_IGNORE_CASE_STRING = "varequalignorecase";

    public static final String NOTVAREQUAL_IGNORE_CASE_STRING = "notvarequalignorecase";

    public static final String VARLT_STRING = "varlt";

    public static final String NOTVARLT_STRING = "notvarlt";

    public static final String VARLTE_STRING = "varlte";

    public static final String NOTVARLTE_STRING = "notvarlte";

    public static final String VARGT_STRING = "vargt";

    public static final String NOTVARGT_STRING = "notvargt";

    public static final String VARGTE_STRING = "vargte";

    public static final String NOTVARGTE_STRING = "notvargte";

    public static final String VARSUBSTRING_STRING = "varsubstring";

    public static final String NOTVARSUBSTRING_STRING = "notvarsubstring";

    //
    public static final String VAREQUAL_SIGNAL = "=";

    public static final String NOTVAREQUAL_SIGNAL = "!=";

    public static final String VARLT_SIGNAL = "<";

    public static final String NOTVARLT_SIGNAL = ">=";

    public static final String VARLTE_SIGNAL = "<=";

    public static final String NOTVARLTE_SIGNAL = ">";

    public static final String VARGT_SIGNAL = ">";

    public static final String NOTVARGT_SIGNAL = "<=";

    public static final String VARGTE_SIGNAL = ">=";

    public static final String NOTVARGTE_SIGNAL = "<";

    private Integer condition;

    private String conditionSignal;

    private String response;

    private String responseLabelId;

    public ResponseCondition(String condition, String response,
            String responseLabelId) {
        this.condition = getConditionCode(condition);
        this.response = response;
        this.responseLabelId = responseLabelId;
        setConditionSignal();
    }

    private Integer getConditionCode(String condition) {
        if (condition.compareToIgnoreCase(VAREQUAL_STRING) == 0)
            return new Integer(VAREQUAL);
        else if (condition.compareToIgnoreCase(VAREQUAL_IGNORE_CASE_STRING) == 0)
            return new Integer(VAREQUAL);
        else if (condition.compareToIgnoreCase(VARLT_STRING) == 0)
            return new Integer(VARLT);
        else if (condition.compareToIgnoreCase(VARLTE_STRING) == 0)
            return new Integer(VARLTE);
        else if (condition.compareToIgnoreCase(VARGT_STRING) == 0)
            return new Integer(VARGT);
        else if (condition.compareToIgnoreCase(VARGTE_STRING) == 0)
            return new Integer(VARGTE);
        else if (condition.compareToIgnoreCase(VARSUBSTRING_STRING) == 0)
                return new Integer(VARSUBSTRING);
        if (condition.compareToIgnoreCase(NOTVAREQUAL_STRING) == 0)
            return new Integer(NOTVAREQUAL);
        else if (condition.compareToIgnoreCase(NOTVAREQUAL_IGNORE_CASE_STRING) == 0)
            return new Integer(NOTVAREQUAL);
        else if (condition.compareToIgnoreCase(NOTVARLT_STRING) == 0)
            return new Integer(NOTVARLT);
        else if (condition.compareToIgnoreCase(NOTVARLTE_STRING) == 0)
            return new Integer(NOTVARLTE);
        else if (condition.compareToIgnoreCase(NOTVARGT_STRING) == 0)
            return new Integer(NOTVARGT);
        else if (condition.compareToIgnoreCase(NOTVARGTE_STRING) == 0)
            return new Integer(NOTVARGTE);
        else if (condition.compareToIgnoreCase(NOTVARSUBSTRING_STRING) == 0)
                return new Integer(NOTVARSUBSTRING);
        return null;
    }

    public static String getConditionString(Integer condition) {
        if (condition.intValue() == VAREQUAL)
            return VAREQUAL_STRING;
        else if (condition.intValue() == VAREQUAL_IGNORE_CASE)
            return VAREQUAL_IGNORE_CASE_STRING;
        else if (condition.intValue() == VARLT)
            return VARLT_STRING;
        else if (condition.intValue() == VARLTE)
            return VARLTE_STRING;
        else if (condition.intValue() == VARGT)
            return VARGT_STRING;
        else if (condition.intValue() == VARGTE)
            return VARGTE_STRING;
        else if (condition.intValue() == VARSUBSTRING)
                return VARSUBSTRING_STRING;
        if (condition.intValue() == NOTVAREQUAL) return NOTVAREQUAL_STRING;
        if (condition.intValue() == NOTVAREQUAL_IGNORE_CASE)
            return NOTVAREQUAL_IGNORE_CASE_STRING;
        else if (condition.intValue() == NOTVARLT)
            return NOTVARLT_STRING;
        else if (condition.intValue() == NOTVARLTE)
            return NOTVARLTE_STRING;
        else if (condition.intValue() == NOTVARGT)
            return NOTVARGT_STRING;
        else if (condition.intValue() == NOTVARGTE)
            return NOTVARGTE_STRING;
        else if (condition.intValue() == NOTVARSUBSTRING)
                return NOTVARSUBSTRING_STRING;
        return null;
    }

    public void setConditionSignal() {
        if (condition.intValue() == VAREQUAL)
            conditionSignal = VAREQUAL_SIGNAL;
        else if (condition.intValue() == NOTVAREQUAL)
            conditionSignal = NOTVAREQUAL_SIGNAL;
        else if (condition.intValue() == VARLT)
            conditionSignal = VARLT_SIGNAL;
        else if (condition.intValue() == NOTVARLT)
            conditionSignal = NOTVARLT_SIGNAL;
        else if (condition.intValue() == VARLTE)
            conditionSignal = VARLTE_SIGNAL;
        else if (condition.intValue() == NOTVARLTE)
            conditionSignal = NOTVARLTE_SIGNAL;
        else if (condition.intValue() == VARGT)
            conditionSignal = VARGT_SIGNAL;
        else if (condition.intValue() == NOTVARGT)
            conditionSignal = NOTVARGT_SIGNAL;
        else if (condition.intValue() == VARGTE)
            conditionSignal = VARGTE_SIGNAL;
        else if (condition.intValue() == NOTVARGTE)
            conditionSignal = NOTVARGTE_SIGNAL;
        else
            conditionSignal = "";
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getConditionSignal() {
        return conditionSignal;
    }

    public void setConditionSignal(String conditionSignal) {
        this.conditionSignal = conditionSignal;
    }

    public String getResponseLabelId() {
        return responseLabelId;
    }

    public void setResponseLabelId(String responseLabelId) {
        this.responseLabelId = responseLabelId;
    }

    public boolean isCorrect(String userResponse) {
        if (getCondition().intValue() == VAREQUAL)
            return (userResponse.compareTo(getResponse()) == 0 ? true : false);
        else if (getCondition().intValue() == ResponseCondition.VAREQUAL_IGNORE_CASE)
            return (userResponse.compareToIgnoreCase(getResponse()) == 0 ? true
                    : false);
        else if (getCondition().intValue() == NOTVAREQUAL)
            return (userResponse.compareTo(getResponse()) == 0 ? false : true);
        else if (getCondition().intValue() == NOTVAREQUAL_IGNORE_CASE)
            return (userResponse.compareToIgnoreCase(getResponse()) == 0 ? false
                    : true);
        else if (getCondition().intValue() == VARLT
                || getCondition().intValue() == NOTVARGTE)
            return ((userResponse.compareTo(getResponse()) < 0) ? true : false);
        else if (getCondition().intValue() == VARLTE
                || getCondition().intValue() == ResponseCondition.NOTVARGT)
            return ((userResponse.compareTo(getResponse()) <= 0) ? true : false);
        else if (getCondition().intValue() == VARGT
                || getCondition().intValue() == NOTVARLTE)
            return ((userResponse.compareTo(getResponse()) > 0) ? true : false);
        else if (getCondition().intValue() == VARGTE
                || getCondition().intValue() == NOTVARLT)
            return ((userResponse.compareTo(getResponse()) >= 0) ? true : false);
        else if (getCondition().intValue() == VARSUBSTRING)
            return ((userResponse.matches(getResponse())) ? true : false);
        else if (getCondition().intValue() == NOTVARSUBSTRING)
                return ((userResponse.matches(getResponse())) ? false : true);
        return false;
    }

    public boolean isCorrect(Double userResponse) {
        Double response = new Double(getResponse());
        if (getCondition().intValue() == VAREQUAL)
            return (userResponse.compareTo(response) == 0 ? true : false);
        else if (getCondition().intValue() == NOTVAREQUAL)
            return (userResponse.compareTo(response) == 0 ? false : true);
        else if (getCondition().intValue() == VARLT
                || getCondition().intValue() == NOTVARGTE)
            return ((userResponse.compareTo(response) < 0) ? true : false);
        else if (getCondition().intValue() == ResponseCondition.VARLTE
                || getCondition().intValue() == NOTVARGT)
            return ((userResponse.compareTo(response) <= 0) ? true : false);
        else if (getCondition().intValue() == ResponseCondition.VARGT
                || getCondition().intValue() == NOTVARLTE)
            return ((userResponse.compareTo(response) > 0) ? true : false);
        else if (getCondition().intValue() == VARGTE
                || getCondition().intValue() == NOTVARLT)
                return ((userResponse.compareTo(response) >= 0) ? true : false);
        return false;
    }

}