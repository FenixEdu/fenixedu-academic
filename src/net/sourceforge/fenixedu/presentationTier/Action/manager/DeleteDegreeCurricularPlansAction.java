/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteDegreeCurricularPlans;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/deleteDegreeCurricularPlans", input = "/readDegreeInput.do",
        attribute = "degreeCurricularPlanForm", formBean = "degreeCurricularPlanForm", scope = "request")
@Forwards(value = { @Forward(name = "readDegree", path = "/readDegree.do") })
public class DeleteDegreeCurricularPlansAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        IUserView userView = UserView.getUser();
        DynaActionForm deleteForm = (DynaActionForm) form;

        List degreeCurricularPlansIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        List errorNames = new ArrayList();

        try {
            errorNames = DeleteDegreeCurricularPlans.run(degreeCurricularPlansIds);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        if (!errorNames.isEmpty()) {
            ActionErrors actionErrors = new ActionErrors();
            Iterator namesIter = errorNames.iterator();
            ActionError error = null;
            while (namesIter.hasNext()) {
                // Create an ACTION_ERROR for each DEGREE_CURRICULAR_PLAN
                error = new ActionError("errors.invalid.delete.not.empty.degree.curricular.plan", namesIter.next());
                actionErrors.add("errors.invalid.delete.not.empty.degree.curricular.plan", error);
            }
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("readDegree");
    }
}