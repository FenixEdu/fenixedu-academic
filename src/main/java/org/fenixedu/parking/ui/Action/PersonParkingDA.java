/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of Fenix Parking.
 *
 * Fenix Parking is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fenix Parking is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.parking.ui.Action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ExecuteFactoryMethod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.person.PersonApplication.PersonalAreaApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.parking.domain.DocumentDeliveryType;
import org.fenixedu.parking.domain.ParkingDocumentState;
import org.fenixedu.parking.domain.ParkingFile;
import org.fenixedu.parking.domain.ParkingParty;
import org.fenixedu.parking.domain.ParkingRequest;
import org.fenixedu.parking.domain.ParkingRequest.ParkingRequestFactory;
import org.fenixedu.parking.domain.ParkingRequest.ParkingRequestFactoryCreator;
import org.fenixedu.parking.domain.ParkingRequest.ParkingRequestFactoryEditor;
import org.fenixedu.parking.domain.ParkingRequestPeriod;
import org.fenixedu.parking.domain.ParkingRequestState;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;

@StrutsFunctionality(app = PersonalAreaApp.class, path = "parking", titleKey = "label.parking", bundle = "ParkingResources")
@Mapping(module = "parkingManager", path = "/personParking", input = "/personParking.do?method=prepareEditParking&page=0",
        formBean = "personParkingForm", validate = false)
@Forwards({ @Forward(name = "prepareParking", path = "/person/parkingRequest.jsp"),
        @Forward(name = "editParkingRequest", path = "/person/editParkingRequest.jsp") })
