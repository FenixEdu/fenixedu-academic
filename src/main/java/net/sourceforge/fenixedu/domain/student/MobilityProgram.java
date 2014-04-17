package net.sourceforge.fenixedu.domain.student;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import java.util.Locale;

public enum MobilityProgram {

    SOCRATES,

    ERASMUS,

    MINERVA,

    COVENANT_WITH_AZORES,

    COVENANT_WITH_INSTITUTION {

        @Override
        protected String getSpecificDescription(final Locale locale) {
            return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName())
                    + UniversityUnit.getInstitutionsUniversityUnit().getName();
        }

    };

    public String getQualifiedName() {
        Class<?> enumClass = this.getClass();
        if (!enumClass.isEnum() && Enum.class.isAssignableFrom(enumClass)) {
            enumClass = enumClass.getEnclosingClass();
        }

        return enumClass.getSimpleName() + "." + name();
    }

    protected String getSpecificDescription(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getDescription() {
        return getSpecificDescription(I18N.getLocale());
    }

    public String getDescription(final Locale locale) {
        return getSpecificDescription(locale);
    }

}
