/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at Jul 23, 2003, 10:12:40 AM
 * 
 */
public class Theme extends Theme_Base {

    public Theme() {
	super();

	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static List<Theme> getAllThemes() {
	return RootDomainObject.getInstance().getThemes();
    }

}
