package net.sourceforge.fenixedu.domain.student;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum RegistrationRegimeType implements IPresentableEnum {

	FULL_TIME,

	PARTIAL_TIME;

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return this.getClass().getSimpleName() + "." + name();
	}

	public String getFullyQualifiedName() {
		return getClass().getName() + "." + name();
	}

	final static public RegistrationRegimeType defaultType() {
		return FULL_TIME;
	}

	@Override
	public String getLocalizedName() {
		return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName());
	}
}
