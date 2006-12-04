package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.parking.DocumentDeliveryType;
import net.sourceforge.fenixedu.domain.parking.ParkingDocumentState;
import net.sourceforge.fenixedu.domain.parking.ParkingFile;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactory;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryEditor;
import net.sourceforge.fenixedu.domain.person.RoleType;
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
import org.joda.time.DateTime;

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

        ParkingRequestFactory parkingRequestFactory = null;
        if (parkingParty.getParkingRequestsSet().isEmpty()) {
            if (request.getAttribute("parkingRequestFactoryCreator") == null) {
                parkingRequestFactory = parkingParty.getParkingRequestFactoryCreator();
                request.setAttribute("parkingRequestFactoryCreator", parkingRequestFactory);
            }
        } else {
            ParkingRequestFactoryEditor parkingRequestFactoryEditor = (ParkingRequestFactoryEditor) request
                    .getAttribute("parkingRequestFactoryEditor");
            if (parkingRequestFactoryEditor == null) {
                parkingRequestFactoryEditor = parkingParty.getFirstRequest()
                        .getParkingRequestFactoryEditor();
                parkingRequestFactory = parkingRequestFactoryEditor;
                request.setAttribute("parkingRequestFactoryEditor", parkingRequestFactoryEditor);
                prepareRadioButtonsDocuments((DynaActionForm) actionForm, parkingRequestFactoryEditor);
            }
            ParkingRequest parkingRequest = parkingParty.getFirstRequest();
            List<RoleType> roles = parkingParty.getSubmitAsRoles();
            if (roles.contains(RoleType.STUDENT) || roles.contains(RoleType.GRANT_OWNER)) {
                if (!parkingRequest.getLimitlessAccessCard()) {
                    DateTime startDatePeriod = new DateTime(2006, 10, 14, 0, 0, 0, 0); //start period for students and grant owners
                    if (parkingRequest.getCreationDate().isBefore(startDatePeriod)) {
                        request.setAttribute("allowToChoose","true");
                    }
                }
            }
        }

        prepareRadioButtonsForm(actionForm, parkingRequestFactory);
        if (parkingParty.isStudent()) {
            request.setAttribute("student", true);
        }

        return mapping.findForward("editParkingRequest");
    }

    private void prepareRadioButtonsDocuments(DynaActionForm actionForm,
            ParkingRequestFactoryEditor parkingRequestFactoryEditor) {
        if (parkingRequestFactoryEditor.getDriverLicenseDeliveryType() != null) {
            actionForm.set("driverLicense", parkingRequestFactoryEditor.getDriverLicenseDeliveryType()
                    .name());
        }
        if (parkingRequestFactoryEditor.getFirstCarPropertyRegistryDeliveryType() != null) {
            actionForm.set("registry1", parkingRequestFactoryEditor
                    .getFirstCarPropertyRegistryDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getFirstCarOwnerIdDeliveryType() != null) {
            actionForm.set("Id1", parkingRequestFactoryEditor.getFirstCarOwnerIdDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getFirstCarDeclarationDeliveryType() != null) {
            actionForm.set("declaration1", parkingRequestFactoryEditor
                    .getFirstCarDeclarationDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarPropertyRegistryDeliveryType() != null) {
            actionForm.set("registry2", parkingRequestFactoryEditor
                    .getSecondCarPropertyRegistryDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarOwnerIdDeliveryType() != null) {
            actionForm.set("Id2", parkingRequestFactoryEditor.getSecondCarOwnerIdDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarDeclarationDeliveryType() != null) {
            actionForm.set("declaration2", parkingRequestFactoryEditor
                    .getSecondCarDeclarationDeliveryType().name());
        }
    }

    private void prepareRadioButtonsForm(ActionForm actionForm, ParkingRequestFactory parkingRequestFactory) {
        DynaActionForm parkingForm = (DynaActionForm) actionForm;
        if (parkingForm.get("ownVehicle1") == null) {
            if (parkingRequestFactory != null
                    && (parkingRequestFactory.getFirstCarOwnerIdDeliveryType() != null || parkingRequestFactory
                            .getFirstCarDeclarationDeliveryType() != null)) {
                parkingForm.set("ownVehicle1", false);
            } else {
                parkingForm.set("ownVehicle1", true);
            }
        }
        if (parkingForm.get("ownVehicle2") == null) {
            if (parkingRequestFactory != null
                    && (parkingRequestFactory.getSecondCarOwnerIdDeliveryType() != null || parkingRequestFactory
                            .getSecondCarDeclarationDeliveryType() != null)) {
                parkingForm.set("ownVehicle2", false);
            } else {
                parkingForm.set("ownVehicle2", true);
            }
        }
        if (parkingForm.get("vehicle2") == null) {
            if (parkingRequestFactory != null
                    && (parkingRequestFactory.getSecondCarMake() != null && parkingRequestFactory.getSecondCarMake()
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
            parkingRequestFactory.setDriverLicenseDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "driverLicense")));
        }
        if (getElementValue(parkingForm, "registry1") != null
                && getElementValue(parkingForm, "registry1").length() != 0) {
            parkingRequestFactory.setFirstCarPropertyRegistryDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "registry1")));
        }
        if (getElementValue(parkingForm, "Id1") != null
                && getElementValue(parkingForm, "Id1").length() != 0) {
            parkingRequestFactory.setFirstCarOwnerIdDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "Id1")));
        }
        if (getElementValue(parkingForm, "declaration1") != null
                && getElementValue(parkingForm, "declaration1").length() != 0) {
            parkingRequestFactory.setFirstCarDeclarationDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "declaration1")));
        }
        if (getElementValue(parkingForm, "registry2") != null
                && getElementValue(parkingForm, "registry2").length() != 0) {
            parkingRequestFactory.setSecondCarPropertyRegistryDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "registry2")));
        }
        if (getElementValue(parkingForm, "Id2") != null
                && getElementValue(parkingForm, "Id2").length() != 0) {
            parkingRequestFactory.setSecondCarOwnerIdDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "Id2")));
        }
        if (getElementValue(parkingForm, "declaration2") != null
                && getElementValue(parkingForm, "declaration2").length() != 0) {
            parkingRequestFactory.setSecondCarDeclarationDeliveryType(DocumentDeliveryType
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
        return result;
    }

    private boolean validateFileName(String fileName) {
        final String filenameT = fileName.trim().toLowerCase();
        return filenameT.endsWith(".pdf") || filenameT.endsWith(".gif") || filenameT.endsWith(".jpg")
                || filenameT.endsWith(".jpeg");
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

        if (!isElectronicDelivery(parkingForm, "registry2")) {
            parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
            parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
        } else if (parkingRequestFactory.getParkingParty().isStudent()
                && !isOwner(parkingForm, "vehicle2")) {
            parkingForm.set("registry2", null);
            parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
            parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
            parkingRequestFactory.setSecondCarPropertyRegistryDeliveryType(null);
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
            parkingRequestFactory.setFirstCarOwnerIdDeliveryType(null);
            parkingForm.set("Id1", null);
            parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
            parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
            parkingRequestFactory.setFirstCarDeclarationDeliveryType(null);
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
            parkingRequestFactory.setSecondCarOwnerIdDeliveryType(null);
            parkingForm.set("Id2", null);
            parkingRequestFactory.setSecondDeclarationAuthorizationFileName(null);
            parkingRequestFactory.setSecondDeclarationAuthorizationInputStream(null);
            parkingRequestFactory.setSecondCarDeclarationDeliveryType(null);
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