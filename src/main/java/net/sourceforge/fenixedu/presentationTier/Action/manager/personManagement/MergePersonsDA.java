/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteObjectByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.TransferDomainObjectProperty;
import net.sourceforge.fenixedu.dataTransferObject.MergeSlotDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPeopleApp;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Role;
import pt.ist.fenixframework.dml.Slot;

@StrutsFunctionality(app = ManagerPeopleApp.class, path = "merge-person", titleKey = "title.personManagement.merge",
        accessGroup = "#managers")
@Mapping(path = "/mergePersons", module = "manager")
@Forwards({
        @Forward(name = "chooseObjectsToMerge", path = "/manager/personManagement/merge/chooseObjectsToMerge.jsp"),
        @Forward(name = "displayObjects", path = "/manager/personManagement/merge/displayObjects.jsp"),
        @Forward(name = "only-one-person-with-student-object", path = "/manager/personManagement/merge/onePersonWithStudent.jsp"),
        @Forward(name = "remove-student", path = "/manager/personManagement/merge/removeStudent.jsp"),
        @Forward(name = "remove-person", path = "/manager/personManagement/merge/removePerson.jsp"),
        @Forward(name = "person-removed", path = "/manager/personManagement/merge/personRemoved.jsp"),
        @Forward(name = "transfer-registrations", path = "/manager/personManagement/merge/transferRegistrations.jsp"),
        @Forward(name = "transfer-events-and-accounts", path = "/manager/personManagement/merge/transferEventsAndAccounts.jsp") })
