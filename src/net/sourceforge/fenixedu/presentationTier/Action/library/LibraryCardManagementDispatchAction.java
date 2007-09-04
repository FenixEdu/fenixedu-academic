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
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class LibraryCardManagementDispatchAction extends FenixDispatchAction {

    public ActionForward showUsers(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        List<LibraryCardDTO> libraryCardsList = new ArrayList<LibraryCardDTO>();
        for (Employee employee : rootDomainObject.getEmployees()) {
            if (employee.getPerson().hasRole(RoleType.RESEARCHER)
                    && !employee.getPerson().hasRole(RoleType.TEACHER)) {
                if (employee.getCurrentWorkingContract() != null) {
                    libraryCardsList.add(new LibraryCardDTO(employee.getPerson(), RoleType.RESEARCHER,
                            employee.getPerson().getNickname(), employee.getLastWorkingPlace(), employee
                                    .getEmployeeNumber()));
                }
            } else if (employee.getPerson().hasRole(RoleType.TEACHER)) {
                Teacher teacher = employee.getPerson().getTeacher();
                if (!teacher.isInactive(ExecutionPeriod.readActualExecutionPeriod())) {
                    libraryCardsList.add(new LibraryCardDTO(employee.getPerson(), RoleType.TEACHER,
                            employee.getPerson().getNickname(), teacher.getLastWorkingUnit(), teacher
                                    .getTeacherNumber()));
                }
            } else if (employee.getPerson().hasRole(RoleType.EMPLOYEE)) {
                if (employee.getCurrentWorkingContract() != null) {
                    libraryCardsList.add(new LibraryCardDTO(employee.getPerson(), RoleType.EMPLOYEE,
                            employee.getPerson().getNickname(), employee.getLastWorkingPlace(), employee
                                    .getEmployeeNumber()));
                }
            }
        }
        //TODO o dto pode ter 1 lista de enum's quando os alunos entrarem ao barulho 
        request.setAttribute("libraryCardsList", libraryCardsList);
        return mapping.findForward("show-users");
    }

    public ActionForward prepareGenerateCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        Integer personID = new Integer(request.getParameter("personID"));
        Person person = (Person) rootDomainObject.readPartyByOID(personID);
        LibraryCardDTO libraryCard = null;
        if (person.hasRole(RoleType.RESEARCHER) && !person.hasRole(RoleType.TEACHER)) {
            if (person.getEmployee().getCurrentWorkingContract() != null) {
                libraryCard = new LibraryCardDTO(person, RoleType.RESEARCHER, person.getNickname(),
                        person.getEmployee().getLastWorkingPlace(), person.getEmployee()
                                .getEmployeeNumber());
            }
        } else if (person.hasRole(RoleType.TEACHER)) {
            Teacher teacher = person.getTeacher();
            if (!teacher.isInactive(ExecutionPeriod.readActualExecutionPeriod())) {
                libraryCard = new LibraryCardDTO(person, RoleType.TEACHER, person.getNickname(), teacher
                        .getLastWorkingUnit(), teacher.getTeacherNumber());
            }
        } else if (person.hasRole(RoleType.EMPLOYEE)) {
            if (person.getEmployee().getCurrentWorkingContract() != null) {
                libraryCard = new LibraryCardDTO(person, RoleType.EMPLOYEE, person.getNickname(), person
                        .getEmployee().getLastWorkingPlace(), person.getEmployee().getEmployeeNumber());
            }
        }

        if (person.getName().length() > 42) {
            addMessage(request, "message.card.userName.tooLong");
        }
        if (libraryCard.getUnitName().length() > 35) {
            addMessage(request, "message.card.unitName.tooLong");
            request.setAttribute("unitsNameList", "unitsNameList");
        }
        List<Integer> pinList = getExistingPins();

        Random random = new Random();
        while (true) {
            Integer pin = random.nextInt(10000);
            if (!pinList.contains(pin)) {
                libraryCard.setPin(pin);
                break;
            }
        }

        request.setAttribute("libraryCard", libraryCard);
        return mapping.findForward("edit-card");
    }

    public ActionForward generateCard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

        LibraryCardDTO libraryCardDTO = (LibraryCardDTO) getRenderedObject();        
        boolean existErrors = Boolean.FALSE;
        if (libraryCardDTO.getPerson().getName().length() > 42) {
            setError(request, "message.card.userName.tooLong");
            existErrors = Boolean.TRUE;
        }
        if (libraryCardDTO.getUnitName().length() > 35) {
            setError(request, "message.card.unitName.tooLong");
            request.setAttribute("unitsNameList", "unitsNameList");
            existErrors = Boolean.TRUE;
        }

        if(existErrors) {
            request.setAttribute("libraryCard", libraryCardDTO);
            return mapping.findForward("edit-card");
        }
        
//      ServiceUtils.executeService(userView, serviceName, serviceArgs)

        
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

    private List<Integer> getExistingPins() {
        List<Integer> pins = new ArrayList<Integer>();
        for (LibraryCard libraryCard : rootDomainObject.getLibraryCards()) {
            if (libraryCard.getPin() != null) {
                pins.add(libraryCard.getPin());
            }
        }
        return pins;
    }

    private void addMessage(HttpServletRequest request, String msg) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add("message", new ActionMessage(msg));
        saveMessages(request, actionMessages);
    }

    private void setError(HttpServletRequest request, String errorMsg) {
        ActionMessages actionMessages = getMessages(request);
        actionMessages.add("error", new ActionMessage(errorMsg));
        saveMessages(request, actionMessages);
    }

}
