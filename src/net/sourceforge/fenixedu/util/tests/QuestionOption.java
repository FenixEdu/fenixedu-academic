package net.sourceforge.fenixedu.util.tests;

import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

public class QuestionOption extends FenixUtil {

    private String optionId;

    private List<LabelValueBean> optionContent;

    private boolean emptyResponse = false;

    public QuestionOption() {
        super();
    }

    public QuestionOption(String optionId) {
        super();
        this.optionId = optionId;
    }

    public boolean getEmptyResponse() {
        return emptyResponse;
    }

    public void setEmptyResponse(boolean emptyResponse) {
        this.emptyResponse = emptyResponse;
    }

    public List<LabelValueBean> getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(List<LabelValueBean> optionContent) {
        this.optionContent = optionContent;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
}