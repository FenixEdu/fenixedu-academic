package ServidorApresentacao.Action.teacher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import DataBeans.ISiteComponent;
import DataBeans.InfoFrequenta;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão
 *
 */
public class WriteMarksAction extends DispatchAction {

	public ActionForward loadFile(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, NotExecuteException {
		HttpSession session = request.getSession();
		ActionErrors actionErrors = new ActionErrors();
		DynaActionForm marksForm = (DynaActionForm) form;
		File file = null;
		String linhaFicheiro = null;
		String lineReader = null;
		Integer objectCode = getObjectCode(request);
		Integer examCode = getExamCode(request);
		//Read uploaded file
		
		FormFile formFile = (FormFile) marksForm.get("theFile");
        if (!(formFile.getContentType().equals("application/vnd.ms-excel")) && !(formFile.getContentType().equals("text/plain"))){
			prepareInputForward(request, session, objectCode, examCode);
			actionErrors.add(
					"FileNotExist",
					new ActionError(
						"error.ficheiro.impossivelLer"));
			
			saveErrors(request, actionErrors);
			return mapping.findForward("loadMarks");
        }
        
    	InputStreamReader input =
			new InputStreamReader(formFile.getInputStream());

		BufferedReader reader = new BufferedReader(input);
	    
	    
		int n = 0;
		do {
			try {
				lineReader = reader.readLine();
				
				System.out.println("readline------>"+lineReader);
				
			} catch (IOException e) {
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}
			if ((lineReader != null) && (lineReader.length() != 0)) {
				n++;
			}

		}
		
		 while ((lineReader != null) && (lineReader.length() != 0));
		
		 reader.close();
		input = new InputStreamReader(formFile.getInputStream());
		reader = new BufferedReader(input);

		String[] studentsNumbers = new String[n];
		String[] marks = new String[n];

		int j = 0;
		try {
			lineReader = reader.readLine();			
		} catch (IOException e) {
			throw new NotExecuteException("error.ficheiro.impossivelLer");
		}
		StringTokenizer stringTokenizer = null;
		do {

			/* leitura do ficheiro de notas linha a linha */
			if ((lineReader != null)) {

				stringTokenizer = new StringTokenizer(lineReader);				
				studentsNumbers[j] = stringTokenizer.nextToken().trim();
				try {
					marks[j] = stringTokenizer.nextToken().trim();
				} catch (NoSuchElementException e1) {
					prepareInputForward(request, session, objectCode, examCode);
								actionErrors.add(
										"BadFormatFile",
										new ActionError(
											"error.file.badFormat"));
			
								saveErrors(request, actionErrors);
								return mapping.findForward("loadMarks");
					
				}

			}
			try {
				lineReader = reader.readLine();
			} catch (IOException e) {
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}

			j++;

		} while ((lineReader != null) && (lineReader.length() != 0));
		reader.close();
		IUserView userView = (IUserView) session.getAttribute("UserView");

		List marksList = new ArrayList();
		for (int i = 0; i < marks.length; i++) {

			InfoMark infoMark = new InfoMark();
			infoMark = getMarkStudent(studentsNumbers, marks, i, userView);

			marksList.add(infoMark);
		}
		

		Object[] args = { objectCode, examCode, marksList };
		GestorServicos manager = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;

		try {
			siteView =
				(TeacherAdministrationSiteView) manager.executar(
					userView,
					"InsertExamMarks",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("examCode", examCode);
		// check for errors in service
		InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
		if (infoSiteMarks.getStudentsListErrors() != null
			&& infoSiteMarks.getStudentsListErrors().size() > 0) {
			ListIterator iterator =
				infoSiteMarks.getStudentsListErrors().listIterator();
			while (iterator.hasNext()) {
				InfoMark infoMark = (InfoMark) iterator.next();
				actionErrors.add(
					"studentNonExistence",
					new ActionError(
						"errors.student.nonExisting",
						String.valueOf(
							(infoMark
								.getInfoFrequenta()
								.getAluno()
								.getNumber())
								.intValue())));
			}
			saveErrors(request, actionErrors);

		}
		if (infoSiteMarks.getMarksListErrors() != null
			&& infoSiteMarks.getMarksListErrors().size() > 0) {
			ListIterator iterator =
				infoSiteMarks.getMarksListErrors().listIterator();
			while (iterator.hasNext()) {
				InfoMark infoMark = (InfoMark) iterator.next();
				actionErrors.add(
					"invalidMark",
					new ActionError(
						"errors.invalidMark",
						infoMark.getMark(),
						infoMark.getInfoFrequenta().getAluno().getNumber()));

			}
			saveErrors(request, actionErrors);

		}
		if (infoSiteMarks.getMarksListErrors() != null
			|| infoSiteMarks.getStudentsListErrors() != null) {
			return mapping.getInputForward();
		}
		return mapping.findForward("success");
	}

	private void prepareInputForward(
		HttpServletRequest request,
		HttpSession session,
		Integer objectCode,
		Integer examCode)
		throws FenixActionException {
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		ISiteComponent commonComponent = new InfoSiteCommon();
		Object[] args = { objectCode, commonComponent, null, null, null, null };
		
		try {
			TeacherAdministrationSiteView siteView =
				(TeacherAdministrationSiteView) ServiceUtils.executeService(userView, "TeacherAdministrationSiteComponentService", args);
		
			request.setAttribute("siteView", siteView);
			request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("examCode", examCode);
	}

	public ActionForward writeMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		ActionErrors actionErrors = new ActionErrors();

		List marksList = new ArrayList();

		Integer sizeList = getSizeList(request);

		//transform form into list with student's number and students's mark
		for (int i = 0; i < sizeList.intValue(); i++) {
			InfoMark infoMark = new InfoMark();
			infoMark = getMark(request, i);

			marksList.add(infoMark);
		}

		Integer objectCode = getObjectCode(request);
		Integer examCode = getExamCode(request);

		IUserView userView = (IUserView) session.getAttribute("UserView");

		Object[] args = { objectCode, examCode, marksList };
		GestorServicos manager = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;

		try {
			siteView =
				(TeacherAdministrationSiteView) manager.executar(
					userView,
					"InsertExamMarks",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("examCode", examCode);

		InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
		//Check if ocurr errors in service
		if ((infoSiteMarks.getMarksListErrors() != null
			&& infoSiteMarks.getMarksListErrors().size() > 0)
			|| (infoSiteMarks.getStudentsListErrors() != null
				&& infoSiteMarks.getStudentsListErrors().size() > 0)) {

			if (infoSiteMarks.getMarksListErrors() != null
				&& infoSiteMarks.getMarksListErrors().size() > 0) {
				//list with	errors Invalid Marks
				ListIterator iterator =
					infoSiteMarks.getMarksListErrors().listIterator();
				while (iterator.hasNext()) {
					InfoMark infoMark = (InfoMark) iterator.next();

					actionErrors.add(
						"invalidMark",
						new ActionError(
							"errors.invalidMark",
							infoMark.getMark(),
							String.valueOf(
								(infoMark
									.getInfoFrequenta()
									.getAluno()
									.getNumber())
									.intValue())));
				}
			}

			if (infoSiteMarks.getStudentsListErrors() != null
				&& infoSiteMarks.getStudentsListErrors().size() > 0) {
				//list with	errors Student Existence
				ListIterator iterator =
					infoSiteMarks.getMarksListErrors().listIterator();
				while (iterator.hasNext()) {
					InfoMark infoMark = (InfoMark) iterator.next();
					System.out.println("Ocorreu erro: studentExistence");
					actionErrors.add(
						"studentExistence",
						new ActionError(
							"errors.student.nonExisting",
							String.valueOf(
								(infoMark
									.getInfoFrequenta()
									.getAluno()
									.getNumber())
									.intValue())));
				}
			}
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

		return mapping.findForward("viewExams");
	}

	private InfoMark getMark(HttpServletRequest request, int index) {
		String mark = request.getParameter("markElem[" + index + "].mark");
		Integer studentCode =
			Integer.valueOf(
				request.getParameter("markElem[" + index + "].studentCode"));

		if (mark != null && studentCode != null) {
			//infoMark with only student code and mark
			InfoStudent infoStudent = new InfoStudent();
			infoStudent.setIdInternal(studentCode);

			InfoFrequenta infoFrequenta = new InfoFrequenta();
			infoFrequenta.setAluno(infoStudent);

			InfoMark infoMark = new InfoMark();
			infoMark.setInfoFrequenta(infoFrequenta);

			infoMark.setMark(mark);
			infoMark.setPublishedMark("0");

			return infoMark;
		}
		return null;
	}

	private Integer getSizeList(HttpServletRequest request) {
		Integer objectCode = null;
		String objectCodeString = (String) request.getAttribute("sizeList");
		if (objectCodeString == null) {
			objectCodeString = request.getParameter("sizeList");
		}
		if (objectCodeString != null) {
			objectCode = new Integer(objectCodeString);
		}
		return objectCode;
	}

	private Integer getExamCode(HttpServletRequest request) {
		String examCodeString = (String) request.getAttribute("examCode");
		if (examCodeString == null) {
			examCodeString = request.getParameter("examCode");
		}
		Integer examCode = new Integer(examCodeString);
		return examCode;
	}

	private Integer getObjectCode(HttpServletRequest request) {
		Integer objectCode = null;
		String objectCodeString = (String) request.getAttribute("objectCode");
		if (objectCodeString == null) {
			objectCodeString = request.getParameter("objectCode");
		}
		if (objectCodeString != null) {
			objectCode = new Integer(objectCodeString);
		}
		return objectCode;
	}

	private InfoMark getMarkStudent(
		String[] student,
		String[] mark,
		int index,
		IUserView userView)
		throws FenixActionException {
		String markString = mark[index];
		Integer studentInt = new Integer(student[index]);
		ActionErrors actionErrors = new ActionErrors();

		if (markString != null && studentInt != null) {

			GestorServicos manager = GestorServicos.manager();
			InfoStudent infoStudentCode = new InfoStudent();
			InfoStudent infoStudent = new InfoStudent();

			System.out.println(
				"-->WriteMarksAction-writeMarks:  mark"
					+ markString
					+ " de "
					+ studentInt);
			//infoMark with only student code and mark
			InfoFrequenta infoFrequenta = new InfoFrequenta();
			infoStudent.setNumber(studentInt);
			infoFrequenta.setAluno(infoStudent);

			InfoMark infoMark = new InfoMark();
			infoMark.setInfoFrequenta(infoFrequenta);

			infoMark.setMark(markString);

			return infoMark;
		}
		return null;
	}
//	private boolean isValidMark(InfoMark infoMark) {
//		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
//			infoMark
//				.getInfoFrequenta()
//				.getInfoEnrolment()
//				.getInfoStudentCurricularPlan()
//				.getInfoDegreeCurricularPlan();
//		IDegreeCurricularPlan degreeCurricularPlan =
//			Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(
//				infoDegreeCurricularPlan);
//
//		// test marks by execution course: strategy 
//		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory =
//			DegreeCurricularPlanStrategyFactory.getInstance();
//		IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
//			degreeCurricularPlanStrategyFactory
//				.getDegreeCurricularPlanStrategy(
//				degreeCurricularPlan);
//
//		return degreeCurricularPlanStrategy.checkMark(infoMark.getMark());
//	}

}