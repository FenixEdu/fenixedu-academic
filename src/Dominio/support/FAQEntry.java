/*
 * Created on 2004/08/30
 *
 */
package Dominio.support;

import Dominio.DomainObject;

/**
 * @author Luis Cruz
 *  
 */
public class FAQEntry extends DomainObject implements IFAQEntry {

    private Integer keyParentSection = null;

    private IFAQSection parentSection = null;

    private String question = null;

    private String answer = null;

    public FAQEntry() {
        super();
    }

    public IFAQSection getParentSection() {
        return parentSection;
    }

    public void setParentSection(IFAQSection parentSection) {
        this.parentSection = parentSection;
    }

    public Integer getKeyParentSection() {
        return keyParentSection;
    }

    public void setKeyParentSection(Integer keyParentSection) {
        this.keyParentSection = keyParentSection;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}