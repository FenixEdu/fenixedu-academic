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

import pt.ist.fenixframework.FenixFramework;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import dml.DomainClass;
import dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DomainObjectStringPropertyFormatterDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

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
            Slot slot = (Slot) slotsIter.next();
            if (slot.getType().equals("java.lang.String")) {
                slots.add(new LabelValueBean(slot.getName(), slot.getName()));
            }
        }

        request.setAttribute("domainClasses", getClasses());
        request.setAttribute("classSlots", slots);

        return mapping.findForward("propertyFormatter");
    }

    public ActionForward formatProperty(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException,
            FenixFilterException, FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm actionForm = (DynaActionForm) form;
        String domainObjectClass = (String) actionForm.get("domainObjectClass");
        String slotName = (String) actionForm.get("slotName");

        Object[] args = { Class.forName(domainObjectClass), slotName };
        ServiceUtils.executeService(userView, "DomainObjectStringPropertyFormatter", args);

        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("formatCompleted", new ActionMessage("label.property.format.ok", ""));
        saveMessages(request, actionMessages);
        
        request.setAttribute("domainClasses", getClasses());

        return mapping.findForward("propertyFormatter");
    }

    private List<LabelValueBean> getClasses() {
        List<LabelValueBean> classes = new ArrayList<LabelValueBean>();
        for (Iterator<DomainClass> iter = FenixFramework.getDomainModel().getClasses(); iter.hasNext();) {
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
