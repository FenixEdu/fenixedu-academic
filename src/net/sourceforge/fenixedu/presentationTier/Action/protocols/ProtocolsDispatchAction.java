package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolFile;
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
                addElement(elementToAdd, protocolFactory);
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

    public ActionForward prepareEditProtocol(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "idInternal");
        ProtocolFactory protocolFactory = null;
        if (protocolID != null) {
            Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class,
                    protocolID);
            protocolFactory = new ProtocolFactory(protocol);
        } else {
            protocolFactory = (ProtocolFactory) getRenderedObject("protocolFactory");
            DynaActionForm form = (DynaActionForm) actionForm;
            String elementToAdd = form.getString("addElement");
            if (!StringUtils.isEmpty(elementToAdd)) {
                addElement(elementToAdd, protocolFactory);
            }
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol");
    }

    public ActionForward editProtocol(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject("protocolFactory");
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Integer[] filesToDelete = (Integer[]) dynaActionForm.get("filesToDelete");
        protocolFactory.setFilesToDelete(filesToDelete);             
        
        executeService(request, "ExecuteFactoryMethod", new Object[] { protocolFactory });
        return showProtocols(mapping, actionForm, request, response);
    }
    
    public ActionForward deleteProtocolFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer protocolFileID = getIntegerFromRequest(request, "idInternal");
        ProtocolFile protocolFile = (ProtocolFile) RootDomainObject.readDomainObjectByOID(ProtocolFile.class,protocolFileID);
        executeService(request, "DeleteProtocolFile", new Object[] { protocolFile });
        return showProtocols(mapping, actionForm, request, response);
    }
    
    private void addElement(String elementToAdd, ProtocolFactory protocolFactory) {
        if (elementToAdd.equalsIgnoreCase("responsible")) {
            protocolFactory.addResponsible();
        } else if (elementToAdd.equalsIgnoreCase("partnerResponsible")) {
            protocolFactory.addPartnerResponsible();
        } else if (elementToAdd.equalsIgnoreCase("unit")) {
            protocolFactory.addUnit();
        } else if (elementToAdd.equalsIgnoreCase("partnerUnit")) {
            protocolFactory.addPartnerUnit();
        } else {
            protocolFactory.addFile();
        }
    }
}