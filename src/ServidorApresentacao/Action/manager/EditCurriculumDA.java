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
			curriculumForm.set("generalObjectives", infoCurriculum.getGeneralObjectives());
			curriculumForm.set("operacionalObjectives", infoCurriculum.getOperacionalObjectives());
			curriculumForm.set("program", infoCurriculum.getProgram());
			curriculumForm.set("generalObjectivesEn", infoCurriculum.getGeneralObjectivesEn());
			curriculumForm.set("operacionalObjectivesEn", infoCurriculum.getOperacionalObjectivesEn());
			curriculumForm.set("programEn", infoCurriculum.getProgramEn());
			curriculumForm.set("evaluationElements", infoCurriculum.getEvaluationElements());
			curriculumForm.set("evaluationElementsEn", infoCurriculum.getEvaluationElementsEn());
		}
		
		String language = request.getParameter("language");
		if(language == null)
			return mapping.findForward("editCurriculum");
			
		return mapping.findForward("editCurriculumEnglish");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm editForm = (DynaActionForm) form;
		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
		
		String generalObjectives = (String) editForm.get("generalObjectives");
		String operacionalObjectives = (String) editForm.get("operacionalObjectives");
		String program = (String) editForm.get("program");
		String generalObjectivesEn = (String) editForm.get("generalObjectivesEn");
		String operacionalObjectivesEn = (String) editForm.get("operacionalObjectivesEn");
		String programEn = (String) editForm.get("programEn");
		String evaluationElements = (String) editForm.get("evaluationElements");
		String evaluationElementsEn = (String) editForm.get("evaluationElementsEn");
		
		InfoCurriculum infoCurriculum = new InfoCurriculum();
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		infoCurricularCourse.setIdInternal(curricularCourseId);
		infoCurriculum.setInfoCurricularCourse(infoCurricularCourse);
		
		if(generalObjectives.compareTo("") != 0)
			infoCurriculum.setGeneralObjectives(generalObjectives);
		if(operacionalObjectives.compareTo("") != 0)
			infoCurriculum.setOperacionalObjectives(operacionalObjectives);
		if(program.compareTo("") != 0)
			infoCurriculum.setProgram(program);
		if(generalObjectivesEn.compareTo("") != 0)
			infoCurriculum.setGeneralObjectivesEn(generalObjectivesEn);
		if(operacionalObjectivesEn.compareTo("") != 0)
			infoCurriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
		if(programEn.compareTo("") != 0)
			infoCurriculum.setProgramEn(programEn);
		if(evaluationElements.compareTo("") != 0)
			infoCurriculum.setEvaluationElements(evaluationElements);
		if(evaluationElementsEn.compareTo("") != 0)
			infoCurriculum.setEvaluationElementsEn(evaluationElementsEn);

		Object args[] = { infoCurriculum };
		
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
