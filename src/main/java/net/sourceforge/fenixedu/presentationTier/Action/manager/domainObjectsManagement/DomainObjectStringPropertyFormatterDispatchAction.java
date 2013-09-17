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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DomainObjectStringPropertyFormatter;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "manager", path = "/domainObjectStringPropertyFormatter", attribute = "stringPropertyFormatterForm",
        formBean = "stringPropertyFormatterForm", scope = "request", parameter = "method")
@Forwards(
        value = { @Forward(name = "propertyFormatter", path = "/manager/domainObjectsManagement/chooseClassAndSlotToFormat.jsp") })
public class DomainObjectStringPropertyFormatterDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("domainClasses", getClasses());

        return mapping.findForward("propertyFormatter");
    }

    public ActionForward chooseClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm actionForm = (DynaActionForm) form;
        String domainObjectClass = (String) actionForm.get("domainObjectClass");

        DomainClass domainObjectToFormat = FenixFramework.getDomainModel().findClass(domainObjectClass);

        List<LabelValueBean> slots = new ArrayList<LabelValueBean>();
        Iterator<Slot> slotsIter = domainObjectToFormat.getSlots();
        while (slotsIter.hasNext()) {
            Slot slot = slotsIter.next();
            if (slot.getType().equals("java.lang.String")) {
                slots.add(new LabelValueBean(slot.getName(), slot.getName()));
            }
        }

        request.setAttribute("domainClasses", getClasses());
        request.setAttribute("classSlots", slots);

        return mapping.findForward("propertyFormatter");
    }

    public ActionForward formatProperty(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ClassNotFoundException, FenixServiceException {

        IUserView userView = UserView.getUser();

        DynaActionForm actionForm = (DynaActionForm) form;
        String domainObjectClass = (String) actionForm.get("domainObjectClass");
        String slotName = (String) actionForm.get("slotName");

        DomainObjectStringPropertyFormatter.run(Class.forName(domainObjectClass), slotName);

        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("formatCompleted", new ActionMessage("label.property.format.ok", ""));
        saveMessages(request, actionMessages);

        request.setAttribute("domainClasses", getClasses());

        return mapping.findForward("propertyFormatter");
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