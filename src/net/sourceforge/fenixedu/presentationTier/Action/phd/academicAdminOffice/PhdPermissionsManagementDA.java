package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.permissions.PhdPermission;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdPermissionsManagement", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "showPermissions", path = "/phd/academicAdminOffice/permissions/showPermissions.jsp"),

@Forward(name = "editElements", path = "/phd/academicAdminOffice/permissions/editElements.jsp")

})
public class PhdPermissionsManagementDA extends FenixDispatchAction {

    public ActionForward showPermissions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final AdministrativeOffice office = AccessControl.getPerson().getEmployeeAdministrativeOffice();

	if (office.hasPhdProcessesManager()) {
	    request.setAttribute("permissions", office.getPhdProcessesManager().getPermissionsSortedByType());
	}

	return mapping.findForward("showPermissions");
    }

    public ActionForward prepareEditMembers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdPermission permission = getPermission(request);

	if (permission != null) {
	    request.setAttribute("permission", permission);
	    request.setAttribute("elements", getElements(permission));
	}

	return mapping.findForward("editElements");
    }

    public ActionForward saveMembers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    
	    if (AccessControl.getPerson().getEmployee().isUnitCoordinator()) {
		getPermission(request).saveMembers(getElementsToSave());
	    }

	} catch (final DomainException e) {
	    addActionMessage("error", request, e.getMessage(), e.getArgs());
	    return prepareEditMembers(mapping, actionForm, request, response);
	}

	return showPermissions(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unchecked")
    private Collection<Person> getElementsToSave() {
	final Collection<Person> result = new HashSet<Person>();
	for (final PhdElementBean element : (Collection<PhdElementBean>) getRenderedObject("elements")) {
	    if (element.isSelected()) {
		result.add(element.getPerson());
	    }
	}
	return result;
    }

    private PhdPermission getPermission(HttpServletRequest request) {
	return getDomainObject(request, "permissionOID");
    }

    private Collection<PhdElementBean> getElements(PhdPermission permission) {
	final Collection<PhdElementBean> result = new TreeSet<PhdElementBean>();
	for (final Employee employee : getAllCurrentActiveWorkingEmployees()) {
	    result.add(new PhdElementBean(permission, employee.getPerson()));
	}
	return result;
    }

    private List<Employee> getAllCurrentActiveWorkingEmployees() {
	return AccessControl.getPerson().getEmployee().getCurrentWorkingPlace().getAllCurrentActiveWorkingEmployees();
    }

    static public class PhdElementBean implements Serializable, Comparable<PhdElementBean> {

	static private final long serialVersionUID = 1L;

	private boolean selected;
	private Person person;

	public PhdElementBean(final PhdPermission permission, final Person person) {
	    setPerson(person);
	    setSelected(permission.isMember(person));
	}

	public boolean isSelected() {
	    return selected;
	}

	public void setSelected(boolean selected) {
	    this.selected = selected;
	}

	public Person getPerson() {
	    return person;
	}

	public void setPerson(Person person) {
	    this.person = person;
	}

	@Override
	public int compareTo(PhdElementBean other) {
	    return Person.COMPARATOR_BY_NAME_AND_ID.compare(getPerson(), other.getPerson());
	}

    }
}
