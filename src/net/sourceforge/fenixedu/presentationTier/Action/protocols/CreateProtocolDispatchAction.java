package net.sourceforge.fenixedu.presentationTier.Action.protocols;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class CreateProtocolDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateProtocolData(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("protocolFactory", new ProtocolFactory());
        return mapping.findForward("prepareCreate-protocol-data");
    }

    public ActionForward prepareCreateProtocolResponsibles(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = protocolFactory = (ProtocolFactory) getRenderedObject();
        request.setAttribute("protocolFactory", protocolFactory);
        if (isCancelled(request)) {
            request.setAttribute("protocols", rootDomainObject.getProtocols());
            return mapping.findForward("show-protocols");
        }
        if (isProtocolNumberValid(protocolFactory)) {
            protocolFactory.resetSearches();
            return mapping.findForward("prepareCreate-protocol-responsibles");
        } else {
            setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                    "error.protocol.number.alreadyExists"));
            return mapping.findForward("prepareCreate-protocol-data");
        }
    }

    public ActionForward insertResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-data");
        }
        if (request.getParameter("next") != null) {
            if (protocolFactory.getResponsibles() == null) {
                setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                        "error.protocol.empty.istResponsibles"));
                request.setAttribute("protocolFactory", protocolFactory);
                return mapping.findForward("prepareCreate-protocol-responsibles");
            }
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

        if (protocolFactory.getPartnerResponsible() == null && protocolFactory.getResponsible() == null) {
            if (StringUtils.isEmpty(protocolFactory.getResponsibleName())
                    || protocolFactory.getIstResponsible()) {
                setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                        "error.protocol.person.selectFromList"));
            } else if (!protocolFactory.getIstResponsible()) {
                request.setAttribute("needToCreatePerson", "true");
            }
        } else {
            if (protocolFactory.getIstResponsible()) {
                if (!protocolFactory.addISTResponsible()) {
                    setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                            "error.protocol.duplicated.responsible"));
                }
            } else {
                if (!protocolFactory.addPartnerResponsible()) {
                    setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                            "error.protocol.duplicated.responsible"));
                }
            }
        }
        RenderUtils.invalidateViewState();
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
            protocolFactory.addPartnerResponsible(externalContract.getPerson());
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
        protocolFactory.addPartnerResponsible(externalContract.getPerson());
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward prepareCreateProtocolUnits(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = protocolFactory = (ProtocolFactory) getRenderedObject();
        protocolFactory.resetSearches();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward insertUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-responsibles");
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
        if (request.getParameter("createProtocol") != null) {
            return createProtocol(mapping, request, protocolFactory);
        }
        if (protocolFactory.getUnitObject() == null) {
            if (StringUtils.isEmpty(protocolFactory.getUnitName()) || protocolFactory.getInternalUnit()) {
                setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                        "error.protocol.unit.selectFromList"));
            } else if (!protocolFactory.getInternalUnit()) {
                request.setAttribute("needToCreateUnit", "true");
            }
        } else {
            if (protocolFactory.getInternalUnit()) {
                if (!protocolFactory.addISTUnit()) {
                    setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                            "error.protocol.duplicated.unit"));
                }
            } else {
                if (!protocolFactory.addPartnerUnit()) {
                    setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                            "error.protocol.duplicated.unit"));
                }
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    private ActionForward createProtocol(ActionMapping mapping, HttpServletRequest request,
            ProtocolFactory protocolFactory) throws Exception {
        if (protocolFactory.getPartnerUnits() == null || protocolFactory.getPartnerUnits().size() == 0) {
            setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                    "error.protocol.empty.partnerUnits"));
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        } else if (protocolFactory.getUnits() == null || protocolFactory.getUnits().size() == 0) {
            setError(request, "errorMessage", (ActionMessage) new ActionMessage(
                    "error.protocol.empty.units"));
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        } else {
            Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                    new Object[] { protocolFactory });
            request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
            return mapping.findForward("view-protocol");
        }
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
        protocolFactory.addPartnerUnit(externalUnit);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward removeISTResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        Person responsible = (Person) RootDomainObject.readDomainObjectByOID(Person.class, getInteger(
                (DynaActionForm) actionForm, "responsibleID"));
        protocolFactory.getResponsibles().remove(responsible);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward removePartnerResponsible(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        Person partnerResponsible = (Person) RootDomainObject.readDomainObjectByOID(Person.class,
                getInteger((DynaActionForm) actionForm, "responsibleID"));
        protocolFactory.getPartnerResponsibles().remove(partnerResponsible);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-responsibles");
    }

    public ActionForward removeISTUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, getInteger(
                (DynaActionForm) actionForm, "unitID"));
        protocolFactory.getUnits().remove(unit);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward removePartnerUnit(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        Unit partnerUnit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, getInteger(
                (DynaActionForm) actionForm, "unitID"));
        protocolFactory.getPartnerUnits().remove(partnerUnit);
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-units");
    }

    public ActionForward prepareCreateProtocolFiles(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("protocolFactory", (ProtocolFactory) getRenderedObject());
        return mapping.findForward("prepareCreate-protocol-files");
    }

    public ActionForward addFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        if (request.getParameter("back") != null) {
            request.setAttribute("protocolFactory", protocolFactory);
            return mapping.findForward("prepareCreate-protocol-units");
        }
        if (request.getParameter("createProtocol") != null) {
            Protocol protocol = (Protocol) executeService(request, "ExecuteFactoryMethod",
                    new Object[] { protocolFactory });
            request.setAttribute("protocolFactory", new ProtocolFactory(protocol));
            return mapping.findForward("view-protocol");
        }
        if (protocolFactory.getInputStream() != null) {
            protocolFactory.addFile();
            protocolFactory.resetFile();
            RenderUtils.invalidateViewState();
        }
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-files");
    }

    public ActionForward removeFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProtocolFactory protocolFactory = (ProtocolFactory) getRenderedObject();
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        protocolFactory.removeFile(dynaActionForm.getString("fileName"));
        request.setAttribute("protocolFactory", protocolFactory);
        return mapping.findForward("prepareCreate-protocol-files");
    }

    private boolean isProtocolNumberValid(ProtocolFactory protocolFactory) {
        for (Protocol protocol : rootDomainObject.getProtocols()) {
            if (protocol.getProtocolNumber().equals(protocolFactory.getProtocolNumber())) {
                return false;
            }
        }
        return true;
    }

    private void setError(HttpServletRequest request, String error, ActionMessage actionMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, actionMessage);
        saveMessages(request, actionMessages);
    }
}