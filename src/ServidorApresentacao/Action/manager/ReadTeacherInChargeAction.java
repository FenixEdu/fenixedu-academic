/*
 * Created on 16/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */
public class ReadTeacherInChargeAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);
//		System.out.println("aaaaaaaaaaaaaaa"+request.getParameter("executionCourseId"));
		Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
		System.out.println("aaaaaaaaaaaaaaa"+request.getParameter("currricularCourseId"));
		Integer curricularCourseId  = new Integer((String)request.getParameter("currricularCourseId"));
		
		
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		infoCurricularCourse.setIdInternal(curricularCourseId);
		
//		System.out.println("aaaaaaaaaaaaaaa"+request.getParameter("infoCurricularCourse"));

		Object args[] = { executionCourseId };
		
		List infoTeachersList = null;

		try {
			infoTeachersList = (List) ServiceUtils.executeService(userView, "ReadExecutionCourseTeachers", args);
			
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
	
		
		request.setAttribute("infoTeachersList", infoTeachersList);
		request.setAttribute("infoCurricularCourse", infoCurricularCourse);
		
		return mapping.findForward("readExecutionCourseTeachers");
	}

}
