package net.sourceforge.fenixedu.domain.inquiries;

public enum ResultPersonCategory {

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

}
