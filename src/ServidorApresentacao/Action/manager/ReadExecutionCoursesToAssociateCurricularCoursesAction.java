///*
// * Created on 8/Set/2003
// */
//package ServidorApresentacao.Action.manager;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.struts.action.ActionForm;
//import org.apache.struts.action.ActionForward;
//import org.apache.struts.action.ActionMapping;
//
//import DataBeans.InfoCurricularCourse;
//import ServidorAplicacao.Servico.UserView;
//import ServidorAplicacao.Servico.exceptions.FenixServiceException;
//import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
//import ServidorApresentacao.Action.base.FenixAction;
//import ServidorApresentacao.Action.exceptions.FenixActionException;
//import ServidorApresentacao.Action.exceptions.NonExistingActionException;
//import ServidorApresentacao.Action.sop.utils.ServiceUtils;
//import ServidorApresentacao.Action.sop.utils.SessionConstants;
//
///**
// * @author lmac1
// *///guardei o nemo do curricular course mas nao sei s sera melhor lê-lo outra vez
//public class ReadExecutionCoursesToAssociateCurricularCoursesAction extends FenixAction {
//
//public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
//
//	HttpSession session = request.getSession(false);
//
//	//		IUserView userView = SessionUtils.getUserView(request);
//	UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
//	Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
//	Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
//
//	Object args[] = { curricularCourseId };
//
//	InfoCurricularCourse infoCurricularCourse = null;
//
//	try {
//		infoCurricularCourse = (InfoCurricularCourse) ServiceUtils.executeService(userView, "ReadCurricularCourse", args);
//
//	} catch (NonExistingServiceException e) {
//		throw new NonExistingActionException("message.nonExistingCurricularCourse", "", e);
//	} catch (FenixServiceException fenixServiceException) {
//		throw new FenixActionException(fenixServiceException.getMessage());
//	}
//
//	if (infoCurricularCourse.getBasic().booleanValue())
//		request.setAttribute("basic", "");
//		
//	Object args1[] = { executionPeriodId };
//
//		List infoExecutionCoursesList = null;
//		try {
//			infoExecutionCoursesList = (List) ServiceUtils.executeService(userView, "ReadExecutionCoursesByExecutionPeriod", args1);
//
//		} catch (NonExistingServiceException e) {
//			throw new NonExistingActionException("message.nonExistingCurricularCourse", "", e);
//		} catch (FenixServiceException fenixServiceException) {
//			throw new FenixActionException(fenixServiceException.getMessage());
//		}
//	request.setAttribute("infoCurricularCourse", infoCurricularCourse);
//	request.setAttribute("infoExecutionCoursesList",infoExecutionCoursesList);
//	
//	return mapping.findForward("viewExecutionCourses");
//	}
//}