package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import java.util.Locale;

public enum ErasmusApplyForSemesterType implements IPresentableEnum {
    FIRST_SEMESTER, SECOND_SEMESTER;

    protected String localizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(fullQualifiedName());
    }

    protected String qualifiedName() {
        return this.getClass().getSimpleName() + "." + name();
    }

    protected String fullQualifiedName() {
        return this.getClass().getName() + "." + name();
    }

    protected String localizedName() {
        return localizedName(I18N.getLocale());
    }

    @Override
    public String getLocalizedName() {
        return localizedName();
    }

}
