/*
 * Created on 31/Jul/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlansAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm deleteForm = (DynaActionForm) form;

        List degreeCurricularPlansIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        Object args[] = { degreeCurricularPlansIds };

        List errorNames = new ArrayList();

        try {
            errorNames = (List) ServiceUtils.executeService(userView, "DeleteDegreeCurricularPlans",
                    args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        if (!errorNames.isEmpty()) {
            ActionErrors actionErrors = new ActionErrors();
            Iterator namesIter = errorNames.iterator();
            ActionError error = null;
            while (namesIter.hasNext()) {
                // Create an ACTION_ERROR for each DEGREE_CURRICULAR_PLAN
                error = new ActionError("errors.invalid.delete.not.empty.degree.curricular.plan",
                        namesIter.next());
                actionErrors.add("errors.invalid.delete.not.empty.degree.curricular.plan", error);
            }
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("readDegree");
    }
}