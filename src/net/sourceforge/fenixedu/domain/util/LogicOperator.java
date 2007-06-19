/*
 * Created on Feb 2, 2006
 */
package net.sourceforge.fenixedu.domain.util;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

public enum LogicOperator {
    
    AND,
    
    OR,
    
    NOT;
    
    public String getName() {
        return name();
    }

    public String getLocalizedName() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(name());
    }

    public boolean isAND() {
	return this.equals(LogicOperator.AND);
    }
    
    public boolean isOR() {
	return this.equals(LogicOperator.OR);
    }
}