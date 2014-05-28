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
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
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

    private Integer optionNumber;

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

    public Integer getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(Integer optionNumber) {
        this.optionNumber = optionNumber;
    }

    public String getShuffleString() {
        StringBuilder result = new StringBuilder();
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

    public String getImage(int imageIndex, Integer responseProcessingInstructionIndex) {
        int imageIdAux = 1;
        for (LabelValueBean lvb : getPrePresentation()) {
            if (lvb.getLabel().startsWith("image/")) {
                if (imageIdAux == imageIndex) {
                    return lvb.getValue();
                }
                imageIdAux++;
            }
        }
        for (LabelValueBean lvb : getPresentation()) {
            if (lvb.getLabel().startsWith("image/")) {
                if (imageIdAux == imageIndex) {
                    return lvb.getValue();
                }
                imageIdAux++;
            }
        }
        for (QuestionOption qo : getOptions()) {
            for (LabelValueBean lvb : qo.getOptionContent()) {
                if (lvb.getLabel().startsWith("image/")) {
                    if (imageIdAux == imageIndex) {
                        return lvb.getValue();
                    }
                    imageIdAux++;
                }
            }
        }

        List<ResponseProcessing> responseProcessingInstructions = new ArrayList<ResponseProcessing>();
        if (responseProcessingInstructionIndex != null) {
            responseProcessingInstructions.add(getResponseProcessingInstructions().get(responseProcessingInstructionIndex));
        } else {
            responseProcessingInstructions.addAll(getResponseProcessingInstructions());
        }

        for (ResponseProcessing responseProcessing : responseProcessingInstructions) {
            for (LabelValueBean lvb : responseProcessing.getFeedback()) {
                if (lvb.getLabel().startsWith("image/")) {
                    if (imageIdAux == imageIndex) {
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
            if (responseProcessing.getAction() != null
                    && (responseProcessing.getAction().intValue() == ResponseProcessing.SET || responseProcessing.getAction()
                            .intValue() == ResponseProcessing.ADD)) {
                if (responseProcessing.getResponseValue() != null
                        && maxValue.compareTo(responseProcessing.getResponseValue()) < 0) {
                    maxValue = responseProcessing.getResponseValue();
                }
            }
        }
        return maxValue;
    }

    public Integer getUnansweredResponseProcessingInstructionIndex() {
        for (ResponseProcessing responseProcessing : getResponseProcessingInstructions()) {
            if (responseProcessing.getResponseConditions().isEmpty() && responseProcessing.isUnansweredResponseProcessing()) {
                return getResponseProcessingInstructions().indexOf(responseProcessing);
            }
        }
        return null;
    }

}
