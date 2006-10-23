package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.parking.ParkingDocumentState;
import net.sourceforge.fenixedu.domain.parking.ParkingFile;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
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

import pt.utl.ist.fenix.tools.file.FileManagerException;
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
        ParkingRequest parkingRequest = parkingParty.getFirstRequest();
        boolean canEdit = true;
        if (parkingRequest != null
                && (parkingRequest.getParkingRequestState() == ParkingRequestState.ACCEPTED || parkingRequest
                        .getParkingRequestState() == ParkingRequestState.REJECTED)) {
            canEdit = false;
        }
        request.setAttribute("canEdit", canEdit);
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
            HttpServletRequest request, HttpServletResponse response) {

        IUserView userView = SessionUtils.getUserView(request);
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        request.setAttribute("parkingParty", parkingParty);

        if (parkingParty.getParkingRequestsSet().isEmpty()) {
            if (request.getAttribute("parkingRequestFactoryCreator") == null) {
                request.setAttribute("parkingRequestFactoryCreator", parkingParty
                        .getParkingRequestFactoryCreator());
            }
        } else {
            ParkingRequestFactoryEditor parkingRequestFactoryEditor = (ParkingRequestFactoryEditor) request
                    .getAttribute("parkingRequestFactoryEditor");
            if (parkingRequestFactoryEditor == null) {
                parkingRequestFactoryEditor = parkingParty.getFirstRequest()
                        .getParkingRequestFactoryEditor();
                request.setAttribute("parkingRequestFactoryEditor", parkingRequestFactoryEditor);
                prepareRadioButtonsDocuments((DynaActionForm) actionForm, parkingRequestFactoryEditor);
            }
        }

        prepareRadioButtonsForm(actionForm, parkingParty);
        if (parkingParty.isStudent()) {
            request.setAttribute("student", true);
        }

        return mapping.findForward("editParkingRequest");
    }

    private void prepareRadioButtonsDocuments(DynaActionForm actionForm,
            ParkingRequestFactoryEditor parkingRequestFactoryEditor) {
        if (parkingRequestFactoryEditor.getDriverLicenseDocumentState() != null) {
            actionForm.set("driverLicense", parkingRequestFactoryEditor.getDriverLicenseDocumentState()
                    .name());
        }
        if (parkingRequestFactoryEditor.getFirstCarPropertyRegistryDocumentState() != null) {
            actionForm.set("registry1", parkingRequestFactoryEditor
                    .getFirstCarPropertyRegistryDocumentState().name());
        }
        if (parkingRequestFactoryEditor.getFirstCarInsuranceDocumentState() != null) {
            actionForm.set("insurance1", parkingRequestFactoryEditor.getFirstCarInsuranceDocumentState()
                    .name());
        }
        if (parkingRequestFactoryEditor.getFirstCarOwnerIdDocumentState() != null) {
            actionForm.set("Id1", parkingRequestFactoryEditor.getFirstCarOwnerIdDocumentState().name());
        }
        if (parkingRequestFactoryEditor.getFirstCarDeclarationDocumentState() != null) {
            actionForm.set("declaration1", parkingRequestFactoryEditor
                    .getFirstCarDeclarationDocumentState().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarPropertyRegistryDocumentState() != null) {
            actionForm.set("registry2", parkingRequestFactoryEditor
                    .getSecondCarPropertyRegistryDocumentState().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarInsuranceDocumentState() != null) {
            actionForm.set("insurance2", parkingRequestFactoryEditor
                    .getSecondCarInsuranceDocumentState().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarOwnerIdDocumentState() != null) {
            actionForm.set("Id2", parkingRequestFactoryEditor.getSecondCarOwnerIdDocumentState().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarDeclarationDocumentState() != null) {
            actionForm.set("declaration2", parkingRequestFactoryEditor
                    .getSecondCarDeclarationDocumentState().name());
        }
    }

    private void prepareRadioButtonsForm(ActionForm actionForm, ParkingParty parkingParty) {
        DynaActionForm parkingForm = (DynaActionForm) actionForm;
        ParkingRequest parkingRequest = parkingParty.getFirstRequest();
        if (parkingForm.get("ownVehicle1") == null) {
            if (parkingRequest != null
                    && (parkingRequest.getFirstCarOwnerIdDocumentState() != null || parkingRequest
                            .getFirstCarDeclarationDocumentState() != null)) {
                parkingForm.set("ownVehicle1", false);
            } else {
                parkingForm.set("ownVehicle1", true);
            }
        }
        if (parkingForm.get("ownVehicle2") == null) {
            if (parkingRequest != null
                    && (parkingRequest.getSecondCarOwnerIdDocumentState() != null || parkingRequest
                            .getSecondCarDeclarationDocumentState() != null)) {
                parkingForm.set("ownVehicle2", false);
            } else {
                parkingForm.set("ownVehicle2", true);
            }
        }
        if (parkingForm.get("vehicle2") == null) {
            if (parkingRequest != null
                    && (parkingRequest.getSecondCarMake() != null && parkingRequest.getSecondCarMake()
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
        if (parkingRequestFactoryEditor == null) {
            ActionMessages actionMessages = getMessages(request);
            actionMessages.add("timeout", new ActionMessage("error.timeout"));
            saveMessages(request, actionMessages);
            return prepareEditParking(mapping, actionForm, request, response);
        }

        parkingRequestFactoryEditor.saveInputStreams();
        request.setAttribute("parkingRequestFactoryEditor", parkingRequestFactoryEditor);
        DynaActionForm parkingForm = (DynaActionForm) actionForm;

        if (!checkRequestFields(parkingRequestFactoryEditor, request, parkingForm)) {
            RenderUtils.invalidateViewState();
            return prepareEditParking(mapping, actionForm, request, response);
        }

        adjustParkingRequest(parkingForm, parkingRequestFactoryEditor);

        if (!areFileNamesValid(parkingRequestFactoryEditor, request)) {
            RenderUtils.invalidateViewState();
            return prepareEditParking(mapping, actionForm, request, response);
        }

        fillInDocumentStates(parkingForm, parkingRequestFactoryEditor);
        try {
            executeService(request, "ExecuteFactoryMethod", new Object[] { parkingRequestFactoryEditor });
        } catch (FileManagerException ex) {
            ActionMessages actionMessages = getActionMessages(request);
            actionMessages.add("fileError", new ActionMessage(ex.getKey(), ex.getArgs()));
            saveMessages(request, actionMessages);
            RenderUtils.invalidateViewState();
            return mapping.getInputForward();
        }
        return prepareParking(mapping, actionForm, request, response);
    }

    private void fillInDocumentStates(DynaActionForm parkingForm,
            ParkingRequestFactory parkingRequestFactory) {
        if (getElementValue(parkingForm, "driverLicense") != null
                && getElementValue(parkingForm, "driverLicense").length() != 0) {
            parkingRequestFactory.setDriverLicenseDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "driverLicense")));
        }
        if (getElementValue(parkingForm, "registry1") != null
                && getElementValue(parkingForm, "registry1").length() != 0) {
            parkingRequestFactory.setFirstCarPropertyRegistryDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "registry1")));
        }
        if (getElementValue(parkingForm, "insurance1") != null
                && getElementValue(parkingForm, "insurance1").length() != 0) {
            parkingRequestFactory.setFirstCarInsuranceDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "insurance1")));
        }
        if (getElementValue(parkingForm, "Id1") != null
                && getElementValue(parkingForm, "Id1").length() != 0) {
            parkingRequestFactory.setFirstCarOwnerIdDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "Id1")));
        }
        if (getElementValue(parkingForm, "declaration1") != null
                && getElementValue(parkingForm, "declaration1").length() != 0) {
            parkingRequestFactory.setFirstCarDeclarationDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "declaration1")));
        }
        if (getElementValue(parkingForm, "registry2") != null
                && getElementValue(parkingForm, "registry2").length() != 0) {
            parkingRequestFactory.setSecondCarPropertyRegistryDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "registry2")));
        }
        if (getElementValue(parkingForm, "insurance2") != null
                && getElementValue(parkingForm, "insurance2").length() != 0) {
            parkingRequestFactory.setSecondCarInsuranceDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "insurance2")));
        }
        if (getElementValue(parkingForm, "Id2") != null
                && getElementValue(parkingForm, "Id2").length() != 0) {
            parkingRequestFactory.setSecondCarOwnerIdDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "Id2")));
        }
        if (getElementValue(parkingForm, "declaration2") != null
                && getElementValue(parkingForm, "declaration2").length() != 0) {
            parkingRequestFactory.setSecondCarDeclarationDocumentState(ParkingDocumentState
                    .valueOf(getElementValue(parkingForm, "declaration2")));
        }

    }

    private boolean areFileNamesValid(ParkingRequestFactory parkingRequestFactory,
            HttpServletRequest request) {
        ActionMessages actionMessages = getActionMessages(request);
        boolean result = true;
        if (parkingRequestFactory.getDriverLicenseFileName() != null) {
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
        if (parkingRequestFactory.getFirstCarOwnerIdFileName() != null) {
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
        if (parkingRequestFactory.getFirstCarPropertyRegistryFileName() != null) {
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
        if (parkingRequestFactory.getFirstDeclarationAuthorizationFileName() != null) {
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
        if (parkingRequestFactory.getFirstInsuranceFileName() != null) {
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
        if (parkingRequestFactory.getSecondCarOwnerIdFileName() != null) {
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
        if (parkingRequestFactory.getSecondCarPropertyRegistryFileName() != null) {
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
        if (parkingRequestFactory.getSecondDeclarationAuthorizationFileName() != null) {
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
        if (parkingRequestFactory.getSecondInsuranceFileName() != null) {
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
        return filenameT.endsWith(".pdf") || filenameT.endsWith(".gif") || filenameT.endsWith(".jpg") || filenameT.endsWith(".jpeg");
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
        } else if (parkingRequestFactory.getFirstCarMake().length() > 20) {
            actionMessages.add("firstCarMakePT", new ActionMessage("error.maxLengthExceeded", "20"));
            result = false;
        }
        if (parkingRequestFactory.getFirstCarPlateNumber() == null
                || parkingRequestFactory.getFirstCarPlateNumber().trim().length() == 0) {
            actionMessages.add("firstCarPlateNumberPT", new ActionMessage("error.requiredField"));
            result = false;
        } else if (parkingRequestFactory.getFirstCarPlateNumber().length() > 10) {
            actionMessages.add("firstCarPlateNumberPT", new ActionMessage("error.maxLengthExceeded",
                    "10"));
            result = false;
        }

        if (hasNotDeliveryValue(parkingForm, "driverLicense")) {
            actionMessages.add("driverLicenseDeliveryMessage", new ActionMessage(
                    "error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "driverLicense"))
                && (parkingRequestFactory.getDriverLicenseFileName() == null || parkingRequestFactory
                        .getDriverLicenseFileName().length() == 0)) {
            actionMessages.add("driverLicenseMessage", new ActionMessage("error.requiredStudentField"));
            parkingForm.set("driverLicense", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        // FirstCar
        if (hasNotDeliveryValue(parkingForm, "registry1")) {
            actionMessages.add("firstCarPropertyRegistryDeliveryMessage", new ActionMessage(
                    "error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "registry1"))
                && (parkingRequestFactory.getFirstCarPropertyRegistryFileName() == null || parkingRequestFactory
                        .getFirstCarPropertyRegistryFileName().length() == 0)) {
            actionMessages.add("firstCarPropertyRegistryMessage", new ActionMessage(
                    "error.requiredStudentField"));
            parkingForm.set("registry1", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        if (hasNotDeliveryValue(parkingForm, "insurance1")) {
            actionMessages.add("firstInsuranceDeliveryMessage", new ActionMessage(
                    "error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "insurance1"))
                && (parkingRequestFactory.getFirstInsuranceFileName() == null || parkingRequestFactory
                        .getFirstInsuranceFileName().length() == 0)) {
            actionMessages.add("firstInsuranceMessage", new ActionMessage("error.requiredStudentField"));
            parkingForm.set("insurance1", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        if (!isOwner(parkingForm, "ownVehicle1")) {
            parkingForm.set("ownVehicle1", false);
            if (hasNotDeliveryValue(parkingForm, "Id1")) {
                actionMessages.add("firstCarOwnerIdDeliveryMessage", new ActionMessage(
                        "error.requiredDocumentDelivery"));
                result = false;
            } else if ((isStudent || isElectronicDelivery(parkingForm, "Id1"))
                    && (parkingRequestFactory.getFirstCarOwnerIdFileName() == null || parkingRequestFactory
                            .getFirstCarOwnerIdFileName().length() == 0)) {
                actionMessages.add("firstCarOwnerIdMessage", new ActionMessage(
                        "error.requiredStudentField"));
                parkingForm.set("Id1", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
                result = false;
            }
            if (hasNotDeliveryValue(parkingForm, "declaration1")) {
                actionMessages.add("firstDeclarationAuthorizationDeliveryMessage", new ActionMessage(
                        "error.requiredDocumentDelivery"));
                result = false;
            } else if ((isStudent || isElectronicDelivery(parkingForm, "declaration1"))
                    && (parkingRequestFactory.getFirstDeclarationAuthorizationFileName() == null || parkingRequestFactory
                            .getFirstDeclarationAuthorizationFileName().length() == 0)) {
                actionMessages.add("firstDeclarationAuthorizationMessage", new ActionMessage(
                        "error.requiredStudentField"));
                parkingForm.set("declaration1", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
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
                } else if (parkingRequestFactory.getSecondCarMake().length() > 20) {
                    actionMessages.add("secondCarMakePT", new ActionMessage("error.maxLengthExceeded",
                            "20"));
                    result = false;
                }
                if (parkingRequestFactory.getSecondCarPlateNumber() == null
                        || parkingRequestFactory.getSecondCarPlateNumber().trim().length() == 0) {
                    actionMessages.add("secondCarPlateNumberPT",
                            new ActionMessage("error.requiredField"));
                    result = false;
                } else if (parkingRequestFactory.getSecondCarPlateNumber().length() > 10) {
                    actionMessages.add("secondCarPlateNumberPT", new ActionMessage(
                            "error.maxLengthExceeded", "10"));
                    result = false;
                }

                if (hasNotDeliveryValue(parkingForm, "registry2")) {
                    actionMessages.add("secondCarPropertyRegistryDeliveryMessage", new ActionMessage(
                            "error.requiredDocumentDelivery"));
                    result = false;
                } else if ((isStudent || isElectronicDelivery(parkingForm, "registry2"))
                        && (parkingRequestFactory.getSecondCarPropertyRegistryFileName() == null || parkingRequestFactory
                                .getSecondCarPropertyRegistryFileName().length() == 0)) {
                    actionMessages.add("secondCarPropertyRegistryMessage", new ActionMessage(
                            "error.requiredStudentField"));
                    parkingForm.set("registry2", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
                    result = false;
                }
                if (hasNotDeliveryValue(parkingForm, "insurance2")) {
                    actionMessages.add("secondInsuranceDeliveryMessage", new ActionMessage(
                            "error.requiredDocumentDelivery"));
                    result = false;
                } else if ((isStudent || isElectronicDelivery(parkingForm, "insurance2"))
                        && (parkingRequestFactory.getSecondInsuranceFileName() == null || parkingRequestFactory
                                .getSecondInsuranceFileName().length() == 0)) {
                    actionMessages.add("secondInsuranceMessage", new ActionMessage(
                            "error.requiredStudentField"));
                    parkingForm.set("insurance2", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
                    result = false;
                }
                if (!isOwner(parkingForm, "ownVehicle2")) {
                    parkingForm.set("ownVehicle2", false);
                    if (hasNotDeliveryValue(parkingForm, "Id2")) {
                        actionMessages.add("secondCarOwnerIdDeliveryMessage", new ActionMessage(
                                "error.requiredDocumentDelivery"));
                        result = false;
                    } else if ((isStudent || isElectronicDelivery(parkingForm, "Id2"))
                            && (parkingRequestFactory.getSecondCarOwnerIdFileName() == null || parkingRequestFactory
                                    .getSecondCarOwnerIdFileName().length() == 0)) {
                        actionMessages.add("secondCarOwnerIdMessage", new ActionMessage(
                                "error.requiredStudentField"));
                        parkingForm.set("Id2", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
                        result = false;
                    }
                    if (hasNotDeliveryValue(parkingForm, "declaration2")) {
                        actionMessages.add("secondDeclarationAuthorizationDeliveryMessage",
                                new ActionMessage("error.requiredDocumentDelivery"));
                        result = false;
                    } else if ((isStudent || isElectronicDelivery(parkingForm, "declaration2"))
                            && (parkingRequestFactory.getSecondDeclarationAuthorizationFileName() == null || parkingRequestFactory
                                    .getSecondDeclarationAuthorizationFileName().length() == 0)) {
                        actionMessages.add("secondDeclarationAuthorizationMessage", new ActionMessage(
                                "error.requiredStudentField"));
                        parkingForm.set("declaration2", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
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
        if (parkingRequestFactoryCreator == null) {
            ActionMessages actionMessages = getMessages(request);
            actionMessages.add("timeout", new ActionMessage("error.timeout"));
            saveMessages(request, actionMessages);
            return prepareEditParking(mapping, actionForm, request, response);
        }
        parkingRequestFactoryCreator.saveInputStreams();
        request.setAttribute("parkingRequestFactoryCreator", parkingRequestFactoryCreator);
        DynaActionForm parkingForm = (DynaActionForm) actionForm;
        if (!checkRequestFields(parkingRequestFactoryCreator, request, parkingForm)) {
            RenderUtils.invalidateViewState();
            return mapping.getInputForward();
        }
        adjustParkingRequest(parkingForm, parkingRequestFactoryCreator);

        if (!areFileNamesValid(parkingRequestFactoryCreator, request)) {
            RenderUtils.invalidateViewState();
            return mapping.getInputForward();
        }

        fillInDocumentStates(parkingForm, parkingRequestFactoryCreator);
        try {
            executeService(request, "ExecuteFactoryMethod",
                    new Object[] { parkingRequestFactoryCreator });
        } catch (FileManagerException ex) {
            ActionMessages actionMessages = getActionMessages(request);
            actionMessages.add("fileError", new ActionMessage(ex.getKey(), ex.getArgs()));
            saveMessages(request, actionMessages);
            RenderUtils.invalidateViewState();
            return mapping.getInputForward();
        }
        return prepareParking(mapping, actionForm, request, response);
    }

    private void adjustParkingRequest(DynaActionForm parkingForm,
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
        } else if (parkingRequestFactory.getParkingParty().isStudent()
                && !isOwner(parkingForm, "vehicle2")) {
            parkingForm.set("registry2", null);
            parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
            parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
            parkingRequestFactory.setSecondCarPropertyRegistryDocumentState(null);
        }
        if (!isElectronicDelivery(parkingForm, "insurance2")) {
            parkingRequestFactory.setSecondInsuranceFileName(null);
            parkingRequestFactory.setSecondInsuranceInputStream(null);
        } else if (parkingRequestFactory.getParkingParty().isStudent()
                && !isOwner(parkingForm, "vehicle2")) {
            parkingForm.set("insurance2", null);
            parkingRequestFactory.setSecondInsuranceFileName(null);
            parkingRequestFactory.setSecondInsuranceInputStream(null);
            parkingRequestFactory.setSecondCarInsuranceDocumentState(null);
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
            parkingRequestFactory.setFirstCarOwnerIdDocumentState(null);
            parkingForm.set("Id1", null);
            parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
            parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
            parkingRequestFactory.setFirstCarDeclarationDocumentState(null);
            parkingForm.set("declaration1", null);
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
            parkingRequestFactory.setSecondCarOwnerIdDocumentState(null);
            parkingForm.set("Id2", null);
            parkingRequestFactory.setSecondDeclarationAuthorizationFileName(null);
            parkingRequestFactory.setSecondDeclarationAuthorizationInputStream(null);
            parkingRequestFactory.setSecondCarDeclarationDocumentState(null);
            parkingForm.set("declaration2", null);
        }
        if (!isOwner(parkingForm, "vehicle2")) {
            parkingRequestFactory.setSecondCarMake(null);
            parkingRequestFactory.setSecondCarPlateNumber(null);
        }
    }

    private boolean isOwner(DynaActionForm parkingForm, String elementName) {
        final Boolean elementValue = (Boolean) parkingForm.get(elementName);
        return elementValue != null && elementValue.booleanValue();
    }

    private String getElementValue(DynaActionForm parkingForm, String elementName) {
        if (parkingForm.get(elementName) != null) {
            return parkingForm.getString(elementName);
        }
        return null;
    }

    private boolean isElectronicDelivery(DynaActionForm parkingForm, String elementName) {
        String element = parkingForm.getString(elementName);
        return element != null && element.length() != 0
                && ParkingDocumentState.valueOf(element) == ParkingDocumentState.ELECTRONIC_DELIVERY;
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