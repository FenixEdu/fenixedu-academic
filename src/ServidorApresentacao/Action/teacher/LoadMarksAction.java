package ServidorApresentacao.Action.teacher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import ServidorAplicacao.Servico.exceptions.NotExecuteException;



/**
 * @author Tânia Pousão
 *
 */
public class LoadMarksAction extends DispatchAction {
	public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return null;
	}

	public ActionForward loadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		ActionErrors actionErrors = new ActionErrors();
		DynaActionForm marksForm = (DynaActionForm) form;
		File file = null;
		BufferedReader leitura = null;
		
		//Read the uploaded file
		FormFile formFile = (FormFile) marksForm.get("theFile");
		
		file = new File(formFile.getFileName());
//		File fileNew = new File(file.getAbsolutePath());
//		String newString = fileNew.getAbsolutePath().replace('\\', '/');
System.out.println("---------------------< " + file.getAbsolutePath());
		


		try {
			leitura = new BufferedReader(new FileReader(formFile.getFileName()));
		} catch (FileNotFoundException e) {
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}

//				
//		DynaActionForm marksForm = (DynaActionForm) form;
//		Integer[] studentsNumbers = (Integer[]) marksForm.get("studentsNumbers");
//		String[] marks	= (String[]) marksForm.get("marks");
//		//transform form into list with student's number and students's mark
//		for (int i = 0; i < marks.length; i++) {
//			InfoStudent infoStudent = new InfoStudent();
//			infoStudent.setNumber((Integer) studentsNumbers[i]);		
//			
//			InfoFrequenta infoFrequenta = new InfoFrequenta();
//			infoFrequenta.setAluno(infoStudent);
//			
//			InfoMark infoMark = new InfoMark();
//			infoMark.setInfoFrequenta(infoFrequenta);
//			
//			infoMark.setMark((String) marks[i]);
//			
//			marksList.add(infoMark);	
//		}
//				
//		Integer objectCode = getObjectCode(request);
//		Integer examCode = getExamCode(request);
//
//		IUserView userView = (IUserView) session.getAttribute("UserView");
//
//		Object[] args = { objectCode, examCode, marksList };
//		GestorServicos manager = GestorServicos.manager();
//		TeacherAdministrationSiteView siteView = null;
//
//		try {
//			siteView = (TeacherAdministrationSiteView) manager.executar(userView, "InsertExamMarks", args);
//		} catch (FenixServiceException e) {
//			throw new FenixActionException(e);
//		}
//
//		// check for errors in service
//		InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
//		if (infoSiteMarks.getMarksListErrors() != null && infoSiteMarks.getMarksListErrors().size() > 0) {
//			ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
//			while (iterator.hasNext()) {
//				InfoMark infoMark = (InfoMark) iterator.next();
//				if (isValidMark(infoMark)) {
//					actionErrors.add(
//						"studentExistence",
//						new ActionError("errors.student.nonExisting", infoMark.getInfoFrequenta().getAluno().getNumber()));
//				} else {
//					actionErrors.add(
//						"invalidMark",
//						new ActionError("errors.invalidMark", infoMark.getMark(), infoMark.getInfoFrequenta().getAluno().getNumber()));
//				}
//			}
//			saveErrors(request, actionErrors);
//			return mapping.getInputForward();
//
//		}
//
//		request.setAttribute("objectCode", objectCode);
//
//		return mapping.findForward("marksList");
//	}
//
//	private Integer getExamCode(HttpServletRequest request) {
//		String examCodeString = (String) request.getAttribute("examCode");
//		if (examCodeString == null) {
//			examCodeString = request.getParameter("examCode");
//		}
//		Integer examCode = new Integer(examCodeString);
//		return examCode;
//	}
//
//	private Integer getObjectCode(HttpServletRequest request) {
//		Integer objectCode = null;
//		String objectCodeString = (String) request.getAttribute("objectCode");
//		if (objectCodeString == null) {
//			objectCodeString = request.getParameter("objectCode");
//		}
//		if (objectCodeString != null) {
//			objectCode = new Integer(objectCodeString);
//		}
//		return objectCode;
//	}
//
//	private boolean isValidMark(InfoMark infoMark) {
//		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
//			infoMark.getInfoFrequenta().getInfoEnrolment().getInfoStudentCurricularPlan().getInfoDegreeCurricularPlan();
//		IDegreeCurricularPlan degreeCurricularPlan = Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoDegreeCurricularPlan);
//
//		// test marks by execution course: strategy 
//		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
//		IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
//			degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
//
//		return degreeCurricularPlanStrategy.checkMark(infoMark.getMark());
//	}
	return mapping.findForward("");
}
}