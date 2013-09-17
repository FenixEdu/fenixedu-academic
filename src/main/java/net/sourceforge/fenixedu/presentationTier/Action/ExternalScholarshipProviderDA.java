package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/externalScholarshipProvider", module = "manager")
@Forwards({ @Forward(name = "list", path = "/manager/listExternalScholarshipProvideres.jsp"),
        @Forward(name = "add", path = "/manager/addExternalScholarshipProvideres.jsp") })
public class ExternalScholarshipProviderDA extends FenixDispatchAction {

    public static class ExternalScholarshipBean implements Serializable {
        private Party selected;

        public Party getSelected() {
            return selected;
        }

        public void setSelected(Party selected) {
            this.selected = selected;
        }
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Collection<Party> externalScholarshipProvider = RootDomainObject.getInstance().getExternalScholarshipProvider();
        request.setAttribute("externalScholarshipProviders", externalScholarshipProvider);
        return mapping.findForward("list");
    }

    @Atomic
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod() != "POST") {
            request.setAttribute("bean", new ExternalScholarshipBean());
            return mapping.findForward("add");
        } else {
            ExternalScholarshipBean bean = getRenderedObject();
            Collection<Party> externalScholarshipProvider = RootDomainObject.getInstance().getExternalScholarshipProvider();
            externalScholarshipProvider.add(bean.getSelected());
            return redirect("/externalScholarshipProvider.do?method=list", request);
        }
    }

    @Atomic
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Party party = FenixFramework.getDomainObject(request.getParameter("provider"));
        Collection<Party> externalScholarshipProvider = RootDomainObject.getInstance().getExternalScholarshipProvider();
        externalScholarshipProvider.remove(party);
        return redirect("/externalScholarshipProvider.do?method=list", request);
    }
}
