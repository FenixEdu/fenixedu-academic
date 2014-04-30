package net.sourceforge.fenixedu.presentationTier.Action.manager.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.InsertProfessorShip;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/insertProfessorShipByNumber",
        input = "/insertProfessorShipByNumber.do?method=prepareInsert&page=0", formBean = "masterDegreeCreditsForm",
        functionality = MasterDegreeCreditsManagementDispatchAction.class)
@Forwards(value = { @Forward(name = "insertProfessorShip", path = "/manager/insertTeacherByNumber_bd.jsp"),
        @Forward(name = "readTeacherInCharge", path = "/scientificCouncil/readTeacherInCharge.do") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "presentationTier.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertProfessorShipByNumberDAForScientificCouncil extends FenixDispatchAction {

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