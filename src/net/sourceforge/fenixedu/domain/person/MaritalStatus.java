/*
 * Created on Apr 15, 2005
 */
package net.sourceforge.fenixedu.domain.person;

import net.sourceforge.fenixedu.util.BundleUtil;

public enum MaritalStatus {

    SINGLE,

    MARRIED,

    DIVORCED,

    WIDOWER,

    SEPARATED,

    CIVIL_UNION,

    //TODO: RAIDES Provider and beans exclude this value.
    //This enum should be refactored to contain an "isActive"  
    UNKNOWN;

    public String getPresentationName() {
	return BundleUtil.getStringFromResourceBundle("resources/EnumerationResources", name());
    }
}
