/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.support;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.support.FAQSection;

/**
 * @author Luis Cruz
 * 
 */
public class InfoFAQSection extends InfoObject implements Serializable {

    private InfoFAQSection parentSection = null;

    private String sectionName = null;

    private List subSections = null;

    private List entries = null;

    public InfoFAQSection() {
	super();
    }

    public int hashCode() {
	if (this.getIdInternal() != null) {
	    return this.getIdInternal().intValue();
	}

	return 0;
    }

    public InfoFAQSection getParentSection() {
	return parentSection;
    }

    public void setParentSection(InfoFAQSection parentSection) {
	this.parentSection = parentSection;
    }

    public String getSectionName() {
	return sectionName;
    }

    public void setSectionName(String sectionName) {
	this.sectionName = sectionName;
    }

    public List getSubSections() {
	return subSections;
    }

    public void setSubSections(List subSections) {
	this.subSections = subSections;
    }

    public List getEntries() {
	return entries;
    }

    public void setEntries(List entries) {
	this.entries = entries;
    }

    public void copyFromDomain(FAQSection faqSection) {
	super.copyFromDomain(faqSection);
	if (faqSection != null) {
	    setSectionName(faqSection.getSectionName());
	    FAQSection parentSection = faqSection.getParentSection();
	    if (parentSection != null) {
		setParentSection(new InfoFAQSection());
		getParentSection().setIdInternal(parentSection.getIdInternal());
	    }
	}
    }

    public static InfoFAQSection newInfoFromDomain(FAQSection faqSection) {
	InfoFAQSection infoFAQSection = null;
	if (faqSection != null) {
	    infoFAQSection = new InfoFAQSection();
	    infoFAQSection.copyFromDomain(faqSection);
	}
	return infoFAQSection;
    }

}
