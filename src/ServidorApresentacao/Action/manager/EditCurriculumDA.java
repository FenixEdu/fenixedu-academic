/*
 * Created on 16/Set/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurriculum;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class EditCurriculumDA extends FenixDispatchAction {

	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm curriculumForm = (DynaActionForm) form;

		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
		String language = request.getParameter("language");

		InfoCurriculum infoCurriculum = null;

		Object args[] = { curricularCourseId };
		
		try {
				infoCurriculum = (InfoCurriculum) ServiceUtils.executeService(userView, "ReadCurriculum", args);
		
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCurricularPlan"));
		}  catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		if(infoCurriculum != null) {
			if(language == null) {
				curriculumForm.set("generalObjectives", infoCurriculum.getGeneralObjectives());
				curriculumForm.set("operacionalObjectives", infoCurriculum.getOperacionalObjectives());
				curriculumForm.set("program", infoCurriculum.getProgram());
			}
			else {
				curriculumForm.set("generalObjectivesEn", infoCurriculum.getGeneralObjectivesEn());
				curriculumForm.set("operacionalObjectivesEn", infoCurriculum.getOperacionalObjectivesEn());
				curriculumForm.set("programEn", infoCurriculum.getProgramEn());
			}
		}
		
		if(language == null)
			return mapping.findForward("editCurriculum");
			
		return mapping.findForward("editCurriculumEnglish");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm editForm = (DynaActionForm) form;
		
		InfoCurriculum infoCurriculum = new InfoCurriculum();
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		infoCurricularCourse.setIdInternal(new Integer(request.getParameter("curricularCourseId")));
		infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
		infoCurriculum.setGeneralObjectives((String) editForm.get("generalObjectives"));
		infoCurriculum.setOperacionalObjectives((String) editForm.get("operacionalObjectives"));
		infoCurriculum.setProgram((String) editForm.get("program"));
		infoCurriculum.setGeneralObjectivesEn((String) editForm.get("generalObjectivesEn"));
		infoCurriculum.setOperacionalObjectivesEn((String) editForm.get("operacionalObjectivesEn"));
		infoCurriculum.setProgramEn((String) editForm.get("programEn"));

		Object args[] = { infoCurriculum, request.getParameter("language") };
		
		try {
				ServiceUtils.executeService(userView, "EditCurriculum", args);
			
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("message.nonExistingCurricularCourse", mapping.findForward("readDegreeCurricularPlan"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return mapping.findForward("readCurricularCourse");
	}
}
