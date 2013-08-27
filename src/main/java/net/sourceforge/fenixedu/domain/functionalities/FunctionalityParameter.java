package net.sourceforge.fenixedu.domain.functionalities;

import pt.ist.bennu.core.domain.Bennu;

/**
 * The <code>FunctionalityParameter</code> reifies a parameters that is required
 * by a functionality. A parameter is identified by it's name, that is, there
 * are no two parameters, for the same functionality, with the same name.
 * 
 * @author cfgi
 */
public class FunctionalityParameter extends FunctionalityParameter_Base {

    protected FunctionalityParameter() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public FunctionalityParameter(Functionality functionality, String name) {
        this();

        setFunctionality(functionality);
        setName(name);
    }

    public void delete() {
        setFunctionality(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFunctionality() {
        return getFunctionality() != null;
    }

}
