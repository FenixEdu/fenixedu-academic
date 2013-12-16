/*
 * Created on 2004/08/30
 *
 */
package net.sourceforge.fenixedu.domain.support;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author Luis Cruz
 * 
 */
public class GlossaryEntry extends GlossaryEntry_Base {

    public GlossaryEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTerm() {
        return getTerm() != null;
    }

    @Deprecated
    public boolean hasDefinition() {
        return getDefinition() != null;
    }

}
