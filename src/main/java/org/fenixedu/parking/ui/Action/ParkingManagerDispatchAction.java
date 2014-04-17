package org.fenixedu.parking.ui.Action;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ExecuteFactoryMethod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.ContentType;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.parking.domain.ParkingGroup;
import org.fenixedu.parking.domain.ParkingParty;
import org.fenixedu.parking.domain.ParkingPartyHistory;
import org.fenixedu.parking.domain.ParkingRequest;
import org.fenixedu.parking.domain.ParkingRequestSearch;
import org.fenixedu.parking.domain.ParkingRequestState;
import org.fenixedu.parking.dto.ParkingPartyBean;
import org.fenixedu.parking.dto.SearchParkingPartyBean;
import org.fenixedu.parking.dto.VehicleBean;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/parking", module = "parkingManager", formBean = "parkingForm")
@Forwards({
        @Forward(name = "searchParty", path = "/parkingManager/searchParty.jsp", tileProperties = @Tile(
                title = "private.parking.users", bundle = "PARKING_MANAGER")),
        @Forward(name = "showParkingPartyRequests", path = "/parkingManager/searchParty.jsp", tileProperties = @Tile(
                title = "private.parking.users", bundle = "PARKING_MANAGER")),
        @Forward(name = "showParkingRequests", path = "/parkingManager/showParkingRequests.jsp", tileProperties = @Tile(
                title = "private.parking.orders", bundle = "PARKING_MANAGER")),
        @Forward(name = "editParkingParty", path = "/parkingManager/editParkingParty.jsp"),
        @Forward(name = "showParkingHistories", path = "/parkingManager/showParkingHistories.jsp"),
        @Forward(name = "showParkingRequest", path = "/parkingManager/showParkingRequest.jsp") })
public class ParkingManagerDispatchAction extends FenixDispatchAction {
    private static final int MAX_NOTE_LENGTH = 250;

    public ActionForward showParkingRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // verificar autorização
        ParkingRequestSearch parkingRequestSearch = getRenderedObject("parkingRequestSearch");

        if (parkingRequestSearch == null) {
            parkingRequestSearch = new ParkingRequestSearch();
            parkingRequestSearch.setParkingRequestState(ParkingRequestState.PENDING);
            setSearchCriteria(request, parkingRequestSearch);
        }
        if (request.getParameter("dontSearch") == null) {
            parkingRequestSearch.doSearch();
        }

