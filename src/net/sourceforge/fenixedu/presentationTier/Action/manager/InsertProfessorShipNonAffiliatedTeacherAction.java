/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertProfessorShipNonAffiliatedTeacherAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException,
            NonExistingActionException {

        IUserView userView = UserView.getUser();

        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService("ReadInstitutions", null);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        }

        if (request.getAttribute("insertInstitution") != null) {
            request.setAttribute("insertInstitution", "true");
        }
        request.setAttribute("institutions", institutions);

        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insertInstitution(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, NonExistingActionException {

        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;
        String institutionName = (String) dynaForm.get("institutionName");

        try{
            ServiceUtils.executeService("InsertInstitution", new Object[] { institutionName });
        }catch(FenixServiceException e){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage(e.getMessage()));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }

        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService("ReadInstitutions", null);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        }

        request.setAttribute("institutions", institutions);

        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insertNonAffiliatedTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, NonExistingActionException {

        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer institutionID = (Integer) dynaForm.get("institutionID");
        String nonAffiliatedTeacherNameToInsert = (String) dynaForm
                .get("nonAffiliatedTeacherNameToInsert");

        
        if(nonAffiliatedTeacherNameToInsert == null || nonAffiliatedTeacherNameToInsert.equals("")){
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("errors.required", "Nome do Docente"));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }
        
        Object[] args = { nonAffiliatedTeacherNameToInsert, institutionID };

        try {
            ServiceUtils.executeService("InsertNonAffiliatedTeacher", args);
        } catch (NotExistingServiceException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage(e.getMessage()));
            saveMessages(request, actionMessages);
            return mapping.getInputForward();
        }        

        if (request.getAttribute("insertInstitution") != null) {
            request.setAttribute("insertInstitution", "true");
        }
        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService("ReadInstitutions", null);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        }

        request.setAttribute("institutions", institutions);
        return mapping.findForward("insertProfessorShip");        
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException,
            NonExistingActionException {

        IUserView userView = UserView.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;
        String nonAffiliatedTeacherNameToInsert = (String) dynaForm.get("nonAffiliatedTeacherName");

        List nonAffiliatedTeachers = (List) ServiceUtils.executeService(
                "ReadNonAffiliatedTeachersByName", new Object[] { nonAffiliatedTeacherNameToInsert });

        request.setAttribute("nonAffiliatedTeachers", nonAffiliatedTeachers);

        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService("ReadInstitutions", null);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        }

        request.setAttribute("institutions", institutions);

        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insertProfessorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, FenixActionException {

        IUserView userView = UserView.getUser();
        Integer executionCourseID = new Integer(request.getParameter("executionCourseId"));
        Integer nonAffiliatedTeacherID = new Integer(request.getParameter("nonAffiliatedTeacherID"));

        insertProfessorshipOperation(mapping, userView, executionCourseID, nonAffiliatedTeacherID);

        return mapping.findForward("readTeacherInCharge");
    }

    private void insertProfessorshipOperation(ActionMapping mapping, IUserView userView,
            Integer executionCourseID, Integer nonAffiliatedTeacherID) throws FenixFilterException,
            NonExistingActionException, FenixActionException {
        Object args[] = { nonAffiliatedTeacherID, executionCourseID };

        try {
            ServiceUtils.executeService("InsertProfessorShipNonAffiliatedTeacher", args);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }
    }
}
