/*
 * Created on 25/Jul/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests.utils;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.utilTests.Element;

/**
 * @author Susana Fernandes
 */
public class QuestionElement {

    private String itemId;

    private String title;

    private List<Element> listQuestionPresentation, listQuestion, listOptions, listResponse,
            listFeedback;

    private String responseId = "";

    public QuestionElement() {
        super();
        listQuestionPresentation = new ArrayList<Element>();
        listQuestion = new ArrayList<Element>();
        listOptions = new ArrayList<Element>();
        listResponse = new ArrayList<Element>();
        listFeedback = new ArrayList<Element>();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Element> getListQuestionPresentation() {
        return listQuestionPresentation;
    }

    public void setListQuestionPresentation(List<Element> listQuestionPresentation) {
        this.listQuestionPresentation = listQuestionPresentation;
    }

    public void addListQuestionPresentation(Element element) {
        listQuestionPresentation.add(element);
    }

    public List<Element> getListFeedback() {
        return listFeedback;
    }

    public void setListFeedback(List<Element> listFeedback) {
        this.listFeedback = listFeedback;
    }

    public void addListFeedback(Element element) {
        listFeedback.add(element);
    }

    public List<Element> getListOptions() {
        return listOptions;
    }

    public void setListOptions(List<Element> listOptions) {
        this.listOptions = listOptions;
    }

    public void addListOptions(Element element) {
        this.listOptions.add(element);
    }

    public List<Element> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<Element> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public void addListQuestion(Element element) {
        this.listQuestion.add(element);
    }

    public List<Element> getListResponse() {
        return listResponse;
    }

    public void setListResponse(List<Element> listResponse) {
        this.listResponse = listResponse;
    }

    public void addListResponse(Element element) {
        this.listResponse.add(element);
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

}