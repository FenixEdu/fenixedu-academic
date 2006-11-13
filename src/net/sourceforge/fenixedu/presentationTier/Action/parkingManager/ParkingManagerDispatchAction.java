package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingPartyBean;
import net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingDocument;
import net.sourceforge.fenixedu.domain.parking.ParkingDocumentType;
import net.sourceforge.fenixedu.domain.parking.ParkingFile;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.domain.parking.ParkingPartyClassification;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestSearch;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class ParkingManagerDispatchAction extends FenixDispatchAction {
    public ActionForward showParkingRequests(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        ParkingRequestSearch parkingRequestSearch = (ParkingRequestSearch) getRendererObject("parkingRequestSearch");

        if (parkingRequestSearch == null) {
            parkingRequestSearch = new ParkingRequestSearch();
            String parkingRequestState = request.getParameter("parkingRequestState");
            if (!StringUtils.isEmpty(parkingRequestState)) {
                parkingRequestSearch.setParkingRequestState(ParkingRequestState
                        .valueOf(parkingRequestState));
            }
            String parkingPartyClassification = request.getParameter("parkingPartyClassification");
            if (!StringUtils.isEmpty(parkingPartyClassification)) {
                parkingRequestSearch.setParkingPartyClassification(ParkingPartyClassification
                        .valueOf(parkingPartyClassification));
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
        request.setAttribute("parkingRequestSearch", parkingRequestSearch);
        return mapping.findForward("showParkingRequests");
    }

    public ActionForward showRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        final String codeString = request.getParameter("idInternal");
        Integer code = null;
        if (codeString == null) {
            code = (Integer) request.getAttribute("idInternal");
        } else {
            code = new Integer(codeString);
        }

        final ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(code);
        if (parkingRequest.getParkingRequestState() == ParkingRequestState.PENDING) {
            request.setAttribute("groups", ParkingGroup.getAll());
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
            if (person.getTeacher() != null
                    && person.getTeacher().isMonitor(ExecutionPeriod.readActualExecutionPeriod())) {
                request.setAttribute("monitor", "true");
            }
        }

        String parkingRequestState = request.getParameter("parkingRequestState");
        if (parkingRequestState == null) {
            parkingRequestState = "";
        }
        String parkingPartyClassification = request.getParameter("parkingPartyClassification");
        if (parkingPartyClassification == null) {
            parkingPartyClassification = "";
        }
        String personName = request.getParameter("personName");
        if (personName == null) {
            personName = "";
        }

        String carPlateNumber = request.getParameter("carPlateNumber");
        if (carPlateNumber == null) {
            carPlateNumber = "";
        }

        prepapreParkingRequestDocumentsLinks(request, parkingRequest);
        prepapreParkingPartyDocumentsLinks(request, parkingRequest.getParkingParty());

        request.setAttribute("parkingRequestState", parkingRequestState);
        request.setAttribute("parkingPartyClassification", parkingPartyClassification);
        request.setAttribute("personName", personName);
        request.setAttribute("carPlateNumber", carPlateNumber);
        return mapping.findForward("showParkingRequest");
    }

    public ActionForward showPhoto(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer personID = new Integer(request.getParameter("personID"));
        Party party = rootDomainObject.readPartyByOID(personID);
        if (party.isPerson()) {
            Person person = (Person) party;
            FileEntry personalPhoto = person.getPersonalPhoto();
            if (personalPhoto != null) {
                try {
                    response.setContentType(personalPhoto.getContentType().getMimeType());
                    DataOutputStream dos = new DataOutputStream(response.getOutputStream());
                    dos.write(personalPhoto.getContents());
                    dos.close();
                } catch (java.io.IOException e) {
                    throw new FenixActionException(e);
                }
            }
        }
        return null;
    }

    private void prepapreParkingPartyDocumentsLinks(HttpServletRequest request,
            final ParkingParty parkingParty) {
        ParkingDocument parkingDocument = parkingParty
                .getParkingDocument(ParkingDocumentType.DRIVER_LICENSE);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyDriverLicenselink", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty
                .getParkingDocument(ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyRegister1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty.getParkingDocument(ParkingDocumentType.FIRST_CAR_INSURANCE);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyInsurance1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty.getParkingDocument(ParkingDocumentType.FIRST_CAR_OWNER_ID);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyOwnerID1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty
                .getParkingDocument(ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyDeclaration1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty
                .getParkingDocument(ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyRegister2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty.getParkingDocument(ParkingDocumentType.SECOND_CAR_INSURANCE);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyInsurance2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty.getParkingDocument(ParkingDocumentType.SECOND_CAR_OWNER_ID);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyOwnerID2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingParty
                .getParkingDocument(ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("partyDeclaration2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }

    }

    private void prepapreParkingRequestDocumentsLinks(HttpServletRequest request,
            final ParkingRequest parkingRequest) {
        ParkingDocument parkingDocument = parkingRequest
                .getParkingDocument(ParkingDocumentType.DRIVER_LICENSE);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestDriverLicenselink", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest
                .getParkingDocument(ParkingDocumentType.FIRST_CAR_PROPERTY_REGISTER);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestRegister1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest.getParkingDocument(ParkingDocumentType.FIRST_CAR_INSURANCE);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestInsurance1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest.getParkingDocument(ParkingDocumentType.FIRST_CAR_OWNER_ID);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestOwnerID1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest
                .getParkingDocument(ParkingDocumentType.FIRST_DECLARATION_OF_AUTHORIZATION);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestDeclaration1link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest
                .getParkingDocument(ParkingDocumentType.SECOND_CAR_PROPERTY_REGISTER);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestRegister2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest.getParkingDocument(ParkingDocumentType.SECOND_CAR_INSURANCE);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestInsurance2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest.getParkingDocument(ParkingDocumentType.SECOND_CAR_OWNER_ID);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestOwnerID2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
        parkingDocument = parkingRequest
                .getParkingDocument(ParkingDocumentType.SECOND_DECLARATION_OF_AUTHORIZATION);
        if (parkingDocument != null) {
            ParkingFile parkingFile = parkingDocument.getParkingFile();
            request.setAttribute("requestDeclaration2link", FileManagerFactory.getFileManager()
                    .getDirectDownloadUrlFormat(parkingFile.getExternalStorageIdentification(),
                            parkingFile.getFilename()));
        }
    }

    public ActionForward editFirstTimeParkingParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer parkingRequestID = new Integer(request.getParameter("code"));
        final ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(parkingRequestID);

        String note = request.getParameter("note");
        Object args[] = { parkingRequest, null, null, null, note, null, null };

        String parkingRequestState = request.getParameter("parkingRequestState");
        if (parkingRequestState == null) {
            parkingRequestState = "";
        }
        String parkingPartyClassification = request.getParameter("parkingPartyClassification");
        if (parkingPartyClassification == null) {
            parkingPartyClassification = "";
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
        request.setAttribute("parkingPartyClassification", parkingPartyClassification);
        request.setAttribute("personName", personName);
        request.setAttribute("carPlateNumber", carPlateNumber);

        DynaActionForm dynaForm = (DynaActionForm) actionForm;
        if (!StringUtils.isEmpty((String) dynaForm.get("accepted"))
                || request.getParameter("acceptPrint") != null) {
            Long cardNumber = null;
            Integer group = null;
            try {
                cardNumber = new Long(request.getParameter("cardNumber"));
                if (cardNumber <= 0) {
                    saveErrorMessage(request, "cardNumber", "error.number.below.minimum");
                    request.setAttribute("idInternal", parkingRequestID);
                    return showRequest(mapping, actionForm, request, response);
                }
            } catch (NullPointerException e) {
                saveErrorMessage(request, "cardNumber", "error.requiredCardNumber");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } catch (NumberFormatException e) {
                saveErrorMessage(request, "cardNumber", "error.invalidCardNumber");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            try {
                group = new Integer(request.getParameter("group"));
            } catch (NullPointerException e) {
                saveErrorMessage(request, "group", "error.requiredGroup");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } catch (NumberFormatException e) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            if (!isValidCardNumber(cardNumber)) {
                saveErrorMessage(request, "cardNumber", "error.alreadyExistsCardNumber");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            if (!isValidGroup(group)) {
                saveErrorMessage(request, "group", "error.invalidGroup");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }

            ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
            String cardAlwaysValid = dynaForm.getString("cardAlwaysValid");
            if (cardAlwaysValid.equalsIgnoreCase("no")
                    && (parkingPartyBean.getCardStartDate() == null || parkingPartyBean.getCardEndDate() == null)) {
                saveErrorMessage(request, "mustFillInDates", "error.card.mustFillInDates");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            } else if (cardAlwaysValid.equalsIgnoreCase("yes")) {
                parkingPartyBean.setCardStartDate(null);
                parkingPartyBean.setCardEndDate(null);
            }

            if (parkingPartyBean.getCardStartDate() != null
                    && parkingPartyBean.getCardStartDate().isAfter(parkingPartyBean.getCardEndDate())) {
                saveErrorMessage(request, "invalidPeriod", "error.parkingParty.invalidPeriod");
                request.setAttribute("idInternal", parkingRequestID);
                return showRequest(mapping, actionForm, request, response);
            }
            args[1] = ParkingRequestState.ACCEPTED;
            args[2] = cardNumber;
            args[3] = group;
            args[5] = parkingPartyBean.getCardStartDate();
            args[6] = parkingPartyBean.getCardEndDate();
        } else if (request.getParameter("reject") != null) {
            args[1] = ParkingRequestState.REJECTED;
        } else {
            args[1] = ParkingRequestState.PENDING;
        }

        ServiceUtils.executeService(SessionUtils.getUserView(request), "UpdateParkingParty", args);
        if (request.getParameter("acceptPrint") != null) {
            request.setAttribute("parkingPartyID", parkingRequest.getParkingParty().getIdInternal());
            printParkingCard(mapping, actionForm, request, response);
        }
        return showParkingRequests(mapping, actionForm, request, response);
    }

    public ActionForward printParkingCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer parkingPartyID = getPopertyID(request, "parkingPartyID");
        final ParkingParty parkingParty = rootDomainObject.readParkingPartyByOID(parkingPartyID);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("imageUrl", getServlet().getServletContext().getRealPath("/").concat(
                "/images/LogoIST.gif"));

        Person person = (Person) parkingParty.getParty();
        parameters.put("number", getMostSignificantNumber(person));

        List<Person> persons = new ArrayList<Person>();
        persons.add(person);

        ReportsUtils.printReport("parkingManager.parkingCard", parameters, null, persons, 91, 58);
        return null;
    }

    public ActionForward exportToPDFParkingCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer parkingPartyID = getPopertyID(request, "parkingPartyID");
        final ParkingParty parkingParty = rootDomainObject.readParkingPartyByOID(parkingPartyID);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("imageUrl", getServlet().getServletContext().getRealPath("/").concat(
                "/images/Logo_IST_color.tiff"));

        Person person = (Person) parkingParty.getParty();
        parameters.put("number", getMostSignificantNumber(person));

        List<Person> persons = new ArrayList<Person>();
        persons.add(person);

        byte[] data = ReportsUtils.exportToPdf("parkingManager.parkingCard", parameters, null, persons);
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

    private String getMostSignificantNumber(Person p) {
        if (p.getParkingParty().getPhdNumber() != null) {
            return "Nº: " + p.getParkingParty().getPhdNumber();
        }
        if (p.getTeacher() != null && p.getTeacher().getCurrentWorkingDepartment() != null) {
            return "Nº Mec: " + p.getTeacher().getTeacherNumber();
        }
        if (p.getEmployee() != null && p.getEmployee().getCurrentWorkingContract() != null) {
            return "Nº Mec: " + p.getEmployee().getEmployeeNumber();
        }
        if (p.getStudent() != null) {
            DegreeType degreeType = p.getStudent().getMostSignificantDegreeType();
            Collection<Registration> registrations = p.getStudent().getRegistrationsByDegreeType(
                    degreeType);
            for (Registration registration : registrations) {
                StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
                if (scp != null) {
                    return "Nº: " + p.getStudent().getNumber();
                }
            }
        }
        if (p.getGrantOwner() != null && p.getGrantOwner().hasCurrentContract()) {
            return "Nº: " + p.getGrantOwner().getNumber();
        }
        return "";
    }

    private boolean isValidGroup(Integer groupId) {
        for (ParkingGroup group : rootDomainObject.getParkingGroups()) {
            if (group.getIdInternal().equals(groupId)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidCardNumber(Long cardNumber) {
        for (ParkingParty parkingParty : rootDomainObject.getParkingParties()) {
            if (parkingParty.getCardNumber() != null && parkingParty.getCardNumber().equals(cardNumber)) {
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

    public ActionForward prepareSearchParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("searchPartyBean", new SearchPartyBean());
        return mapping.findForward("searchParty");
    }

    public ActionForward showParkingPartyRequests(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("searchPartyBean");
        SearchPartyBean searchPartyBean = null;
        if (viewState != null) {
            searchPartyBean = (SearchPartyBean) viewState.getMetaObject().getObject();
        }

        if (searchPartyBean != null) {
            searchPartyBean.setParty(null);
            Object[] args = { searchPartyBean.getPartyName(), searchPartyBean.getCarPlateNumber() };
            List<Party> partyList = (List<Party>) ServiceUtils.executeService(SessionUtils
                    .getUserView(request), "SearchPartyCarPlate", args);
            request.setAttribute("searchPartyBean", searchPartyBean);
            request.setAttribute("partyList", partyList);

        } else if (request.getParameter("partyID") != null || request.getAttribute("partyID") != null) {
            final Integer idInternal = getPopertyID(request, "partyID");
            final String carPlateNumber = request.getParameter("plateNumber");
            Party party = rootDomainObject.readPartyByOID(idInternal);
            if (party.getParkingParty() != null) {
                prepapreParkingPartyDocumentsLinks(request, party.getParkingParty());
            }
            setupParkingRequests(request, party, carPlateNumber);
        }

        return mapping.findForward("showParkingPartyRequests");
    }

    private int getPopertyID(HttpServletRequest request, String property) {
        if (request.getParameter(property) != null) {
            return new Integer(request.getParameter(property));
        } else {
            return (Integer) request.getAttribute(property);
        }
    }

    private void setupParkingRequests(HttpServletRequest request, Party party, String carPlateNumber)
            throws FenixFilterException, FenixServiceException {
        if (party.getParkingParty() != null) {
            List<ParkingRequest> parkingRequests = new ArrayList<ParkingRequest>(party.getParkingParty()
                    .getParkingRequests());
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("creationDate"), true);
            comparatorChain.addComparator(new BeanComparator("parkingRequestState"));
            Collections.sort(parkingRequests, comparatorChain);
            request.setAttribute("parkingRequests", parkingRequests);
        }
        request.setAttribute("searchPartyBean", new SearchPartyBean(party, carPlateNumber));
    }

    public ActionForward prepareEditParkingParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer parkingPartyID = getPopertyID(request, "parkingPartyID");
        ParkingParty parkingParty = rootDomainObject.readParkingPartyByOID(parkingPartyID);
        request.setAttribute("parkingPartyBean", new ParkingPartyBean(parkingParty));
        request.setAttribute("parkingPartyID", parkingParty.getIdInternal());

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (parkingParty.getFirstCarMake() != null) {
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

    public ActionForward editParkingParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ParkingPartyBean parkingPartyBean = (ParkingPartyBean) getFactoryObject();
        request.setAttribute("parkingPartyID", parkingPartyBean.getParkingParty().getIdInternal());
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
        ServiceUtils.executeService(SessionUtils.getUserView(request), "ExecuteFactoryMethod",
                new Object[] { parkingPartyBean });
        request.setAttribute("partyID", parkingPartyBean.getParkingParty().getParty().getIdInternal());

        return showParkingPartyRequests(mapping, actionForm, request, response);
    }

    private boolean isRepeatedCardNumber(Long cardNumber, ParkingParty parkingParty) {
        for (ParkingParty tempParkingParty : rootDomainObject.getParkingParties()) {
            if (tempParkingParty.getCardNumber() != null
                    && tempParkingParty.getCardNumber().equals(cardNumber)
                    && tempParkingParty != parkingParty) {
                return false;
            }
        }
        return true;
    }
}