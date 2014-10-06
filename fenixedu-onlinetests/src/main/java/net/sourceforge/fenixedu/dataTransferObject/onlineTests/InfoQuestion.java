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
 * Created on 25/Jul/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 */
public class InfoQuestion extends InfoObject {

    private String xmlFile;

    private String xmlFileName;

    private Boolean visibility;

    private InfoMetadata infoMetadata;

    private List question;

    private List options;

    // This is the list with all of the instructions for the response
    // processing.
    private List<ResponseProcessing> responseProcessingInstructions;

    // This is the list with ONE correct response (to use with fenix correction
    // formulas)
    // private List correctResponse;

    private Double questionValue;

    private Integer optionNumber;

    private QuestionType questionType;

    public InfoQuestion() {
    }

    public InfoMetadata getInfoMetadata() {
        return infoMetadata;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setInfoMetadata(InfoMetadata metadata) {
        infoMetadata = metadata;
    }

    public void setXmlFile(String string) {
        xmlFile = string;
    }

    public void setXmlFileName(String string) {
        xmlFileName = string;
    }

    public List getQuestion() {
        return question;
    }

    public void setQuestion(List list) {
        question = list;
    }

    public List getOptions() {
        return options;
    }

    public void setOptions(List list) {
        options = list;
    }

    public Double getQuestionValue() {
        return questionValue;
    }

    public void setQuestionValue(Double value) {
        questionValue = value;
    }

    /*
     * public List getCorrectResponse() { return correctResponse; }
     * 
     * public void setCorrectResponse(List list) { correctResponse = list; }
     */
    public Integer getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(Integer integer) {
        optionNumber = integer;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean boolean1) {
        visibility = boolean1;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<ResponseProcessing> getResponseProcessingInstructions() {
        return responseProcessingInstructions;
    }

    public void setResponseProcessingInstructions(List responseProcessingInstructions) {
        this.responseProcessingInstructions = responseProcessingInstructions;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoQuestion) {
            InfoQuestion infoQuestion = (InfoQuestion) obj;
            result = getExternalId().equals(infoQuestion.getExternalId());
            result =
                    result
                            || (getXmlFile().equals(infoQuestion.getXmlFile())
                                    && (getXmlFileName().equals(infoQuestion.getXmlFileName()))
                                    && (getInfoMetadata().equals(infoQuestion.getInfoMetadata()))
                                    && (getQuestionValue().equals(infoQuestion.getQuestionValue()))
                                    && (getQuestion().containsAll(infoQuestion.getQuestion()))
                                    && (infoQuestion.getQuestion().containsAll(getQuestion()))
                                    && (getOptions().containsAll(infoQuestion.getOptions()))
                                    && (infoQuestion.getOptions().containsAll(getOptions()))
                                    // && (getCorrectResponse().containsAll(infoQuestion
                                    // .getCorrectResponse()))
                                    // &&
                                    // (infoQuestion.getCorrectResponse().containsAll(
                                    // getCorrectResponse()))

                                    && (getOptionNumber().equals(infoQuestion.getOptionNumber()))
                                    && (getVisibility().equals(infoQuestion.getVisibility())) && (getQuestionType().getType()
                                        .equals(infoQuestion.getQuestionType().getType())));
        }
        return result;
    }

    public void copyFromDomain(Question question) {
        super.copyFromDomain(question);
        if (question != null) {
            setXmlFile(question.getXmlFile());
            setXmlFileName(question.getXmlFileName());
            setVisibility(question.getVisibility());
        }
    }

    public static InfoQuestion newInfoFromDomain(Question question) {
        InfoQuestion infoQuestion = null;
        if (question != null) {
            infoQuestion = new InfoQuestion();
            infoQuestion.copyFromDomain(question);
        }
        return infoQuestion;
    }
}