/*
 * Created on 22/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.InsertProfessorShip;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */

public class InsertProfessorShipByNumberDA extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final DynaActionForm form = (DynaValidatorForm) actionForm;
        final String teacherId = form.getString("id");
        final String executionCourseId = request.getParameter("executionCourseId");

        try {
            InsertProfessorShip.runInsertProfessorShip(executionCourseId, teacherId, Boolean.FALSE, 0.0);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping.findForward("insertProfessorShip"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }
        return mapping.findForward("readTeacherInCharge");
    }
}