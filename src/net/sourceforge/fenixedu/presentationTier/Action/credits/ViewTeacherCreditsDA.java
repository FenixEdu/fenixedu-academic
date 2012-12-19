package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits;
import net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean;
import net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public abstract class ViewTeacherCreditsDA extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	request.setAttribute("teacherBean", new TeacherCreditsBean());
	return mapping.findForward("selectTeacher");
    }

    public abstract ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception;

    public abstract ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception;

    protected ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, RoleType roleType) throws NumberFormatException, FenixServiceException, Exception {
	Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOid"));
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionYearOid"));
	if (teacher == null) {
	    Professorship professorship = rootDomainObject.readProfessorshipByOID(getIntegerFromRequest(request,
		    "professorshipID"));
	    if (professorship != null) {
		teacher = professorship.getTeacher();
		executionYear = professorship.getExecutionCourse().getExecutionYear();
	    }
	}
	AnnualTeachingCreditsBean annualTeachingCreditsBean = new AnnualTeachingCreditsBean(executionYear, teacher, roleType);

	for (AnnualTeachingCredits annualTeachingCredits : teacher.getAnnualTeachingCredits()) {
	    if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(executionYear)) {
		if (annualTeachingCredits.isPastResume()) {
		    TeacherCreditsBean teacherBean = new TeacherCreditsBean(teacher);
		    teacherBean.preparePastTeachingCredits();
		    request.setAttribute("teacherBean", teacherBean);
		    return mapping.findForward("showPastTeacherCredits");
		} else {
		    annualTeachingCreditsBean = new AnnualTeachingCreditsBean(annualTeachingCredits, roleType);
		    break;
		}
	    }
	}
	request.setAttribute("annualTeachingCreditsBean", annualTeachingCreditsBean);
	request.setAttribute("teacherBean", new TeacherCreditsBean());
	return mapping.findForward("showAnnualTeacherCredits");
    }

    public ActionForward recalculateCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOid"));
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionYearOid"));
	AnnualTeachingCredits annualTeachingCredits = AnnualTeachingCredits.readByYearAndTeacher(executionYear, teacher);
	if (annualTeachingCredits != null) {
	    annualTeachingCredits.calculateCredits();
	}
	return viewAnnualTeachingCredits(mapping, form, request, response);
    }

}