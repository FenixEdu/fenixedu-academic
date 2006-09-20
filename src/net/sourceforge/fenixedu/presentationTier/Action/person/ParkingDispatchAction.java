package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.parking.DocumentDeliveryType;
import net.sourceforge.fenixedu.domain.parking.ParkingFile;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactory;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.httpclient.HttpException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.utl.ist.fenix.tools.util.FileUtils;

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
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        request.setAttribute("parkingParty", parkingParty);

        if (parkingParty.getParkingRequestsSet().isEmpty()) {
            if (request.getAttribute("parkingRequestFactoryCreator") == null) {
                request.setAttribute("parkingRequestFactoryCreator", parkingParty
                        .getParkingRequestFactoryCreator());
            }
        } else {
            if (request.getAttribute("parkingRequestFactoryEditor") == null) {
                request.setAttribute("parkingRequestFactoryEditor", parkingParty.getFirstRequest()
                        .getParkingRequestFactoryEditor());
            }
        }

        prepareRadioButtonsForm(actionForm, parkingParty);

        return mapping.findForward("editParkingRequest");
    }

    private void prepareRadioButtonsForm(ActionForm actionForm, ParkingParty parkingParty) {
        DynaActionForm parkingForm = (DynaActionForm) actionForm;
        ParkingRequest parkingRequest = parkingParty.getFirstRequest();
        if (parkingForm.get("ownVehicle1") == null) {
            if (parkingRequest != null
                    && parkingParty.getFirstRequest().getFirstCarPropertyRegistryFileName() != null) {
                parkingForm.set("ownVehicle1", false);
            } else {
                parkingForm.set("ownVehicle1", true);
            }
        }
        if (parkingForm.get("ownVehicle2") == null) {
            if (parkingRequest != null
                    && parkingParty.getFirstRequest().getSecondCarPropertyRegistryFileName() != null) {
                parkingForm.set("ownVehicle2", false);
            } else {
                parkingForm.set("ownVehicle2", true);
            }
        }
        if (parkingForm.get("vehicle2") == null) {
            if (parkingRequest != null
                    && (parkingParty.getSecondCarMake() != null && parkingParty.getSecondCarMake()
                            .length() != 0)) {
                parkingForm.set("vehicle2", true);
            } else {
                parkingForm.set("vehicle2", false);
            }
        }
    }

    public ActionForward editParkingRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ParkingRequestFactoryEditor parkingRequestFactoryEditor = (ParkingRequestFactoryEditor) getFactoryObject();
        request.setAttribute("parkingRequestFactoryEditor", parkingRequestFactoryEditor);
        DynaActionForm parkingForm = (DynaActionForm) actionForm;

        if (!checkRequestFields(parkingRequestFactoryEditor, request, parkingForm)) {
            RenderUtils.invalidateViewState();
            return prepareEditParking(mapping, actionForm, request, response);
        }

        ajustParkingRequest(parkingForm, parkingRequestFactoryEditor);

        if (!areFileNamesValid(parkingRequestFactoryEditor, request)) {
            RenderUtils.invalidateViewState();
            return prepareEditParking(mapping, actionForm, request, response);
        }

        executeService(request, "ExecuteFactoryMethod", new Object[] { parkingRequestFactoryEditor });
        return prepareParking(mapping, actionForm, request, response);
    }

    private boolean areFileNamesValid(ParkingRequestFactory parkingRequestFactory,
            HttpServletRequest request) {
        ActionMessages actionMessages = getActionMessages(request);
        boolean result = true;
        if (parkingRequestFactory.getDriverLicenseInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getDriverLicenseFileName())) {
                actionMessages.add("driverLicenseMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setDriverLicenseFileName(null);
                parkingRequestFactory.setDriverLicenseInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getDriverLicenseFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("driverLicenseMessage", new ActionMessage("error.file.size"));
                parkingRequestFactory.setDriverLicenseFileName(null);
                parkingRequestFactory.setDriverLicenseInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getFirstCarOwnerIdInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getFirstCarOwnerIdFileName())) {
                actionMessages.add("firstCarOwnerIdMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setFirstCarOwnerIdFileName(null);
                parkingRequestFactory.setFirstCarOwnerIdInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getFirstCarOwnerIdFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("firstCarOwnerIdMessage", new ActionMessage("error.file.size"));
                parkingRequestFactory.setFirstCarOwnerIdFileName(null);
                parkingRequestFactory.setFirstCarOwnerIdInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getFirstCarPropertyRegistryInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getFirstCarPropertyRegistryFileName())) {
                actionMessages.add("firstCarPropertyRegistryMessage", new ActionMessage(
                        "error.file.extension"));
                parkingRequestFactory.setFirstCarPropertyRegistryFileName(null);
                parkingRequestFactory.setFirstCarPropertyRegistryInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getFirstCarPropertyRegistryFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("firstCarPropertyRegistryMessage", new ActionMessage(
                        "error.file.size"));
                parkingRequestFactory.setFirstCarPropertyRegistryFileName(null);
                parkingRequestFactory.setFirstCarPropertyRegistryInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getFirstDeclarationAuthorizationInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getFirstDeclarationAuthorizationFileName())) {
                actionMessages.add("firstDeclarationAuthorizationMessage", new ActionMessage(
                        "error.file.extension"));
                parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getFirstDeclarationAuthorizationFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("firstDeclarationAuthorizationMessage", new ActionMessage(
                        "error.file.size"));
                parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getFirstInsuranceInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getFirstInsuranceFileName())) {
                actionMessages.add("firstInsuranceMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setFirstInsuranceFileName(null);
                parkingRequestFactory.setFirstInsuranceInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getFirstInsuranceFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("firstInsuranceMessage", new ActionMessage("error.file.size"));
                parkingRequestFactory.setFirstInsuranceFileName(null);
                parkingRequestFactory.setFirstInsuranceInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getSecondCarOwnerIdInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getSecondCarOwnerIdFileName())) {
                actionMessages.add("secondCarOwnerIdMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setSecondCarOwnerIdFileName(null);
                parkingRequestFactory.setSecondCarOwnerIdInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getSecondCarOwnerIdFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("secondCarOwnerIdMessage", new ActionMessage("error.file.size"));
                parkingRequestFactory.setSecondCarOwnerIdFileName(null);
                parkingRequestFactory.setSecondCarOwnerIdInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getSecondCarPropertyRegistryInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getSecondCarPropertyRegistryFileName())) {
                actionMessages.add("secondCarPropertyRegistryMessage", new ActionMessage(
                        "error.file.extension"));
                parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
                parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getSecondCarPropertyRegistryFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("secondCarPropertyRegistryMessage", new ActionMessage(
                        "error.file.size"));
                parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
                parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getSecondDeclarationAuthorizationInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getSecondDeclarationAuthorizationFileName())) {
                actionMessages.add("secondDeclarationAuthorizationMessage", new ActionMessage(
                        "error.file.extension"));
                parkingRequestFactory.setSecondDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setSecondDeclarationAuthorizationInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getSecondDeclarationAuthorizationFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("secondDeclarationAuthorizationMessage", new ActionMessage(
                        "error.file.size"));
                parkingRequestFactory.setSecondDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setSecondDeclarationAuthorizationInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getSecondInsuranceInputStream() != null) {
            if (!validateFileName(parkingRequestFactory.getSecondInsuranceFileName())) {
                actionMessages.add("secondInsuranceMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setSecondInsuranceFileName(null);
                parkingRequestFactory.setSecondInsuranceInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getSecondInsuranceFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("secondInsuranceMessage", new ActionMessage("error.file.size"));
                parkingRequestFactory.setSecondInsuranceFileName(null);
                parkingRequestFactory.setSecondInsuranceInputStream(null);
                result = false;
            }
        }
        return result;
    }

    private boolean validateFileName(String fileName) {
        final String filenameT = fileName.trim().toLowerCase();
        return filenameT.endsWith(".gif") || filenameT.endsWith(".jpg") || filenameT.endsWith(".jpeg");
    }

    public ActionForward downloadAuthorizationDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return downloadFile(mapping, actionForm, request, response, "anexoIV.pdf");
    }

    public ActionForward downloadParkingRegulamentation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return downloadFile(mapping, actionForm, request, response, "RegulamentoEstacionamento.pdf");
    }

    private ActionForward downloadFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
        try {
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            DataOutputStream dataOut = new DataOutputStream(response.getOutputStream());
            String filePath = getServlet().getServletContext().getRealPath("/").concat(
                    "/person/parking/").concat(fileName);
            dataOut.write(getParkingRegulationDocument(filePath));
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    private static byte[] getParkingRegulationDocument(String documentPath) throws HttpException,
            IOException {
        return FileUtils.readFileInBytes(documentPath);
    }

    private boolean checkRequestFields(ParkingRequestFactory parkingRequestFactory,
            HttpServletRequest request, DynaActionForm parkingForm) {
        final boolean isStudent = isStudent(SessionUtils.getUserView(request));
        ActionMessages actionMessages = new ActionMessages();
        boolean result = true;

        if (parkingRequestFactory.getFirstCarMake() == null
                || parkingRequestFactory.getFirstCarMake().trim().length() == 0) {
            actionMessages.add("firstCarMakePT", new ActionMessage("error.requiredField"));
            result = false;
        }
        if (parkingRequestFactory.getFirstCarPlateNumber() == null
                || parkingRequestFactory.getFirstCarPlateNumber().trim().length() == 0) {
            actionMessages.add("firstCarPlateNumberPT", new ActionMessage("error.requiredField"));
            result = false;
        }

        if (hasNotDeliveryValue(parkingForm, "driverLicense")) {
            actionMessages.add("driverLicenseDeliveryMessage", new ActionMessage(
                    "error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "driverLicense"))
                && (parkingRequestFactory.getDriverLicenseFileName() == null && parkingRequestFactory
                        .getDriverLicenseFileName().length() == 0)) {
            actionMessages.add("driverLicenseMessage", new ActionMessage("error.requiredStudentField"));
            parkingForm.set("driverLicense", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        // FirstCar
        if (hasNotDeliveryValue(parkingForm, "registry1")) {
            actionMessages.add("firstCarPropertyRegistryDeliveryMessage", new ActionMessage(
                    "error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "registry1"))
                && (parkingRequestFactory.getFirstCarPropertyRegistryFileName() == null && parkingRequestFactory
                        .getFirstCarPropertyRegistryFileName().length() == 0)) {
            actionMessages.add("firstCarPropertyRegistryMessage", new ActionMessage(
                    "error.requiredStudentField"));
            parkingForm.set("registry1", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        if (hasNotDeliveryValue(parkingForm, "insurance1")) {
            actionMessages.add("firstInsuranceDeliveryMessage", new ActionMessage(
                    "error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "insurance1"))
                && (parkingRequestFactory.getFirstInsuranceFileName() == null && parkingRequestFactory
                        .getFirstInsuranceFileName().length() == 0)) {
            actionMessages.add("firstInsuranceMessage", new ActionMessage("error.requiredStudentField"));
            parkingForm.set("insurance1", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        if (!isOwner(parkingForm, "ownVehicle1")) {
            parkingForm.set("ownVehicle1", false);
            if (hasNotDeliveryValue(parkingForm, "Id1")) {
                actionMessages.add("firstCarOwnerIdDeliveryMessage", new ActionMessage(
                        "error.requiredDocumentDelivery"));
                result = false;
            } else if ((isStudent || isElectronicDelivery(parkingForm, "Id1"))
                    && (parkingRequestFactory.getFirstCarOwnerIdFileName() == null && parkingRequestFactory
                            .getFirstCarOwnerIdFileName().length() == 0)) {
                actionMessages.add("firstCarOwnerIdMessage", new ActionMessage(
                        "error.requiredStudentField"));
                parkingForm.set("Id1", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
                result = false;
            }
            if (hasNotDeliveryValue(parkingForm, "declaration1")) {
                actionMessages.add("firstDeclarationAuthorizationDeliveryMessage", new ActionMessage(
                        "error.requiredDocumentDelivery"));
                result = false;
            } else if ((isStudent || isElectronicDelivery(parkingForm, "declaration1"))
                    && (parkingRequestFactory.getFirstDeclarationAuthorizationFileName() == null && parkingRequestFactory
                            .getFirstDeclarationAuthorizationFileName().length() == 0)) {
                actionMessages.add("firstDeclarationAuthorizationMessage", new ActionMessage(
                        "error.requiredStudentField"));
                parkingForm.set("declaration1", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
                result = false;
            }
        }
        // SecondCar
        if (isOwner(parkingForm, "vehicle2")) {
            if ((parkingRequestFactory.getSecondCarMake() != null && parkingRequestFactory
                    .getSecondCarMake().trim().length() != 0)
                    || (parkingRequestFactory.getSecondCarPlateNumber() != null && parkingRequestFactory
                            .getSecondCarPlateNumber().trim().length() != 0)) {

                if (parkingRequestFactory.getSecondCarMake() == null
                        || parkingRequestFactory.getSecondCarMake().trim().length() == 0) {
                    actionMessages.add("secondCarMakePT", new ActionMessage("error.requiredField"));
                    result = false;
                }
                if (parkingRequestFactory.getSecondCarPlateNumber() == null
                        || parkingRequestFactory.getSecondCarPlateNumber().trim().length() == 0) {
                    actionMessages.add("secondCarPlateNumberPT",
                            new ActionMessage("error.requiredField"));
                    result = false;
                }

                if (hasNotDeliveryValue(parkingForm, "registry2")) {
                    actionMessages.add("secondCarPropertyRegistryDeliveryMessage", new ActionMessage(
                            "error.requiredDocumentDelivery"));
                    result = false;
                } else if ((isStudent || isElectronicDelivery(parkingForm, "registry2"))
                        && (parkingRequestFactory.getSecondCarPropertyRegistryFileName() == null && parkingRequestFactory
                                .getSecondCarPropertyRegistryFileName().length() == 0)) {
                    actionMessages.add("secondCarPropertyRegistryMessage", new ActionMessage(
                            "error.requiredStudentField"));
                    parkingForm.set("registry2", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
                    result = false;
                }
                if (hasNotDeliveryValue(parkingForm, "insurance2")) {
                    actionMessages.add("secondInsuranceDeliveryMessage", new ActionMessage(
                            "error.requiredDocumentDelivery"));
                    result = false;
                } else if ((isStudent || isElectronicDelivery(parkingForm, "insurance2"))
                        && (parkingRequestFactory.getSecondInsuranceFileName() == null && parkingRequestFactory
                                .getSecondInsuranceFileName().length() == 0)) {
                    actionMessages.add("secondInsuranceMessage", new ActionMessage(
                            "error.requiredStudentField"));
                    parkingForm.set("insurance2", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
                    result = false;
                }
                if (!isOwner(parkingForm, "ownVehicle2")) {
                    parkingForm.set("ownVehicle2", false);
                    if (hasNotDeliveryValue(parkingForm, "Id2")) {
                        actionMessages.add("secondCarOwnerIdDeliveryMessage", new ActionMessage(
                                "error.requiredDocumentDelivery"));
                        result = false;
                    } else if ((isStudent || isElectronicDelivery(parkingForm, "Id2"))
                            && (parkingRequestFactory.getSecondCarOwnerIdFileName() == null && parkingRequestFactory
                                    .getSecondCarOwnerIdFileName().length() == 0)) {
                        actionMessages.add("secondCarOwnerIdMessage", new ActionMessage(
                                "error.requiredStudentField"));
                        parkingForm.set("Id2", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
                        result = false;
                    }
                    if (hasNotDeliveryValue(parkingForm, "declaration2")) {
                        actionMessages.add("secondDeclarationAuthorizationDeliveryMessage",
                                new ActionMessage("error.requiredDocumentDelivery"));
                        result = false;
                    } else if ((isStudent || isElectronicDelivery(parkingForm, "declaration2"))
                            && (parkingRequestFactory.getSecondDeclarationAuthorizationFileName() == null && parkingRequestFactory
                                    .getSecondDeclarationAuthorizationFileName().length() == 0)) {
                        actionMessages.add("secondDeclarationAuthorizationMessage", new ActionMessage(
                                "error.requiredStudentField"));
                        parkingForm.set("declaration2", DocumentDeliveryType.ELECTRONIC_DELIVERY.name());
                        result = false;
                    }
                }
            }
        }

        saveMessages(request, actionMessages);
        return result;
    }

    public ActionForward createParkingRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ParkingRequestFactoryCreator parkingRequestFactoryCreator = (ParkingRequestFactoryCreator) getFactoryObject();
        request.setAttribute("parkingRequestFactoryCreator", parkingRequestFactoryCreator);
        DynaActionForm parkingForm = (DynaActionForm) actionForm;
        if (!checkRequestFields(parkingRequestFactoryCreator, request, parkingForm)) {
            RenderUtils.invalidateViewState();
            return mapping.getInputForward();
        }
        ajustParkingRequest(parkingForm, parkingRequestFactoryCreator);

        if (!areFileNamesValid(parkingRequestFactoryCreator, request)) {
            RenderUtils.invalidateViewState();
            return mapping.getInputForward();
        }
        executeService(request, "ExecuteFactoryMethod", new Object[] { parkingRequestFactoryCreator });
        return prepareParking(mapping, actionForm, request, response);
    }

    private void ajustParkingRequest(DynaActionForm parkingForm,
            ParkingRequestFactory parkingRequestFactory) {
        if (!isElectronicDelivery(parkingForm, "driverLicense")) {
            parkingRequestFactory.setDriverLicenseFileName(null);
            parkingRequestFactory.setDriverLicenseInputStream(null);
        }
        if (!isElectronicDelivery(parkingForm, "registry1")) {
            parkingRequestFactory.setFirstCarPropertyRegistryFileName(null);
            parkingRequestFactory.setFirstCarPropertyRegistryInputStream(null);
        }
        if (!isElectronicDelivery(parkingForm, "insurance1")) {
            parkingRequestFactory.setFirstInsuranceFileName(null);
            parkingRequestFactory.setFirstInsuranceInputStream(null);
        }

        if (!isElectronicDelivery(parkingForm, "registry2")) {
            parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
            parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
        }
        if (!isElectronicDelivery(parkingForm, "insurance2")) {
            parkingRequestFactory.setSecondInsuranceFileName(null);
            parkingRequestFactory.setSecondInsuranceInputStream(null);
        }

        if (!isOwner(parkingForm, "ownVehicle1")) {
            if (!isElectronicDelivery(parkingForm, "Id1")) {
                parkingRequestFactory.setFirstCarOwnerIdFileName(null);
                parkingRequestFactory.setFirstCarOwnerIdInputStream(null);
            }
            if (!isElectronicDelivery(parkingForm, "declaration1")) {
                parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
            }
        } else {
            parkingRequestFactory.setFirstCarOwnerIdFileName(null);
            parkingRequestFactory.setFirstCarOwnerIdInputStream(null);
            parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
            parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
        }
        if (!isOwner(parkingForm, "ownVehicle2")) {
            if (!isElectronicDelivery(parkingForm, "Id2")) {
                parkingRequestFactory.setSecondCarOwnerIdFileName(null);
                parkingRequestFactory.setSecondCarOwnerIdInputStream(null);
            }
            if (!isElectronicDelivery(parkingForm, "declaration2")) {
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

    private boolean isOwner(DynaActionForm parkingForm, String elementName) {
        final Boolean elementValue = (Boolean) parkingForm.get(elementName);
        return elementValue != null && elementValue.booleanValue();
    }

    private boolean isElectronicDelivery(DynaActionForm parkingForm, String elementName) {
        String element = parkingForm.getString(elementName);
        return element != null && element.length() != 0
                && DocumentDeliveryType.valueOf(element) == DocumentDeliveryType.ELECTRONIC_DELIVERY;
    }

    private boolean hasNotDeliveryValue(DynaActionForm parkingForm, String elementName) {
        String element = parkingForm.getString(elementName);
        return element == null || element.length() == 0;
    }

    private boolean isStudent(IUserView userView) {
        return (userView.getPerson().getTeacher() != null || userView.getPerson().getEmployee() != null) ? false
                : true;
    }
}