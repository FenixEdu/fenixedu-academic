package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

import org.apache.struts.util.LabelValueBean;

public class SubQuestion {

    private String itemId;

    private String nextItemId;

    private String title;

    private List<LabelValueBean> prePresentation;

    private List<LabelValueBean> presentation;

    private List<QuestionOption> options;

    private List<ResponseProcessing> responseProcessingInstructions;

    private Double questionValue;

    private QuestionType questionType;

    private List<SubQuestion> subQuestions;

    private String[] shuffle;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getNextItemId() {
        return nextItemId;
    }

    public void setNextItemId(String nextItemId) {
        this.nextItemId = nextItemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

    public List<LabelValueBean> getPrePresentation() {
        return prePresentation;
    }

    public void setPrePresentation(List<LabelValueBean> prePresentation) {
        this.prePresentation = prePresentation;
    }

    public List<LabelValueBean> getPresentation() {
        return presentation;
    }

    public void setPresentation(List<LabelValueBean> presentation) {
        this.presentation = presentation;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Double getQuestionValue() {
        return questionValue;
    }

    public void setQuestionValue(Double questionValue) {
        this.questionValue = questionValue;
    }

    public List<ResponseProcessing> getResponseProcessingInstructions() {
        return responseProcessingInstructions;
    }

    public void setResponseProcessingInstructions(List<ResponseProcessing> responseProcessingInstructions) {
        this.responseProcessingInstructions = responseProcessingInstructions;
    }

    public String[] getShuffle() {
        return shuffle;
    }

    public void setShuffle(String[] shuffle) {
        this.shuffle = shuffle;
    }

    public String getShuffleString() {
        StringBuffer result = new StringBuffer();
        result.append("[");
        for (int i = 0; i < getShuffle().length; i++) {
            result.append(getShuffle()[i]);
            if (i != getShuffle().length - 1) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }

    public List<SubQuestion> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<SubQuestion> subQuestions) {
        this.subQuestions = subQuestions;
    }

    public String getImage(int imageId) {
        int imageIdAux = 1;
        for (LabelValueBean lvb : getPrePresentation()) {
            if (lvb.getLabel().startsWith("image")) {
                if (imageIdAux == imageId) {
                    return lvb.getValue();
                }
                imageIdAux++;
            }
        }
        for (LabelValueBean lvb : getPresentation()) {
            if (lvb.getLabel().startsWith("image")) {
                if (imageIdAux == imageId) {
                    return lvb.getValue();
                }
                imageIdAux++;
            }
        }
        for (QuestionOption qo : getOptions()) {
            for (LabelValueBean lvb : qo.getOptionContent()) {
                if (lvb.getLabel().startsWith("image")) {
                    if (imageIdAux == imageId) {
                        return lvb.getValue();
                    }
                    imageIdAux++;
                }
            }
        }
        for (ResponseProcessing responseProcessing : getResponseProcessingInstructions()) {
            for (LabelValueBean lvb : responseProcessing.getFeedback()) {
                if (lvb.getLabel().startsWith("image")) {
                    if (imageIdAux == imageId) {
                        return lvb.getValue();
                    }
                    imageIdAux++;
                }
            }
        }
        return null;
    }

    public Double getMaxValue() {
        Double maxValue = new Double(0);
        for (ResponseProcessing responseProcessing : getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        return maxValue;
    }

}
