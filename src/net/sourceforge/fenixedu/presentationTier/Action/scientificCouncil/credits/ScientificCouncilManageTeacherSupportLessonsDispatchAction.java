package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherSupportLessonsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ScientificCouncilManageTeacherSupportLessonsDispatchAction extends ManageTeacherSupportLessonsDispatchAction {

    public ActionForward showSupportLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm supportLessonForm = (DynaActionForm) form;
	Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");
	Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

	if (professorship == null) {
	    return mapping.findForward("teacher-not-found");
	}

	getSupportLessons(request, professorship);
	return mapping.findForward("list-support-lessons");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm supportLessonForm = (DynaActionForm) form;
	Integer supportLesssonID = (Integer) supportLessonForm.get("supportLessonID");
	Integer professorshipID = (Integer) supportLessonForm.get("professorshipID");

	Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

	if (professorship == null) {
	    return mapping.findForward("teacher-not-found");
	}

	SupportLesson supportLesson = null;
	if (supportLesssonID != null && supportLesssonID != 0) {
	    supportLesson = rootDomainObject.readSupportLessonByOID(supportLesssonID);
	    if (!professorship.getSupportLessons().contains(supportLesson)) {
		return mapping.findForward("teacher-not-found");
	    }
	}

	prepareToEdit(supportLesson, professorship, supportLessonForm, request);
	return mapping.findForward("edit-support-lesson");
    }

    public ActionForward editSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
	    InvalidPeriodException {

	editSupportLesson(form, request, RoleType.SCIENTIFIC_COUNCIL);
	return mapping.findForward("successfull-edit");
    }

    public ActionForward deleteSupportLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	deleteSupportLesson(request, form, RoleType.SCIENTIFIC_COUNCIL);
	return mapping.findForward("successfull-delete");
    }
}
