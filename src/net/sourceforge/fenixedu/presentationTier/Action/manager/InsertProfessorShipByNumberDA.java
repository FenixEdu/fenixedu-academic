/*
 * Created on 22/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
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
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author lmac1
 */

public class InsertProfessorShipByNumberDA extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("insertProfessorShip");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String number = (String) dynaForm.get("number");

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer(number));
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(executionCourseId);
        InfoProfessorship infoProfessorShip = new InfoProfessorship();
        infoProfessorShip.setInfoExecutionCourse(infoExecutionCourse);
        infoProfessorShip.setInfoTeacher(infoTeacher);

        Object args[] = { infoProfessorShip, Boolean.FALSE };

        try {
            ServiceUtils.executeService(userView, "InsertProfessorShip", args);

        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(ex.getMessage(), mapping
                    .findForward("insertProfessorShip"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }
        return mapping.findForward("readTeacherInCharge");
    }
}