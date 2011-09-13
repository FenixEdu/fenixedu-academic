package net.sourceforge.fenixedu.presentationTier.TagLib.phd;

import javax.servlet.jsp.JspTagException;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.struts.taglib.logic.ConditionalTagBase;

public class PermissionFor extends ConditionalTagBase {

    private static final long serialVersionUID = 1L;

    private PermissionType permission;

    public PermissionType getPermission() {
	return permission;
    }

    public void setPermission(PermissionType permission) {
	this.permission = permission;
    }

    @Override
    protected boolean condition() throws JspTagException {
	IUserView userView = AccessControl.getUserView();
	Person person = userView.getPerson();

	AdministrativeOffice employeeAdministrativeOffice = person.getEmployeeAdministrativeOffice();
	Campus employeeCampus = person.getEmployeeCampus();

	AdministrativeOfficePermission permission = employeeAdministrativeOffice.getPermission(getPermission(), employeeCampus);
	return permission.isMember(person);
    }
}
