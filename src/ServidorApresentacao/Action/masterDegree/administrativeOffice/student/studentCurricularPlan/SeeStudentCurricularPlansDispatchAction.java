package ServidorApresentacao.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoDocumentoIdentificacao;

/**
 * @author David Santos
 * 2/Out/2003
 */

public class SeeStudentCurricularPlansDispatchAction extends DispatchAction {

	public ActionForward prepareReading(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession();

		String idNumber = this.getFromRequest("idNumber", request);
		TipoDocumentoIdentificacao idType = new TipoDocumentoIdentificacao(new Integer(this.getFromRequest("idType", request)));
		String studentName = this.getFromRequest("studentName", request);
		Integer studentNumber = new Integer(this.getFromRequest("studentNumber", request));

		Object args[] = { studentName, idNumber, idType, studentNumber };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		ArrayList studentList = null;
		try {
			studentList = (ArrayList) ServiceUtils.executeService(userView, "ReadStudentsByExecutionDegreeAndExecutionYear", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("studentList", studentList);

		return mapping.findForward("PrepareSuccess");
	}

	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
}