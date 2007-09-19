package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardSearch;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class LibraryCardManagementDispatchAction extends FenixDispatchAction {

    private final static int maxUserNameLength = 47;

    private final static int maxUnitNameLength = 42;

    private final static int minimumPinNumber = 6910;

    private final static int maximumPinNumber = 100000;

    public ActionForward showUsers(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        LibraryCardSearch libraryCardSearch = (LibraryCardSearch) getRenderedObject("libraryCardSearch");

        if (libraryCardSearch == null) {
            libraryCardSearch = (LibraryCardSearch) getRenderedObject();
            if (libraryCardSearch == null) {
                libraryCardSearch = new LibraryCardSearch();
                libraryCardSearch.setPartyClassification(PartyClassification.TEACHER);
            }
        }

        libraryCardSearch.getSearch();
        RenderUtils.invalidateViewState();
        request.setAttribute("libraryCardSearch", libraryCardSearch);
        return mapping.findForward("show-users");
    }

    public ActionForward prepareGenerateCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        Integer personID = new Integer(request.getParameter("personID"));
        Person person = (Person) rootDomainObject.readPartyByOID(personID);
        PartyClassification partyClassification = person.getPartyClassification();
        LibraryCardDTO libraryCardDTO = new LibraryCardDTO(person, partyClassification);

        if (!partyClassification.equals(PartyClassification.EMPLOYEE)
                && !partyClassification.equals(PartyClassification.TEACHER)) {
            request.setAttribute("presentDate", "presentDate");
        }

        if (!libraryCardDTO.isStudent()) {
            libraryCardDTO.setUnlimitedCard(Boolean.TRUE);
        } else {
            libraryCardDTO.setUnlimitedCard(Boolean.FALSE);
        }
        if (person.getName().length() > maxUserNameLength) {
            addMessage(request, "message.card.userName.tooLong", person.getName().length());
        }
        if (partyClassification.equals(PartyClassification.EMPLOYEE)) {
            if (libraryCardDTO.getUnitName().length() > maxUnitNameLength) {
                addMessage(request, "message.card.unitName.tooLong", libraryCardDTO.getUnitName()
                        .length());
            }
            libraryCardDTO.setChosenUnitName(libraryCardDTO.getUnitName());
            request.setAttribute("employee", "employee");
        }
        List<Integer> pinList = getExistingPins();

        Random random = new Random();
        while (true) {
            Integer pin = random.nextInt(maximumPinNumber);
            if (pin <= minimumPinNumber) {
                continue;
            }
            if (!pinList.contains(pin)) {
                libraryCardDTO.setPin(pin);
                break;
            }
        }

        request.setAttribute("libraryCardSearch", new LibraryCardSearch(partyClassification));
        request.setAttribute("libraryCardDTO", libraryCardDTO);
        return mapping.findForward("create-card");
    }

    public ActionForward createCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        if (RenderUtils.getViewState().isCanceled()) {
            return showUsers(mapping, actionForm, request, response);
        }

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("libraryCardToCreate");

        boolean validationError = validateNamesMaxLength(request, libraryCardDTO);
        if (validationError) {
            request.setAttribute("libraryCardDTO", libraryCardDTO);
            return mapping.findForward("create-card");
        }

        LibraryCard libraryCard = (LibraryCard) ServiceUtils.executeService(SessionUtils
                .getUserView(request), "CreateLibraryCard", new Object[] { libraryCardDTO });
        request.setAttribute("libraryCardDTO", new LibraryCardDTO(libraryCard));
        request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson()
                .getPartyClassification()));
        return mapping.findForward("show-details");
    }

    public ActionForward generatePdfCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        if (request.getParameter("cancel") != null) {
            return showUsers(mapping, actionForm, request, response);
        }

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("libraryCardDTO");

        if (request.getParameter("modify") != null) {
            libraryCardDTO = getRenderedObject("libraryCardEdit") != null ? (LibraryCardDTO) getRenderedObject("libraryCardEdit")
                    : libraryCardDTO;
            request.setAttribute("libraryCardDTO", libraryCardDTO);
            request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson()
                    .getPartyClassification()));
            //RenderUtils.invalidateViewState();
            return mapping.findForward("edit-card");
        }

        ServiceUtils.executeService(SessionUtils.getUserView(request), "MarkLibraryCardAsEmited",
                new Object[] { libraryCardDTO.getLibraryCard() });

        List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
        cardList.add(libraryCardDTO);
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources",
                LanguageUtils.getLocale());
        byte[] data = ReportsUtils.exportToPdf("net.sourceforge.fenixedu.domain.library.LibrabryCard",
                null, bundle, cardList);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=cartao.pdf");
        response.setContentLength(data.length);
        ServletOutputStream writer = response.getOutputStream();
        writer.write(data);
        writer.flush();
        writer.close();
        response.flushBuffer();

        return null;
    }

    public ActionForward editCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        if (request.getParameter("cancel") != null) {
            return showUsers(mapping, actionForm, request, response);
        }

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("libraryCardEdit");

        boolean validationError = validateNamesMaxLength(request, libraryCardDTO);
        if (validationError) {
            request.setAttribute("libraryCardDTO", libraryCardDTO);
            request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson()
                    .getPartyClassification()));
            RenderUtils.invalidateViewState();
            return mapping.findForward("edit-card");
        }

        LibraryCard libraryCard = (LibraryCard) ServiceUtils.executeService(SessionUtils
                .getUserView(request), "EditLibraryCard", new Object[] { libraryCardDTO });

        request.setAttribute("libraryCardDTO", new LibraryCardDTO(libraryCard));
        request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson()
                .getPartyClassification()));

        RenderUtils.invalidateViewState();
        return mapping.findForward("show-details");
    }

    public ActionForward prepareGenerateMissingCards(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        return mapping.findForward("generate-missing-cards");
    }

    public ActionForward generateMissingCards(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
        for (LibraryCard libraryCard : rootDomainObject.getLibraryCards()) {
            if (!libraryCard.getIsCardEmited()) {
                cardList.add(new LibraryCardDTO(libraryCard));
            }
        }

        if (!cardList.isEmpty()) {
            ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "MarkLibraryCardListAsEmited", new Object[] { cardList });

            final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources",
                    LanguageUtils.getLocale());
            byte[] data = ReportsUtils.exportToPdf(
                    "net.sourceforge.fenixedu.domain.library.LibrabryCard", null, bundle, cardList);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=cartoes.pdf");
            response.setContentLength(data.length);
            ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();
            response.flushBuffer();

            return null;
        } else {
            request.setAttribute("nothingMissing", "nothingMissing");
            return mapping.findForward("generate-missing-cards");
        }
    }

    public ActionForward generatePdfLetter(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        Integer libraryCardID = new Integer(request.getParameter("libraryCardID"));

        LibraryCard libraryCard = rootDomainObject.readLibraryCardByOID(libraryCardID);

        ServiceUtils.executeService(SessionUtils.getUserView(request), "MarkLibraryCardLetterAsEmited",
                new Object[] { libraryCard });

        List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
        cardList.add(new LibraryCardDTO(libraryCard));
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources",
                LanguageUtils.getLocale());
        byte[] data = ReportsUtils.exportToPdf(
                "net.sourceforge.fenixedu.domain.library.LibrabryCard.letter", null, bundle, cardList);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=carta.pdf");
        response.setContentLength(data.length);
        ServletOutputStream writer = response.getOutputStream();
        writer.write(data);
        writer.flush();
        writer.close();
        response.flushBuffer();

        return null;
    }

    public ActionForward prepareGenerateMissingLetters(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        return mapping.findForward("generate-missing-letters");
    }

    public ActionForward generateMissingLetters(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        List<LibraryCardDTO> cardList = new ArrayList<LibraryCardDTO>();
        for (LibraryCard libraryCard : rootDomainObject.getLibraryCards()) {
            if (!libraryCard.getIsLetterGenerated()) {
                cardList.add(new LibraryCardDTO(libraryCard));
            }
        }

        if (!cardList.isEmpty()) {
            ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "MarkLibraryCardListLettersAsEmited", new Object[] { cardList });

            final ResourceBundle bundle = ResourceBundle.getBundle("resources.LibraryResources",
                    LanguageUtils.getLocale());
            byte[] data = ReportsUtils.exportToPdf(
                    "net.sourceforge.fenixedu.domain.library.LibrabryCard.letter", null, bundle,
                    cardList);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=cartas.pdf");
            response.setContentLength(data.length);
            ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();
            response.flushBuffer();

            return null;
        } else {
            request.setAttribute("nothingMissing", "nothingMissing");
            return mapping.findForward("generate-missing-letters");
        }
    }

    public ActionForward showDetails(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

        Integer libraryCardID = new Integer(request.getParameter("libraryCardID"));
        LibraryCard libraryCard = rootDomainObject.readLibraryCardByOID(libraryCardID);
        request.setAttribute("libraryCardDTO", new LibraryCardDTO(libraryCard));
        request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCard.getPerson()
                .getPartyClassification()));
        return mapping.findForward("show-details");
    }

    public ActionForward changeDateVisibility(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("validUntil");
        if (libraryCardDTO.getPerson().getName().length() > maxUserNameLength) {
            addMessage(request, "message.card.userName.tooLong", maxUserNameLength);
        }

        if (!libraryCardDTO.getUnlimitedCard()) {
            request.setAttribute("presentDate", "presentDate");
        } else {
            libraryCardDTO.setValidUntil(null);
        }
        request.setAttribute("libraryCardDTO", libraryCardDTO);
        request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson()
                .getPartyClassification()));
        RenderUtils.invalidateViewState();
        return mapping.findForward("create-card");
    }

    public ActionForward invalidDate(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

        if (request.getParameter("cancel") != null) {
            return showUsers(mapping, actionForm, request, response);
        }

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("validUntil");
        request.setAttribute("libraryCardDTO", libraryCardDTO);
        request.setAttribute("libraryCardSearch", new LibraryCardSearch(libraryCardDTO.getPerson()
                .getPartyClassification()));
        request.setAttribute("presentDate", "presentDate");
        return mapping.findForward("create-card");
    }

    private boolean validateNamesMaxLength(HttpServletRequest request, LibraryCardDTO libraryCardDTO) {
        boolean validationError = Boolean.FALSE;
        if (libraryCardDTO.getUserName().length() > maxUserNameLength) {
            setError(request, "message.card.userName.tooLong", libraryCardDTO.getUserName().length());
            validationError = Boolean.TRUE;
        }
        if (libraryCardDTO.getPerson().getPartyClassification().equals(PartyClassification.EMPLOYEE)) {
            if (libraryCardDTO.getChosenUnitName().length() > maxUnitNameLength) {
                setError(request, "message.card.unitName.tooLong", libraryCardDTO.getUnitName().length());
                validationError = Boolean.TRUE;
            }
        }
        return validationError;
    }

    private List<Integer> getExistingPins() {
        List<Integer> pins = new ArrayList<Integer>();
        for (LibraryCard libraryCard : rootDomainObject.getLibraryCards()) {
            if (libraryCard.getPin() != null) {
                pins.add(libraryCard.getPin());
            }
        }
        return pins;
    }

    private void addMessage(HttpServletRequest request, String msg, int parameter) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add("message", new ActionMessage(msg, parameter));
        saveMessages(request, actionMessages);
    }

    private void setError(HttpServletRequest request, String errorMsg, int parameter) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add("error", new ActionMessage(errorMsg, parameter));
        saveMessages(request, actionMessages);
    }
}
