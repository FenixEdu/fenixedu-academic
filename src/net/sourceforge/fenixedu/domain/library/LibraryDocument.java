package net.sourceforge.fenixedu.domain.library;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class LibraryDocument extends LibraryDocument_Base {

	public LibraryDocument() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}
