package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author David Santos
 *
 */

public class GeneralCurricularCourseEnrolmentManagerDispatchAction extends TransactionalDispatchAction {

	protected void initializeForm(InfoEnrolmentContext infoEnrolmentContext, DynaActionForm enrolmentForm) {
		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();
		List infoFinalSpan = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		Integer[] curricularCoursesIndexes = new Integer[infoFinalSpan.size()];

		for (int i = 0; i < infoFinalSpan.size(); i++) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoFinalSpan.get(i);
			InfoCurricularCourse infoCurricularCourse = infoCurricularCourseScope.getInfoCurricularCourse();

			if (infoCurricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {
				List optionalEnrolments = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();
				Iterator optionalEnrolmentsIterator = optionalEnrolments.iterator();
				while (optionalEnrolmentsIterator.hasNext()) {
					InfoEnrolmentInOptionalCurricularCourse optionalEnrolment = (InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator.next();
					if (optionalEnrolment.getInfoCurricularCourseScope().getInfoCurricularCourse().equals(infoCurricularCourse)) {
						curricularCoursesIndexes[i] = new Integer(i);
						break;
					}
				}
			} else {
				if (actualEnrolment.contains(infoCurricularCourseScope)) {
					curricularCoursesIndexes[i] = new Integer(i);
				} else {
					curricularCoursesIndexes[i] = null;
				}
			}
		}
		enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
	}

	protected InfoEnrolmentContext processEnrolment(HttpServletRequest request, DynaActionForm enrolmentForm, HttpSession session) {

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if (enrolmentForm.get("curricularCourses") == null) {
			enrolmentForm.set("curricularCourses", new Integer[infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().size()]);
		}

		Integer[] curricularCourses = (Integer[]) enrolmentForm.get("curricularCourses");

		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();

		actualEnrolment.clear();
		actualEnrolment.addAll(infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled());

		List curricularCourseScopesToBeEnrolled = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		List optionalCurricularCoursesChoosen = new ArrayList();
		if (curricularCourses != null) {
			for (int i = 0; i < curricularCourses.length; i++) {
				// TODO see if is struts-bug : When parameter is null it won't reset array position.
				if (request.getParameter("curricularCourses["+ i + "]") == null) {
					curricularCourses[i] = null;
				}
				Integer curricularCourseIndex = curricularCourses[i];
				if (curricularCourseIndex != null) {
					InfoCurricularCourseScope curricularCourseScope = (InfoCurricularCourseScope) curricularCourseScopesToBeEnrolled.get(curricularCourseIndex.intValue());
					if (!curricularCourseScope.getInfoCurricularCourse().getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {
						actualEnrolment.add(curricularCourseScope);
					} else {
						optionalCurricularCoursesChoosen.add(curricularCourseScope.getInfoCurricularCourse());
					}
				}
			}
		}

		List enrolmentsInOptionalCourses = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();

		if (enrolmentsInOptionalCourses.size() != optionalCurricularCoursesChoosen.size()) {
			Iterator optionalEnrolmentsIterator = enrolmentsInOptionalCourses.iterator();
			while (optionalEnrolmentsIterator.hasNext()) {
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator.next();
				InfoCurricularCourse optionalCurricularCourse = infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope().getInfoCurricularCourse();
				if (!optionalCurricularCoursesChoosen.contains(optionalCurricularCourse)) {
					optionalEnrolmentsIterator.remove();
				}
			}
		}
		this.computeRemovedCurricularCourseScope(request, infoEnrolmentContext);
		return infoEnrolmentContext;
	}

	protected void computeRemovedCurricularCourseScope(HttpServletRequest request, InfoEnrolmentContext infoEnrolmentContext) {
		HttpSession session = request.getSession();
		List list = (List) session.getAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
		if(list != null) {
			List toRemove = (List) session.getAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
			List aux = null;
			if(toRemove == null) {
				aux = new ArrayList();
			} else {
				aux = toRemove;
				aux.clear();
			}
			aux.addAll(infoEnrolmentContext.getActualEnrolment());
			Iterator iterator = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().iterator();
			while(iterator.hasNext()) {
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) iterator.next();
				InfoCurricularCourseScope optionalCurricularCourseScope = infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope();
				aux.add(optionalCurricularCourseScope);
			}
			List result = (List) CollectionUtils.subtract(list, aux);
			session.setAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY, result);
		}
	}

	protected void initializeRemovedCurricularCourseScopesList(HttpServletRequest request, InfoEnrolmentContext infoEnrolmentContext) {
		HttpSession session = request.getSession();
		List list = (List) session.getAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
		if( (list == null) || (list.isEmpty()) ) {
			list = new ArrayList();
			list.addAll(infoEnrolmentContext.getActualEnrolment());
			Iterator iterator = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().iterator();
			while(iterator.hasNext()) {
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) iterator.next();
				InfoCurricularCourseScope optionalCurricularCourseScope = infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope();
				list.add(optionalCurricularCourseScope);
			}
			session.setAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY, list);
		}
	}

	protected void saveErrorsFromInfoEnrolmentContext(HttpServletRequest request, InfoEnrolmentContext infoEnrolmentContext) {
		ActionErrors actionErrors = new ActionErrors();

		EnrolmentValidationResult enrolmentValidationResult = infoEnrolmentContext.getEnrolmentValidationResult();

		Map messages = enrolmentValidationResult.getMessage();

		Iterator messagesIterator = messages.keySet().iterator();
		ActionError actionError;
		while (messagesIterator.hasNext()) {
			String message = (String) messagesIterator.next();
			List messageArguments = (List) messages.get(message);
			actionError = new ActionError(message, messageArguments.toArray());
			actionErrors.add(message, actionError);
		}
		saveErrors(request, actionErrors);
	}

	protected InfoStudent getInfoStudent(HttpServletRequest request, ActionForm form, IUserView userView) throws FenixActionException {
		DynaActionForm enrolmentForm = (DynaActionForm) form;
		InfoStudent infoStudent = (InfoStudent) request.getAttribute(SessionConstants.STUDENT);
		if(infoStudent == null) {
			Integer infoStudentOID = (Integer) enrolmentForm.get("studentOID");
			try {
				Object args[] = { infoStudentOID };
				infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByOID", args);
			} catch(FenixServiceException e) {
				throw new FenixActionException(e);
			}
		} else {
			enrolmentForm.set("studentOID", infoStudent.getIdInternal());
		}
		return infoStudent;
	}

	protected void uncheckCurricularCourse(DynaValidatorForm enrolmentForm, InfoEnrolmentContext infoEnrolmentContext) {

		if(enrolmentForm.get("curricularCourses") == null) {
			enrolmentForm.set("curricularCourses", new Integer[infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().size()]);
		}
		Integer[] curricularCoursesIndexes = (Integer[]) enrolmentForm.get("curricularCourses");
		Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
		curricularCoursesIndexes[optionalCurricularCourseIndex.intValue()] = null;
		enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
	}

}