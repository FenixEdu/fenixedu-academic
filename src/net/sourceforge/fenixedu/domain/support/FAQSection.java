/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Luis Cruz
 *  
 */
public class FAQSection extends DomainObject implements IFAQSection {

    private Integer keyParentSection = null;

    private IFAQSection parentSection = null;

    private String sectionName = null;

    public FAQSection() {
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

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}