/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.domainObjectsManagement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.MergeSlotDTO;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import dml.DomainClass;
import dml.Role;
import dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class MergeObjectsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("domainClasses", getClasses());
	return mapping.findForward("chooseObjects");
    }

    public ActionForward chooseObjects(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException, IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException, ClassNotFoundException {

	DynaActionForm actionForm = (DynaActionForm) form;
	String classToMerge = (String) actionForm.get("classToMerge");
	Integer object1IdInternal = (Integer) actionForm.get("object1IdInternal");
	Integer object2IdInternal = (Integer) actionForm.get("object2IdInternal");

	if (classToMerge.length() == 0) {
	    classToMerge = (String) request.getParameter("classToMerge");
	    object1IdInternal = Integer.valueOf(request.getParameter("object1IdInternal"));
	    object2IdInternal = Integer.valueOf(request.getParameter("object2IdInternal"));
	}

	DomainObject domainObject1 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge),
		object1IdInternal);
	DomainObject domainObject2 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge),
		object2IdInternal);

	if (domainObject1 == null || domainObject2 == null) {
	    request.setAttribute("domainClasses", getClasses());
	    return mapping.findForward("chooseObjects");
	}

	DomainClass domainClass = MetadataManager.getDomainModel().findClass(classToMerge);

	List<MergeSlotDTO> results = new ArrayList<MergeSlotDTO>();
	DomainClass domainClassOrSuperClass = domainClass;
	while (domainClassOrSuperClass.getSuperclass() != null) {

	    for (Iterator<Slot> iter = domainClassOrSuperClass.getSlots(); iter.hasNext();) {
		Slot slot = iter.next();

		String slotName = slot.getName();

		if (slotName.startsWith("key")) {
		    continue;
		}

		Object objProperty1 = PropertyUtils.getSimpleProperty(domainObject1, slotName);
		Object objProperty2 = PropertyUtils.getSimpleProperty(domainObject2, slotName);

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

		Object property1 = PropertyUtils.getSimpleProperty(domainObject1, slotName);
		Object property2 = PropertyUtils.getSimpleProperty(domainObject2, slotName);

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

		    fillDtoWithCollectionProperty(mergeSlot, property1, MergeSlotDTO.VALUE1,
			    domainObject1.getIdInternal(), classToMerge);
		    fillDtoWithCollectionProperty(mergeSlot, property2, MergeSlotDTO.VALUE2,
			    domainObject2.getIdInternal(), classToMerge);
		}

		results.add(mergeSlot);
	    }
	    domainClassOrSuperClass = (DomainClass) domainClassOrSuperClass.getSuperclass();
	}

	request.setAttribute("slots", results);
	request.setAttribute("classToMerge", classToMerge);
	request.setAttribute("object1IdInternal", domainObject1.getIdInternal());
	request.setAttribute("object2IdInternal", domainObject2.getIdInternal());

	return mapping.findForward("displayObjects");
    }

    private void fillDtoWithCollectionProperty(MergeSlotDTO mergeSlot, Object collection, String order,
	    Integer idInternal, String domainClass) {
	if (collection != null) {
	    mergeSlot.setValueProperty(order, "Size: " + ((Collection) collection).size());
	    mergeSlot.setValueLinkProperty(order, "/domainbrowser/listRole?domainClass=" + domainClass
		    + "&objId=" + idInternal + "&role=" + mergeSlot.getName());

	} else {
	    mergeSlot.setValueProperty(order, "null collection");
	}
    }

    private void fillDtoWithSimpleProperty(Role roleSlot, MergeSlotDTO mergeSlot, Object reference,
	    String order) {
	if (reference != null) {
	    if (roleSlot.getType() instanceof DomainClass) {
		final Integer idInternal = ((DomainObject) reference).getIdInternal();
		mergeSlot.setValueProperty(order, "OID: " + idInternal);
		mergeSlot.setValueLinkProperty(order, "/domainbrowser/showObj?domainClass="
			+ reference.getClass().getName() + "&objId=" + idInternal);
	    } else {
		// enum ??
		mergeSlot.setValueProperty(order, reference.toString());
	    }
	} else {
	    mergeSlot.setValueProperty(order, "null");
	}
    }

    public ActionForward mergeProperty(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException, IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException, ClassNotFoundException {

	IUserView userView = SessionUtils.getUserView(request);

	String classToMerge = (String) request.getParameter("classToMerge");
	Integer object1IdInternal = Integer.valueOf(request.getParameter("object1IdInternal"));
	Integer object2IdInternal = Integer.valueOf(request.getParameter("object2IdInternal"));

	DomainObject domainObject1 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge),
		object1IdInternal);
	DomainObject domainObject2 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge),
		object2IdInternal);

	Integer sourceOrder = Integer.valueOf(request.getParameter("source"));
	String slotName = request.getParameter("slotName");

	Object[] args = { (sourceOrder == 1) ? domainObject1 : domainObject2,
		(sourceOrder == 1) ? domainObject2 : domainObject1, slotName };
	ServiceUtils.executeService(userView, "TransferDomainObjectProperty", args);

	return chooseObjects(mapping, form, request, response);

    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException,
	    IllegalAccessException, InvocationTargetException, NoSuchMethodException,
	    ClassNotFoundException {

	IUserView userView = SessionUtils.getUserView(request);
	Integer objectIdInternal = Integer.valueOf(request.getParameter("objectIdInternal"));

	final String classToMerge = request.getParameter("classToMerge");
	Object[] args = { Class.forName(classToMerge), objectIdInternal };
	try {
	    ServiceUtils.executeService(userView, "DeleteObjectByOID", args);
	} catch (DomainException e) {
	    e.printStackTrace();
	    return chooseObjects(mapping, form, request, response);
	}
	request.setAttribute("domainClasses", getClasses());
	request.setAttribute("classToMerge", classToMerge);
	return mapping.findForward("chooseObjects");
    }

    private List<LabelValueBean> getClasses() {
	List<LabelValueBean> classes = new ArrayList<LabelValueBean>();
	for (Iterator<DomainClass> iter = MetadataManager.getDomainModel().getClasses(); iter.hasNext();) {
	    DomainClass domainClass = (DomainClass) iter.next();
	    classes.add(new LabelValueBean(domainClass.getName(), domainClass.getFullName()));
	}

	Collections.sort(classes, new Comparator<LabelValueBean>() {
	    public int compare(LabelValueBean bean1, LabelValueBean bean2) {
		return bean1.compareTo(bean2);
	    }
	});

	return classes;
    }

}
