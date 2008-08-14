/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.support;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQSection;

/**
 * @author Luis Cruz
 * 
 */
public class InfoFAQEntry extends InfoObject implements Serializable {

    private InfoFAQSection parentSection = null;

    private String question = null;

    private String answer = null;

    public InfoFAQEntry() {
	super();
    }

    public String getAnswer() {
	return answer;
    }

    public void setAnswer(String answer) {
	this.answer = answer;
    }

    public InfoFAQSection getParentSection() {
	return parentSection;
    }

    public void setParentSection(InfoFAQSection parentSection) {
	this.parentSection = parentSection;
    }

    public String getQuestion() {
	return question;
    }

    public void setQuestion(String question) {
	this.question = question;
    }

    public void copyFromDomain(FAQEntry faqEntry) {
	super.copyFromDomain(faqEntry);
	if (faqEntry != null) {
	    setQuestion(faqEntry.getQuestion());
	    setAnswer(faqEntry.getAnswer());
	    FAQSection parentSection = faqEntry.getParentSection();
	    if (parentSection != null) {
		setParentSection(new InfoFAQSection());
		getParentSection().setIdInternal(parentSection.getIdInternal());
	    }
	}
    }

    public static InfoFAQEntry newInfoFromDomain(FAQEntry faqEntry) {
	InfoFAQEntry infoFAQEntry = null;
	if (faqEntry != null) {
	    infoFAQEntry = new InfoFAQEntry();
	    infoFAQEntry.copyFromDomain(faqEntry);
	}
	return infoFAQEntry;
    }

}
