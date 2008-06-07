/*
 * Created on Feb 18, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class TfcManagementAction extends FenixDispatchAction {
    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("");
    }

    public ActionForward prepareTfcInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixServiceException, FenixFilterException {

        IUserView userView = UserView.getUser();

        Object args[] = { userView.getUtilizador() };

        InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(
                "ReadTeacherByUsername", args);

        request.setAttribute("infoTeacher", infoTeacher);

        Integer degreeCurricularPlanId = new Integer(48);
        Object[] args1 = { degreeCurricularPlanId };
        List branches = null;
        try {
            branches = (List) ServiceUtils.executeService(
                    "ReadBranchesByDegreeCurricularPlanId", args1);
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse);
        }

        request.setAttribute("branchList", branches);
        return mapping.findForward("submitTfcProposal");

    }
}