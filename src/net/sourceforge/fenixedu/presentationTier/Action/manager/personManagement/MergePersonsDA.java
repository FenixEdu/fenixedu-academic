package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteObjectByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.TransferDomainObjectProperty;
import net.sourceforge.fenixedu.dataTransferObject.MergeSlotDTO;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import dml.DomainClass;
import dml.Role;
import dml.Slot;

@Mapping(path = "/mergePersons", module = "manager")
@Forwards( {
	@Forward(name = "chooseObjectsToMerge", path = "/manager/personManagement/merge/chooseObjectsToMerge.jsp"),
	@Forward(name = "displayObjects", path = "/manager/personManagement/merge/displayObjects.jsp"),
	@Forward(name = "only-one-person-with-student-object", path = "/manager/personManagement/merge/onePersonWithStudent.jsp"),
	@Forward(name = "remove-student", path = "/manager/personManagement/merge/removeStudent.jsp"),
	@Forward(name = "remove-person", path = "/manager/personManagement/merge/removePerson.jsp"),
	@Forward(name = "person-removed", path = "/manager/personManagement/merge/personRemoved.jsp"),
	@Forward(name = "transfer-registrations", path = "/manager/personManagement/merge/transferRegistrations.jsp"),
	@Forward(name = "transfer-events-and-accounts", path = "/manager/personManagement/merge/transferEventsAndAccounts.jsp") })
