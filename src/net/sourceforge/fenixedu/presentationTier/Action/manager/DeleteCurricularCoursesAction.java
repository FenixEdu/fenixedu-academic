/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author lmac1
 */

public class DeleteCurricularCoursesAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm deleteForm = (DynaActionForm) form;

        List curricularCoursesIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        Object args[] = { curricularCoursesIds };

        List errorsList = new ArrayList();

        try {
            errorsList = (List) ServiceUtils.executeService(userView,
                    "DeleteCurricularCoursesOfDegreeCurricularPlan", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (!errorsList.isEmpty()) {
            int size = errorsList.size();
            int count = 0;
            String name, code;
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            while (count < size) {
                // Create an ACTION_ERROR for each CURRICULAR_COURSE
                name = (String) errorsList.get(count);
                code = (String) errorsList.get(count + 1);
                error = new ActionError("errors.invalid.delete.not.empty.curricular.course", name, code);
                actionErrors.add("errors.invalid.delete.not.empty.curricular.course", error);
                count = count + 2;
            }
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("readDegreeCurricularPlan");
    }
}