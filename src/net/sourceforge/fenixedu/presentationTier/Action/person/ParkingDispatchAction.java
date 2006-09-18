package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactory;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class ParkingDispatchAction extends FenixDispatchAction {
    public ActionForward prepareParking(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        if (parkingParty == null) {
            parkingParty = (ParkingParty) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "CreateParkingParty", new Object[] { userView.getPerson() });
        }
        request.setAttribute("parkingParty", parkingParty);
        return mapping.findForward("prepareParking");
    }

    public ActionForward acceptRegulation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        if (parkingParty != null) {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "AcceptRegulation",
                    new Object[] { parkingParty });
        }
        return prepareParking(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditParking(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        // em 1º lugar ver se ele tem os dados pessoais necessários
        // if (userView.getPerson().getParkingParty() != null) {
        // request.setAttribute("parkingRequest", userView.getPerson().getParkingParty()
        // .getParkingRequests().iterator().next());
        // }

        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        request.setAttribute("parkingParty", parkingParty);

        DynaActionForm parkingForm = (DynaActionForm) actionForm;
        if (parkingParty.getFirstRequest().getFirstCarPropertyRegistryFileName() != null) {
            parkingForm.set("ownVehicle1", false);
        } else {
            parkingForm.set("ownVehicle1", true);
        }
        if (parkingParty.getFirstRequest().getSecondCarPropertyRegistryFileName() != null) {
            parkingForm.set("ownVehicle2", false);
        } else {
            parkingForm.set("ownVehicle2", true);
        }

        return mapping.findForward("editParkingRequest");
    }

    public ActionForward editParkingRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ParkingRequestFactoryEditor parkingRequestFactoryEditor = (ParkingRequestFactoryEditor) getFactoryObject();
        DynaActionForm parkingForm = (DynaActionForm) actionForm;

        if (validDataFirstCar(parkingRequestFactoryEditor, request)) {
            return mapping.getInputForward();
        }

        ajustParkingRequest(parkingForm, parkingRequestFactoryEditor);
        executeService(request, "ExecuteFactoryMethod", new Object[] { parkingRequestFactoryEditor });
        return prepareParking(mapping, actionForm, request, response);
    }

    public ActionForward downloadParkingRegulamentation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition",
                    "attachment; filename=RegulamentoEstacionamento.pdf");
            DataOutputStream dataOut = new DataOutputStream(response.getOutputStream());
            dataOut.write(getParkingRegulationDocument("Reg.%20Acesso%20Parques%20Estacion.pdf"));
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    private static byte[] getParkingRegulationDocument(String documentName) throws HttpException,
            IOException {
        final String host = "cd.ist.utl.pt";
        final int port = 80;

        final HttpClient httpClient = new HttpClient();
        final Protocol protocol = new Protocol(host, new DefaultProtocolSocketFactory(), port);
        httpClient.getHostConfiguration().setHost(host, port, protocol);

        final GetMethod getMethod = new GetMethod();
        getMethod.setFollowRedirects(false);
        getMethod.setPath("http://cd.ist.utl.pt/documentos/" + documentName);
        httpClient.executeMethod(getMethod);
        return getMethod.getResponseBody();
    }
    private boolean validDataFirstCar(ParkingRequestFactory parkingRequestFactory,
            HttpServletRequest request) {
        ActionMessages actionMessages = new ActionMessages();
        boolean result = false;
        if (parkingRequestFactory.getFirstCarMake() == null
                || parkingRequestFactory.getFirstCarMake().trim().equals("")) {
            actionMessages.add("firstCarMake", new ActionMessage("error.requiredField"));
            result = true;
        }
        if (parkingRequestFactory.getFirstCarPlateNumber() == null
                || parkingRequestFactory.getFirstCarPlateNumber().trim().equals("")) {
            actionMessages.add("firstCarPlateNumber", new ActionMessage("error.requiredField"));
            result = true;
        }
        saveMessages(request, actionMessages);
        return result;
    }

    public ActionForward createParkingRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ParkingRequestFactoryCreator parkingRequestFactoryCreator = (ParkingRequestFactoryCreator) getFactoryObject();
        DynaActionForm parkingForm = (DynaActionForm) actionForm;
        if (validDataFirstCar(parkingRequestFactoryCreator, request)) {
            return mapping.getInputForward();
        }
        ajustParkingRequest(parkingForm, parkingRequestFactoryCreator);
        executeService(request, "ExecuteFactoryMethod", new Object[] { parkingRequestFactoryCreator });
        return prepareParking(mapping, actionForm, request, response);
    }

    private void ajustParkingRequest(DynaActionForm parkingForm,
            ParkingRequestFactory parkingRequestFactory) {
        if (getElement(parkingForm, "driverLicense")) {
            parkingRequestFactory.setDriverLicenseFileName(null);
            parkingRequestFactory.setDriverLicenseInputStream(null);
        }
        if (getElement(parkingForm, "registry1")) {
            parkingRequestFactory.setFirstCarPropertyRegistryFileName(null);
            parkingRequestFactory.setFirstCarPropertyRegistryInputStream(null);
        }
        if (getElement(parkingForm, "insurance1")) {
            parkingRequestFactory.setFirstInsuranceFileName(null);
            parkingRequestFactory.setFirstInsuranceInputStream(null);
        }

        if (getElement(parkingForm, "registry2")) {
            parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
            parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
        }
        if (getElement(parkingForm, "insurance2")) {
            parkingRequestFactory.setSecondInsuranceFileName(null);
            parkingRequestFactory.setSecondInsuranceInputStream(null);
        }

        if (!getElement(parkingForm, "ownVehicle1")) {
            if (getElement(parkingForm, "Id1")) {
                parkingRequestFactory.setFirstCarOwnerIdFileName(null);
                parkingRequestFactory.setFirstCarOwnerIdInputStream(null);
            }
            if (getElement(parkingForm, "declaration1")) {
                parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
            }
        } else {
            parkingRequestFactory.setFirstCarOwnerIdFileName(null);
            parkingRequestFactory.setFirstCarOwnerIdInputStream(null);
            parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
            parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
        }
        if (!getElement(parkingForm, "ownVehicle2")) {
            if (getElement(parkingForm, "Id2")) {
                parkingRequestFactory.setSecondCarOwnerIdFileName(null);
                parkingRequestFactory.setSecondCarOwnerIdInputStream(null);
            }
            if (getElement(parkingForm, "declaration2")) {
                parkingRequestFactory.setSecondDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setSecondDeclarationAuthorizationInputStream(null);
            }
        } else {
            parkingRequestFactory.setSecondCarOwnerIdFileName(null);
            parkingRequestFactory.setSecondCarOwnerIdInputStream(null);
            parkingRequestFactory.setSecondDeclarationAuthorizationFileName(null);
            parkingRequestFactory.setSecondDeclarationAuthorizationInputStream(null);
        }
    }

    private boolean getElement(DynaActionForm parkingForm, String elementName) {
        if (parkingForm.get(elementName) != null) {
            return ((Boolean) parkingForm.get(elementName)).booleanValue();
        }
        return false;
    }
}