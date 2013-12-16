/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteExecutionDegreesOfDegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/deleteExecutionDegrees", input = "/readDegreeCurricularPlanInput.do",
        attribute = "executionDegreeForm", formBean = "executionDegreeForm", scope = "request")
@Forwards(value = { @Forward(name = "readDegreeCurricularPlan", path = "/readDegreeCurricularPlan.do") })
public class DeleteExecutionDegreesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();
        DynaActionForm deleteForm = (DynaActionForm) form;

        List executionDegreesIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        List errorsList = new ArrayList();

        try {
            errorsList = DeleteExecutionDegreesOfDegreeCurricularPlan.run(executionDegreesIds);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (!errorsList.isEmpty()) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            Iterator iter = errorsList.iterator();
            while (iter.hasNext()) {
                // Create an ACTION_ERROR for each EXECUTION_DEGREE
                error = new ActionError("errors.invalid.delete.not.empty.execution.degree", iter.next());
                actionErrors.add("errors.invalid.delete.not.empty.execution.degree", error);
            }
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("readDegreeCurricularPlan");
    }
}