package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.permissionManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/permissionManagement", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "showAcademicAdminOfficePermissions", path = "/academicAdminOffice/permissions/showPermissions.jsp"),
	@Forward(name = "editPermissionMembersGroup", path = "/academicAdminOffice/permissions/editPermissionMembersGroup.jsp") })
public class PermissionManagementDA extends FenixDispatchAction {

    public ActionForward showPermissions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Employee employee = AccessControl.getPerson().getEmployee();
	final Campus campus = employee.getCurrentCampus();
	final AdministrativeOffice administrativeOffice = employee.getAdministrativeOffice();

	final List<PermissionViewBean> permissions = new ArrayList<PermissionViewBean>();
	for (final PermissionType type : PermissionType.getSortedPermissionTypes(administrativeOffice)) {
	    permissions.add(PermissionViewBean.create(type, administrativeOffice, campus));
	}

	request.setAttribute("permissions", permissions);
	return mapping.findForward("showAcademicAdminOfficePermissions");
    }

    public ActionForward prepareEditMembers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Person person = AccessControl.getPerson();
	final AdministrativeOfficePermission permission = getOrCreatePermission(request);

	final List<PermissionMemberBean> permissionMembers = new ArrayList<PermissionMemberBean>();
	for (final Employee employee : person.getEmployee().getCurrentWorkingPlace().getAllWorkingEmployees()) {
	    permissionMembers.add(new PermissionMemberBean(permission, employee.getPerson()));
	}

	Collections.sort(permissionMembers);

	request.setAttribute("permission", permission);
	request.setAttribute("permissionMembers", permissionMembers);

	return mapping.findForward("editPermissionMembersGroup");
    }

    public ActionForward editMembers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException {

	final List<Person> persons = new ArrayList<Person>();
	for (final PermissionMemberBean permissionBean : getPermissionMemberListFromViewState()) {
	    if (permissionBean.getSelected()) {
		persons.add(permissionBean.getPerson());
	    }
	}

	getPermission(request).setNewGroup(persons);
	return showPermissions(mapping, actionForm, request, response);
    }

    private AdministrativeOfficePermission getPermission(final HttpServletRequest request) {
	return getDomainObject(request, "permissionId");
    }

    private AdministrativeOfficePermission getOrCreatePermission(HttpServletRequest request) {
	final PermissionType permissionType = PermissionType.valueOf((String) getFromRequest(request, "permissionTypeName"));

	final Employee employee = AccessControl.getPerson().getEmployee();
	final Campus campus = employee.getCurrentCampus();
	final AdministrativeOffice administrativeOffice = employee.getAdministrativeOffice();

	if (!administrativeOffice.hasPermission(permissionType, campus)) {
	    administrativeOffice.createAdministrativeOfficePermission(permissionType, campus);
	}

	return administrativeOffice.getPermission(permissionType, campus);
    }

    @SuppressWarnings("unchecked")
    private List<PermissionMemberBean> getPermissionMemberListFromViewState() {
	return (List<PermissionMemberBean>) getRenderedObject("permissionMembers");
    }

    public static class Member implements java.io.Serializable, Comparable<Member> {

	private static final long serialVersionUID = 1L;

	private DomainReference<Person> person;

	public Member(final Person person) {
	    this.person = new DomainReference<Person>(person);
	}

	public Person getPerson() {
	    return this.person != null ? this.person.getObject() : null;
	}

	public void setPerson(final Person person) {
	    this.person = person != null ? new DomainReference<Person>(person) : null;
	}

	@Override
	public int compareTo(final Member o) {
	    return Person.COMPARATOR_BY_NAME_AND_ID.compare(getPerson(), o.getPerson());
	}
    }

    public static class PermissionViewBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private PermissionType type;
	private List<Member> members;

	private PermissionViewBean(final PermissionType type, final List<Member> members) {
	    setPermissionType(type);
	    setMembers(members);
	}

	private PermissionViewBean(final PermissionType type, final Group permissionMembersGroup) {
	    setPermissionType(type);
	    setMembers(new ArrayList<Member>());

	    for (final Person person : permissionMembersGroup.getElements()) {
		members.add(new Member(person));
	    }
	}

	public PermissionType getPermissionType() {
	    return this.type;
	}

	public void setPermissionType(PermissionType type) {
	    this.type = type;
	}

	public List<Member> getMembers() {
	    return this.members;
	}

	public void setMembers(final List<Member> members) {
	    this.members = members;
	    Collections.sort(this.members);
	}

	static private PermissionViewBean create(final PermissionType type, final AdministrativeOffice office, final Campus campus) {
	    final AdministrativeOfficePermission permission = office.getPermission(type, campus);
	    if (permission != null) {
		return new PermissionViewBean(type, permission.getPermissionMembersGroup());
	    } else {
		return new PermissionViewBean(type, new ArrayList<Member>());
	    }
	}
    }

    public static class PermissionMemberBean implements java.io.Serializable, Comparable<PermissionMemberBean> {
	private static final long serialVersionUID = 1L;

	private Boolean selected;
	private DomainReference<Person> person;

	public PermissionMemberBean(final AdministrativeOfficePermission permission, final Person person) {
	    setSelected(permission.isMember(person));
	    setPerson(person);
	}

	public Boolean getSelected() {
	    return selected;
	}

	public void setSelected(Boolean selected) {
	    this.selected = selected;
	}

	public Person getPerson() {
	    return this.person != null ? this.person.getObject() : null;
	}

	public void setPerson(Person person) {
	    this.person = person != null ? new DomainReference<Person>(person) : null;
	}

	@Override
	public int compareTo(final PermissionMemberBean o) {
	    return Person.COMPARATOR_BY_NAME_AND_ID.compare(getPerson(), o.getPerson());
	}
    }
}
