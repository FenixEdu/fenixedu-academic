package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ScientificArea extends ScientificArea_Base {

	public ScientificArea() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public ScientificArea(final String name) {
		this();
		super.setName(name);
	}
}
