/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.domain.support;


/**
 * @author Luis Cruz
 *  
 */
public class FAQSection extends FAQSection_Base {

    private IFAQSection parentSection = null;

    public IFAQSection getParentSection() {
        return parentSection;
    }

    public void setParentSection(IFAQSection parentSection) {
        this.parentSection = parentSection;
    }

}