/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.SaveTeachersBody;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.UpdateNonAffiliatedTeachersProfessorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author lmac1
 */

public class SaveTeachersBodyAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        IUserView userView = UserView.getUser();
        String executionCourseId = request.getParameter("executionCourseId");
        DynaActionForm actionForm = (DynaActionForm) form;

        String[] responsibleTeachersIds = (String[]) actionForm.get("responsibleTeachersIds");
        String[] professorShipTeachersIds = (String[]) actionForm.get("professorShipTeachersIds");
        String[] nonAffiliatedTeachersIds = (String[]) actionForm.get("nonAffiliatedTeachersIds");

        List<String> respTeachersIds = Arrays.asList(responsibleTeachersIds);
        List<String> profTeachersIds = Arrays.asList(professorShipTeachersIds);
        List<String> nonAffilTeachersIds = Arrays.asList(nonAffiliatedTeachersIds);

        // TODO: Collections.sort(profTeachersIds, new BeanComparator("name"));
        Boolean result;

        try {
            result = SaveTeachersBody.runSaveTeachersBody(respTeachersIds, profTeachersIds, executionCourseId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readCurricularCourse"));
        }

        try {
            UpdateNonAffiliatedTeachersProfessorship.runUpdateNonAffiliatedTeachersProfessorship(nonAffilTeachersIds,
                    executionCourseId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readCurricularCourse"));
        }
        if (!result.booleanValue()) {
            throw new InvalidArgumentsActionException("message.non.existing.teachers");
        }

        return mapping.findForward("readCurricularCourse");
    }
}