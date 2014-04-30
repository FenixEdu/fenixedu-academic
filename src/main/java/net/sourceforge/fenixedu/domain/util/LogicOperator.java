/*
 * Created on Feb 2, 2006
 */
package net.sourceforge.fenixedu.domain.util;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import java.util.Locale;

public enum LogicOperator {

    AND,

    OR,

    NOT;

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(name());
    }

    public boolean isAND() {
        return this.equals(LogicOperator.AND);
    }

    public boolean isOR() {
        return this.equals(LogicOperator.OR);
    }

    public boolean doLogic(final boolean first, final boolean other) {
        switch (this) {
        case AND:
            return first && other;
        case OR:
            return first || other;
        default:
            throw new DomainException("error.LogicOperator.invalid.operator");
        }
    }
}