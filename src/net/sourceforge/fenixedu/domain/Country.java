/*
 * Country.java
 * 
 * Created on 28 of December 2002, 10:04
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

public class Country extends Country_Base {

	public Country() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
    }

    public Country(final String name, final String nationality, final String code) {
		this();
		setCode(code);
		setNationality(nationality);
		setName(name);
    }

}