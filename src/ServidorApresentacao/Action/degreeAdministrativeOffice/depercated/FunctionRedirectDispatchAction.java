package ServidorApresentacao.Action.degreeAdministrativeOffice.depercated;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 */

public class FunctionRedirectDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "chooseStudentAndDegreeTypeForEnrolmentWithRules", "chooseStudentAndDegreeTypeForEnrolmentWithoutRules", "chooseStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules", "chooseStudentAndDegreeTypeForManualEquivalence" };

	public ActionForward chooseStudentAndDegreeTypeForEnrolmentWithRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.getDegreeTypeList(request);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward chooseStudentAndDegreeTypeForEnrolmentWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.getDegreeTypeList(request);
		return mapping.findForward(forwards[1]);
	}

	public ActionForward chooseStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.getDegreeTypeList(request);
		return mapping.findForward(forwards[2]);
	}

	public ActionForward chooseStudentAndDegreeTypeForManualEquivalence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.getDegreeTypeList(request);
		return mapping.findForward(forwards[3]);
	}
	
	private void getDegreeTypeList(HttpServletRequest request) {
//		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		List degreeTypeList = new ArrayList();
		degreeTypeList.add(new LabelValueBean(TipoCurso.LICENCIATURA_STRING, String.valueOf(TipoCurso.LICENCIATURA)));
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
	}
}