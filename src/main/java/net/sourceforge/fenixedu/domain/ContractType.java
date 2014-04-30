package net.sourceforge.fenixedu.domain;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum ContractType {

    EFFECTIVE, ON_TERM, INDEPENDENT_WORKER, INDEPENDENT_WORKER_WITH_EMPLOYEES, INDEPENDENT_WORKER_WITHOUT_EMPLOYEES,
    RECEIPT_CONTRACT, PROFESSIONAL_INTERNSHIP, SCHOLARSHIP, OTHER, NO_ANSWER;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ContractType.class.getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
