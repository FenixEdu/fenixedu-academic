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
public class GlossaryEntry extends DomainObject implements IGlossaryEntry {

    private String term = null;

    private String definition = null;

    public GlossaryEntry() {
        super();
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}