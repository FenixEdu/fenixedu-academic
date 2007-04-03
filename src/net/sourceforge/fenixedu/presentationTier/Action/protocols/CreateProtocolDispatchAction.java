package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.EditProtocolAction;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolFile;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class CreateProtocolDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateProtocolData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("protocolFactory", new ProtocolFactory());
        return mapping.findForward("prepareCreate-protocol-data");
    }

    public ActionForward prepareCreateProtocolResponsibles(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancelProtocolCreation") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("show-protocols");
        }
        request.setAttribute("protocolFactory",protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }
    
    public ActionForward prepareCreateProtocolUnits(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = protocolFactory = (ProtocolFactory) getRenderedObject();
        protocolFactory.resetSearches();
        request.setAttribute("protocolFactory",protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }
    
    public ActionForward insertResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-data");
        }
        if (request.getParameter("next") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }

        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }
        if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalPerson", "true");
            protocolFactory.setInternalUnit(false);
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }

        if (protocolFactory.getResponsible() == null) {
            if (StringUtils.isEmpty(protocolFactory.getResponsibleName())) {
                setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                        "error.protocol.person.selectFromList"));
            } else if (!protocolFactory.getIstResponsible()) {
                request.setAttribute("needToCreatePerson", "true");
            }
        } else {
            if (protocolFactory.getIstResponsible()) {
                protocolFactory.addISTResponsible();
            } else {
                protocolFactory.addPartnerResponsible();
            }
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward createExternalResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }
        if (protocolFactory.getUnitObject() != null) {
            ExternalContract externalContract = (ExternalContract) executeService(
                    "InsertExternalPerson", new Object[] { protocolFactory.getResponsibleName(),
                            protocolFactory.getUnitObject().getUnit() });
            protocolFactory.setResponsibleToAdd(externalContract.getPerson());
            protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_RESPONSIBLE);
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
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward createExternalPersonAndUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            request.setAttribute("createExternalPerson", "true");
            return mapping.findForward("prepareCreate-protocol-responsibles");
        }

        ExternalContract externalContract = (ExternalContract) executeService("InsertExternalPerson",
                new Object[] { protocolFactory.getResponsibleName(), protocolFactory.getUnitName() });
        protocolFactory.setResponsibleToAdd(externalContract.getPerson());
        protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_RESPONSIBLE);
        Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolFactory });
        protocolFactory = new ProtocolFactory(protocol);

        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward insertUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        //TODO fazer a validação dos responsáveis!!
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }
        if (request.getParameter("next") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-files");
        }
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }
        if (request.getParameter("createNew") != null) {
            request.setAttribute("createExternalUnit", "true");
            protocolFactory.setInternalUnit(false);
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }

        if (protocolFactory.getUnitObject() == null) {
            if (protocolFactory.getInternalUnit()) {
                setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                        "error.protocol.unit.selectFromList"));
            }
            if (!protocolFactory.getInternalUnit()) {
                request.setAttribute("needToCreateUnit", "true");
            }
        } else {
            if (protocolFactory.getInternalUnit()) {
                protocolFactory.addISTUnit();
            } else {
                protocolFactory.addPartnerUnit();
            }
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward createExternalUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("cancel") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }

        Unit externalUnit = (Unit) executeService("CreateExternalUnitByName",
                new Object[] { protocolFactory.getUnitName() });
        protocolFactory.setUnitToAdd(externalUnit);
        protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_UNIT);
        Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolFactory });
        protocolFactory = new ProtocolFactory(protocol);

        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward removeISTResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        protocolFactory.getResponsibles().remove(protocolFactory.getResponsibleToRemove());
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward removePartnerResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        protocolFactory.getPartnerResponsibles().remove(protocolFactory.getResponsibleToRemove());
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward removeISTUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        protocolFactory.getUnits().remove(protocolFactory.getUnitToRemove());
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward removePartnerUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        protocolFactory.getPartnerUnits().remove(protocolFactory.getUnitToRemove());
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward prepareCreateProtocolFiless(ActionMapping mapping, ActionForm actionForm,
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
        return mapping.findForward("prepareCreate-protocol-files");
    }

    public ActionForward addProtocolFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("view-protocol");
        }
        protocolFactory.setEditProtocolAction(EditProtocolAction.ADD_FILE);
        Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolFactory });
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
        return mapping.findForward("prepareCreate-protocol-files");
    }

    public ActionForward deleteProtocolFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer fileID = getIntegerFromRequest(request, "fileID");
        ProtocolFile protocolFile = (ProtocolFile) RootDomainObject.readDomainObjectByOID(
                ProtocolFile.class, fileID);
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        protocolFactory.setFileToDelete(protocolFile);
        protocolFactory.setEditProtocolAction(EditProtocolAction.DELETE_FILE);
        Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                new Object[] { protocolFactory });
        request.setAttribute("protocolFactory", new ProtocolFactory(protocol));

        return mapping.findForward("prepareCreate-protocol-files");
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }
}