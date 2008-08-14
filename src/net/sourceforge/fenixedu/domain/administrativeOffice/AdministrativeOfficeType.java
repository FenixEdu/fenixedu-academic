package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

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
	return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName());
    }

}
