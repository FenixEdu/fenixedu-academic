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
public interface IGlossaryEntry extends IDomainObject {

    public String getDefinition();

    public void setDefinition(String definition);

    public String getTerm();

    public void setTerm(String term);

}