/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Luis Cruz
 *  
 */
public interface IFAQSection extends IDomainObject {
    public IFAQSection getParentSection();

    public void setParentSection(IFAQSection parentSection);

    public String getSectionName();

    public void setSectionName(String sectionName);
}