public class PersonParkingDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareParking(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = Authenticate.getUser();
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        if (parkingParty == null) {
            parkingParty = createParkingParty(userView.getPerson());
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

    @Atomic
    private ParkingParty createParkingParty(Party party) {
        return new ParkingParty(party);
    }

    public ActionForward acceptRegulation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = Authenticate.getUser();
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        if (parkingParty != null) {
            acceptRegulation(parkingParty);
        }
        return prepareParking(mapping, actionForm, request, response);
    }

    @Atomic
    private void acceptRegulation(ParkingParty parkingParty) {
        parkingParty.setAcceptedRegulation(Boolean.TRUE);
    }

    public ActionForward prepareEditParking(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        User userView = Authenticate.getUser();
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        request.setAttribute("parkingParty", parkingParty);

        ParkingRequestFactory parkingRequestFactory = null;
        if (parkingParty.getParkingRequestsSet().isEmpty()) {
            if (request.getAttribute("parkingRequestFactoryCreator") == null) {
                parkingRequestFactory = parkingParty.getParkingRequestFactoryCreator();
                request.setAttribute("parkingRequestFactoryCreator", parkingRequestFactory);
            }
        } else {
            ParkingRequestFactoryEditor parkingRequestFactoryEditor =
                    (ParkingRequestFactoryEditor) request.getAttribute("parkingRequestFactoryEditor");
            if (parkingRequestFactoryEditor == null) {
                parkingRequestFactoryEditor = parkingParty.getFirstRequest().getParkingRequestFactoryEditor();
                parkingRequestFactory = parkingRequestFactoryEditor;
                request.setAttribute("parkingRequestFactoryEditor", parkingRequestFactoryEditor);
                prepareRadioButtonsDocuments((DynaActionForm) actionForm, parkingRequestFactoryEditor);
            }
        }

        if (parkingParty.hasRolesToRequestUnlimitedCard()) {
            if (ParkingRequestPeriod.getCurrentRequestPeriod() != null) {
                request.setAttribute("allowToChoose", "true");
            } else {
                request.setAttribute("periodExpired", "true");
            }
        }

        prepareRadioButtonsForm(actionForm, parkingRequestFactory);
        if (parkingParty.isStudent()) {
            request.setAttribute("student", true);
        }

        return mapping.findForward("editParkingRequest");
    }

    private void prepareRadioButtonsDocuments(DynaActionForm actionForm, ParkingRequestFactoryEditor parkingRequestFactoryEditor) {
        if (parkingRequestFactoryEditor.getDriverLicenseDeliveryType() != null) {
            actionForm.set("driverLicense", parkingRequestFactoryEditor.getDriverLicenseDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getFirstCarPropertyRegistryDeliveryType() != null) {
            actionForm.set("registry1", parkingRequestFactoryEditor.getFirstCarPropertyRegistryDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getFirstCarOwnerIdDeliveryType() != null) {
            actionForm.set("Id1", parkingRequestFactoryEditor.getFirstCarOwnerIdDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getFirstCarDeclarationDeliveryType() != null) {
            actionForm.set("declaration1", parkingRequestFactoryEditor.getFirstCarDeclarationDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarPropertyRegistryDeliveryType() != null) {
            actionForm.set("registry2", parkingRequestFactoryEditor.getSecondCarPropertyRegistryDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarOwnerIdDeliveryType() != null) {
            actionForm.set("Id2", parkingRequestFactoryEditor.getSecondCarOwnerIdDeliveryType().name());
        }
        if (parkingRequestFactoryEditor.getSecondCarDeclarationDeliveryType() != null) {
            actionForm.set("declaration2", parkingRequestFactoryEditor.getSecondCarDeclarationDeliveryType().name());
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
                    && (parkingRequestFactory.getSecondCarMake() != null && parkingRequestFactory.getSecondCarMake().length() != 0)) {
                parkingForm.set("vehicle2", true);
            } else {
                parkingForm.set("vehicle2", false);
            }
        }
    }

    public ActionForward editParkingRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

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
        ExecuteFactoryMethod.run(parkingRequestFactoryEditor);
        return prepareParking(mapping, actionForm, request, response);
    }

    private void fillInDocumentStates(DynaActionForm parkingForm, ParkingRequestFactory parkingRequestFactory) {
        if (getElementValue(parkingForm, "driverLicense") != null && getElementValue(parkingForm, "driverLicense").length() != 0) {
            parkingRequestFactory.setDriverLicenseDeliveryType(DocumentDeliveryType.valueOf(getElementValue(parkingForm,
                    "driverLicense")));
        }
        if (getElementValue(parkingForm, "registry1") != null && getElementValue(parkingForm, "registry1").length() != 0) {
            parkingRequestFactory.setFirstCarPropertyRegistryDeliveryType(DocumentDeliveryType.valueOf(getElementValue(
                    parkingForm, "registry1")));
        }
        if (getElementValue(parkingForm, "Id1") != null && getElementValue(parkingForm, "Id1").length() != 0) {
            parkingRequestFactory.setFirstCarOwnerIdDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "Id1")));
        }
        if (getElementValue(parkingForm, "declaration1") != null && getElementValue(parkingForm, "declaration1").length() != 0) {
            parkingRequestFactory.setFirstCarDeclarationDeliveryType(DocumentDeliveryType.valueOf(getElementValue(parkingForm,
                    "declaration1")));
        }
        if (getElementValue(parkingForm, "registry2") != null && getElementValue(parkingForm, "registry2").length() != 0) {
            parkingRequestFactory.setSecondCarPropertyRegistryDeliveryType(DocumentDeliveryType.valueOf(getElementValue(
                    parkingForm, "registry2")));
        }
        if (getElementValue(parkingForm, "Id2") != null && getElementValue(parkingForm, "Id2").length() != 0) {
            parkingRequestFactory.setSecondCarOwnerIdDeliveryType(DocumentDeliveryType
                    .valueOf(getElementValue(parkingForm, "Id2")));
        }
        if (getElementValue(parkingForm, "declaration2") != null && getElementValue(parkingForm, "declaration2").length() != 0) {
            parkingRequestFactory.setSecondCarDeclarationDeliveryType(DocumentDeliveryType.valueOf(getElementValue(parkingForm,
                    "declaration2")));
        }

    }

    private boolean areFileNamesValid(ParkingRequestFactory parkingRequestFactory, HttpServletRequest request) {
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
                actionMessages.add("firstCarPropertyRegistryMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setFirstCarPropertyRegistryFileName(null);
                parkingRequestFactory.setFirstCarPropertyRegistryInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getFirstCarPropertyRegistryFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("firstCarPropertyRegistryMessage", new ActionMessage("error.file.size"));
                parkingRequestFactory.setFirstCarPropertyRegistryFileName(null);
                parkingRequestFactory.setFirstCarPropertyRegistryInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getFirstDeclarationAuthorizationFileName() != null) {
            if (!validateFileName(parkingRequestFactory.getFirstDeclarationAuthorizationFileName())) {
                actionMessages.add("firstDeclarationAuthorizationMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setFirstDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setFirstDeclarationAuthorizationInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getFirstDeclarationAuthorizationFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("firstDeclarationAuthorizationMessage", new ActionMessage("error.file.size"));
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
                actionMessages.add("secondCarPropertyRegistryMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
                parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getSecondCarPropertyRegistryFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("secondCarPropertyRegistryMessage", new ActionMessage("error.file.size"));
                parkingRequestFactory.setSecondCarPropertyRegistryFileName(null);
                parkingRequestFactory.setSecondCarPropertyRegistryInputStream(null);
                result = false;
            }
        }
        if (parkingRequestFactory.getSecondDeclarationAuthorizationFileName() != null) {
            if (!validateFileName(parkingRequestFactory.getSecondDeclarationAuthorizationFileName())) {
                actionMessages.add("secondDeclarationAuthorizationMessage", new ActionMessage("error.file.extension"));
                parkingRequestFactory.setSecondDeclarationAuthorizationFileName(null);
                parkingRequestFactory.setSecondDeclarationAuthorizationInputStream(null);
                result = false;
            }
            if (parkingRequestFactory.getSecondDeclarationAuthorizationFileSize() > ParkingFile.MAX_FILE_SIZE) {
                actionMessages.add("secondDeclarationAuthorizationMessage", new ActionMessage("error.file.size"));
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

    public ActionForward downloadAuthorizationDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return downloadFile(mapping, actionForm, request, response, "anexoIV.pdf");
    }

    public ActionForward downloadParkingRegulamentation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return downloadFile(mapping, actionForm, request, response, "RegulamentoEstacionamento.pdf");
    }

    private ActionForward downloadFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, String fileName) throws Exception {
        try {
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            InputStream stream = getClass().getResourceAsStream("/parking/" + fileName);
            ByteStreams.copy(stream, response.getOutputStream());
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    private boolean checkRequestFields(ParkingRequestFactory parkingRequestFactory, HttpServletRequest request,
            DynaActionForm parkingForm) {
        final User userView = Authenticate.getUser();
        final boolean isStudent = isStudent(userView);
        ActionMessages actionMessages = new ActionMessages();
        boolean result = true;

        if (parkingRequestFactory.getFirstCarMake() == null || parkingRequestFactory.getFirstCarMake().trim().length() == 0) {
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
            actionMessages.add("firstCarPlateNumberPT", new ActionMessage("error.maxLengthExceeded", "10"));
            result = false;
        }

        if (hasNotDeliveryValue(parkingForm, "driverLicense")) {
            actionMessages.add("driverLicenseDeliveryMessage", new ActionMessage("error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "driverLicense"))
                && (parkingRequestFactory.getDriverLicenseFileName() == null || parkingRequestFactory.getDriverLicenseFileName()
                        .length() == 0)) {
            actionMessages.add("driverLicenseMessage", new ActionMessage("error.requiredStudentField"));
            parkingForm.set("driverLicense", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        // FirstCar
        if (hasNotDeliveryValue(parkingForm, "registry1")) {
            actionMessages.add("firstCarPropertyRegistryDeliveryMessage", new ActionMessage("error.requiredDocumentDelivery"));
            result = false;
        } else if ((isStudent || isElectronicDelivery(parkingForm, "registry1"))
                && (parkingRequestFactory.getFirstCarPropertyRegistryFileName() == null || parkingRequestFactory
                        .getFirstCarPropertyRegistryFileName().length() == 0)) {
            actionMessages.add("firstCarPropertyRegistryMessage", new ActionMessage("error.requiredStudentField"));
            parkingForm.set("registry1", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
            result = false;
        }
        if (!isOwner(parkingForm, "ownVehicle1")) {
            parkingForm.set("ownVehicle1", false);
            if (hasNotDeliveryValue(parkingForm, "Id1")) {
                actionMessages.add("firstCarOwnerIdDeliveryMessage", new ActionMessage("error.requiredDocumentDelivery"));
                result = false;
            } else if ((isStudent || isElectronicDelivery(parkingForm, "Id1"))
                    && (parkingRequestFactory.getFirstCarOwnerIdFileName() == null || parkingRequestFactory
                            .getFirstCarOwnerIdFileName().length() == 0)) {
                actionMessages.add("firstCarOwnerIdMessage", new ActionMessage("error.requiredStudentField"));
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
                actionMessages.add("firstDeclarationAuthorizationMessage", new ActionMessage("error.requiredStudentField"));
                parkingForm.set("declaration1", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
                result = false;
            }
        }
        // SecondCar
        if (isOwner(parkingForm, "vehicle2")) {
            if ((parkingRequestFactory.getSecondCarMake() != null && parkingRequestFactory.getSecondCarMake().trim().length() != 0)
                    || (parkingRequestFactory.getSecondCarPlateNumber() != null && parkingRequestFactory
                            .getSecondCarPlateNumber().trim().length() != 0)) {

                if (parkingRequestFactory.getSecondCarMake() == null
                        || parkingRequestFactory.getSecondCarMake().trim().length() == 0) {
                    actionMessages.add("secondCarMakePT", new ActionMessage("error.requiredField"));
                    result = false;
                } else if (parkingRequestFactory.getSecondCarMake().length() > 20) {
                    actionMessages.add("secondCarMakePT", new ActionMessage("error.maxLengthExceeded", "20"));
                    result = false;
                }
                if (parkingRequestFactory.getSecondCarPlateNumber() == null
                        || parkingRequestFactory.getSecondCarPlateNumber().trim().length() == 0) {
                    actionMessages.add("secondCarPlateNumberPT", new ActionMessage("error.requiredField"));
                    result = false;
                } else if (parkingRequestFactory.getSecondCarPlateNumber().length() > 10) {
                    actionMessages.add("secondCarPlateNumberPT", new ActionMessage("error.maxLengthExceeded", "10"));
                    result = false;
                }

                if (hasNotDeliveryValue(parkingForm, "registry2")) {
                    actionMessages.add("secondCarPropertyRegistryDeliveryMessage", new ActionMessage(
                            "error.requiredDocumentDelivery"));
                    result = false;
                } else if ((isStudent || isElectronicDelivery(parkingForm, "registry2"))
                        && (parkingRequestFactory.getSecondCarPropertyRegistryFileName() == null || parkingRequestFactory
                                .getSecondCarPropertyRegistryFileName().length() == 0)) {
                    actionMessages.add("secondCarPropertyRegistryMessage", new ActionMessage("error.requiredStudentField"));
                    parkingForm.set("registry2", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
                    result = false;
                }
                if (!isOwner(parkingForm, "ownVehicle2")) {
                    parkingForm.set("ownVehicle2", false);
                    if (hasNotDeliveryValue(parkingForm, "Id2")) {
                        actionMessages
                                .add("secondCarOwnerIdDeliveryMessage", new ActionMessage("error.requiredDocumentDelivery"));
                        result = false;
                    } else if ((isStudent || isElectronicDelivery(parkingForm, "Id2"))
                            && (parkingRequestFactory.getSecondCarOwnerIdFileName() == null || parkingRequestFactory
                                    .getSecondCarOwnerIdFileName().length() == 0)) {
                        actionMessages.add("secondCarOwnerIdMessage", new ActionMessage("error.requiredStudentField"));
                        parkingForm.set("Id2", ParkingDocumentState.ELECTRONIC_DELIVERY.name());
                        result = false;
                    }
                    if (hasNotDeliveryValue(parkingForm, "declaration2")) {
                        actionMessages.add("secondDeclarationAuthorizationDeliveryMessage", new ActionMessage(
                                "error.requiredDocumentDelivery"));
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

    public ActionForward createParkingRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

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
            ExecuteFactoryMethod.run(parkingRequestFactoryCreator);
        } catch (DomainException ex) {
            ActionMessages actionMessages = getActionMessages(request);
            actionMessages.add("fileError", new ActionMessage(ex.getKey(), ex.getArgs()));
            saveMessages(request, actionMessages);
            RenderUtils.invalidateViewState();
            return mapping.getInputForward();
        }
        return prepareParking(mapping, actionForm, request, response);
    }

    private void adjustParkingRequest(DynaActionForm parkingForm, ParkingRequestFactory parkingRequestFactory) {
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
        } else if (parkingRequestFactory.getParkingParty().isStudent() && !isOwner(parkingForm, "vehicle2")) {
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

    private boolean isStudent(User userView) {
        return (userView.getPerson().getTeacher() != null || userView.getPerson().getEmployee() != null) ? false : true;
    }

    public ActionForward renewUnlimitedParkingRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User userView = Authenticate.getUser();
        renewUnlimitedParkingRequest(userView.getPerson().getParkingParty().getFirstRequest(), Boolean.TRUE);
        request.setAttribute("renewUnlimitedParkingRequest.sucess", true);
        return prepareParking(mapping, actionForm, request, response);
    }

    @Atomic
    public void renewUnlimitedParkingRequest(ParkingRequest oldParkingRequest, Boolean limitlessAccessCard) {
        if (oldParkingRequest.getParkingParty().getCanRequestUnlimitedCardAndIsInAnyRequestPeriod()) {
            new ParkingRequest(oldParkingRequest, limitlessAccessCard);
        }
    }

}