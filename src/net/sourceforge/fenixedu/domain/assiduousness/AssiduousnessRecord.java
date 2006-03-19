package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class AssiduousnessRecord extends AssiduousnessRecord_Base {

	public AssiduousnessRecord() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
