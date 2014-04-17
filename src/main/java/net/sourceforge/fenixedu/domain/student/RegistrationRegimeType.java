package net.sourceforge.fenixedu.domain.student;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import java.util.Locale;

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
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName());
    }
}
