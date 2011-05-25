package net.sourceforge.fenixedu.domain.inquiries;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum ResultPersonCategory implements IPresentableEnum {

    DELEGATE(1), TEACHER(2), REGENT(3), DEGREE_COORDINATOR(4), DEPARTMENT_PRESIDENT(5);

    private int permissionOrder;

    private ResultPersonCategory(int permissionOrder) {
	setPermissionOrder(permissionOrder);
    }

    public void setPermissionOrder(int permissionOrder) {
	this.permissionOrder = permissionOrder;
    }

    public int getPermissionOrder() {
	return permissionOrder;
    }

    @Override
    public String getLocalizedName() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return bundle.getString(this.getClass().getName() + "." + name());
    }

}
