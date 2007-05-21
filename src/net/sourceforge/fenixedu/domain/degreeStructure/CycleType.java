package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

public enum CycleType {

    FIRST_CYCLE,

    SECOND_CYCLE,

    THIRD_CYCLE;

    public String getQualifiedName() {
	return this.getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale())
		.getString(name());
    }

}
