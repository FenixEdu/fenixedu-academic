/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.InsertNonAffiliatedTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.InsertProfessorShipNonAffiliatedTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadInstitutions;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadNonAffiliatedTeachersByName;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Mapping(module = "manager", path = "/insertProfessorShipNonAffiliatedTeacher",
        input = "/insertProfessorShipNonAffiliatedTeacher.do?method=prepare&page=0", attribute = "nonAffiliatedTeacherForm",
        formBean = "nonAffiliatedTeacherForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "insertProfessorShip", path = "/manager/insertNonAffiliatedTeacher_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/teachersBodyModificationNavLocalManager.jsp")),
        @Forward(name = "readTeacherInCharge", path = "/readTeacherInCharge.do?page=0") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertProfessorShipNonAffiliatedTeacherAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, NonExistingActionException {

        List institutions = ReadInstitutions.run();

        if (request.getAttribute("insertInstitution") != null) {
            request.setAttribute("insertInstitution", "true");
        }
        request.setAttribute("institutions", institutions);

        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insertInstitution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, NonExistingActionException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        String institutionName = (String) dynaForm.get("institutionName");

        try {
            InsertInstitution.run(institutionName);
        } catch (FenixServiceException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage(e.getMessage()));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }

        List institutions = ReadInstitutions.run();
        request.setAttribute("institutions", institutions);

        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insertNonAffiliatedTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, NonExistingActionException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        String institutionID = (String) dynaForm.get("institutionID");
        String nonAffiliatedTeacherNameToInsert = (String) dynaForm.get("nonAffiliatedTeacherNameToInsert");

        if (nonAffiliatedTeacherNameToInsert == null || nonAffiliatedTeacherNameToInsert.equals("")) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("errors.required", "Nome do Docente"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }

        try {
            InsertNonAffiliatedTeacher.runInsertNonAffiliatedTeacher(nonAffiliatedTeacherNameToInsert, institutionID);
        } catch (NotExistingServiceException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage(e.getMessage()));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }

        if (request.getAttribute("insertInstitution") != null) {
            request.setAttribute("insertInstitution", "true");
        }
        List institutions = ReadInstitutions.run();

        request.setAttribute("institutions", institutions);
        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException, NonExistingActionException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        String nonAffiliatedTeacherNameToInsert = (String) dynaForm.get("nonAffiliatedTeacherName");

        List nonAffiliatedTeachers =
                ReadNonAffiliatedTeachersByName.runReadNonAffiliatedTeachersByName(nonAffiliatedTeacherNameToInsert);

        request.setAttribute("nonAffiliatedTeachers", nonAffiliatedTeachers);

        List institutions = null;
        institutions = ReadInstitutions.run();

        request.setAttribute("institutions", institutions);

        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insertProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixActionException {

        User userView = Authenticate.getUser();
        String executionCourseID = request.getParameter("executionCourseId");
        String nonAffiliatedTeacherID = request.getParameter("nonAffiliatedTeacherID");

        insertProfessorshipOperation(mapping, userView, executionCourseID, nonAffiliatedTeacherID);

        return mapping.findForward("readTeacherInCharge");
    }

    private void insertProfessorshipOperation(ActionMapping mapping, User userView, String executionCourseID,
            String nonAffiliatedTeacherID) throws NonExistingActionException, FenixActionException {

        try {
            InsertProfessorShipNonAffiliatedTeacher.runInsertProfessorShipNonAffiliatedTeacher(nonAffiliatedTeacherID,
                    executionCourseID);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }
    }
}