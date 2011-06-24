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

    UNKNOWN;

    public String getPresentationName() {
	return BundleUtil.getStringFromResourceBundle("resources/EnumerationResources", name());
    }
}
