/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import dml.DomainClass;
import dml.Role;
import dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class MergePersonsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("choosePersons");
    }

    public ActionForward choosePersons(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm actionForm = (DynaActionForm) form;
        String person1Username = (String) actionForm.get("person1Username");
        String person2Username = (String) actionForm.get("person2Username");

        Person person1 = null;
        Person person2 = null;

        if (person1Username.length() > 0 && person2Username.length() > 0) {
            Object[] args1 = { person1Username };
            person1 = (Person) ServiceUtils
                    .executeService(userView, "ReadDomainPersonByUsername", args1);
            Object[] args2 = { person2Username };
            person2 = (Person) ServiceUtils
                    .executeService(userView, "ReadDomainPersonByUsername", args2);
        } else {
            Integer person1ID = Integer.valueOf(request.getParameter("person1ID"));
            Integer person2ID = Integer.valueOf(request.getParameter("person2ID"));
            person1 = (Person) rootDomainObject.readPartyByOID(person1ID);
            person2 = (Person) rootDomainObject.readPartyByOID(person2ID);
        }

        if (person1 == null || person2 == null) {
            return mapping.findForward("choosePersons");
        }

        DomainClass personClass = MetadataManager.getDomainModel().findClass(
                "net.sourceforge.fenixedu.domain.Person");

        List<MergeSlotDTO> results = new ArrayList<MergeSlotDTO>();
        DomainClass personClassOrSuperClass = personClass;
        while (personClassOrSuperClass.getSuperclass() != null) {

            for (Iterator<Slot> iter = personClassOrSuperClass.getSlots(); iter.hasNext();) {
                Slot slot = iter.next();

                String slotName = slot.getName();
                Object objPropertyPerson1 = PropertyUtils.getSimpleProperty(person1, slotName);
                Object objPropertyPerson2 = PropertyUtils.getSimpleProperty(person2, slotName);

                String propertyPerson1 = (objPropertyPerson1 == null) ? "" : objPropertyPerson1
                        .toString();
                String propertyPerson2 = (objPropertyPerson2 == null) ? "" : objPropertyPerson2
                        .toString();

                results.add(new MergeSlotDTO(slotName, "Simple Primitive", propertyPerson1,
                        propertyPerson2));
            }
            personClassOrSuperClass = (DomainClass) personClassOrSuperClass.getSuperclass();
        }

        personClassOrSuperClass = personClass;        
        while (personClassOrSuperClass.getSuperclass() != null) {
            for (Iterator<Role> iter = personClassOrSuperClass.getRoleSlots(); iter.hasNext();) {
                Role roleSlot = iter.next();

                String slotName = roleSlot.getName();
                MergeSlotDTO mergeSlot = new MergeSlotDTO(slotName);

                Object propertyPerson1 = PropertyUtils.getSimpleProperty(person1, slotName);
                Object propertyPerson2 = PropertyUtils.getSimpleProperty(person2, slotName);

                if (roleSlot.getMultiplicityUpper() == 1) {
                    mergeSlot.setType("Reference");

                    fillDtoWithSimpleProperty(roleSlot, mergeSlot, propertyPerson1, MergeSlotDTO.VALUE1);
                    fillDtoWithSimpleProperty(roleSlot, mergeSlot, propertyPerson2, MergeSlotDTO.VALUE2);

                } else {
                    // collection
                    mergeSlot.setType("Collection");

                    fillDtoWithColelctionProperty(mergeSlot, propertyPerson1, MergeSlotDTO.VALUE1);
                    fillDtoWithColelctionProperty(mergeSlot, propertyPerson2, MergeSlotDTO.VALUE2);
                }

                results.add(mergeSlot);
            }
            personClassOrSuperClass = (DomainClass) personClassOrSuperClass.getSuperclass();
        }

        request.setAttribute("slots", results);
        request.setAttribute("person1ID", person1.getIdInternal());
        request.setAttribute("person2ID", person2.getIdInternal());

        return mapping.findForward("displayPersons");
    }

    private void fillDtoWithColelctionProperty(MergeSlotDTO mergeSlot, Object propertyPerson1,
            String order) {
        if (propertyPerson1 != null) {
            mergeSlot.setValueProperty(order, "Size: " + ((Collection) propertyPerson1).size());
        } else {
            mergeSlot.setValueProperty(order, "null collection");
        }
    }

    private void fillDtoWithSimpleProperty(Role roleSlot, MergeSlotDTO mergeSlot, Object person,
            String order) {
        if (person != null) {
            if (roleSlot.getType() instanceof DomainClass) {
                mergeSlot.setValueProperty(order, "OID: " + ((DomainObject) person).getIdInternal());
            } else {
                // enum ??
                mergeSlot.setValueProperty(order, person.toString());
            }
        } else {
            mergeSlot.setValueProperty(order, "null");
        }
    }

    public ActionForward mergeProperty(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer person1ID = Integer.valueOf(request.getParameter("person1ID"));
        Integer person2ID = Integer.valueOf(request.getParameter("person2ID"));
        final Person person1 = (Person) rootDomainObject.readPartyByOID(person1ID);
        final Person person2 = (Person) rootDomainObject.readPartyByOID(person2ID);

        Integer sourceOrder = Integer.valueOf(request.getParameter("source"));
        String slotName = request.getParameter("slotName");

        Object[] args = { (sourceOrder == 1) ? person1 : person2,
                (sourceOrder == 1) ? person2 : person1, slotName };
        ServiceUtils.executeService(userView, "TransferDomainObjectProperty", args);

        return choosePersons(mapping, form, request, response);

    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer personID = Integer.valueOf(request.getParameter("personID"));

        Object[] args = { personID };
        try {
            ServiceUtils.executeService(userView, "DeletePersonByOID", args);
        } catch (DomainException e) {
            return choosePersons(mapping, form, request, response);
        }
        return mapping.findForward("choosePersons");
    }
}
