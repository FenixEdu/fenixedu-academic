/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.common
 * 
 * Created on 8/Jan/2003
 *
 */
package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.DegreeKey;
import DataBeans.InfoExecutionDegree;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jpvl
 */
public class ChooseContextDispatchAction extends DispatchAction {

	protected static final String INFO_DEGREE_INITIALS_PARAMETER =
		"degreeInitials";
	protected static final String SEMESTER_PARAMETER = "semester";
	protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";
	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		

		setInfoDegreeList(request);
		setSemesterList(mapping, form, request, response);
		setCurricularYearList(mapping, form, request, response);

		return mapping.findForward("formPage");
	}

	public ActionForward nextPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaValidatorForm actionForm = (DynaValidatorForm) form;

		String degreeInitials =
			(String) actionForm.get(INFO_DEGREE_INITIALS_PARAMETER);

		Object args[] = { new DegreeKey(degreeInitials)};

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) ServiceUtils.executeService(
				null,
				"LerLicenciaturaExecucaoDeLicenciatura",
				args);

		Integer curricularYear =
			new Integer((String) actionForm.get(CURRICULAR_YEAR_PARAMETER));
		Integer semester = new Integer((String) actionForm.get(SEMESTER_PARAMETER));

		CurricularYearAndSemesterAndInfoExecutionDegree ctx =
			new CurricularYearAndSemesterAndInfoExecutionDegree(
				curricularYear,
				semester,
				infoExecutionDegree);

		request.getSession().setAttribute(SessionConstants.CONTEXT_KEY, ctx);
		
		return mapping.findForward("nextPage");
		
	}

	/**
	 * Method setCurricularYearList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setCurricularYearList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List curricularYearList =
			(List) request.getSession().getAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY);

		if (curricularYearList == null) {
			curricularYearList = new ArrayList();
			curricularYearList.add(new LabelValueBean("1º", "1"));
			curricularYearList.add(new LabelValueBean("2º", "2"));
			curricularYearList.add(new LabelValueBean("3º", "3"));
			curricularYearList.add(new LabelValueBean("4º", "4"));
			curricularYearList.add(new LabelValueBean("5º", "5"));
			request.getSession().setAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY,
				curricularYearList);
		}
		return curricularYearList;
	}
	/**
	 * Method setSemesterList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setSemesterList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List semesterList =
			(List) request.getSession().getAttribute(
				SessionConstants.SEMESTER_LIST_KEY);

		if (semesterList == null) {

			semesterList = new ArrayList();
			semesterList.add(new LabelValueBean("1º", "1"));
			semesterList.add(new LabelValueBean("2º", "2"));
			request.getSession().setAttribute(
				SessionConstants.SEMESTER_LIST_KEY,
				semesterList);
		}
		return semesterList;
	}
	/**
	 * Method setInfoDegreeList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setInfoDegreeList(HttpServletRequest request)
		throws Exception {

		List infoDegreeList = null;

		Object args[] = {
		};
		infoDegreeList =
			(List) ServiceUtils.executeService(null, "LerLicenciaturas", args);

		request.getSession().setAttribute(
			SessionConstants.INFO_DEGREE_LIST_KEY,
			infoDegreeList);

		return infoDegreeList;
	}

}
