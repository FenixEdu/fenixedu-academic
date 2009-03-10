package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.permissionManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
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

    public ActionForward prepareEditMembers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Person user = AccessControl.getPerson();

	AdministrativeOfficePermission permission = getSelectedPermission(request);
	List<PermissionMemberBean> permissionMembers = new ArrayList<PermissionMemberBean>();

	if (user.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
	    for (Employee employee : user.getEmployeeAdministrativeOffice().getUnit().getAllWorkingEmployees()) {
		permissionMembers.add(new PermissionMemberBean(isPersonMemberPermissionGroup(permission, employee.getPerson()),
			employee.getPerson()));
	    }
	}

	Collections.sort(permissionMembers, new BeanComparator("person.name"));
	
	Campus workingCampus = AccessControl.getPerson().getEmployee().getCurrentWorkingPlace().getCampus();

	request.setAttribute("permissionMembers", permissionMembers);
	request.setAttribute("permission", permission);
	request.setAttribute("workingCampus", workingCampus);
	
	return mapping.findForward("editPermissionMembersGroup");
    }

    public ActionForward editMembers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException {

	List<Person> persons = new ArrayList<Person>();
	for (PermissionMemberBean permissionBean : getPermissionMemberListFromViewState()) {
	    if (permissionBean.getSelected()) {
		persons.add(permissionBean.getPerson());
	    }
	}

	getSelectedPermissionById(request).setNewGroup(new FixedSetGroup(persons));
	return showPermissions(mapping, actionForm, request, response);
    }

    public ActionForward showPermissions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Campus workingCampus = AccessControl.getPerson().getEmployee().getCurrentWorkingPlace().getCampus();
	List<PermissionType> permissionTypes = PermissionType.getAdministrativeOfficePermissionTypes();

	List<PermissionViewBean> permissions = new ArrayList<PermissionViewBean>();
	for (PermissionType type : permissionTypes) {
	    permissions.add(createPermissionViewBean(type, workingCampus));
	}

	request.setAttribute("permissions", permissions);
	request.setAttribute("workingCampus", workingCampus);
	
	return mapping.findForward("showAcademicAdminOfficePermissions");
    }

    private PermissionViewBean createPermissionViewBean(PermissionType type, Campus workingCampus) {
	AdministrativeOfficePermission permission = workingCampus.getAdministrativeOfficePermissionByType(type);

	if (permission == null) {
	    return new PermissionViewBean(type, new ArrayList<Member>());
	}

	return new PermissionViewBean(type, permission.getPermissionMembersGroup());
    }

    private Boolean isPersonMemberPermissionGroup(AdministrativeOfficePermission permission, Person person) {
	return permission.getPermissionMembersGroup().isMember(person);
    }
    
    private AdministrativeOfficePermission getSelectedPermissionById(HttpServletRequest request) {
	return (AdministrativeOfficePermission) RootDomainObject.readDomainObjectByOID(AdministrativeOfficePermission.class,
		getIntegerFromRequest(request, "permissionId"));
    }
    
    private AdministrativeOfficePermission getSelectedPermission(HttpServletRequest request) {
	String permissionTypeName = (String) getFromRequest(request, "permissionTypeName");
	PermissionType permissionType = PermissionType.valueOf(permissionTypeName);
	Campus workingCampus = AccessControl.getPerson().getEmployee().getCurrentWorkingPlace().getCampus();
	AdministrativeOfficePermission permission = workingCampus.getAdministrativeOfficePermissionByType(permissionType);
	
	if(permission == null) {
	    workingCampus.createAdministrativeOfficePermission(permissionType);
	}
	
	return workingCampus.getAdministrativeOfficePermissionByType(permissionType);
    }

    @SuppressWarnings("unchecked")
    private List<PermissionMemberBean> getPermissionMemberListFromViewState() {
	return (List<PermissionMemberBean>) getRenderedObject("permissionMembers");
    }

    public static class Member implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DomainReference<Person> person;

	public Member() {

	}

	public Member(final Person person) {
	    this.person = new DomainReference<Person>(person);
	}

	public Person getPerson() {
	    return this.person != null ? this.person.getObject() : null;
	}

	public void setPerson(final Person person) {
	    this.person = person != null ? new DomainReference<Person>(person) : null;
	}
    }

    public static class PermissionViewBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PermissionType type;
	private List<Member> members;

	public PermissionViewBean() {

	}

	public PermissionViewBean(final PermissionType type, final List<Member> members) {
	    setPermissionType(type);
	    setMembers(members);
	}

	public PermissionViewBean(final PermissionType type, final Group permissionMembersGroup) {
	    this.type = type;
	    List<Member> members = new ArrayList<Member>();
	    setMembers(members);
	    
	    for (Person person : permissionMembersGroup.getElements()) {
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
	    Collections.sort(this.members, new BeanComparator("person.name"));
	}
    }

    public static class PermissionMemberBean implements java.io.Serializable {
	/**
	 * c
	 */
	private static final long serialVersionUID = 1L;

	private Boolean selected;
	private DomainReference<Person> person;

	public PermissionMemberBean() {
	}

	public PermissionMemberBean(final Boolean selected, final Person person) {
	    setSelected(selected);
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
    }
}
