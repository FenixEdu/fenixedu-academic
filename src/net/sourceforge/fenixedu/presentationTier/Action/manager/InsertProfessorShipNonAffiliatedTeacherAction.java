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
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertProfessorShipNonAffiliatedTeacherAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException,
            NonExistingActionException {

        IUserView userView = SessionUtils.getUserView(request);

        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService(userView, "ReadInstitutions", null);

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

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;
        String institutionName = (String) dynaForm.get("institutionName");

        ServiceUtils.executeService(userView, "InsertInstitution", new Object[] { institutionName });

        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService(userView, "ReadInstitutions", null);

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

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer institutionID = (Integer) dynaForm.get("institutionID");
        String nonAffiliatedTeacherNameToInsert = (String) dynaForm
                .get("nonAffiliatedTeacherNameToInsert");

        Object[] args = { nonAffiliatedTeacherNameToInsert, institutionID };

        ServiceUtils.executeService(userView, "InsertNonAffiliatedTeacher", args);

        if (request.getAttribute("insertInstitution") != null) {
            request.setAttribute("insertInstitution", "true");
        }
        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService(userView, "ReadInstitutions", null);

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

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;
        String nonAffiliatedTeacherNameToInsert = (String) dynaForm.get("nonAffiliatedTeacherName");

        List nonAffiliatedTeachers = (List) ServiceUtils.executeService(userView,
                "ReadNonAffiliatedTeachersByName", new Object[] { nonAffiliatedTeacherNameToInsert });

        request.setAttribute("nonAffiliatedTeachers", nonAffiliatedTeachers);

        List institutions = null;
        try {
            institutions = (List) ServiceUtils.executeService(userView, "ReadInstitutions", null);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        }

        request.setAttribute("institutions", institutions);

        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insertProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException,
            FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionCourseID = new Integer(request.getParameter("executionCourseId"));
        Integer nonAffiliatedTeacherID = new Integer(request.getParameter("nonAffiliatedTeacherID"));
        System.out.println("vamos la verrrrrrr: " + nonAffiliatedTeacherID);        

        Object args[] = { nonAffiliatedTeacherID, executionCourseID };

        try {
            ServiceUtils.executeService(userView, "InsertProfessorShipNonAffiliatedTeacher", args);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }
        
        return mapping.findForward("readTeacherInCharge");
    }

}