        request.setAttribute("dontSearch", request.getParameter("dontSearch"));
        request.setAttribute("parkingRequestSearch", parkingRequestSearch);
        return mapping.findForward("showParkingRequests");
    }

    private void setSearchCriteria(HttpServletRequest request, ParkingRequestSearch parkingRequestSearch) {
        String parkingRequestState = request.getParameter("parkingRequestState");
        if (!StringUtils.isEmpty(parkingRequestState)) {
            parkingRequestSearch.setParkingRequestState(ParkingRequestState.valueOf(parkingRequestState));
        }
        String partyClassification = request.getParameter("partyClassification");
        if (!StringUtils.isEmpty(partyClassification)) {
            parkingRequestSearch.setPartyClassification(PartyClassification.valueOf(partyClassification));
        }
        String personName = request.getParameter("personName");
        if (!StringUtils.isEmpty(personName)) {
            parkingRequestSearch.setPersonName(personName);
        }

        String carPlateNumber = request.getParameter("carPlateNumber");
        if (!StringUtils.isEmpty(carPlateNumber)) {
            parkingRequestSearch.setCarPlateNumber(carPlateNumber);
        }
    }

    public ActionForward showRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // verificar autorização

        final ParkingRequest parkingRequest = getDomainObject(request, "externalId");

        if (parkingRequest.getParkingRequestState() == ParkingRequestState.PENDING) {
            request.setAttribute("groups", ParkingGroup.getAll());
        }
        if (parkingRequest.getParkingParty().getCardNumber() != null) {
            ((DynaActionForm) actionForm).set("cardNumber", parkingRequest.getParkingParty().getCardNumber());
        }
        if (parkingRequest.getParkingParty().getParkingGroup() != null) {
            ((DynaActionForm) actionForm).set("groupID", parkingRequest.getParkingParty().getParkingGroup().getExternalId());
        }
        request.setAttribute("parkingRequest", parkingRequest);
        request.setAttribute("parkingPartyBean", new ParkingPartyBean(parkingRequest.getParkingParty()));
        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (!StringUtils.isEmpty(dynaActionForm.getString("cardAlwaysValid"))) { // in case of error validation
            dynaActionForm.set("cardAlwaysValid", dynaActionForm.getString("cardAlwaysValid"));
        } else {
            dynaActionForm.set("cardAlwaysValid", "no");
        }

        if (parkingRequest.getParkingParty().getParty().isPerson()) {
            Person person = (Person) parkingRequest.getParkingParty().getParty();
            if (person.getTeacher() != null && person.getTeacher().isMonitor(ExecutionSemester.readActualExecutionSemester())) {
                request.setAttribute("monitor", "true");
            }
        }

        String parkingRequestState = request.getParameter("parkingRequestState");
        if (parkingRequestState == null) {
            parkingRequestState = "";
        }
        String partyClassification = request.getParameter("partyClassification");
        if (partyClassification == null) {
            partyClassification = "";
        }
        String personName = request.getParameter("personName");
        if (personName == null) {
            personName = "";
        }

        String carPlateNumber = request.getParameter("carPlateNumber");
        if (carPlateNumber == null) {
            carPlateNumber = "";
        }

        request.setAttribute("parkingRequestState", parkingRequestState);
        request.setAttribute("partyClassification", partyClassification);
        request.setAttribute("personName", personName);
        request.setAttribute("carPlateNumber", carPlateNumber);
        return mapping.findForward("showParkingRequest");
    }

    public ActionForward showPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Party party = FenixFramework.getDomainObject(request.getParameter("personID"));
        if (party.isPerson()) {
            Person person = (Person) party;
            Photograph personalPhoto = person.getPersonalPhoto();
            if (personalPhoto != null) {
                try {
                    byte[] avatar = personalPhoto.getDefaultAvatar();
                    response.setContentType(ContentType.PNG.getMimeType());
                    DataOutputStream dos = new DataOutputStream(response.getOutputStream());
                    dos.write(avatar);
                    dos.close();
                } catch (java.io.IOException e) {
                    throw new FenixActionException(e);
                }
            }
        }
        return null;
    }

    public ActionForward editFirstTimeParkingParty(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String parkingRequestID = request.getParameter("code");
        final ParkingRequest parkingRequest = FenixFramework.getDomainObject(parkingRequestID);

        String note = request.getParameter("note");

        String parkingRequestState = request.getParameter("parkingRequestState");
        if (parkingRequestState == null) {
            parkingRequestState = "";
        }
        String partyClassification = request.getParameter("partyClassification");
        if (partyClassification == null) {
            partyClassification = "";
        }
        String personName = request.getParameter("personName");
        if (personName == null) {
            personName = "";
        }
        String carPlateNumber = request.getParameter("carPlateNumber");
        if (carPlateNumber == null) {
            carPlateNumber = "";
        }
        request.setAttribute("parkingRequestState", parkingRequestState);
        request.setAttribute("partyClassification", partyClassification);
        request.setAttribute("personName", personName);
        request.setAttribute("carPlateNumber", carPlateNumber);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;
        if (!StringUtils.isEmpty((String) dynaForm.get("accepted")) || request.getParameter("acceptPrint") != null) {
            Long cardNumber = null;
            String group = null;
            try {
                cardNumber = new Long(request.getParameter("cardNumber"));
                if (cardNumber < 0) {
                    saveErrorMessage(request, "cardNumber", "error.number.below.minimum");
                    request.setAttribute("externalId", parkingRequestID);
                    return showRequest(mapping, actionForm, request, response);
                }
            } catch (NullPointerException e) {
                saveErrorMessage(request, "cardNumber", "error.requiredCardNumber");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } catch (NumberFormatException e) {
                saveErrorMessage(request, "cardNumber", "error.invalidCardNumber");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            try {
                group = request.getParameter("groupID");
            } catch (NullPointerException e) {
                saveErrorMessage(request, "group", "error.requiredGroup");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } catch (NumberFormatException e) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
            if (!isRepeatedCardNumber(cardNumber, parkingPartyBean.getParkingParty())) {
                saveErrorMessage(request, "cardNumber", "error.alreadyExistsCardNumber");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            if (!isValidGroup(group)) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            String cardAlwaysValid = dynaForm.getString("cardAlwaysValid");
            if (cardAlwaysValid.equalsIgnoreCase("no")
                    && (parkingPartyBean.getCardStartDate() == null || parkingPartyBean.getCardEndDate() == null)) {
                saveErrorMessage(request, "mustFillInDates", "error.card.mustFillInDates");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } else if (cardAlwaysValid.equalsIgnoreCase("yes")) {
                parkingPartyBean.setCardStartDate(null);
                parkingPartyBean.setCardEndDate(null);
            }

            if (!StringUtils.isEmpty(note) && note.length() > MAX_NOTE_LENGTH) {
                ActionMessages actionMessages = getMessages(request);
                actionMessages.add("note", new ActionMessage("error.maxLengthExceeded", MAX_NOTE_LENGTH));
                saveMessages(request, actionMessages);
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            if (parkingPartyBean.getCardStartDate() != null
                    && parkingPartyBean.getCardStartDate().isAfter(parkingPartyBean.getCardEndDate())) {
                saveErrorMessage(request, "invalidPeriod", "error.parkingParty.invalidPeriod");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            Integer mostSignificantNumber =
                    getMostSignificantNumber((Person) parkingRequest.getParkingParty().getParty(),
                            FenixFramework.<ParkingGroup> getDomainObject(group));
            updateParkingParty(parkingRequest, ParkingRequestState.ACCEPTED, cardNumber,
                    FenixFramework.<ParkingGroup> getDomainObject(group), note, parkingPartyBean.getCardStartDate(),
                    parkingPartyBean.getCardEndDate(), mostSignificantNumber);
        } else if (request.getParameter("reject") != null) {
            updateParkingParty(parkingRequest, ParkingRequestState.REJECTED, null, null, note, null, null, null);
        } else {
            updateParkingParty(parkingRequest, ParkingRequestState.REJECTED, null, null, note, null, null, null);
        }
        return showParkingRequests(mapping, actionForm, request, response);
    }

    public ActionForward exportToPDFParkingCard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String parkingPartyID = request.getParameter("parkingPartyID");

        final ParkingParty parkingParty = FenixFramework.getDomainObject(parkingPartyID);
        String parkingGroupID = request.getParameter("groupID");

        if (parkingGroupID != null) {
            String parkingRequestID = request.getParameter("code");
            if (request.getParameter("groupID") == null) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            if (!isValidGroup(parkingGroupID)) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("externalId", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
        }

        ParkingGroup parkingGroup = null;
        if (parkingGroupID != null) {
            parkingGroup = FenixFramework.getDomainObject(parkingGroupID);
        } else {
            parkingGroup = parkingParty.getParkingGroup();
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("imageUrl", "images/Logo_IST_color.tiff");

        Person person = (Person) parkingParty.getParty();
        parameters.put("number", getMostSignificantNumberString(person, parkingGroup));

        List<Person> persons = new ArrayList<Person>();
        persons.add(person);

        byte[] data = ReportsUtils.exportToPdfFileAsByteArray("parkingManager.parkingCard", parameters, null, persons);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=cartao.pdf");
        response.setContentLength(data.length);
        ServletOutputStream writer = response.getOutputStream();
        writer.write(data);
        writer.flush();
        writer.close();
        response.flushBuffer();

        return mapping.findForward("");
    }

    private String getMostSignificantNumberString(Person person, ParkingGroup parkingGroup) {
        Integer number = getMostSignificantNumber(person, parkingGroup);
        if (number != null) {
            if ((person.getTeacher() != null && person.getTeacher().getCurrentWorkingDepartment() != null)
                    || (person.getEmployee() != null && person.getEmployee().getCurrentWorkingContract() != null && person
                            .getPersonRole(RoleType.TEACHER) == null)) {
                return "Nº Mec: " + number;
            } else {
                return "Nº" + number;
            }
        }
        return "";
    }

    private Integer getMostSignificantNumber(Person person, ParkingGroup parkingGroup) {
        if (person.getParkingParty().getPhdNumber() != null) {
            return person.getParkingParty().getPhdNumber();
        }
        if (person.getTeacher() != null && person.getTeacher().getCurrentWorkingDepartment() != null
                && !person.getTeacher().isMonitor(ExecutionSemester.readActualExecutionSemester())
                && person.getEmployee() != null) {
            return person.getEmployee().getEmployeeNumber();
        }
        if (person.getEmployee() != null && person.getEmployee().getCurrentWorkingContract() != null
                && person.getPersonRole(RoleType.TEACHER) == null) {
            return person.getEmployee().getEmployeeNumber();
        }
        if (person.getStudent() != null && !parkingGroup.getGroupName().equalsIgnoreCase("Bolseiros")) {
            DegreeType degreeType = person.getStudent().getMostSignificantDegreeType();
            Collection<Registration> registrations = person.getStudent().getRegistrationsByDegreeType(degreeType);
            for (Registration registration : registrations) {
                StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                if (scp != null) {
                    return person.getStudent().getNumber();
                }
            }
        }
        if (person.getPersonRole(RoleType.GRANT_OWNER) != null && person.getEmployee() != null) {
            PersonContractSituation currentGrantOwnerContractSituation =
                    person.getPersonProfessionalData() != null ? person.getPersonProfessionalData()
                            .getCurrentPersonContractSituationByCategoryType(CategoryType.GRANT_OWNER) : null;
            if (currentGrantOwnerContractSituation != null) {
                return person.getEmployee().getEmployeeNumber();
            }
        }
        if (person.getTeacher() != null && person.getTeacher().getCurrentWorkingDepartment() != null
                && person.getTeacher().isMonitor(ExecutionSemester.readActualExecutionSemester()) && person.getEmployee() != null) {
            return person.getEmployee().getEmployeeNumber();
        }

        if (person.getEmployee() != null && person.getResearcher() != null) {
            return person.getEmployee().getEmployeeNumber();
        }
        return null;
    }

    private boolean isValidGroup(String groupId) {
        for (ParkingGroup group : rootDomainObject.getParkingGroupsSet()) {
            if (group.getExternalId().equals(groupId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRepeatedCardNumber(Long cardNumber, ParkingParty parkingParty) {
        for (ParkingParty tempParkingParty : rootDomainObject.getParkingPartiesSet()) {
            if (tempParkingParty.getCardNumber() != null && tempParkingParty.getCardNumber() != 0
                    && tempParkingParty.getCardNumber().equals(cardNumber) && tempParkingParty != parkingParty) {
                return false;
            }
        }
        return true;
    }

    private void saveErrorMessage(HttpServletRequest request, String error, String errorMessage) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add(error, new ActionMessage(errorMessage));
        saveMessages(request, actionMessages);
    }

    public ActionForward prepareSearchParty(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("searchPartyBean", new SearchParkingPartyBean());
        return mapping.findForward("searchParty");
    }

    public ActionForward showParkingPartyRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("searchPartyBean");
        SearchParkingPartyBean SearchParkingPartyBean = null;
        if (viewState != null) {
            SearchParkingPartyBean = (SearchParkingPartyBean) viewState.getMetaObject().getObject();
        }

        if (SearchParkingPartyBean != null) {
            SearchParkingPartyBean.setParty(null);

            List<Party> partyList =
                    searchPartyCarPlate(SearchParkingPartyBean.getPartyName(), SearchParkingPartyBean.getCarPlateNumber(),
                            SearchParkingPartyBean.getParkingCardNumber());
            request.setAttribute("searchPartyBean", SearchParkingPartyBean);
            request.setAttribute("partyList", partyList);

        } else if (request.getParameter("partyID") != null || request.getAttribute("partyID") != null) {
            final String externalId = getPopertyID(request, "partyID");
            final String carPlateNumber = request.getParameter("plateNumber");
            final String parkingCardNumberString = request.getParameter("parkingCardNumber");
            Long parkingCardNumber = null;
            if (!org.apache.commons.lang.StringUtils.isEmpty(parkingCardNumberString)) {
                parkingCardNumber = new Long(parkingCardNumberString);
            }
            Party party = FenixFramework.getDomainObject(externalId);
            setupParkingRequests(request, party, carPlateNumber, parkingCardNumber);
        }

        return mapping.findForward("showParkingPartyRequests");
    }

    private String getPopertyID(HttpServletRequest request, String property) {
        if (request.getParameter(property) != null) {
            return request.getParameter(property);
        } else {
            return (String) request.getAttribute(property);
        }
    }

    private void setupParkingRequests(HttpServletRequest request, Party party, String carPlateNumber, Long parkingCardNumber)
            throws FenixServiceException {
        if (party.getParkingParty() != null) {
            request.setAttribute("parkingRequests", party.getParkingParty().getParkingRequests());
        }
        request.setAttribute("searchPartyBean", new SearchParkingPartyBean(party, carPlateNumber, parkingCardNumber));
    }

    public ActionForward prepareEditParkingParty(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ParkingParty parkingParty = null;
        ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
        if ((!StringUtils.isEmpty(request.getParameter("addVehicle")) && request.getParameter("addVehicle").equals("yes"))
                && parkingPartyBean != null) {
            parkingPartyBean.addVehicle();
            parkingParty = parkingPartyBean.getParkingParty();
            request.setAttribute("parkingPartyBean", parkingPartyBean);
            request.setAttribute("parkingPartyID", parkingParty.getExternalId());
            ((DynaActionForm) actionForm).set("addVehicle", "no");
            RenderUtils.invalidateViewState();
        } else {
            String parkingPartyID = getPopertyID(request, "parkingPartyID");
            parkingParty = FenixFramework.getDomainObject(parkingPartyID);
            request.setAttribute("parkingPartyBean", new ParkingPartyBean(parkingParty));
            request.setAttribute("parkingPartyID", parkingParty.getExternalId());
        }

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (parkingParty.hasAnyVehicles()) {
            if (!StringUtils.isEmpty(dynaActionForm.getString("cardAlwaysValid"))) {
                dynaActionForm.set("cardAlwaysValid", dynaActionForm.getString("cardAlwaysValid")); // in case of error validation
            } else {
                if (parkingParty.getCardStartDate() == null) {
                    dynaActionForm.set("cardAlwaysValid", "yes");
                } else {
                    dynaActionForm.set("cardAlwaysValid", "no");
                }
            }
        } else {
            if (!StringUtils.isEmpty(dynaActionForm.getString("cardAlwaysValid"))) { // in case of error validation
                dynaActionForm.set("cardAlwaysValid", dynaActionForm.getString("cardAlwaysValid"));
            } else {
                dynaActionForm.set("cardAlwaysValid", "no");
            }
        }
        return mapping.findForward("editParkingParty");
    }

    public ActionForward editParkingParty(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
        request.setAttribute("parkingPartyID", parkingPartyBean.getParkingParty().getExternalId());
        if (!isRepeatedCardNumber(parkingPartyBean.getCardNumber(), parkingPartyBean.getParkingParty())) {
            saveErrorMessage(request, "cardNumber", "error.alreadyExistsCardNumber");
            return prepareEditParkingParty(mapping, actionForm, request, response);
        }
        DynaActionForm dynaForm = (DynaActionForm) actionForm;
        String cardAlwaysValid = dynaForm.getString("cardAlwaysValid");
        parkingPartyBean.setCardAlwaysValid(false);
        if (cardAlwaysValid.equalsIgnoreCase("no")
                && (parkingPartyBean.getCardStartDate() == null || parkingPartyBean.getCardEndDate() == null)) {
            saveErrorMessage(request, "mustFillInDates", "error.card.mustFillInDates");
            return prepareEditParkingParty(mapping, actionForm, request, response);
        } else if (cardAlwaysValid.equalsIgnoreCase("yes")) {
            parkingPartyBean.setCardAlwaysValid(true);
            parkingPartyBean.setCardStartDate(null);
            parkingPartyBean.setCardEndDate(null);
        }
        boolean vehicleDataCorrect = true;
        boolean deleteAllVehicles = true;
        Set<String> vehiclePlates = new HashSet<String>();
        for (final Iterator<VehicleBean> iter = parkingPartyBean.getVehicles().iterator(); iter.hasNext();) {
            VehicleBean vehicle = iter.next();
            if (!vehicle.getDeleteVehicle()) {
                deleteAllVehicles = false;
                if (StringUtils.isEmpty(vehicle.getVehicleMake()) || StringUtils.isEmpty(vehicle.getVehiclePlateNumber())) {
                    vehicleDataCorrect = false;
                    break;
                }
            } else if (vehicle.getVehicle() == null) {
                iter.remove();
                parkingPartyBean.getVehicles().remove(vehicle);
            }
            if (!vehicle.getDeleteVehicle() && !vehiclePlates.add(vehicle.getVehiclePlateNumber())) {
                saveErrorMessage(request, "repeatedPlates", "error.parkingParty.vehicle.repeatedPlates");
                return prepareEditParkingParty(mapping, actionForm, request, response);
            }
        }
        if (!vehicleDataCorrect) {
            saveErrorMessage(request, "vehicleMandatoryData", "error.parkingParty.vehicle.mandatoryData");
            return prepareEditParkingParty(mapping, actionForm, request, response);
        }
        if (deleteAllVehicles) {
            saveErrorMessage(request, "noVehicles", "error.parkingParty.no.vehicles");
            return prepareEditParkingParty(mapping, actionForm, request, response);
        }
        ExecuteFactoryMethod.run(parkingPartyBean);
        request.setAttribute("partyID", parkingPartyBean.getParkingParty().getParty().getExternalId());

        return showParkingPartyRequests(mapping, actionForm, request, response);
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        ParkingRequestSearch parkingRequestSearch = new ParkingRequestSearch();
        setSearchCriteria(request, parkingRequestSearch);
        parkingRequestSearch.doSearch();
        List<ParkingRequest> parkingRequestList = parkingRequestSearch.getSearchResult();
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Pedidos_Parque", 15);
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader("Categoria");
        spreadsheet.addHeader("Número");
        spreadsheet.addHeader("Nome", 9000);
        spreadsheet.addHeader("Estado");
        spreadsheet.addHeader("Data Pedido");
        spreadsheet.addHeader("Outras Informações", 6000);

        final ResourceBundle enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
        for (ParkingRequest parkingRequest : parkingRequestList) {
            if (parkingRequest.getParkingParty().getParty().isPerson()) {
                Person person = (Person) parkingRequest.getParkingParty().getParty();
                spreadsheet.newRow();
                int firstRow = spreadsheet.getRow().getRowNum();
                spreadsheet.addCell(enumerationBundle.getString(parkingRequestSearch.getPartyClassification().name()));
                spreadsheet.addCell(parkingRequest.getParkingParty().getMostSignificantNumber());
                spreadsheet.addCell(person.getName());
                spreadsheet.addCell(enumerationBundle.getString(parkingRequest.getParkingRequestState().name()));
                spreadsheet.addDateTimeCell(parkingRequest.getCreationDate());
                if (!parkingRequest.getParkingParty().getDegreesInformation().isEmpty()) {
                    Iterator<String> iterator = parkingRequest.getParkingParty().getDegreesInformation().iterator();
                    String degreeInfo = iterator.next();
                    spreadsheet.addCell(degreeInfo);
                    while (iterator.hasNext()) {
                        spreadsheet.newRow();
                        degreeInfo = iterator.next();
                        spreadsheet.addCell(degreeInfo, 5);
                    }
                    int lastRow = firstRow + parkingRequest.getParkingParty().getDegreesInformation().size() - 1;
                    if (firstRow != lastRow) {
                        for (int iter = 0; iter < 5; iter++) {
                            spreadsheet.getSheet().addMergedRegion(new Region(firstRow, (short) iter, lastRow, (short) iter));
                        }
                    }
                }
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=pedidos_parque.xls");
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    public ActionForward showHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String codeString = request.getParameter("externalId");
        String code = null;
        if (codeString == null) {
            code = (String) request.getAttribute("externalId");
        } else {
            code = codeString;
        }
        final ParkingRequest parkingRequest = FenixFramework.getDomainObject(code);
        List<ParkingPartyHistory> parkingPartyHistories =
                new ArrayList<ParkingPartyHistory>(parkingRequest.getParkingParty().getParty().getParkingPartyHistoriesSet());

        Collections.sort(parkingPartyHistories, new BeanComparator("historyDate"));
        request.setAttribute("parkingPartyHistories", parkingPartyHistories);
        request.setAttribute("parkingParty", parkingRequest.getParkingParty());
        return mapping.findForward("showParkingHistories");
    }

    public ActionForward showParkingPartyHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String codeString = request.getParameter("externalId");
        String code = null;
        if (codeString == null) {
            code = (String) request.getAttribute("externalId");
        } else {
            code = codeString;
        }
        final ParkingParty parkingParty = FenixFramework.getDomainObject(code);
        List<ParkingPartyHistory> parkingPartyHistories =
                new ArrayList<ParkingPartyHistory>(parkingParty.getParty().getParkingPartyHistoriesSet());

        Collections.sort(parkingPartyHistories, new BeanComparator("historyDate"));
        request.setAttribute("parkingPartyHistories", parkingPartyHistories);
        request.setAttribute("parkingParty", parkingParty);
        return mapping.findForward("showParkingHistories");
    }

    @Atomic
    private void updateParkingParty(ParkingRequest parkingRequest, final ParkingRequestState parkingRequestState,
            final Long cardCode, final ParkingGroup parkingGroup, final String note, final DateTime cardStartDate,
            final DateTime cardEndDate, final Integer number) {
        ParkingParty parkingParty = parkingRequest.getParkingParty();

        if (parkingRequestState == ParkingRequestState.ACCEPTED) {
            if (parkingParty.getCardNumber() != null
                    && (changedObject(parkingParty.getCardStartDate(), cardStartDate)
                            || changedObject(parkingParty.getCardEndDate(), cardEndDate)
                            || changedObject(parkingParty.getCardNumber(), cardCode) || changedObject(
                                parkingParty.getParkingGroup(), parkingGroup))) {
                new ParkingPartyHistory(parkingParty, true);
            }
            parkingParty.setCardStartDate(cardStartDate);
            parkingParty.setCardEndDate(cardEndDate);
            parkingParty.setAuthorized(true);
            parkingParty.setCardNumber(cardCode);
            parkingParty.setUsedNumber(number);
            parkingParty.edit(parkingRequest);
            parkingParty.setParkingGroup(parkingGroup);
        }
        parkingParty.setRequestedAs(parkingRequest.getRequestedAs());
        parkingRequest.setParkingRequestState(parkingRequestState);
        parkingRequest.setNote(note);

        String email = null;
        EmailAddress defaultEmailAddress = parkingParty.getParty().getDefaultEmailAddress();
        if (defaultEmailAddress != null) {
            email = defaultEmailAddress.getValue();
        }

        if (note != null && note.trim().length() != 0 && email != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", Language.getLocale());
            Sender sender = Bennu.getInstance().getSystemSender();
            ConcreteReplyTo replyTo = new ConcreteReplyTo(bundle.getString("label.fromAddress"));
            new Message(sender, replyTo.asCollection(), Collections.EMPTY_LIST, bundle.getString("label.subject"), note, email);
        }
    }

    private boolean changedObject(Object oldObject, Object newObject) {
        return oldObject == null && newObject == null ? false : (oldObject != null && newObject != null ? (!oldObject
                .equals(newObject)) : true);
    }

    @Atomic
    private List<Party> searchPartyCarPlate(String nameSearch, String carPlateNumber, Long parkingCardNumber) {
        List<Party> result = new ArrayList<Party>();
        if (!StringUtils.isEmpty(carPlateNumber) || !StringUtils.isEmpty(nameSearch) || parkingCardNumber != null) {
            Collection<ParkingParty> parkingParties = Bennu.getInstance().getParkingPartiesSet();
            for (ParkingParty parkingParty : parkingParties) {
                if (parkingParty.getParty() != null) {
                    if (satisfiedParkingCardNumber(parkingParty, parkingCardNumber)
                            && satisfiedPlateNumber(parkingParty, carPlateNumber) && satisfiedName(parkingParty, nameSearch)) {
                        result.add(parkingParty.getParty());
                    }
                }
            }
        }
        return result;
    }

    private boolean satisfiedName(ParkingParty parkingParty, String nameSearch) {
        if (!StringUtils.isEmpty(nameSearch)) {
            String[] nameValues = StringNormalizer.normalize(nameSearch).split("\\p{Space}+");
            return areNamesPresent(parkingParty.getParty().getName(), nameValues);
        }
        return true;
    }

    private boolean satisfiedParkingCardNumber(ParkingParty parkingParty, Long parkingCardNumber) {
        if (parkingCardNumber != null) {
            return (parkingParty.getCardNumber() != null && parkingParty.getCardNumber().toString()
                    .contains(parkingCardNumber.toString())) ? true : false;
        }
        return true;
    }

    private boolean satisfiedPlateNumber(ParkingParty parkingParty, String carPlateNumber) {
        return !StringUtils.isEmpty(carPlateNumber) ? (parkingParty.hasVehicleContainingPlateNumber(carPlateNumber.trim())) : true;
    }

    private static boolean areNamesPresent(String name, String[] searchNameParts) {
        String nameNormalized = StringNormalizer.normalize(name);
        for (String searchNamePart : searchNameParts) {
            String namePart = searchNamePart;
            if (!nameNormalized.contains(namePart)) {
                return false;
            }
        }
        return true;
    }
}