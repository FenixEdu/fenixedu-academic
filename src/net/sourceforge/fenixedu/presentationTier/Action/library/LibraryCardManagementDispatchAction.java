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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.person.RoleType;
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

    private final static int minimumPinNumber = 6910;
    
    private final static int maximumPinNumber = 100000;

    public ActionForward showUsers(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        LibraryCardSearch libraryCardSearch = (LibraryCardSearch) getRenderedObject("libraryCardSearch");

        if (libraryCardSearch == null) {
            libraryCardSearch = new LibraryCardSearch();
            libraryCardSearch.setCategory(RoleType.TEACHER);
        }

        //TODO o dto pode ter 1 lista de enum's quando os alunos entrarem ao barulho 
        request.setAttribute("libraryCardSearch", libraryCardSearch);
        return mapping.findForward("show-users");
    }

    public ActionForward prepareGenerateCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        Integer personID = new Integer(request.getParameter("personID"));
        Person person = (Person) rootDomainObject.readPartyByOID(personID);
        LibraryCardDTO libraryCardDTO = null;
        if (person.hasRole(RoleType.RESEARCHER) && !person.hasRole(RoleType.TEACHER)) {
            if (person.getEmployee().getCurrentWorkingContract() != null) {
                libraryCardDTO = new LibraryCardDTO(person, RoleType.RESEARCHER, person.getNickname(),
                        person.getEmployee().getLastWorkingPlace(), person.getEmployee()
                                .getEmployeeNumber());
            }
        } else if (person.hasRole(RoleType.TEACHER)) {
            Teacher teacher = person.getTeacher();
            if (!teacher.isInactive(ExecutionPeriod.readActualExecutionPeriod())) {
                libraryCardDTO = new LibraryCardDTO(person, RoleType.TEACHER, person.getNickname(), teacher
                        .getLastWorkingUnit(), teacher.getTeacherNumber());
            }
        } else if (person.hasRole(RoleType.EMPLOYEE)) {
            if (person.getEmployee().getCurrentWorkingContract() != null) {
                libraryCardDTO = new LibraryCardDTO(person, RoleType.EMPLOYEE, person.getNickname(), person
                        .getEmployee().getLastWorkingPlace(), person.getEmployee().getEmployeeNumber());
            }
        }

        libraryCardDTO.setUnlimitedCard(Boolean.TRUE);
        if (person.getName().length() > maxUserNameLength) {
            addMessage(request, "message.card.userName.tooLong",person.getName().length());
        }

        List<Integer> pinList = getExistingPins();

        Random random = new Random();
        while (true) {
            Integer pin = random.nextInt(maximumPinNumber);
            if (pin < minimumPinNumber) {
                continue;
            }
            if (!pinList.contains(pin)) {
                libraryCardDTO.setPin(pin);
                break;
            }
        }

        request.setAttribute("libraryCardDTO", libraryCardDTO);
        return mapping.findForward("edit-card");
    }

    public ActionForward createCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        
        if (RenderUtils.getViewState().isCanceled()) {
            return showUsers(mapping, actionForm, request, response);
        }

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject();

        if (libraryCardDTO.getUserName().length() > maxUserNameLength) {
            setError(request, "message.card.userName.tooLong",libraryCardDTO.getUserName().length());
            request.setAttribute("libraryCardDTO", libraryCardDTO);
            return mapping.findForward("edit-card");
        }

        LibraryCard libraryCard = (LibraryCard) ServiceUtils.executeService(SessionUtils
                .getUserView(request), "CreateLibraryCard", new Object[] { libraryCardDTO });
        request.setAttribute("libraryCardDTO", new LibraryCardDTO(libraryCard));

        return mapping.findForward("show-details");
    }

    public ActionForward generatePdfCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException,
            FenixFilterException, FenixServiceException {

        if (request.getParameter("cancel") != null) {
            return showUsers(mapping, actionForm, request, response);
        }

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject("libraryCardDTO");

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

        return mapping.findForward("show-details");
    }

    public ActionForward changeDateVisibility(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject();
        if (libraryCardDTO.getPerson().getName().length() > maxUserNameLength) {
            addMessage(request, "message.card.userName.tooLong",maxUserNameLength);
        }

        if (!libraryCardDTO.getUnlimitedCard()) {
            request.setAttribute("presentDate", "presentDate");
        } else {
            libraryCardDTO.setValidUntil(null);
        }
        request.setAttribute("libraryCardDTO", libraryCardDTO);
        RenderUtils.invalidateViewState();
        return mapping.findForward("edit-card");
    }

    public ActionForward invalidDate(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

        if (request.getParameter("cancel") != null) {
            return showUsers(mapping, actionForm, request, response);
        }

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject();
        request.setAttribute("libraryCardDTO", libraryCardDTO);
        request.setAttribute("presentDate", "presentDate");
        return mapping.findForward("edit-card");
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
        actionMessages.add("message", new ActionMessage(msg,parameter));
        saveMessages(request, actionMessages);
    }
    
    private void setError(HttpServletRequest request, String errorMsg, int parameter) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add("error", new ActionMessage(errorMsg, parameter));
        saveMessages(request, actionMessages);
    }
}
