package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

public enum AdministrativeOfficeType {
    
    DEGREE, MASTER_DEGREE;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AdministrativeOfficeType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AdministrativeOfficeType.class.getName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(getQualifiedName());
    }
    
}
