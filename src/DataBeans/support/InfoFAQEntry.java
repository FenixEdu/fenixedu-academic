/*
 * Created on 2004/08/30
 *
 */
package DataBeans.support;

import java.io.Serializable;

import DataBeans.InfoObject;
import Dominio.support.FAQEntry;
import Dominio.support.FAQSection;
import Dominio.support.IFAQEntry;
import Dominio.support.IFAQSection;

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

    public void copyFromDomain(IFAQEntry faqEntry) {
        super.copyFromDomain(faqEntry);
        if (faqEntry != null) {
            setQuestion(faqEntry.getQuestion());
            setAnswer(faqEntry.getAnswer());
            IFAQSection parentSection = faqEntry.getParentSection();
            if (parentSection != null) {
                setParentSection(new InfoFAQSection());
                getParentSection().setIdInternal(parentSection.getIdInternal());
            }
        }
    }

    public static InfoFAQEntry newInfoFromDomain(IFAQEntry faqEntry) {
        InfoFAQEntry infoFAQEntry = null;
        if (faqEntry != null) {
            infoFAQEntry = new InfoFAQEntry();
            infoFAQEntry.copyFromDomain(faqEntry);
        }
        return infoFAQEntry;
    }

    public void copyToDomain(InfoFAQEntry infoFAQEntry, IFAQEntry faqEntry) {
        if (infoFAQEntry != null && infoFAQEntry != null) {
            super.copyToDomain(infoFAQEntry, faqEntry);
            faqEntry.setQuestion(infoFAQEntry.getQuestion());
            faqEntry.setAnswer(infoFAQEntry.getAnswer());
            InfoFAQSection infoParentFAQSection = infoFAQEntry.getParentSection();
            if (infoParentFAQSection != null) {
                faqEntry.setParentSection(new FAQSection());
                faqEntry.getParentSection().setIdInternal(infoParentFAQSection.getIdInternal());
            }
        }
    }

    public static IFAQEntry newDomainFromInfo(InfoFAQEntry infoFAQEntry) {
        IFAQEntry faqEntry = null;
        if (infoFAQEntry != null) {
            faqEntry = new FAQEntry();
            infoFAQEntry.copyToDomain(infoFAQEntry, faqEntry);
        }
        return faqEntry;
    }
}