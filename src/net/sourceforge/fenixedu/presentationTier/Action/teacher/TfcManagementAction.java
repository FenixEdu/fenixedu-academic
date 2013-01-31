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
import net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher.ReadTeacherByUsername;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadBranchesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class TfcManagementAction extends FenixDispatchAction {
	public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("");
	}

	public ActionForward prepareTfcInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {

		IUserView userView = UserView.getUser();

		InfoTeacher infoTeacher = ReadTeacherByUsername.run(userView.getUtilizador());

		request.setAttribute("infoTeacher", infoTeacher);

		Integer degreeCurricularPlanId = new Integer(48);

		List branches = null;
		try {
			branches = ReadBranchesByDegreeCurricularPlan.run(degreeCurricularPlanId);
		} catch (FenixServiceException fse) {
			throw new FenixActionException(fse);
		}

		request.setAttribute("branchList", branches);
		return mapping.findForward("submitTfcProposal");

	}
}