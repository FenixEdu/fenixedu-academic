/*
 * Created on Apr 15, 2005
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum MaritalStatus implements IPresentableEnum {

	SINGLE,

	MARRIED,

	DIVORCED,

	WIDOWER,

	SEPARATED,

	CIVIL_UNION,

	// TODO: RAIDES Provider and beans exclude this value.
	// This enum should be refactored to contain an "isActive"
	UNKNOWN;

	public String getPresentationName() {
		return BundleUtil.getStringFromResourceBundle("resources/EnumerationResources", name());
	}

	@Override
	public String getLocalizedName() {
		final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
		return bundle.getString(this.getClass().getName() + "." + name());
	}
}