public class MergePersonsDA extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("mergePersonsBean", new MergePersonsBean(Person.class.getName()));
	return mapping.findForward("chooseObjectsToMerge");
    }

    public ActionForward chooseObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IllegalAccessException,
	    NoSuchMethodException, ClassNotFoundException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	if (Student.class.getName().equals(mergePersonsBean.getCurrentClass())) {
	    return chooseObjects(mapping, form, request, response, domainObject1.getStudent().getExternalId(), domainObject2
		    .getStudent().getExternalId(), Student.class.getName());
	}

	return chooseObjects(mapping, form, request, response, domainObject1.getExternalId(), domainObject2.getExternalId(),
		Person.class.getName());
    }

    public ActionForward chooseObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, String leftOid, String rightOid, String currentClass) throws FenixFilterException,
	    FenixServiceException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {

	DomainObject domainObject1 = RootDomainObject.fromExternalId(leftOid);
	DomainObject domainObject2 = RootDomainObject.fromExternalId(rightOid);

	if (domainObject1 == null || domainObject2 == null) {
	    request.setAttribute("mergePersonsBean", new MergePersonsBean());
	    return mapping.findForward("chooseObjectsToMerge");
	}

	DomainClass domainClass = FenixFramework.getDomainModel().findClass(currentClass);

	List<MergeSlotDTO> results = new ArrayList<MergeSlotDTO>();
	DomainClass domainClassOrSuperClass = domainClass;
	while (domainClassOrSuperClass.getSuperclass() != null) {

	    for (Iterator<Slot> iter = domainClassOrSuperClass.getSlots(); iter.hasNext();) {
		Slot slot = iter.next();

		String slotName = slot.getName();

		Object objProperty1 = null;
		Object objProperty2 = null;
		try {
		    objProperty1 = PropertyUtils.getSimpleProperty(domainObject1, slotName);
		    objProperty2 = PropertyUtils.getSimpleProperty(domainObject2, slotName);
		} catch (InvocationTargetException e) {
		    continue;
		}

		String property1 = (objProperty1 == null) ? "" : objProperty1.toString();
		String property2 = (objProperty2 == null) ? "" : objProperty2.toString();

		if (property1.equals(property2)) {
		    continue;
		}

		results.add(new MergeSlotDTO(slotName, "Simple Primitive", property1, property2));
	    }
	    domainClassOrSuperClass = (DomainClass) domainClassOrSuperClass.getSuperclass();
	}

	domainClassOrSuperClass = domainClass;
	while (domainClassOrSuperClass.getSuperclass() != null) {
	    for (Iterator<Role> iter = domainClassOrSuperClass.getRoleSlots(); iter.hasNext();) {
		Role roleSlot = iter.next();

		String slotName = roleSlot.getName();
		MergeSlotDTO mergeSlot = new MergeSlotDTO(slotName);

		Object property1 = null;
		Object property2 = null;
		try {
		    property1 = PropertyUtils.getSimpleProperty(domainObject1, slotName);
		    property2 = PropertyUtils.getSimpleProperty(domainObject2, slotName);
		} catch (InvocationTargetException e) {
		    continue;
		}

		if (roleSlot.getMultiplicityUpper() == 1) {

		    if ((property1 == null && property2 == null)
			    || (property1 != null && property2 != null && property1.equals(property2))) {
			continue;
		    }

		    mergeSlot.setType("Reference");

		    fillDtoWithSimpleProperty(roleSlot, mergeSlot, property1, MergeSlotDTO.VALUE1);
		    fillDtoWithSimpleProperty(roleSlot, mergeSlot, property2, MergeSlotDTO.VALUE2);

		} else {

		    if (((Collection) property1).size() == 0 && ((Collection) property2).size() == 0) {
			continue;
		    }

		    // collection
		    mergeSlot.setType("Collection");

		    fillDtoWithCollectionProperty(mergeSlot, property1, MergeSlotDTO.VALUE1, domainObject1.getOID(), Person.class
			    .getName());
		    fillDtoWithCollectionProperty(mergeSlot, property2, MergeSlotDTO.VALUE2, domainObject2.getOID(), Person.class
			    .getName());
		}

		results.add(mergeSlot);
	    }
	    domainClassOrSuperClass = (DomainClass) domainClassOrSuperClass.getSuperclass();
	}

	request.setAttribute("slots", results);
	request.setAttribute("classToMerge", currentClass);

	return mapping.findForward("displayObjects");
    }

    private MergePersonsBean getMergePersonsBean(HttpServletRequest request) {
	MergePersonsBean bean = (MergePersonsBean) this.getObjectFromViewState("mergePersonsBean");

	if (bean == null) {
	    bean = new MergePersonsBean();
	    bean.setLeftOid(request.getParameter("object1IdInternal"));
	    bean.setRightOid(request.getParameter("object2IdInternal"));
	    bean.setCurrentClass(request.getParameter("classToMerge"));
	}

	return bean;
    }

    private void fillDtoWithCollectionProperty(MergeSlotDTO mergeSlot, Object collection, String order, long oid,
	    String domainClass) {
	if (collection != null) {
	    mergeSlot.setValueProperty(order, "Size: " + ((Collection) collection).size());
	    mergeSlot.setValueLinkProperty(order, "/domainbrowser/listRole?OID=" + oid + "&role=" + mergeSlot.getName());

	} else {
	    mergeSlot.setValueProperty(order, "null collection");
	}
    }

    private void fillDtoWithSimpleProperty(Role roleSlot, MergeSlotDTO mergeSlot, Object reference, String order) {
	if (reference != null) {
	    if (roleSlot.getType() instanceof DomainClass) {
		final long idInternal = ((DomainObject) reference).getOID();
		mergeSlot.setValueProperty(order, "OID: " + idInternal);
		mergeSlot.setValueLinkProperty(order, "/domainbrowser/showObj?OID=" + idInternal);
	    } else {
		// enum ??
		mergeSlot.setValueProperty(order, reference.toString());
	    }
	} else {
	    mergeSlot.setValueProperty(order, "null");
	}
    }

    public ActionForward mergeProperty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	if (Student.class.getName().equals(mergePersonsBean.getCurrentClass())) {
	    return mergeProperty(mapping, form, request, response, domainObject1.getStudent().getExternalId(), domainObject2
		    .getStudent().getExternalId(), Student.class.getName());
	}

	return mergeProperty(mapping, form, request, response, domainObject1.getExternalId(), domainObject2.getExternalId(),
		Person.class.getName());
    }

    public ActionForward mergeProperty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, String leftOid, String rightOid, String currentClass) throws FenixFilterException,
	    FenixServiceException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
	    ClassNotFoundException {

	IUserView userView = UserView.getUser();

	DomainObject domainObject1 = RootDomainObject.fromExternalId(leftOid);
	DomainObject domainObject2 = RootDomainObject.fromExternalId(rightOid);

	Integer sourceOrder = Integer.valueOf(request.getParameter("source"));
	String slotName = request.getParameter("slotName");

	TransferDomainObjectProperty.run((sourceOrder == 1) ? domainObject1 : domainObject2, (sourceOrder == 1) ? domainObject2
		: domainObject1, slotName);

	return chooseObjects(mapping, form, request, response);

    }

    public ActionForward mergeStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IllegalAccessException,
	    NoSuchMethodException, ClassNotFoundException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	if (domainObject1.getStudent() == null || domainObject2.getStudent() == null) {
	    return mapping.findForward("only-one-person-with-student-object");
	}

	mergePersonsBean.setCurrentClass(Student.class.getName());

	return chooseObjects(mapping, form, request, response);
    }

    public ActionForward prepareTransferRegistrations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IllegalAccessException,
	    NoSuchMethodException, ClassNotFoundException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	chooseObjects(mapping, form, request, response);

	return mapping.findForward("transfer-registrations");
    }

    public ActionForward prepareDeleteStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	request.setAttribute("studentToDelete", domainObject2.getStudent());

	return mapping.findForward("remove-student");
    }

    public ActionForward deleteStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	Student studentToRemove = domainObject2.getStudent();

	if (studentToRemove != null) {
	    DeleteObjectByOID.run(Student.class, studentToRemove.getIdInternal());
	    request.setAttribute("studentRemoved", true);
	}

	return prepareDeletePerson(mapping, form, request, response);
    }

    public ActionForward prepareDeletePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);

	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	request.setAttribute("person", domainObject2);

	return mapping.findForward("remove-person");
    }

    public ActionForward deletePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	DeleteObjectByOID.run(Person.class, domainObject2.getIdInternal());
	request.setAttribute("studentRemoved", true);

	return mapping.findForward("person-removed");
    }

    public ActionForward transferRegistrations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException, IllegalAccessException,
	    NoSuchMethodException, ClassNotFoundException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	Student studentDestiny = domainObject1.getStudent();
	Student studentToRemove = domainObject2.getStudent();

	studentDestiny.acceptRegistrationsFromOtherStudent(studentToRemove.getRegistrations());

	return prepareTransferRegistrations(mapping, form, request, response);

    }

    public ActionForward prepareTransferEventsAndAccounts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IllegalAccessException,
	    NoSuchMethodException, ClassNotFoundException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person domainObject1 = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person domainObject2 = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	chooseObjects(mapping, form, request, response);

	return mapping.findForward("transfer-events-and-accounts");
    }

    public ActionForward transferEventsAndAccounts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IllegalAccessException,
	    NoSuchMethodException, ClassNotFoundException {
	MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
	request.setAttribute("mergePersonsBean", mergePersonsBean);
	Person destinyPerson = RootDomainObject.fromExternalId(mergePersonsBean.getLeftOid());
	Person sourcePerson = RootDomainObject.fromExternalId(mergePersonsBean.getRightOid());

	destinyPerson.transferEventsAndAccounts(sourcePerson);

	return prepareTransferEventsAndAccounts(mapping, form, request, response);
    }

    public static class MergePersonsBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String leftOid;
	private String rightOid;
	private String currentClass;

	public MergePersonsBean() {

	}

	public MergePersonsBean(String currentClass) {
	    setCurrentClass(currentClass);
	}

	public String getLeftOid() {
	    return leftOid;
	}

	public void setLeftOid(String leftOid) {
	    this.leftOid = leftOid;
	}

	public String getRightOid() {
	    return rightOid;
	}

	public void setRightOid(String rightOid) {
	    this.rightOid = rightOid;
	}

	public String getCurrentClass() {
	    return currentClass;
	}

	public void setCurrentClass(String value) {
	    this.currentClass = value;
	}
    }

}
