package Util.tests;

import java.util.List;

import Util.FenixUtil;

public class QuestionOption extends FenixUtil {

    private String optionId;

    //list of labelValueBeans
    private List optionContent;

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

    public List getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(List optionContent) {
        this.optionContent = optionContent;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
}