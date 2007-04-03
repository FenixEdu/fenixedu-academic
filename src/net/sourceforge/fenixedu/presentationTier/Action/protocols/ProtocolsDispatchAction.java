package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ProtocolsDispatchAction extends FenixDispatchAction {

    public ActionForward showProtocols(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("protocols", RootDomainObject.getInstance().getProtocols());
        return mapping.findForward("show-protocols");
    }

    public ActionForward prepareCreateProtocol(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject("protocolFactory");
        if (protocolFactory != null) {
            DynaActionForm form = (DynaActionForm) actionForm;
            String elementToAdd = form.getString("addElement");
            if (!StringUtils.isEmpty(elementToAdd)) {
                //addElement(elementToAdd, protocolFactory);
            }
        } else {
            protocolFactory = new ProtocolFactory();
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("create-protocol");
    }

    public ActionForward createProtocol(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject("protocolFactory");
        executeService(request, "ExecuteFactoryMethod", new Object[] { protocolFactory });
        return showProtocols(mapping, actionForm, request, response);
    }
}