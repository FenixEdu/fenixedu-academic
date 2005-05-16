/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author lmac1
 */

public class SaveTeachersBodyAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
        DynaActionForm actionForm = (DynaActionForm) form;

        Integer[] responsibleTeachersIds = (Integer[]) actionForm.get("responsibleTeachersIds");
        Integer[] professorShipTeachersIds = (Integer[]) actionForm.get("professorShipTeachersIds");
        Integer[] nonAffiliatedTeachersIds = (Integer[]) actionForm.get("nonAffiliatedTeachersIds");
        List respTeachersIds = Arrays.asList(responsibleTeachersIds);
        List profTeachersIds = Arrays.asList(professorShipTeachersIds);
        List nonAffilTeachersIds = Arrays.asList(nonAffiliatedTeachersIds);
        // TODO: Collections.sort(profTeachersIds, new BeanComparator("name"));
        Object args[] = { respTeachersIds, profTeachersIds, executionCourseId };
        Boolean result;
        
        try {
            result = (Boolean) ServiceUtils.executeService(userView, "SaveTeachersBody", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping
                    .findForward("readCurricularCourse"));
        }
        
        Object args1[] = { nonAffilTeachersIds, executionCourseId };
        try {
            ServiceUtils.executeService(userView, "UpdateNonAffiliatedTeachersProfessorship", args1);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping
                    .findForward("readCurricularCourse"));
        }
        if (!result.booleanValue())
            throw new InvalidArgumentsActionException("message.non.existing.teachers");

        return mapping.findForward("readCurricularCourse");
    }
}