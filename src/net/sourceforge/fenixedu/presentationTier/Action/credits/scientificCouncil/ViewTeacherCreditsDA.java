package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits;
import net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean;
import net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "scientificCouncil", path = "/credits", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "selectTeacher", path = "/credits/selectTeacher.jsp"),
	@Forward(name = "showTeacherCredits", path = "/credits/showTeacherCredits.jsp"),
	@Forward(name = "showPastTeacherCredits", path = "/credits/showPastTeacherCredits.jsp"),
	@Forward(name = "showAnnualTeacherCredits", path = "/credits/showAnnualTeacherCredits.jsp") })
public class ViewTeacherCreditsDA extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	request.setAttribute("teacherBean", new TeacherCreditsBean());
	return mapping.findForward("selectTeacher");
    }

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	TeacherCreditsBean teacherBean = getRenderedObject();
	teacherBean.prepareAnnualTeachingCredits();
	request.setAttribute("teacherBean", teacherBean);
	return mapping.findForward("showTeacherCredits");
    }

    public ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOid"));
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionYearOid"));

	AnnualTeachingCreditsBean annualTeachingCreditsBean = new AnnualTeachingCreditsBean(executionYear, teacher);

	for (AnnualTeachingCredits annualTeachingCredits : teacher.getAnnualTeachingCredits()) {
	    if (annualTeachingCredits.getAnnualCreditsState().getExecutionYear().equals(executionYear)) {
		if (annualTeachingCredits.isPastResume()) {
		    TeacherCreditsBean teacherBean = new TeacherCreditsBean(teacher);
		    teacherBean.preparePastTeachingCredits();
		    request.setAttribute("teacherBean", teacherBean);
		    return mapping.findForward("showPastTeacherCredits");
		} else {
		    annualTeachingCreditsBean = new AnnualTeachingCreditsBean(annualTeachingCredits);
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