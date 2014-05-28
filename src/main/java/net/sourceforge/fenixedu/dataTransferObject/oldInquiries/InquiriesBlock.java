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
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesBlock implements Serializable {

    private QuestionHeader header;

    private Boolean required;

    private List<InquiriesQuestion> questions = new ArrayList<InquiriesQuestion>();

    private Map<String, InquiriesQuestion> questionsMap = new HashMap<String, InquiriesQuestion>();

    public InquiriesBlock(Boolean required) {
        this.required = required;
    }

    public InquiriesBlock(String title, Boolean required) {
        this.header = new QuestionHeader(title);
        this.required = required;
    }

    public InquiriesBlock(String title, Boolean required, String... scaleHeaders) {
        this.header = new QuestionHeader(title, scaleHeaders);
        this.required = required;
    }

    public void addQuestion(InquiriesQuestion question) {
        this.questionsMap.put(question.getLabel(), question);
        this.questions.add(question);
        if (question.getRequired() == null) {
            question.setRequired(getRequired());
        }
    }

    public InquiriesQuestion getQuestion(String label) {
        return questionsMap.get(label);
    }

    public Collection<InquiriesQuestion> getQuestions() {
        return questions;
    }

    public boolean hasHeader() {
        return this.header != null;
    }

    public QuestionHeader getHeader() {
        return header;
    }

    public Boolean getRequired() {
        return required;
    }

    public boolean validate() {
        for (InquiriesQuestion question : getQuestions()) {
            if (question.getRequired() && question.isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
