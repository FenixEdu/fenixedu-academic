/*
 * Created on 22/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */

public class InsertProfessorShipByNumberDA extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        final DynaActionForm form = (DynaValidatorForm) actionForm;
        
        final Integer teacherNumber = Integer.valueOf(form.getString("number"));
        final Integer executionCourseId = Integer.valueOf(request.getParameter("executionCourseId"));
        
        try {
            final Object args[] = { executionCourseId, teacherNumber, Boolean.FALSE, 0.0 };
            ServiceUtils.executeService(getUserView(request), "InsertProfessorShip", args);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping.findForward("insertProfessorShip"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }
        return mapping.findForward("readTeacherInCharge");
    }
}