package ServidorApresentacao.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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

public class SeeStudentAndCurricularPlansDispatchAction extends DispatchAction {

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		request.setAttribute("docIDTypeList", TipoDocumentoIdentificacao.toIntegerArrayList());
		return mapping.findForward("start");
	}

	public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession();

		String idNumber1 = this.getFromRequest("idNumber", request);
		String idType1 = this.getFromRequest("idType", request);
		String studentName1 = this.getFromRequest("studentName", request);
		String studentNumber1 = this.getFromRequest("studentNumber", request);

		String idNumber2 = null;
		TipoDocumentoIdentificacao idType2 = null;
		String studentName2 = null;
		Integer studentNumber2 = null;

		if(!idNumber1.equals("")) {
			idNumber2 = idNumber1;
		}

		if(!idType1.equals("")) {
			idType2 = new TipoDocumentoIdentificacao(new Integer(idType1));
		}

		if(!studentName1.equals("")) {
			studentName2 = studentName1;
		}

		if(!studentNumber1.equals("")) {
			try {
				studentNumber2 = new Integer(studentNumber1);
			} catch (NumberFormatException e) {
				ActionError actionError = new ActionError("error.numberFormat");
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add("idNumber", actionError);
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
		}
		
		if(!this.isValid(mapping, request, idNumber2, idType2)) {
			return mapping.getInputForward();
		}

		Object args[] = { studentName2, idNumber2, idType2, studentNumber2 };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		ArrayList studentList = null;
		try {
			studentList = (ArrayList) ServiceUtils.executeService(userView, "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args);
			if(studentList != null && !studentList.isEmpty()) {
				this.sort(studentList);
			}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("studentList", studentList);

		return mapping.findForward("viewStudents");
	}

	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}

	private void sort(List listOfInfoStudents) {

		ComparatorChain comparatorChain = new ComparatorChain();
		comparatorChain.addComparator(new BeanComparator("number"));
		comparatorChain.addComparator(new BeanComparator("infoPerson.nome"));
		if(listOfInfoStudents != null) {
			Collections.sort(listOfInfoStudents, comparatorChain);
		}
	}

	private boolean isValid(ActionMapping mapping, HttpServletRequest request, String idNumber, TipoDocumentoIdentificacao idType) {
		boolean result = true;

		if(idNumber != null && idType == null) {
			ActionError actionError = new ActionError("error.required.idType");
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add("idType", actionError);
			saveErrors(request, actionErrors);
			result = false;
		} else if(idNumber == null && idType != null) {
			ActionError actionError = new ActionError("error.required.idNumber");
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add("idNumber", actionError);
			saveErrors(request, actionErrors);
			result = false;
		}
		return result;
	}
}