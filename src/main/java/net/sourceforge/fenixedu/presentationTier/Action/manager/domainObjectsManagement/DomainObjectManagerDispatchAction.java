/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.domainObjectsManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteObjectByOID;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "manager", path = "/domainObjectManager", attribute = "domainObjectManagerForm",
        formBean = "domainObjectManagerForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "prepareEditObject", path = "/manager/domainObjectsManagement/editObject.jsp"),
        @Forward(name = "chooseClassToManage", path = "/manager/domainObjectsManagement/chooseClassToManage.jsp") })
public class DomainObjectManagerDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        Person person = checkUser();

        generateIdIndexesToAnswer(form, person);

        request.setAttribute("method", "deleteObject");
        ((DynaActionForm) form).set("method", "prepareEditObject");
        request.setAttribute("domainClasses", getClasses());
        return mapping.findForward("chooseClassToManage");
    }

    public ActionForward deleteObject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, ClassNotFoundException {

        Person person = checkUser();

        String documentIdNumber = person.getDocumentIdNumber();

        DynaActionForm actionForm = (DynaActionForm) form;
        Integer idPos1Index = (Integer) actionForm.get("idPos1Index");
        Integer idPos2Index = (Integer) actionForm.get("idPos2Index");
        Integer idPos3Index = (Integer) actionForm.get("idPos3Index");
        Character idPos1Value = (Character) actionForm.get("idPos1Value");
        Character idPos2Value = (Character) actionForm.get("idPos2Value");
        Character idPos3Value = (Character) actionForm.get("idPos3Value");

        if (documentIdNumber.charAt(idPos1Index - 1) == idPos1Value.charValue()
                && documentIdNumber.charAt(idPos2Index - 1) == idPos2Value.charValue()
                && documentIdNumber.charAt(idPos3Index - 1) == idPos3Value.charValue()) {

            String classToDelete = (String) actionForm.get("classToManage");
            String classToDeleteId = (String) actionForm.get("classToManageId");

            try {
                DeleteObjectByOID.run(classToDeleteId);
                request.setAttribute("message", "Object " + classToDelete + " with ID:" + classToDeleteId
                        + " Deleted. God have mercy of your soul...");

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("message", "Error deleting Object " + classToDelete + " with ID:" + classToDeleteId);
            }

        } else {
            request.setAttribute("message", "Who the hell are you!?!?!");
        }

        request.setAttribute("domainClasses", getClasses());
        ((DynaActionForm) form).set("method", "prepareEditObject");
        return mapping.findForward("chooseClassToManage");
    }

    public ActionForward prepareEditObject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, ClassNotFoundException {

        Person person = checkUser();

        String documentIdNumber = person.getDocumentIdNumber();
        DynaActionForm actionForm = (DynaActionForm) form;
        Integer idPos1Index = (Integer) actionForm.get("idPos1Index");
        Integer idPos2Index = (Integer) actionForm.get("idPos2Index");
        Integer idPos3Index = (Integer) actionForm.get("idPos3Index");
        Character idPos1Value = (Character) actionForm.get("idPos1Value");
        Character idPos2Value = (Character) actionForm.get("idPos2Value");
        Character idPos3Value = (Character) actionForm.get("idPos3Value");

        if (documentIdNumber.charAt(idPos1Index - 1) == idPos1Value.charValue()
                && documentIdNumber.charAt(idPos2Index - 1) == idPos2Value.charValue()
                && documentIdNumber.charAt(idPos3Index - 1) == idPos3Value.charValue()) {

            DomainObject object = getDomainObject(actionForm, "classToManageId");

            if (object != null) {
                request.setAttribute("objectToEdit", object);
            }
        } else {
            request.setAttribute("message", "Who the hell are you!?!?!");
        }
        return mapping.findForward("prepareEditObject");
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

    private void generateIdIndexesToAnswer(ActionForm form, Person person) {
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

        DynaActionForm actionForm = (DynaActionForm) form;
        actionForm.set("idPos1Index", pos1);
        actionForm.set("idPos2Index", pos2);
        actionForm.set("idPos3Index", pos3);
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