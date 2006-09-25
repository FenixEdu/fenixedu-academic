/*
 * Created on 21/Apr/2004
 */

package net.sourceforge.fenixedu.util.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

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

    public static final int VARSUBSTRING_IGNORE_CASE = 15;

    public static final int NOTVARSUBSTRING_IGNORE_CASE = 16;

    //
    public static final String VAREQUAL_XML_STRING = "varequal";

    public static final String NOTVAREQUAL_XML_STRING = "notvarequal";

    public static final String VAREQUAL_IGNORE_CASE_XML_STRING = "varequalignorecase";

    public static final String NOTVAREQUAL_IGNORE_CASE_XML_STRING = "notvarequalignorecase";

    public static final String VARLT_XML_STRING = "varlt";

    public static final String NOTVARLT_XML_STRING = "notvarlt";

    public static final String VARLTE_XML_STRING = "varlte";

    public static final String NOTVARLTE_XML_STRING = "notvarlte";

    public static final String VARGT_XML_STRING = "vargt";

    public static final String NOTVARGT_XML_STRING = "notvargt";

    public static final String VARGTE_XML_STRING = "vargte";

    public static final String NOTVARGTE_XML_STRING = "notvargte";

    public static final String VARSUBSTRING_XML_STRING = "varsubstring";

    public static final String NOTVARSUBSTRING_XML_STRING = "notvarsubstring";

    public static final String VARSUBSTRING_IGNORE_CASE_XML_STRING = "varsubstringignorecase";

    public static final String NOTVARSUBSTRING_IGNORE_CASE_XML_STRING = "notvarsubstringignorecase";

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

    //
    public static final String VAREQUAL_STRING = "Igual";

    public static final String NOTVAREQUAL_STRING = "Diferente";

    public static final String VARSUBSTRING_STRING = "Contém ";

    public static final String NOTVARSUBSTRING_STRING = "Não contém";

    private Integer condition;

    private String conditionSignal;

    private String response;

    private String responseLabelId;

    public ResponseCondition(String condition, String response, String responseLabelId) {
        this.condition = getConditionCode(condition);
        this.response = response;
        this.responseLabelId = responseLabelId;
        setConditionSignal();
    }

    private static Integer getConditionCode(String condition) {
        if (condition.compareToIgnoreCase(VAREQUAL_XML_STRING) == 0)
            return new Integer(VAREQUAL);
        else if (condition.compareToIgnoreCase(VAREQUAL_IGNORE_CASE_XML_STRING) == 0)
            return new Integer(VAREQUAL_IGNORE_CASE);
        else if (condition.compareToIgnoreCase(VARLT_XML_STRING) == 0)
            return new Integer(VARLT);
        else if (condition.compareToIgnoreCase(VARLTE_XML_STRING) == 0)
            return new Integer(VARLTE);
        else if (condition.compareToIgnoreCase(VARGT_XML_STRING) == 0)
            return new Integer(VARGT);
        else if (condition.compareToIgnoreCase(VARGTE_XML_STRING) == 0)
            return new Integer(VARGTE);
        else if (condition.compareToIgnoreCase(VARSUBSTRING_XML_STRING) == 0)
            return new Integer(VARSUBSTRING);
        else if (condition.compareToIgnoreCase(VARSUBSTRING_IGNORE_CASE_XML_STRING) == 0)
            return new Integer(VARSUBSTRING_IGNORE_CASE);
        if (condition.compareToIgnoreCase(NOTVAREQUAL_XML_STRING) == 0)
            return new Integer(NOTVAREQUAL);
        else if (condition.compareToIgnoreCase(NOTVAREQUAL_IGNORE_CASE_XML_STRING) == 0)
            return new Integer(NOTVAREQUAL_IGNORE_CASE);
        else if (condition.compareToIgnoreCase(NOTVARLT_XML_STRING) == 0)
            return new Integer(NOTVARLT);
        else if (condition.compareToIgnoreCase(NOTVARLTE_XML_STRING) == 0)
            return new Integer(NOTVARLTE);
        else if (condition.compareToIgnoreCase(NOTVARGT_XML_STRING) == 0)
            return new Integer(NOTVARGT);
        else if (condition.compareToIgnoreCase(NOTVARGTE_XML_STRING) == 0)
            return new Integer(NOTVARGTE);
        else if (condition.compareToIgnoreCase(NOTVARSUBSTRING_XML_STRING) == 0)
            return new Integer(NOTVARSUBSTRING);
        else if (condition.compareToIgnoreCase(NOTVARSUBSTRING_IGNORE_CASE_XML_STRING) == 0)
            return new Integer(NOTVARSUBSTRING_IGNORE_CASE);

        return null;
    }

    public static String getConditionString(Integer condition) {
        if (condition.intValue() == VAREQUAL)
            return VAREQUAL_XML_STRING;
        else if (condition.intValue() == VAREQUAL_IGNORE_CASE)
            return VAREQUAL_IGNORE_CASE_XML_STRING;
        else if (condition.intValue() == VARLT)
            return VARLT_XML_STRING;
        else if (condition.intValue() == VARLTE)
            return VARLTE_XML_STRING;
        else if (condition.intValue() == VARGT)
            return VARGT_XML_STRING;
        else if (condition.intValue() == VARGTE)
            return VARGTE_XML_STRING;
        else if (condition.intValue() == VARSUBSTRING)
            return VARSUBSTRING_XML_STRING;
        else if (condition.intValue() == VARSUBSTRING_IGNORE_CASE)
            return VARSUBSTRING_IGNORE_CASE_XML_STRING;
        if (condition.intValue() == NOTVAREQUAL)
            return NOTVAREQUAL_XML_STRING;
        if (condition.intValue() == NOTVAREQUAL_IGNORE_CASE)
            return NOTVAREQUAL_IGNORE_CASE_XML_STRING;
        else if (condition.intValue() == NOTVARLT)
            return NOTVARLT_XML_STRING;
        else if (condition.intValue() == NOTVARLTE)
            return NOTVARLTE_XML_STRING;
        else if (condition.intValue() == NOTVARGT)
            return NOTVARGT_XML_STRING;
        else if (condition.intValue() == NOTVARGTE)
            return NOTVARGTE_XML_STRING;
        else if (condition.intValue() == NOTVARSUBSTRING)
            return NOTVARSUBSTRING_XML_STRING;
        else if (condition.intValue() == NOTVARSUBSTRING_IGNORE_CASE)
            return NOTVARSUBSTRING_IGNORE_CASE_XML_STRING;
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
        else if (condition.intValue() == VARSUBSTRING_IGNORE_CASE)
            conditionSignal = VARSUBSTRING_IGNORE_CASE_XML_STRING;
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
        else if (condition.intValue() == NOTVARSUBSTRING_IGNORE_CASE)
            conditionSignal = NOTVARSUBSTRING_IGNORE_CASE_XML_STRING;
        else
            conditionSignal = "";
    }

    public static List<LabelValueBean> getConditionSignalsToStringQuestion() {
        List<LabelValueBean> signalsList = new ArrayList<LabelValueBean>();
        signalsList.add(new LabelValueBean(VAREQUAL_STRING, getConditionCode(VAREQUAL_XML_STRING)
                .toString()));
        signalsList.add(new LabelValueBean(NOTVAREQUAL_STRING, getConditionCode(NOTVAREQUAL_XML_STRING)
                .toString()));
        signalsList.add(new LabelValueBean(VARSUBSTRING_STRING,
                getConditionCode(VARSUBSTRING_XML_STRING).toString()));
        signalsList.add(new LabelValueBean(NOTVARSUBSTRING_STRING, getConditionCode(
                NOTVARSUBSTRING_XML_STRING).toString()));
        return signalsList;
    }

    public static List<LabelValueBean> getConditionSignalsToNumericalQuestion() {
        List<LabelValueBean> signalsList = new ArrayList<LabelValueBean>();
        signalsList.add(new LabelValueBean(VAREQUAL_SIGNAL, getConditionCode(VAREQUAL_XML_STRING)
                .toString()));
        signalsList.add(new LabelValueBean(NOTVAREQUAL_SIGNAL, getConditionCode(NOTVAREQUAL_XML_STRING)
                .toString()));
        signalsList.add(new LabelValueBean(VARLT_SIGNAL, getConditionCode(VARLT_XML_STRING).toString()));
        signalsList
                .add(new LabelValueBean(VARLTE_SIGNAL, getConditionCode(VARLTE_XML_STRING).toString()));
        signalsList.add(new LabelValueBean(VARGT_SIGNAL, getConditionCode(VARGT_XML_STRING).toString()));
        signalsList
                .add(new LabelValueBean(VARGTE_SIGNAL, getConditionCode(VARGTE_XML_STRING).toString()));
        return signalsList;
    }

    public Integer getReverseCondition() {
        if (condition.intValue() == VAREQUAL)
            return new Integer(NOTVAREQUAL);
        if (condition.intValue() == NOTVAREQUAL)
            return new Integer(VAREQUAL);
        if (condition.intValue() == VAREQUAL_IGNORE_CASE)
            return new Integer(NOTVAREQUAL_IGNORE_CASE);
        if (condition.intValue() == NOTVAREQUAL_IGNORE_CASE)
            return new Integer(VAREQUAL_IGNORE_CASE);
        if (condition.intValue() == VARSUBSTRING)
            return new Integer(NOTVARSUBSTRING);
        if (condition.intValue() == NOTVARSUBSTRING)
            return new Integer(VARSUBSTRING);
        if (condition.intValue() == VARSUBSTRING_IGNORE_CASE)
            return new Integer(NOTVARSUBSTRING_IGNORE_CASE);
        if (condition.intValue() == NOTVARSUBSTRING_IGNORE_CASE)
            return new Integer(VARSUBSTRING_IGNORE_CASE);
        if (condition.intValue() == VARLT)
            return new Integer(NOTVARLT);
        if (condition.intValue() == NOTVARLT)
            return new Integer(VARLT);
        if (condition.intValue() == VARLTE)
            return new Integer(NOTVARLTE);
        if (condition.intValue() == NOTVARLTE)
            return new Integer(VARLTE);
        if (condition.intValue() == VARGT)
            return new Integer(NOTVARGT);
        if (condition.intValue() == NOTVARGT)
            return new Integer(VARGT);
        if (condition.intValue() == VARGTE)
            return new Integer(NOTVARGTE);
        if (condition.intValue() == NOTVARGTE)
            return new Integer(VARGTE);
        return null;
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

    public boolean isCorrectLID(String userResponse) {
        if (getCondition().intValue() == VAREQUAL) {
            return (userResponse.compareTo(getResponse()) == 0 ? true : false);
        } else if (getCondition().intValue() == NOTVAREQUAL) {
            return (userResponse.compareTo(getResponse()) == 0 ? false : true);
        }
        return false;
    }

    public boolean isCorrectSTR(String userResponse) {
        if (getCondition().intValue() == VAREQUAL)
            return (userResponse.compareTo(getResponse()) == 0 ? true : false);
        else if (getCondition().intValue() == ResponseCondition.VAREQUAL_IGNORE_CASE)
            return (userResponse.compareToIgnoreCase(getResponse()) == 0 ? true : false);
        else if (getCondition().intValue() == NOTVAREQUAL)
            return (userResponse.compareTo(getResponse()) == 0 ? false : true);
        else if (getCondition().intValue() == NOTVAREQUAL_IGNORE_CASE)
            return (userResponse.compareToIgnoreCase(getResponse()) == 0 ? false : true);
        else if (getCondition().intValue() == VARSUBSTRING)
            return ((userResponse.matches(getResponse())) ? true : false);
        else if (getCondition().intValue() == NOTVARSUBSTRING)
            return ((userResponse.matches(getResponse())) ? false : true);
        else if (getCondition().intValue() == VARSUBSTRING_IGNORE_CASE)
            return ((userResponse.matches(getResponse().toLowerCase())) ? true : false);
        else if (getCondition().intValue() == NOTVARSUBSTRING_IGNORE_CASE)
            return ((userResponse.matches(getResponse().toLowerCase())) ? false : true);
        return false;
    }

    public boolean isCorrectNUM(Double userResponse) {
        Double response = new Double(getResponse());
        if (getCondition().intValue() == VAREQUAL)
            return (userResponse.compareTo(response) == 0 ? true : false);
        else if (getCondition().intValue() == NOTVAREQUAL)
            return (userResponse.compareTo(response) == 0 ? false : true);
        else if (getCondition().intValue() == VARLT || getCondition().intValue() == NOTVARGTE)
            return ((userResponse.compareTo(response) < 0) ? true : false);
        else if (getCondition().intValue() == ResponseCondition.VARLTE
                || getCondition().intValue() == NOTVARGT)
            return ((userResponse.compareTo(response) <= 0) ? true : false);
        else if (getCondition().intValue() == ResponseCondition.VARGT
                || getCondition().intValue() == NOTVARLTE)
            return ((userResponse.compareTo(response) > 0) ? true : false);
        else if (getCondition().intValue() == VARGTE || getCondition().intValue() == NOTVARLT)
            return ((userResponse.compareTo(response) >= 0) ? true : false);
        return false;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ResponseCondition) {
            ResponseCondition rc = (ResponseCondition) obj;
            result = getResponse().equals(rc.getResponse()) && getCondition().equals(rc.getCondition());
        }
        return result;
    }

    public String toXML() {
        String result = new String();
        String endResult = new String();
        String ignoreCase = "No";

        String condition = getConditionString(this.condition);
        if (condition.startsWith("not")) {
            result = result.concat("<not>");
            endResult = endResult.concat("</not>");
            condition = condition.substring(3, condition.length());
        }
        if (condition.endsWith("ignorecase")) {
            condition = condition.replaceAll("ignorecase", "");
            ignoreCase = "Yes";
        }
        result = result.concat(new String("<" + condition));
        if (condition.equals(VAREQUAL_XML_STRING) || condition.equals(VARSUBSTRING_XML_STRING))
            result = result.concat(new String(" case=\"" + ignoreCase) + "\"");
        result = result.concat(new String(" respident=\"" + responseLabelId + "\">\n" + response
                + "\n</" + condition + ">"));
        return result.concat(endResult);
    }
}