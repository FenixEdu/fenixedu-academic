package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;

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
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public FunctionalityParameter(Functionality functionality, String name) {
	this();

	setFunctionality(functionality);
	setName(name);
    }

    public FunctionalityParameter(Functionality functionality, String name, MetaDomainObject type) {
	this(functionality, name);
	setType(type);
    }

    public void delete() {
	removeType();
	removeFunctionality();
	removeRootDomainObject();

	deleteDomainObject();
    }

}
