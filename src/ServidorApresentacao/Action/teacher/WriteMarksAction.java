package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoFrequenta;
import DataBeans.InfoMark;
import DataBeans.InfoSiteMarks;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author Tânia Pousão
 *
 */
public class WriteMarksAction extends DispatchAction {
	public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return null;
	}

	public ActionForward writeMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		System.out.println("-->WriteMarksAction: writeMarks");

		HttpSession session = request.getSession();
		ActionErrors actionErrors = new ActionErrors();

		List marksList = new ArrayList();

		Integer sizeList = getSizeList(request);
		System.out.println("-->WriteMarksAction-writeMarks:  size " + sizeList);

		//transform form into list with student's number and students's mark
		for (int i = 0; i < sizeList.intValue(); i++) {
			InfoMark infoMark = new InfoMark();
			infoMark = getMark(request, i);

			marksList.add(infoMark);
		}
		System.out.println("-->WriteMarksAction-writeMarks:  marksList" + marksList);

		Integer objectCode = getObjectCode(request);
		Integer examCode = getExamCode(request);

		IUserView userView = (IUserView) session.getAttribute("UserView");

		Object[] args = { objectCode, examCode, marksList };
		GestorServicos manager = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;

		try {
			siteView = (TeacherAdministrationSiteView) manager.executar(userView, "InsertExamMarks", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("examCode", examCode);

		InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
		//Check if ocurr errors in service
		if ((infoSiteMarks.getMarksListErrors() != null && infoSiteMarks.getMarksListErrors().size() > 0)
			|| (infoSiteMarks.getMarksListErrors2() != null && infoSiteMarks.getMarksListErrors2().size() > 0)) {

			if (infoSiteMarks.getMarksListErrors() != null && infoSiteMarks.getMarksListErrors().size() > 0) {
				//list with	errors Invalid Marks
				ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
				while (iterator.hasNext()) {
					InfoMark infoMark = (InfoMark) iterator.next();
					System.out.println("Ocorreu erro: invalidMark");
					actionErrors.add(
						"invalidMark",
						new ActionError("errors.invalidMark", infoMark.getMark(), infoMark.getInfoFrequenta().getAluno().getNumber()));
				}
			}

			if (infoSiteMarks.getMarksListErrors2() != null && infoSiteMarks.getMarksListErrors2().size() > 0) {
				//list with	errors Student Existence
				ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
				while (iterator.hasNext()) {
					InfoMark infoMark = (InfoMark) iterator.next();
					System.out.println("Ocorreu erro: studentExistence");
					actionErrors.add(
						"studentExistence",
						new ActionError("errors.student.nonExisting", infoMark.getInfoFrequenta().getAluno().getNumber()));
				}
			}

			System.out.println(
				"Ocorreram erros: "
					+ infoSiteMarks.getMarksListErrors().size()
					+ " invalidMark e "
					+ infoSiteMarks.getMarksListErrors2().size()
					+ " studentExistence no total de "
					+ actionErrors.size()
					+ " erros");

			//System.out.println("-->" + mapping.getInputForward());	
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

		return mapping.findForward("viewExams");
	}

	private InfoMark getMark(HttpServletRequest request, int index) {
		String mark = request.getParameter("markElem[" + index + "].mark");
		Integer studentCode = Integer.valueOf(request.getParameter("markElem[" + index + "].studentCode"));

		if (mark != null && studentCode != null) {
			System.out.println("-->WriteMarksAction-writeMarks:  mark" + mark + " de " + studentCode);
			//infoMark with only student code and mark
			InfoStudent infoStudent = new InfoStudent();
			infoStudent.setIdInternal(studentCode);

			InfoFrequenta infoFrequenta = new InfoFrequenta();
			infoFrequenta.setAluno(infoStudent);

			InfoMark infoMark = new InfoMark();
			infoMark.setInfoFrequenta(infoFrequenta);

			infoMark.setMark(mark);

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
}