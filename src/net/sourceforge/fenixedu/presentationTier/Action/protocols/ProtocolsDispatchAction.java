package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.EditProtocolAction;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
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

    public ActionForward viewProtocol(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "idInternal");
        Protocol protocol = (Protocol) RootDomainObject
                .readDomainObjectByOID(Protocol.class, protocolID);
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
        return mapping.findForward("view-protocol");
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
        ProtocolFile protocolFile = (ProtocolFile) RootDomainObject.readDomainObjectByOID(
                ProtocolFile.class, protocolFileID);
        executeService(request, "DeleteProtocolFile", new Object[] { protocolFile });
        return showProtocols(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditProtocolData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer protocolID = getIntegerFromRequest(request, "protocolID");
        Protocol protocol = (Protocol) RootDomainObject
                .readDomainObjectByOID(Protocol.class, protocolID);
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
        return mapping.findForward("edit-protocol-data");
    }

    public ActionForward editProtocolData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }
        protocolFactory.setEditProtocolAction(EditProtocolAction.EDIT_PROTOCOL_DATA);
        Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolFactory });
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
        return mapping.findForward("view-protocol");
    }

    public ActionForward prepareEditResponsibles(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "protocolID");
        ProtocolFactory protocolFactory = null;
        if (protocolID != null) {
            Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class,
                    protocolID);
            protocolFactory = new ProtocolFactory(protocol);
        } else {
            protocolFactory = (ProtocolFactory) getRenderedObject();
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward editResponsibles(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        } else if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalPerson", "true");
            protocolFactory.setInternalUnit(false);
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-responsibles");
        }

        if (protocolFactory.getResponsible() == null) {
            if (StringUtils.isEmpty(protocolFactory.getResponsibleName())) {
                setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                        "error.protocol.person.selectFromList"));
            } else if (!protocolFactory.getIstResponsible()) {
                request.setAttribute("needToCreatePerson", "true");
            }
        } else {
            protocolFactory.setEditProtocolAction(EditProtocolAction.EDIT_PROTOCOL_RESPONSIBLES);
            protocolFactory.setResponsibleToAdd(protocolFactory.getResponsible().getPerson());
            Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                    new Object[] { protocolFactory });
            protocolFactory = new ProtocolFactory(protocol);
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward prepareEditUnits(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer protocolID = getIntegerFromRequest(request, "protocolID");
        ProtocolFactory protocolFactory = null;
        if (protocolID != null) {
            Protocol protocol = (Protocol) RootDomainObject.readDomainObjectByOID(Protocol.class,
                    protocolID);
            protocolFactory = new ProtocolFactory(protocol);
        } else {
            protocolFactory = (ProtocolFactory) getRenderedObject();
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-units");
    }

    public ActionForward editUnits(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }

        if (protocolFactory.getUnitObject() == null) {
            if (protocolFactory.getInternalUnit()) {
                setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                        "error.protocol.unit.selectFromList"));
            } else {
                //TODO
            }
            request.setAttribute("newUnit", "newUnit");
        } else {
            protocolFactory.setEditProtocolAction(EditProtocolAction.EDIT_PROTOCOL_UNITS);
            Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                    new Object[] { protocolFactory });
            protocolFactory = new ProtocolFactory(protocol);
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-units");
    }

    public ActionForward createExternalResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("edit-protocol-responsibles");
        }
        if (protocolFactory.getUnitObject() != null) {
            ExternalContract externalContract = (ExternalContract) executeService(
                    "InsertExternalPerson", new Object[] { protocolFactory.getResponsibleName(),
                            protocolFactory.getUnitObject().getUnit() });
            protocolFactory.setResponsibleToAdd(externalContract.getPerson());
            protocolFactory.setEditProtocolAction(EditProtocolAction.EDIT_PROTOCOL_RESPONSIBLES);
            Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                    new Object[] { protocolFactory });
            protocolFactory = new ProtocolFactory(protocol);
        } else if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalUnit", "true");
        } else {
            request.setAttribute("createExternalPerson", "true");
            request.setAttribute("needToCreateUnit", "true");
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    public ActionForward createExternalPersonAndUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            request.setAttribute("createExternalPerson", "true");
            return mapping.findForward("edit-protocol-responsibles");
        }

        ExternalContract externalContract = (ExternalContract) executeService("InsertExternalPerson",
                new Object[] { protocolFactory.getResponsibleName(), protocolFactory.getUnitName() });
        protocolFactory.setResponsibleToAdd(externalContract.getPerson());        
        protocolFactory.setEditProtocolAction(EditProtocolAction.EDIT_PROTOCOL_RESPONSIBLES);
        Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolFactory });
        protocolFactory = new ProtocolFactory(protocol);

        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("edit-protocol-responsibles");
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }
}