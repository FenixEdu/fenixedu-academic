/*
 * Created on 11/Jun/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExam;
import DataBeans.InfoRoom;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteExam;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.teacher.ExamRoomDistribution;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class DistributeStudentsByRoomDispatchAction extends DispatchAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		DynaActionForm distributionExam = (DynaActionForm) form;
		
		Integer examCode = new Integer ((String) distributionExam.get("examCode"));
		Integer executionCourseCode = new Integer ((String) distributionExam.get("objectCode"));
		
		InfoSiteExam infoSiteExam = new InfoSiteExam();
		Object []args = {executionCourseCode, new InfoSiteCommon(), infoSiteExam, null,null, null};
		
		TeacherAdministrationSiteView siteView =  (TeacherAdministrationSiteView) ServiceUtils.executeService(SessionUtils.getUserView(request),"TeacherAdministrationSiteComponentService", args);
		
		infoSiteExam = (InfoSiteExam) siteView.getComponent();
		
		List infoExamList = infoSiteExam.getInfoExams();
		
		for (int i=0; i <infoExamList.size(); i++) {
			InfoExam infoExamFromList = (InfoExam) infoExamList.get(i);
			if (infoExamFromList.getIdInternal().equals(examCode)) {
				Collections.sort(infoExamFromList.getAssociatedRooms(), new ReverseComparator(new BeanComparator("capacidadeExame")));
				
				request.setAttribute("infoExam", infoExamFromList);
				break;
			}
		}
		
		request.setAttribute("siteView", siteView);
		return mapping.findForward("show-rooms");
	}

	public ActionForward distribute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm distributionExam = (DynaActionForm) form;
		Integer examCode = new Integer ((String) distributionExam.get("examCode"));
		Integer executionCourseCode = new Integer ((String) distributionExam.get("objectCode"));
		Integer [] rooms = (Integer[]) distributionExam.get("rooms");
		Object[] args = {executionCourseCode, examCode, Arrays.asList(rooms), Boolean.FALSE};
		try {
			ServiceUtils.executeService(SessionUtils.getUserView(request),"ExamRoomDistribution", args); 
		}catch (FenixServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError actionError = null;
			switch (e.getErrorType()) {
				case ExamRoomDistribution.NON_DEFINED_ENROLLMENT_PERIOD:
					actionError = new ActionError("error.non.defined.enrollment.period");
					break;
				case ExamRoomDistribution.OUT_OF_ENROLLMENT_PERIOD:
					actionError = new ActionError("error.out.of.period.enrollment.period");
					break;
				default:
					throw e;
			}
			actionErrors.add("error", actionError);
			saveErrors(request, actionErrors);
		}
		return mapping.findForward("show-distribution");
	}
	

}
