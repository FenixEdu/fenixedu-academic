/*
 * Created on Feb 18, 2004
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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

        IUserView userView = SessionUtils.getUserView(request);

        Object args[] = { userView.getUtilizador() };

        InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,
                "ReadTeacherByUsername", args);

        request.setAttribute("infoTeacher", infoTeacher);

        Integer degreeCurricularPlanId = new Integer(48);
        Object[] args1 = { degreeCurricularPlanId };
        List branches = null;
        try {
            branches = (List) ServiceUtils.executeService(userView,
                    "ReadBranchesByDegreeCurricularPlanId", args1);
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse);
        }

        request.setAttribute("branchList", branches);
        return mapping.findForward("submitTfcProposal");

    }
}