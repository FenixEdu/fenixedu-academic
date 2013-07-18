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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteObjectByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.TransferDomainObjectProperty;
import net.sourceforge.fenixedu.dataTransferObject.MergeSlotDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import dml.DomainClass;
import dml.Role;
import dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "manager", path = "/mergeObjects", attribute = "objectsMergeForm", formBean = "objectsMergeForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "displayObjects", path = "/manager/personManagement/mergeObjects.jsp"),
        @Forward(name = "chooseObjects", path = "/manager/personManagement/chooseObjectsToMerge.jsp") })
public class MergeObjectsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("domainClasses", getClasses());
        return mapping.findForward("chooseObjects");
    }

    public ActionForward chooseObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException, IllegalAccessException,
            NoSuchMethodException, ClassNotFoundException {

        DynaActionForm actionForm = (DynaActionForm) form;
        String classToMerge = (String) actionForm.get("classToMerge");
        Integer object1IdInternal = (Integer) actionForm.get("object1IdInternal");
        Integer object2IdInternal = (Integer) actionForm.get("object2IdInternal");

        if (classToMerge.length() == 0) {
            classToMerge = request.getParameter("classToMerge");
            object1IdInternal = Integer.valueOf(request.getParameter("object1IdInternal"));
            object2IdInternal = Integer.valueOf(request.getParameter("object2IdInternal"));
        }

        DomainObject domainObject1 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge), object1IdInternal);
        DomainObject domainObject2 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge), object2IdInternal);

        if (domainObject1 == null || domainObject2 == null) {
            request.setAttribute("domainClasses", getClasses());
            return mapping.findForward("chooseObjects");
        }

        DomainClass domainClass = FenixFramework.getDomainModel().findClass(classToMerge);

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

                    fillDtoWithCollectionProperty(mergeSlot, property1, MergeSlotDTO.VALUE1, domainObject1.getOID(), classToMerge);
                    fillDtoWithCollectionProperty(mergeSlot, property2, MergeSlotDTO.VALUE2, domainObject2.getOID(), classToMerge);
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
            HttpServletResponse response) throws  FenixServiceException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ClassNotFoundException {

        IUserView userView = UserView.getUser();

        String classToMerge = request.getParameter("classToMerge");
        Integer object1IdInternal = Integer.valueOf(request.getParameter("object1IdInternal"));
        Integer object2IdInternal = Integer.valueOf(request.getParameter("object2IdInternal"));

        DomainObject domainObject1 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge), object1IdInternal);
        DomainObject domainObject2 = rootDomainObject.readDomainObjectByOID(Class.forName(classToMerge), object2IdInternal);

        Integer sourceOrder = Integer.valueOf(request.getParameter("source"));
        String slotName = request.getParameter("slotName");

        TransferDomainObjectProperty.run((sourceOrder == 1) ? domainObject1 : domainObject2,
                (sourceOrder == 1) ? domainObject2 : domainObject1, slotName);

        return chooseObjects(mapping, form, request, response);

    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws  FenixServiceException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException {

        IUserView userView = UserView.getUser();
        Integer objectIdInternal = Integer.valueOf(request.getParameter("objectIdInternal"));

        final String classToMerge = request.getParameter("classToMerge");

        try {
            DeleteObjectByOID.run(Class.forName(classToMerge), objectIdInternal);
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
        for (Iterator<DomainClass> iter = FenixFramework.getDomainModel().getClasses(); iter.hasNext();) {
            DomainClass domainClass = iter.next();
            classes.add(new LabelValueBean(domainClass.getName(), domainClass.getFullName()));
        }

        Collections.sort(classes, new Comparator<LabelValueBean>() {
            @Override
            public int compare(LabelValueBean bean1, LabelValueBean bean2) {
                return bean1.compareTo(bean2);
            }
        });

        return classes;
    }

}