public class MergePersonsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Person person = checkUser();

        MergePersonsBean bean = new MergePersonsBean(Person.class.getName());
        generateIdIndexesToAnswer(bean, person);

        request.setAttribute("mergePersonsBean", bean);
        return mapping.findForward("chooseObjectsToMerge");
    }

    public ActionForward chooseObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IllegalAccessException, NoSuchMethodException,
            ClassNotFoundException {
        Person person = checkUser();

        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);

        String documentIdNumber = person.getDocumentIdNumber();

        Integer idPos1Index = mergePersonsBean.getIdPosOneIndex();
        Integer idPos2Index = mergePersonsBean.getIdPosTwoIndex();
        Integer idPos3Index = mergePersonsBean.getIdPosThreeIndex();
        String idPos1Value = mergePersonsBean.getIdPosOneValue();
        String idPos2Value = mergePersonsBean.getIdPosTwoValue();
        String idPos3Value = mergePersonsBean.getIdPosThreeValue();

        if (documentIdNumber.charAt(idPos1Index - 1) != idPos1Value.charAt(0)
                || documentIdNumber.charAt(idPos2Index - 1) != idPos2Value.charAt(0)
                || documentIdNumber.charAt(idPos3Index - 1) != idPos3Value.charAt(0)) {
            throw new RuntimeException("Who the hell are you????");
        }

        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        if (Student.class.getName().equals(mergePersonsBean.getCurrentClass())) {
            return chooseObjects(mapping, form, request, response, domainObject1.getStudent().getExternalId(), domainObject2
                    .getStudent().getExternalId(), Student.class.getName());
        }

        return chooseObjects(mapping, form, request, response, domainObject1.getExternalId(), domainObject2.getExternalId(),
                Person.class.getName());
    }

    public ActionForward chooseObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String leftOid, String rightOid, String currentClass) throws FenixServiceException,
            IllegalAccessException, NoSuchMethodException, ClassNotFoundException {

        DomainObject domainObject1 = FenixFramework.getDomainObject(leftOid);
        DomainObject domainObject2 = FenixFramework.getDomainObject(rightOid);

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

                    fillDtoWithCollectionProperty(mergeSlot, property1, MergeSlotDTO.VALUE1, domainObject1.getExternalId(),
                            Person.class.getName());
                    fillDtoWithCollectionProperty(mergeSlot, property2, MergeSlotDTO.VALUE2, domainObject2.getExternalId(),
                            Person.class.getName());
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
            bean.setLeftOid(request.getParameter("object1ExternalId"));
            bean.setRightOid(request.getParameter("object2ExternalId"));
            bean.setCurrentClass(request.getParameter("classToMerge"));
        }

        return bean;
    }

    private void fillDtoWithCollectionProperty(MergeSlotDTO mergeSlot, Object collection, String order, String oid,
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
                final String externalId = ((DomainObject) reference).getExternalId();
                mergeSlot.setValueProperty(order, "OID: " + externalId);
                mergeSlot.setValueLinkProperty(order, "/domainbrowser/showObj?OID=" + externalId);
            } else {
                // enum ??
                mergeSlot.setValueProperty(order, reference.toString());
            }
        } else {
            mergeSlot.setValueProperty(order, "null");
        }
    }

    public ActionForward mergeProperty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        if (Student.class.getName().equals(mergePersonsBean.getCurrentClass())) {
            mergeProperty(mapping, form, request, response, domainObject1.getStudent().getExternalId(), domainObject2
                    .getStudent().getExternalId(), Student.class.getName());
            return chooseObjects(mapping, form, request, response, mergePersonsBean.getLeftOid(), mergePersonsBean.getRightOid(),
                    Person.class.getName());

        }

        mergeProperty(mapping, form, request, response, domainObject1.getExternalId(), domainObject2.getExternalId(),
                Person.class.getName());

        return chooseObjects(mapping, form, request, response, mergePersonsBean.getLeftOid(), mergePersonsBean.getRightOid(),
                Person.class.getName());
    }

    public void mergeProperty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
            String leftOid, String rightOid, String currentClass) throws FenixServiceException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ClassNotFoundException {

        User userView = Authenticate.getUser();

        DomainObject domainObject1 = FenixFramework.getDomainObject(leftOid);
        DomainObject domainObject2 = FenixFramework.getDomainObject(rightOid);

        Integer sourceOrder = Integer.valueOf(request.getParameter("source"));
        String slotName = request.getParameter("slotName");

        TransferDomainObjectProperty.run((sourceOrder == 1) ? domainObject1 : domainObject2,
                (sourceOrder == 1) ? domainObject2 : domainObject1, slotName);

    }

    public ActionForward mergeStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IllegalAccessException, NoSuchMethodException,
            ClassNotFoundException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        if (domainObject1.getStudent() == null || domainObject2.getStudent() == null) {
            return mapping.findForward("only-one-person-with-student-object");
        }

        mergePersonsBean.setCurrentClass(Student.class.getName());

        return chooseObjects(mapping, form, request, response, domainObject1.getStudent().getExternalId(), domainObject2
                .getStudent().getExternalId(), Student.class.getName());
    }

    public ActionForward prepareTransferRegistrations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IllegalAccessException, NoSuchMethodException,
            ClassNotFoundException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        if (domainObject1.getStudent() == null || domainObject2.getStudent() == null) {
            return mapping.findForward("only-one-person-with-student-object");
        }

        mergePersonsBean.setCurrentClass(Student.class.getName());

        chooseObjects(mapping, form, request, response, domainObject1.getStudent().getExternalId(), domainObject2.getStudent()
                .getExternalId(), Student.class.getName());

        return mapping.findForward("transfer-registrations");
    }

    public ActionForward prepareDeleteStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        request.setAttribute("studentToDelete", domainObject2.getStudent());

        return mapping.findForward("remove-student");
    }

    public ActionForward deleteStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        Student studentToRemove = domainObject2.getStudent();

        if (studentToRemove != null) {
            DeleteObjectByOID.run(studentToRemove.getExternalId());
            request.setAttribute("studentRemoved", true);
        }

        return prepareDeletePerson(mapping, form, request, response);
    }

    public ActionForward prepareDeletePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);

        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        request.setAttribute("person", domainObject2);

        return mapping.findForward("remove-person");
    }

    public ActionForward deletePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        domainObject2.mergeAndDelete(domainObject1);

        request.setAttribute("studentRemoved", true);

        return mapping.findForward("person-removed");
    }

    public ActionForward transferRegistrations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IllegalAccessException, NoSuchMethodException,
            ClassNotFoundException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        Student studentDestiny = domainObject1.getStudent();
        Student studentToRemove = domainObject2.getStudent();

        studentDestiny.acceptRegistrationsFromOtherStudent(studentToRemove.getRegistrations());

        return prepareTransferRegistrations(mapping, form, request, response);

    }

    public ActionForward prepareTransferEventsAndAccounts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IllegalAccessException, NoSuchMethodException,
            ClassNotFoundException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person domainObject1 = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person domainObject2 = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        chooseObjects(mapping, form, request, response, mergePersonsBean.getLeftOid(), mergePersonsBean.getRightOid(),
                Person.class.getName());

        return mapping.findForward("transfer-events-and-accounts");
    }

    public ActionForward transferEventsAndAccounts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IllegalAccessException, NoSuchMethodException,
            ClassNotFoundException {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person destinyPerson = FenixFramework.getDomainObject(mergePersonsBean.getLeftOid());
        Person sourcePerson = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        destinyPerson.transferEventsAndAccounts(sourcePerson);

        return prepareTransferEventsAndAccounts(mapping, form, request, response);
    }

    public ActionForward removeFromPersistentGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person sourcePerson = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        MergePersonsOperations.removeFromPersistentGroups(sourcePerson);

        return prepareDeletePerson(mapping, form, request, response);
    }

    public ActionForward removeFromUploadUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        MergePersonsBean mergePersonsBean = getMergePersonsBean(request);
        request.setAttribute("mergePersonsBean", mergePersonsBean);
        Person sourcePerson = FenixFramework.getDomainObject(mergePersonsBean.getRightOid());

        MergePersonsOperations.removeFromUploadUnits(sourcePerson);

        return prepareDeletePerson(mapping, form, request, response);
    }

    private Person checkUser() {
        Person person = AccessControl.getPerson();
        Unit ciistUnit = Unit.readByCostCenterCode(FenixConfigurationManager.getConfiguration().getCIISTCostCenterCode());
        Unit currentWorkingPlace = person.getEmployee().getCurrentWorkingPlace();
        if ((currentWorkingPlace != null && ciistUnit != null && !currentWorkingPlace.equals(ciistUnit))
                || person.getPersonRole(RoleType.MANAGER) == null) {
            throw new DomainException("What you want do do hein?!?!");
        }
        return person;
    }

    private void generateIdIndexesToAnswer(MergePersonsBean bean, Person person) {
        int documentIdLength = person.getDocumentIdNumber().length();
        Integer pos1 = null;
        Integer pos2 = null;
        Integer pos3 = null;

        pos1 = (int) Math.round(Math.random() * (documentIdLength - 1)) + 1;

        do {
            pos2 = (int) Math.round(Math.random() * (documentIdLength - 1)) + 1;
        } while (pos1 == pos2);

        do {
            pos3 = (int) Math.round(Math.random() * (documentIdLength - 1)) + 1;
        } while (pos3 == pos2 && pos3 == pos1);

        bean.setIdPosOneIndex(pos1);
        bean.setIdPosTwoIndex(pos2);
        bean.setIdPosThreeIndex(pos3);
    }

    public static class MergePersonsBean implements java.io.Serializable {
        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private String leftOid;
        private String rightOid;
        private String currentClass;

        private String idPosOneValue;
        private String idPosTwoValue;
        private String idPosThreeValue;

        private Integer idPosOneIndex;
        private Integer idPosTwoIndex;
        private Integer idPosThreeIndex;

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

        public String getIdPosOneValue() {
            return idPosOneValue;
        }

        public void setIdPosOneValue(String idPosOneValue) {
            this.idPosOneValue = idPosOneValue;
        }

        public String getIdPosTwoValue() {
            return idPosTwoValue;
        }

        public void setIdPosTwoValue(String idPosTwoValue) {
            this.idPosTwoValue = idPosTwoValue;
        }

        public String getIdPosThreeValue() {
            return idPosThreeValue;
        }

        public void setIdPosThreeValue(String idPosThreeValue) {
            this.idPosThreeValue = idPosThreeValue;
        }

        public Integer getIdPosOneIndex() {
            return idPosOneIndex;
        }

        public void setIdPosOneIndex(Integer idPosOneIndex) {
            this.idPosOneIndex = idPosOneIndex;
        }

        public Integer getIdPosTwoIndex() {
            return idPosTwoIndex;
        }

        public void setIdPosTwoIndex(Integer idPosTwoIndex) {
            this.idPosTwoIndex = idPosTwoIndex;
        }

        public Integer getIdPosThreeIndex() {
            return idPosThreeIndex;
        }

        public void setIdPosThreeIndex(Integer idPosThreeIndex) {
            this.idPosThreeIndex = idPosThreeIndex;
        }

    }

}
