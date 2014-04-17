/*
 * StudentCurricularState.java Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.studentCurricularPlan;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

/**
 * @author Nuno Nunes & Joana Mota
 */
@Deprecated
public enum StudentCurricularPlanState {

    ACTIVE,

    CONCLUDED,

    INCOMPLETE,

    SCHOOLPARTCONCLUDED,

    INACTIVE,

    PAST;

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getName());
    }
}